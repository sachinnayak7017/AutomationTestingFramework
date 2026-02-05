package org.example.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * EnvironmentConfig - Handles environment-specific configurations.
 * Loads environment data from JSON files (qa.json, uat.json, prod.json).
 */
public class EnvironmentConfig {

    private static final Logger logger = LogManager.getLogger(EnvironmentConfig.class);
    private static EnvironmentConfig instance;
    private final Map<String, String> environmentData;
    private final String currentEnvironment;
    private final ObjectMapper objectMapper;

    /**
     * Private constructor - loads environment specific configuration
     */
    private EnvironmentConfig() {
        this.objectMapper = new ObjectMapper();
        this.environmentData = new HashMap<>();
        this.currentEnvironment = ConfigLoader.getInstance().getEnvironment();
        loadEnvironmentConfig();
    }

    /**
     * Get singleton instance of EnvironmentConfig
     * @return EnvironmentConfig instance
     */
    public static synchronized EnvironmentConfig getInstance() {
        if (instance == null) {
            instance = new EnvironmentConfig();
        }
        return instance;
    }

    /**
     * Reload environment configuration (useful when switching environments)
     */
    public static void reload() {
        instance = null;
        getInstance();
    }

    /**
     * Load environment-specific JSON config file
     */
    private void loadEnvironmentConfig() {
        String envFilePath = FrameworkConstants.ENVIRONMENTS_PATH + File.separator + currentEnvironment + ".json";
        File envFile = new File(envFilePath);

        if (!envFile.exists()) {
            logger.warn("Environment config file not found: {}. Using default configuration.", envFilePath);
            return;
        }

        try {
            JsonNode rootNode = objectMapper.readTree(envFile);
            parseJsonNode(rootNode, "");
            logger.info("Environment configuration loaded successfully for: {}", currentEnvironment);
        } catch (IOException e) {
            logger.error("Error loading environment config: {}", e.getMessage());
            throw new RuntimeException("Error loading environment config", e);
        }
    }

    /**
     * Parse JSON node recursively and flatten to key-value pairs
     * @param node JSON node
     * @param prefix Key prefix for nested objects
     */
    private void parseJsonNode(JsonNode node, String prefix) {
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String key = prefix.isEmpty() ? field.getKey() : prefix + "." + field.getKey();
                parseJsonNode(field.getValue(), key);
            }
        } else if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                parseJsonNode(node.get(i), prefix + "[" + i + "]");
            }
        } else {
            environmentData.put(prefix, node.asText());
        }
    }

    /**
     * Get environment-specific value by key
     * @param key Configuration key
     * @return Value or null if not found
     */
    public String get(String key) {
        return environmentData.get(key);
    }

    /**
     * Get environment-specific value by key with default
     * @param key Configuration key
     * @param defaultValue Default value if key not found
     * @return Value or default value
     */
    public String get(String key, String defaultValue) {
        return environmentData.getOrDefault(key, defaultValue);
    }

    /**
     * Get current environment name
     * @return Environment name (qa, uat, prod, etc.)
     */
    public String getCurrentEnvironment() {
        return currentEnvironment;
    }

    /**
     * Get base URL for current environment
     * @return Base URL
     */
    public String getBaseUrl() {
        String envUrl = get("baseUrl");
        if (envUrl != null && !envUrl.isEmpty()) {
            return envUrl;
        }
        return ConfigLoader.getInstance().getBaseUrl();
    }

    /**
     * Get API base URL for current environment
     * @return API Base URL
     */
    public String getApiBaseUrl() {
        String apiUrl = get("apiBaseUrl");
        if (apiUrl != null && !apiUrl.isEmpty()) {
            return apiUrl;
        }
        return ConfigLoader.getInstance().getApiBaseUrl();
    }

    /**
     * Get all environment data as map
     * @return Map of all environment configurations
     */
    public Map<String, String> getAllEnvironmentData() {
        return new HashMap<>(environmentData);
    }

    /**
     * Check if current environment is QA
     * @return true if QA environment
     */
    public boolean isQaEnvironment() {
        return FrameworkConstants.ENV_QA.equalsIgnoreCase(currentEnvironment);
    }

    /**
     * Check if current environment is UAT
     * @return true if UAT environment
     */
    public boolean isUatEnvironment() {
        return FrameworkConstants.ENV_UAT.equalsIgnoreCase(currentEnvironment);
    }

    /**
     * Check if current environment is Production
     * @return true if Production environment
     */
    public boolean isProdEnvironment() {
        return FrameworkConstants.ENV_PROD.equalsIgnoreCase(currentEnvironment);
    }

    /**
     * Print all environment configurations (for debugging)
     */
    public void printEnvironmentConfig() {
        logger.info("========== Environment Configuration ({}) ==========", currentEnvironment);
        environmentData.forEach((key, value) -> {
            if (key.toLowerCase().contains("password") || key.toLowerCase().contains("secret")) {
                logger.info("{} = ********", key);
            } else {
                logger.info("{} = {}", key, value);
            }
        });
        logger.info("=====================================================");
    }
}
