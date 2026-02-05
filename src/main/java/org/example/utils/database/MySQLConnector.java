package org.example.utils.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.ConfigLoader;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MySQLConnector - Handles MySQL database connections and operations.
 * Provides methods for executing queries, updates, and retrieving data.
 */
public class MySQLConnector {

    private static final Logger logger = LogManager.getLogger(MySQLConnector.class);
    private Connection connection;
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    /**
     * Constructor using config properties
     */
    public MySQLConnector() {
        ConfigLoader config = ConfigLoader.getInstance();
        this.host = config.getDbHost();
        this.port = config.getDbPort();
        this.database = config.getDbName();
        this.username = config.getDbUsername();
        this.password = config.getDbPassword();
    }

    /**
     * Constructor with custom connection details
     * @param host Database host
     * @param port Database port
     * @param database Database name
     * @param username Username
     * @param password Password
     */
    public MySQLConnector(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    /**
     * Establish database connection
     * @return true if connection successful
     */
    public boolean connect() {
        try {
            String url = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC", host, port, database);
            connection = DriverManager.getConnection(url, username, password);
            logger.info("MySQL connection established to: {}:{}/{}", host, port, database);
            return true;
        } catch (SQLException e) {
            logger.error("Failed to connect to MySQL: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Check if connection is active
     * @return true if connected
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Close database connection
     */
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("MySQL connection closed");
            }
        } catch (SQLException e) {
            logger.error("Error closing MySQL connection: {}", e.getMessage());
        }
    }

    /**
     * Execute SELECT query and return results as list of maps
     * @param query SQL SELECT query
     * @return List of rows (each row is a Map of column name to value)
     */
    public List<Map<String, Object>> executeQuery(String query) {
        List<Map<String, Object>> results = new ArrayList<>();

        if (!isConnected()) {
            logger.error("Not connected to database");
            return results;
        }

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    Object value = rs.getObject(i);
                    row.put(columnName, value);
                }
                results.add(row);
            }

            logger.info("Query executed successfully, {} rows returned", results.size());

        } catch (SQLException e) {
            logger.error("Error executing query: {}", e.getMessage());
        }

        return results;
    }

    /**
     * Execute SELECT query with parameters
     * @param query SQL SELECT query with ? placeholders
     * @param params Parameters to substitute
     * @return List of rows
     */
    public List<Map<String, Object>> executeQuery(String query, Object... params) {
        List<Map<String, Object>> results = new ArrayList<>();

        if (!isConnected()) {
            logger.error("Not connected to database");
            return results;
        }

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {

            // Set parameters
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnLabel(i);
                        Object value = rs.getObject(i);
                        row.put(columnName, value);
                    }
                    results.add(row);
                }
            }

            logger.info("Parameterized query executed, {} rows returned", results.size());

        } catch (SQLException e) {
            logger.error("Error executing parameterized query: {}", e.getMessage());
        }

        return results;
    }

    /**
     * Execute UPDATE/INSERT/DELETE query
     * @param query SQL query
     * @return Number of affected rows, -1 on error
     */
    public int executeUpdate(String query) {
        if (!isConnected()) {
            logger.error("Not connected to database");
            return -1;
        }

        try (Statement stmt = connection.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);
            logger.info("Update executed, {} rows affected", rowsAffected);
            return rowsAffected;
        } catch (SQLException e) {
            logger.error("Error executing update: {}", e.getMessage());
            return -1;
        }
    }

    /**
     * Execute UPDATE/INSERT/DELETE with parameters
     * @param query SQL query with ? placeholders
     * @param params Parameters to substitute
     * @return Number of affected rows, -1 on error
     */
    public int executeUpdate(String query, Object... params) {
        if (!isConnected()) {
            logger.error("Not connected to database");
            return -1;
        }

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            int rowsAffected = pstmt.executeUpdate();
            logger.info("Parameterized update executed, {} rows affected", rowsAffected);
            return rowsAffected;

        } catch (SQLException e) {
            logger.error("Error executing parameterized update: {}", e.getMessage());
            return -1;
        }
    }

    /**
     * Get single value from query
     * @param query SQL query
     * @param columnName Column name to retrieve
     * @return Value or null
     */
    public Object getSingleValue(String query, String columnName) {
        List<Map<String, Object>> results = executeQuery(query);
        if (!results.isEmpty()) {
            return results.get(0).get(columnName);
        }
        return null;
    }

    /**
     * Get row count from table
     * @param tableName Table name
     * @return Row count
     */
    public int getRowCount(String tableName) {
        String query = "SELECT COUNT(*) as count FROM " + tableName;
        Object count = getSingleValue(query, "count");
        return count != null ? ((Number) count).intValue() : 0;
    }

    /**
     * Check if record exists
     * @param query SQL query
     * @return true if records exist
     */
    public boolean recordExists(String query) {
        List<Map<String, Object>> results = executeQuery(query);
        return !results.isEmpty();
    }

    /**
     * Get column values as list
     * @param query SQL query
     * @param columnName Column name
     * @return List of values
     */
    public List<Object> getColumnValues(String query, String columnName) {
        List<Object> values = new ArrayList<>();
        List<Map<String, Object>> results = executeQuery(query);

        for (Map<String, Object> row : results) {
            if (row.containsKey(columnName)) {
                values.add(row.get(columnName));
            }
        }
        return values;
    }

    /**
     * Begin transaction
     */
    public void beginTransaction() {
        try {
            if (isConnected()) {
                connection.setAutoCommit(false);
                logger.info("Transaction started");
            }
        } catch (SQLException e) {
            logger.error("Error starting transaction: {}", e.getMessage());
        }
    }

    /**
     * Commit transaction
     */
    public void commit() {
        try {
            if (isConnected()) {
                connection.commit();
                connection.setAutoCommit(true);
                logger.info("Transaction committed");
            }
        } catch (SQLException e) {
            logger.error("Error committing transaction: {}", e.getMessage());
        }
    }

    /**
     * Rollback transaction
     */
    public void rollback() {
        try {
            if (isConnected()) {
                connection.rollback();
                connection.setAutoCommit(true);
                logger.info("Transaction rolled back");
            }
        } catch (SQLException e) {
            logger.error("Error rolling back transaction: {}", e.getMessage());
        }
    }

    /**
     * Get database metadata
     * @return Map of metadata
     */
    public Map<String, String> getDatabaseInfo() {
        Map<String, String> info = new HashMap<>();

        try {
            if (isConnected()) {
                DatabaseMetaData metaData = connection.getMetaData();
                info.put("databaseProductName", metaData.getDatabaseProductName());
                info.put("databaseProductVersion", metaData.getDatabaseProductVersion());
                info.put("driverName", metaData.getDriverName());
                info.put("driverVersion", metaData.getDriverVersion());
                info.put("url", metaData.getURL());
                info.put("userName", metaData.getUserName());
            }
        } catch (SQLException e) {
            logger.error("Error getting database info: {}", e.getMessage());
        }

        return info;
    }

    /**
     * Get list of tables in database
     * @return List of table names
     */
    public List<String> getTables() {
        List<String> tables = new ArrayList<>();

        try {
            if (isConnected()) {
                DatabaseMetaData metaData = connection.getMetaData();
                try (ResultSet rs = metaData.getTables(database, null, "%", new String[]{"TABLE"})) {
                    while (rs.next()) {
                        tables.add(rs.getString("TABLE_NAME"));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting tables: {}", e.getMessage());
        }

        return tables;
    }
}
