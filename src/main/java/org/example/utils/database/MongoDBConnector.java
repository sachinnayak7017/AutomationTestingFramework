package org.example.utils.database;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.example.config.ConfigLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * MongoDBConnector - Handles MongoDB connections and operations.
 * Provides methods for CRUD operations on MongoDB collections.
 */
public class MongoDBConnector {

    private static final Logger logger = LogManager.getLogger(MongoDBConnector.class);
    private MongoClient mongoClient;
    private MongoDatabase database;
    private final String host;
    private final int port;
    private final String databaseName;

    /**
     * Constructor using config properties
     */
    public MongoDBConnector() {
        ConfigLoader config = ConfigLoader.getInstance();
        this.host = config.getMongoHost();
        this.port = config.getMongoPort();
        this.databaseName = config.getMongoDatabase();
    }

    /**
     * Constructor with custom connection details
     * @param host MongoDB host
     * @param port MongoDB port
     * @param databaseName Database name
     */
    public MongoDBConnector(String host, int port, String databaseName) {
        this.host = host;
        this.port = port;
        this.databaseName = databaseName;
    }

    /**
     * Establish connection to MongoDB
     * @return true if connection successful
     */
    public boolean connect() {
        try {
            String connectionString = String.format("mongodb://%s:%d", host, port);
            mongoClient = MongoClients.create(connectionString);
            database = mongoClient.getDatabase(databaseName);

            // Test connection
            database.listCollectionNames().first();

            logger.info("MongoDB connection established to: {}:{}/{}", host, port, databaseName);
            return true;
        } catch (Exception e) {
            logger.error("Failed to connect to MongoDB: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Connect with authentication
     * @param username Username
     * @param password Password
     * @return true if connection successful
     */
    public boolean connect(String username, String password) {
        try {
            String connectionString = String.format("mongodb://%s:%s@%s:%d/%s",
                    username, password, host, port, databaseName);
            mongoClient = MongoClients.create(connectionString);
            database = mongoClient.getDatabase(databaseName);

            // Test connection
            database.listCollectionNames().first();

            logger.info("MongoDB connection established (authenticated) to: {}:{}/{}", host, port, databaseName);
            return true;
        } catch (Exception e) {
            logger.error("Failed to connect to MongoDB: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Check if connected
     * @return true if connected
     */
    public boolean isConnected() {
        try {
            if (mongoClient != null && database != null) {
                database.listCollectionNames().first();
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * Close connection
     */
    public void disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
            logger.info("MongoDB connection closed");
        }
    }

    /**
     * Get collection
     * @param collectionName Collection name
     * @return MongoCollection
     */
    public MongoCollection<Document> getCollection(String collectionName) {
        return database.getCollection(collectionName);
    }

    // ==================== INSERT Operations ====================

    /**
     * Insert single document
     * @param collectionName Collection name
     * @param document Document to insert
     * @return Inserted document ID or null
     */
    public String insertOne(String collectionName, Document document) {
        try {
            MongoCollection<Document> collection = getCollection(collectionName);
            collection.insertOne(document);
            Object id = document.get("_id");
            String idStr = id != null ? id.toString() : null;
            logger.info("Document inserted into {}: {}", collectionName, idStr);
            return idStr;
        } catch (Exception e) {
            logger.error("Error inserting document: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Insert document from Map
     * @param collectionName Collection name
     * @param data Map of field names to values
     * @return Inserted document ID or null
     */
    public String insertOne(String collectionName, Map<String, Object> data) {
        Document document = new Document(data);
        return insertOne(collectionName, document);
    }

    /**
     * Insert multiple documents
     * @param collectionName Collection name
     * @param documents List of documents
     * @return Number of inserted documents
     */
    public int insertMany(String collectionName, List<Document> documents) {
        try {
            MongoCollection<Document> collection = getCollection(collectionName);
            collection.insertMany(documents);
            logger.info("{} documents inserted into {}", documents.size(), collectionName);
            return documents.size();
        } catch (Exception e) {
            logger.error("Error inserting documents: {}", e.getMessage());
            return 0;
        }
    }

    // ==================== FIND Operations ====================

    /**
     * Find all documents in collection
     * @param collectionName Collection name
     * @return List of documents
     */
    public List<Document> findAll(String collectionName) {
        List<Document> results = new ArrayList<>();
        try {
            MongoCollection<Document> collection = getCollection(collectionName);
            collection.find().into(results);
            logger.info("Found {} documents in {}", results.size(), collectionName);
        } catch (Exception e) {
            logger.error("Error finding documents: {}", e.getMessage());
        }
        return results;
    }

    /**
     * Find documents by filter
     * @param collectionName Collection name
     * @param filter Bson filter
     * @return List of matching documents
     */
    public List<Document> find(String collectionName, Bson filter) {
        List<Document> results = new ArrayList<>();
        try {
            MongoCollection<Document> collection = getCollection(collectionName);
            collection.find(filter).into(results);
            logger.info("Found {} documents matching filter in {}", results.size(), collectionName);
        } catch (Exception e) {
            logger.error("Error finding documents: {}", e.getMessage());
        }
        return results;
    }

    /**
     * Find documents by field value
     * @param collectionName Collection name
     * @param fieldName Field name
     * @param value Field value
     * @return List of matching documents
     */
    public List<Document> findByField(String collectionName, String fieldName, Object value) {
        return find(collectionName, Filters.eq(fieldName, value));
    }

    /**
     * Find single document by filter
     * @param collectionName Collection name
     * @param filter Bson filter
     * @return First matching document or null
     */
    public Document findOne(String collectionName, Bson filter) {
        try {
            MongoCollection<Document> collection = getCollection(collectionName);
            return collection.find(filter).first();
        } catch (Exception e) {
            logger.error("Error finding document: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Find document by ID
     * @param collectionName Collection name
     * @param id Document ID
     * @return Document or null
     */
    public Document findById(String collectionName, String id) {
        return findOne(collectionName, Filters.eq("_id", id));
    }

    // ==================== UPDATE Operations ====================

    /**
     * Update single document
     * @param collectionName Collection name
     * @param filter Filter to find document
     * @param update Update operations
     * @return Number of modified documents
     */
    public long updateOne(String collectionName, Bson filter, Bson update) {
        try {
            MongoCollection<Document> collection = getCollection(collectionName);
            UpdateResult result = collection.updateOne(filter, update);
            logger.info("{} document(s) updated in {}", result.getModifiedCount(), collectionName);
            return result.getModifiedCount();
        } catch (Exception e) {
            logger.error("Error updating document: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Update field in document
     * @param collectionName Collection name
     * @param filterField Filter field name
     * @param filterValue Filter field value
     * @param updateField Field to update
     * @param updateValue New value
     * @return Number of modified documents
     */
    public long updateField(String collectionName, String filterField, Object filterValue,
                            String updateField, Object updateValue) {
        Bson filter = Filters.eq(filterField, filterValue);
        Bson update = Updates.set(updateField, updateValue);
        return updateOne(collectionName, filter, update);
    }

    /**
     * Update multiple documents
     * @param collectionName Collection name
     * @param filter Filter to find documents
     * @param update Update operations
     * @return Number of modified documents
     */
    public long updateMany(String collectionName, Bson filter, Bson update) {
        try {
            MongoCollection<Document> collection = getCollection(collectionName);
            UpdateResult result = collection.updateMany(filter, update);
            logger.info("{} document(s) updated in {}", result.getModifiedCount(), collectionName);
            return result.getModifiedCount();
        } catch (Exception e) {
            logger.error("Error updating documents: {}", e.getMessage());
            return 0;
        }
    }

    // ==================== DELETE Operations ====================

    /**
     * Delete single document
     * @param collectionName Collection name
     * @param filter Filter to find document
     * @return Number of deleted documents
     */
    public long deleteOne(String collectionName, Bson filter) {
        try {
            MongoCollection<Document> collection = getCollection(collectionName);
            DeleteResult result = collection.deleteOne(filter);
            logger.info("{} document(s) deleted from {}", result.getDeletedCount(), collectionName);
            return result.getDeletedCount();
        } catch (Exception e) {
            logger.error("Error deleting document: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Delete documents by field value
     * @param collectionName Collection name
     * @param fieldName Field name
     * @param value Field value
     * @return Number of deleted documents
     */
    public long deleteByField(String collectionName, String fieldName, Object value) {
        return deleteOne(collectionName, Filters.eq(fieldName, value));
    }

    /**
     * Delete multiple documents
     * @param collectionName Collection name
     * @param filter Filter to find documents
     * @return Number of deleted documents
     */
    public long deleteMany(String collectionName, Bson filter) {
        try {
            MongoCollection<Document> collection = getCollection(collectionName);
            DeleteResult result = collection.deleteMany(filter);
            logger.info("{} document(s) deleted from {}", result.getDeletedCount(), collectionName);
            return result.getDeletedCount();
        } catch (Exception e) {
            logger.error("Error deleting documents: {}", e.getMessage());
            return 0;
        }
    }

    // ==================== COUNT Operations ====================

    /**
     * Count all documents in collection
     * @param collectionName Collection name
     * @return Document count
     */
    public long count(String collectionName) {
        try {
            MongoCollection<Document> collection = getCollection(collectionName);
            return collection.count();
        } catch (Exception e) {
            logger.error("Error counting documents: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Count documents matching filter
     * @param collectionName Collection name
     * @param filter Bson filter
     * @return Document count
     */
    public long count(String collectionName, Bson filter) {
        try {
            MongoCollection<Document> collection = getCollection(collectionName);
            return collection.count(filter);
        } catch (Exception e) {
            logger.error("Error counting documents: {}", e.getMessage());
            return 0;
        }
    }

    // ==================== Collection Operations ====================

    /**
     * Get list of collection names
     * @return List of collection names
     */
    public List<String> getCollectionNames() {
        List<String> names = new ArrayList<>();
        try {
            database.listCollectionNames().into(names);
        } catch (Exception e) {
            logger.error("Error getting collection names: {}", e.getMessage());
        }
        return names;
    }

    /**
     * Check if collection exists
     * @param collectionName Collection name
     * @return true if exists
     */
    public boolean collectionExists(String collectionName) {
        return getCollectionNames().contains(collectionName);
    }

    /**
     * Create collection
     * @param collectionName Collection name
     */
    public void createCollection(String collectionName) {
        try {
            database.createCollection(collectionName);
            logger.info("Collection created: {}", collectionName);
        } catch (Exception e) {
            logger.error("Error creating collection: {}", e.getMessage());
        }
    }

    /**
     * Drop collection
     * @param collectionName Collection name
     */
    public void dropCollection(String collectionName) {
        try {
            getCollection(collectionName).drop();
            logger.info("Collection dropped: {}", collectionName);
        } catch (Exception e) {
            logger.error("Error dropping collection: {}", e.getMessage());
        }
    }
}
