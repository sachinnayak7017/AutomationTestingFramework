package org.example.utils.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JsonReader - Utility class for reading JSON files.
 * Uses Jackson for JSON parsing.
 */
public class JsonReader {

    private static final Logger logger = LogManager.getLogger(JsonReader.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonReader() {
        // Private constructor - utility class
    }

    /**
     * Read JSON file and return as JsonNode
     * @param filePath Path to JSON file
     * @return JsonNode object
     */
    public static JsonNode readJsonFile(String filePath) {
        try {
            JsonNode node = objectMapper.readTree(new File(filePath));
            logger.info("JSON file loaded: {}", filePath);
            return node;
        } catch (IOException e) {
            logger.error("Error reading JSON file: {}", e.getMessage());
            throw new RuntimeException("Error reading JSON file: " + filePath, e);
        }
    }

    /**
     * Read JSON file from classpath and return as JsonNode
     * @param resourcePath Resource path (relative to classpath)
     * @return JsonNode object
     */
    public static JsonNode readJsonFromClasspath(String resourcePath) {
        try (InputStream is = JsonReader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }
            JsonNode node = objectMapper.readTree(is);
            logger.info("JSON resource loaded: {}", resourcePath);
            return node;
        } catch (IOException e) {
            logger.error("Error reading JSON resource: {}", e.getMessage());
            throw new RuntimeException("Error reading JSON resource: " + resourcePath, e);
        }
    }

    /**
     * Read JSON file and map to object
     * @param filePath Path to JSON file
     * @param valueType Class of target object
     * @param <T> Type of target object
     * @return Mapped object
     */
    public static <T> T readJsonFile(String filePath, Class<T> valueType) {
        try {
            T result = objectMapper.readValue(new File(filePath), valueType);
            logger.info("JSON file loaded and mapped to {}: {}", valueType.getSimpleName(), filePath);
            return result;
        } catch (IOException e) {
            logger.error("Error reading JSON file: {}", e.getMessage());
            throw new RuntimeException("Error reading JSON file: " + filePath, e);
        }
    }

    /**
     * Read JSON file and map to generic type (List, Map, etc.)
     * @param filePath Path to JSON file
     * @param typeReference TypeReference for generic type
     * @param <T> Type of target object
     * @return Mapped object
     */
    public static <T> T readJsonFile(String filePath, TypeReference<T> typeReference) {
        try {
            T result = objectMapper.readValue(new File(filePath), typeReference);
            logger.info("JSON file loaded: {}", filePath);
            return result;
        } catch (IOException e) {
            logger.error("Error reading JSON file: {}", e.getMessage());
            throw new RuntimeException("Error reading JSON file: " + filePath, e);
        }
    }

    /**
     * Read JSON file as Map
     * @param filePath Path to JSON file
     * @return Map representation of JSON
     */
    public static Map<String, Object> readJsonAsMap(String filePath) {
        return readJsonFile(filePath, new TypeReference<Map<String, Object>>() {});
    }

    /**
     * Read JSON file as List of Maps
     * @param filePath Path to JSON file
     * @return List of Maps
     */
    public static List<Map<String, Object>> readJsonAsList(String filePath) {
        return readJsonFile(filePath, new TypeReference<List<Map<String, Object>>>() {});
    }

    /**
     * Read JSON file as flat key-value map (nested keys become dot-separated)
     * @param filePath Path to JSON file
     * @return Flat map of all values
     */
    public static Map<String, String> readJsonAsFlat(String filePath) {
        JsonNode rootNode = readJsonFile(filePath);
        Map<String, String> flatMap = new HashMap<>();
        flattenJson(rootNode, "", flatMap);
        return flatMap;
    }

    /**
     * Flatten JSON node recursively
     * @param node JSON node
     * @param prefix Current key prefix
     * @param result Result map
     */
    private static void flattenJson(JsonNode node, String prefix, Map<String, String> result) {
        if (node.isObject()) {
            node.fields().forEachRemaining(entry -> {
                String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
                flattenJson(entry.getValue(), key, result);
            });
        } else if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                flattenJson(node.get(i), prefix + "[" + i + "]", result);
            }
        } else {
            result.put(prefix, node.asText());
        }
    }

    /**
     * Get value from JSON node by path (e.g., "user.name" or "items[0].id")
     * @param node Root JSON node
     * @param path Path to value
     * @return Value as string or null if not found
     */
    public static String getValue(JsonNode node, String path) {
        String[] parts = path.split("\\.");
        JsonNode current = node;

        for (String part : parts) {
            if (current == null) return null;

            // Handle array index notation
            if (part.contains("[")) {
                int bracketStart = part.indexOf("[");
                int bracketEnd = part.indexOf("]");
                String fieldName = part.substring(0, bracketStart);
                int index = Integer.parseInt(part.substring(bracketStart + 1, bracketEnd));

                current = current.get(fieldName);
                if (current != null && current.isArray()) {
                    current = current.get(index);
                }
            } else {
                current = current.get(part);
            }
        }

        return current != null ? current.asText() : null;
    }

    /**
     * Parse JSON string to JsonNode
     * @param jsonString JSON string
     * @return JsonNode object
     */
    public static JsonNode parseJson(String jsonString) {
        try {
            return objectMapper.readTree(jsonString);
        } catch (IOException e) {
            logger.error("Error parsing JSON string: {}", e.getMessage());
            throw new RuntimeException("Error parsing JSON string", e);
        }
    }

    /**
     * Convert object to JSON string
     * @param object Object to convert
     * @return JSON string
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            logger.error("Error converting object to JSON: {}", e.getMessage());
            throw new RuntimeException("Error converting object to JSON", e);
        }
    }

    /**
     * Convert object to pretty JSON string
     * @param object Object to convert
     * @return Pretty formatted JSON string
     */
    public static String toPrettyJson(Object object) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (IOException e) {
            logger.error("Error converting object to JSON: {}", e.getMessage());
            throw new RuntimeException("Error converting object to JSON", e);
        }
    }

    /**
     * Get ObjectMapper instance for custom operations
     * @return ObjectMapper instance
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
