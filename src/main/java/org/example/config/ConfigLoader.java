package org.example.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigLoader - Loads and manages configuration from config.properties file.
 * Implements singleton pattern for consistent configuration access across the framework.
 */
public class ConfigLoader {

    private static final Logger logger = LogManager.getLogger(ConfigLoader.class);
    private static ConfigLoader instance;
    private final Properties properties;

    // Property Keys
    private static final String PROP_BASE_URL = "base.url";
    private static final String PROP_BROWSER = "browser";
    private static final String PROP_RUN_MODE = "runMode";
    private static final String PROP_GRID_URL = "gridUrl";
    private static final String PROP_HEADLESS = "headless";
    private static final String PROP_IMPLICIT_WAIT = "implicit.wait";
    private static final String PROP_EXPLICIT_WAIT = "explicit.wait";
    private static final String PROP_PAGE_LOAD_TIMEOUT = "page.load.timeout";
    private static final String PROP_SCREENSHOT_ON_PASS = "screenshot.on.pass";
    private static final String PROP_SCREENSHOT_ON_FAIL = "screenshot.on.fail";
    private static final String PROP_SUITE_EXCEL = "suite.excel";
    private static final String PROP_OR_FOLDER = "or.folder";
    private static final String PROP_ENVIRONMENT = "environment";

    // Database Properties
    private static final String PROP_DB_HOST = "db.host";
    private static final String PROP_DB_PORT = "db.port";
    private static final String PROP_DB_NAME = "db.name";
    private static final String PROP_DB_USERNAME = "db.username";
    private static final String PROP_DB_PASSWORD = "db.password";

    // MongoDB Properties
    private static final String PROP_MONGO_HOST = "mongo.host";
    private static final String PROP_MONGO_PORT = "mongo.port";
    private static final String PROP_MONGO_DATABASE = "mongo.database";

    // API Properties
    private static final String PROP_API_BASE_URL = "api.base.url";
    private static final String PROP_API_TIMEOUT = "api.timeout";

    /**
     * Private constructor - loads properties from config file
     */
    private ConfigLoader() {
        properties = new Properties();
        loadProperties();
    }

    /**
     * Get singleton instance of ConfigLoader
     * @return ConfigLoader instance
     */
    public static synchronized ConfigLoader getInstance() {
        if (instance == null) {
            instance = new ConfigLoader();
        }
        return instance;
    }

    /**
     * Load properties from config.properties file
     */
    private void loadProperties() {
        String configPath = FrameworkConstants.CONFIG_PROPERTIES_PATH;
        try (FileInputStream fis = new FileInputStream(configPath)) {
            properties.load(fis);
            logger.info("Configuration loaded successfully from: {}", configPath);
        } catch (FileNotFoundException e) {
            logger.error("Config file not found at: {}", configPath);
            throw new RuntimeException("Config file not found: " + configPath, e);
        } catch (IOException e) {
            logger.error("Error reading config file: {}", e.getMessage());
            throw new RuntimeException("Error reading config file", e);
        }
    }

    /**
     * Reload properties from config file
     */
    public void reloadProperties() {
        loadProperties();
    }

