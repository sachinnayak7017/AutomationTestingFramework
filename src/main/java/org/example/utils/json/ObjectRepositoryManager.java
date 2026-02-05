package org.example.utils.json;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.ConfigLoader;
import org.example.config.FrameworkConstants;
import org.openqa.selenium.By;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ObjectRepositoryManager - Manages Object Repository from JSON files.
 * Loads element locators by module and provides By locators for elements.
 *
 * JSON Structure:
 * {
 *   "module": "Login",
 *   "objects": {
 *     "txt_username": { "locatorType": "id", "locatorValue": "username" },
 *     "btn_login": { "locatorType": "xpath", "locatorValue": "//button[@id='login']" }
 *   }
 * }
 */
public class ObjectRepositoryManager {

    private static final Logger logger = LogManager.getLogger(ObjectRepositoryManager.class);
    private static ObjectRepositoryManager instance;

    // Cache for loaded modules: moduleName -> (elementKey -> LocatorInfo)
    private final Map<String, Map<String, LocatorInfo>> moduleCache;

    // Global cache for all elements: elementKey -> LocatorInfo
    private final Map<String, LocatorInfo> globalCache;

    private final String orFolderPath;

    /**
     * LocatorInfo - Inner class to hold locator details
     */
    public static class LocatorInfo {
        private final String locatorType;
        private final String locatorValue;
        private final String module;

        public LocatorInfo(String locatorType, String locatorValue, String module) {
            this.locatorType = locatorType;
            this.locatorValue = locatorValue;
            this.module = module;
        }

        public String getLocatorType() {
            return locatorType;
        }

        public String getLocatorValue() {
            return locatorValue;
        }

        public String getModule() {
            return module;
        }

        @Override
        public String toString() {
            return String.format("%s: %s [%s]", locatorType, locatorValue, module);
        }
    }

    /**
     * Private constructor - loads all OR files from configured folder
     */
    private ObjectRepositoryManager() {
        this.moduleCache = new ConcurrentHashMap<>();
        this.globalCache = new ConcurrentHashMap<>();
        this.orFolderPath = ConfigLoader.getInstance().getObjectRepositoryFolder();
        loadAllModules();
    }

    /**
     * Get singleton instance
     * @return ObjectRepositoryManager instance
          */
    public static synchronized ObjectRepositoryManager getInstance() {
        if (instance == null) {
            instance = new ObjectRepositoryManager();
        }
        return instance;
    }

    /**
     * Reload all OR files (useful for dynamic updates)
     */
    public static void reload() {
        instance = null;
        getInstance();
    }