    /**
     * Get property value by key
     * @param key Property key
     * @return Property value or null if not found
     */
    public String getProperty(String key) {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null && !systemProperty.isEmpty()) {
            return systemProperty;
        }
        return properties.getProperty(key);
    }

    /**
     * Get property value by key with default value
     * @param key Property key
     * @param defaultValue Default value if property not found
     * @return Property value or default value
     */
    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    // ==================== Getter Methods ====================

    public String getBaseUrl() {
        return getProperty(PROP_BASE_URL, "");
    }

    public String getBrowser() {
        return getProperty(PROP_BROWSER, FrameworkConstants.DEFAULT_BROWSER).toLowerCase();
    }

    public String getRunMode() {
        return getProperty(PROP_RUN_MODE, FrameworkConstants.DEFAULT_RUN_MODE).toLowerCase();
    }

    public String getGridUrl() {
        return getProperty(PROP_GRID_URL, "http://localhost:4444/wd/hub");
    }

    public boolean isHeadless() {
        return Boolean.parseBoolean(getProperty(PROP_HEADLESS, String.valueOf(FrameworkConstants.DEFAULT_HEADLESS)));
    }

    public int getImplicitWait() {
        return Integer.parseInt(getProperty(PROP_IMPLICIT_WAIT, String.valueOf(FrameworkConstants.DEFAULT_IMPLICIT_WAIT)));
    }

    public int getExplicitWait() {
        return Integer.parseInt(getProperty(PROP_EXPLICIT_WAIT, String.valueOf(FrameworkConstants.DEFAULT_EXPLICIT_WAIT)));
    }

    public int getPageLoadTimeout() {
        return Integer.parseInt(getProperty(PROP_PAGE_LOAD_TIMEOUT, String.valueOf(FrameworkConstants.DEFAULT_PAGE_LOAD_TIMEOUT)));
    }

    public boolean isScreenshotOnPass() {
        return Boolean.parseBoolean(getProperty(PROP_SCREENSHOT_ON_PASS, String.valueOf(FrameworkConstants.DEFAULT_SCREENSHOT_ON_PASS)));
    }

    public boolean isScreenshotOnFail() {
        return Boolean.parseBoolean(getProperty(PROP_SCREENSHOT_ON_FAIL, String.valueOf(FrameworkConstants.DEFAULT_SCREENSHOT_ON_FAIL)));
    }

    public String getSuiteExcelPath() {
        return getProperty(PROP_SUITE_EXCEL, FrameworkConstants.TEST_SUITE_EXCEL_PATH);
    }

    public String getObjectRepositoryFolder() {
        return getProperty(PROP_OR_FOLDER, FrameworkConstants.OBJECT_REPOSITORY_PATH);
    }

    public String getEnvironment() {
        return getProperty(PROP_ENVIRONMENT, FrameworkConstants.DEFAULT_ENVIRONMENT).toLowerCase();
    }

    // ==================== Database Getters ====================

    public String getDbHost() {
        return getProperty(PROP_DB_HOST, "localhost");
    }

    public int getDbPort() {
        return Integer.parseInt(getProperty(PROP_DB_PORT, "3306"));
    }

    public String getDbName() {
        return getProperty(PROP_DB_NAME, "");
    }

    public String getDbUsername() {
        return getProperty(PROP_DB_USERNAME, "");
    }

    public String getDbPassword() {
        return getProperty(PROP_DB_PASSWORD, "");
    }

    // ==================== MongoDB Getters ====================

    public String getMongoHost() {
        return getProperty(PROP_MONGO_HOST, "localhost");
    }

    public int getMongoPort() {
        return Integer.parseInt(getProperty(PROP_MONGO_PORT, "27017"));
    }

    public String getMongoDatabase() {
        return getProperty(PROP_MONGO_DATABASE, "");
    }

    // ==================== API Getters ====================

    public String getApiBaseUrl() {
        return getProperty(PROP_API_BASE_URL, "");
    }

    public int getApiTimeout() {
        return Integer.parseInt(getProperty(PROP_API_TIMEOUT, String.valueOf(FrameworkConstants.API_DEFAULT_TIMEOUT)));
    }

    // ==================== Utility Methods ====================

    public boolean isLocalExecution() {
        return FrameworkConstants.RUN_MODE_LOCAL.equalsIgnoreCase(getRunMode());
    }

    public boolean isGridExecution() {
        return FrameworkConstants.RUN_MODE_GRID.equalsIgnoreCase(getRunMode());
    }

    public boolean isCloudExecution() {
        String runMode = getRunMode();
        return FrameworkConstants.RUN_MODE_BROWSERSTACK.equalsIgnoreCase(runMode) ||
               FrameworkConstants.RUN_MODE_SAUCELABS.equalsIgnoreCase(runMode);
    }

    /**
     * Print all loaded properties (for debugging)
     */
    public void printAllProperties() {
        logger.info("========== Loaded Configuration ==========");
        properties.forEach((key, value) -> {
            if (key.toString().toLowerCase().contains("password")) {
                logger.info("{} = ********", key);
            } else {
                logger.info("{} = {}", key, value);
            }
        });
        logger.info("===========================================");
    }
}