    /**
     * Load all JSON files from OR folder
     */
    private void loadAllModules() {
        File orFolder = new File(orFolderPath);

        if (!orFolder.exists() || !orFolder.isDirectory()) {
            logger.warn("Object Repository folder not found: {}", orFolderPath);
            return;
        }

        File[] jsonFiles = orFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));

        if (jsonFiles == null || jsonFiles.length == 0) {
            logger.warn("No JSON files found in OR folder: {}", orFolderPath);
            return;
        }

        for (File jsonFile : jsonFiles) {
            loadModule(jsonFile);
        }

        logger.info("Object Repository loaded: {} modules, {} total elements",
                    moduleCache.size(), globalCache.size());
    }

    /**
     * Load a single module from JSON file
     * @param jsonFile JSON file
     */
    private void loadModule(File jsonFile) {
        try {
            JsonNode rootNode = JsonReader.readJsonFile(jsonFile.getAbsolutePath());

            String moduleName = rootNode.has("module") ?
                                rootNode.get("module").asText() :
                                jsonFile.getName().replace(".json", "");

            JsonNode objectsNode = rootNode.get("objects");

            if (objectsNode == null) {
                logger.warn("No 'objects' node found in: {}", jsonFile.getName());
                return;
            }

            Map<String, LocatorInfo> moduleElements = new HashMap<>();

            objectsNode.fields().forEachRemaining(entry -> {
                String elementKey = entry.getKey();
                JsonNode elementNode = entry.getValue();

                String locatorType = elementNode.has("locatorType") ?
                                     elementNode.get("locatorType").asText() : "xpath";
                String locatorValue = elementNode.has("locatorValue") ?
                                      elementNode.get("locatorValue").asText() : "";

                LocatorInfo locatorInfo = new LocatorInfo(locatorType, locatorValue, moduleName);
                moduleElements.put(elementKey, locatorInfo);
                globalCache.put(elementKey, locatorInfo);
            });

            moduleCache.put(moduleName, moduleElements);
            logger.debug("Loaded module '{}' with {} elements", moduleName, moduleElements.size());

        } catch (Exception e) {
            logger.error("Error loading OR file {}: {}", jsonFile.getName(), e.getMessage());
        }
    }

    /**
     * Get By locator for element by key
     * @param elementKey Element key from OR
     * @return By locator
     */
    public By getLocator(String elementKey) {
        LocatorInfo locatorInfo = globalCache.get(elementKey);

        if (locatorInfo == null) {
            logger.error("Element not found in Object Repository: {}", elementKey);
            throw new RuntimeException("Element not found in OR: " + elementKey);
        }

        return createByLocator(locatorInfo.getLocatorType(), locatorInfo.getLocatorValue());
    }

    /**
     * Get By locator for element by module and key
     * @param moduleName Module name
     * @param elementKey Element key
     * @return By locator
     */
    public By getLocator(String moduleName, String elementKey) {
        Map<String, LocatorInfo> moduleElements = moduleCache.get(moduleName);

        if (moduleElements == null) {
            logger.error("Module not found in Object Repository: {}", moduleName);
            throw new RuntimeException("Module not found in OR: " + moduleName);
        }

        LocatorInfo locatorInfo = moduleElements.get(elementKey);

        if (locatorInfo == null) {
            logger.error("Element '{}' not found in module '{}'", elementKey, moduleName);
            throw new RuntimeException("Element not found: " + elementKey + " in module: " + moduleName);
        }

        return createByLocator(locatorInfo.getLocatorType(), locatorInfo.getLocatorValue());
    }

    /**
     * Get LocatorInfo for element
     * @param elementKey Element key
     * @return LocatorInfo object
     */
    public LocatorInfo getLocatorInfo(String elementKey) {
        return globalCache.get(elementKey);
    }

    /**
     * Get locator value (raw string) for element
     * @param elementKey Element key
     * @return Locator value string
     */
    public String getLocatorValue(String elementKey) {
        LocatorInfo info = globalCache.get(elementKey);
        return info != null ? info.getLocatorValue() : null;
    }

    /**
     * Get locator type for element
     * @param elementKey Element key
     * @return Locator type string
     */
    public String getLocatorType(String elementKey) {
        LocatorInfo info = globalCache.get(elementKey);
        return info != null ? info.getLocatorType() : null;
    }

    /**
     * Check if element exists in OR
     * @param elementKey Element key
     * @return true if element exists
     */
    public boolean hasElement(String elementKey) {
        return globalCache.containsKey(elementKey);
    }

    /**
     * Check if module exists in OR
     * @param moduleName Module name
     * @return true if module exists
     */
    public boolean hasModule(String moduleName) {
        return moduleCache.containsKey(moduleName);
    }

    /**
     * Get all element keys for a module
     * @param moduleName Module name
     * @return Map of element keys to LocatorInfo
     */
    public Map<String, LocatorInfo> getModuleElements(String moduleName) {
        return moduleCache.getOrDefault(moduleName, new HashMap<>());
    }

    /**
     * Get all loaded module names
     * @return Array of module names
     */
    public String[] getModuleNames() {
        return moduleCache.keySet().toArray(new String[0]);
    }

    /**
     * Create By locator from type and value
     * @param locatorType Locator type (id, name, xpath, css, etc.)
     * @param locatorValue Locator value
     * @return By locator
     */
    private By createByLocator(String locatorType, String locatorValue) {
        String type = locatorType.toLowerCase().trim();

        switch (type) {
            case FrameworkConstants.LOCATOR_ID:
                return By.id(locatorValue);
            case FrameworkConstants.LOCATOR_NAME:
                return By.name(locatorValue);
            case FrameworkConstants.LOCATOR_XPATH:
                return By.xpath(locatorValue);
            case FrameworkConstants.LOCATOR_CSS:
            case "cssselector":
                return By.cssSelector(locatorValue);
            case FrameworkConstants.LOCATOR_LINK_TEXT:
            case "linktext":
                return By.linkText(locatorValue);
            case FrameworkConstants.LOCATOR_PARTIAL_LINK_TEXT:
            case "partiallinktext":
                return By.partialLinkText(locatorValue);
            case FrameworkConstants.LOCATOR_TAG_NAME:
            case "tagname":
            case "tag":
                return By.tagName(locatorValue);
            case FrameworkConstants.LOCATOR_CLASS_NAME:
            case "classname":
            case "class":
                return By.className(locatorValue);
            default:
                logger.warn("Unknown locator type '{}', defaulting to xpath", locatorType);
                return By.xpath(locatorValue);
        }
    }

    /**
     * Get By locator with dynamic value replacement
     * @param elementKey Element key
     * @param replacements Map of placeholder to value (e.g., {text} -> "Hello")
     * @return By locator with replaced values
     */
    public By getLocatorWithDynamicValue(String elementKey, Map<String, String> replacements) {
        LocatorInfo locatorInfo = globalCache.get(elementKey);

        if (locatorInfo == null) {
            throw new RuntimeException("Element not found in OR: " + elementKey);
        }

        String locatorValue = locatorInfo.getLocatorValue();

        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            locatorValue = locatorValue.replace("{" + entry.getKey() + "}", entry.getValue());
        }

        return createByLocator(locatorInfo.getLocatorType(), locatorValue);
    }

    /**
     * Get By locator with single dynamic value replacement
     * @param elementKey Element key
     * @param placeholder Placeholder name (without braces)
     * @param value Replacement value
     * @return By locator with replaced value
     */
    public By getLocatorWithDynamicValue(String elementKey, String placeholder, String value) {
        Map<String, String> replacements = new HashMap<>();
        replacements.put(placeholder, value);
        return getLocatorWithDynamicValue(elementKey, replacements);
    }

    /**
     * Print all loaded elements (for debugging)
     */
    public void printAllElements() {
        logger.info("========== Object Repository Contents ==========");
        for (Map.Entry<String, Map<String, LocatorInfo>> module : moduleCache.entrySet()) {
            logger.info("Module: {}", module.getKey());
            for (Map.Entry<String, LocatorInfo> element : module.getValue().entrySet()) {
                logger.info("  {} -> {}", element.getKey(), element.getValue());
            }
        }
        logger.info("================================================");
    }
}
