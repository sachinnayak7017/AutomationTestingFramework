package org.example.config;

import java.io.File;

/**
 * Framework Constants - Contains all static constants used across the framework.
 * Centralized location for paths, timeouts, formats, and other configuration values.
 */
public final class FrameworkConstants {

    private FrameworkConstants() {
        // Private constructor to prevent instantiation
    }

    // ==================== Project Paths ====================
    public static final String PROJECT_PATH = System.getProperty("user.dir");
    public static final String RESOURCES_PATH = PROJECT_PATH + File.separator + "src" + File.separator + "main" + File.separator + "resources";
    public static final String TEST_RESOURCES_PATH = PROJECT_PATH + File.separator + "src" + File.separator + "test" + File.separator + "resources";

    // ==================== Config Files ====================
    public static final String CONFIG_PROPERTIES_PATH = RESOURCES_PATH + File.separator + "config.properties";
    public static final String LOG4J_CONFIG_PATH = RESOURCES_PATH + File.separator + "log4j2.xml";
    public static final String VALIDATION_RULES_PATH = RESOURCES_PATH + File.separator + "validationRules.json";

    // ==================== Object Repository ====================
    public static final String OBJECT_REPOSITORY_PATH = RESOURCES_PATH + File.separator + "objectrepository";

    // ==================== Test Data ====================
    public static final String TEST_DATA_PATH = TEST_RESOURCES_PATH + File.separator + "testdata";
    public static final String TEST_SUITE_EXCEL_PATH = TEST_DATA_PATH + File.separator + "TestSuite.xlsx";
    public static final String ENVIRONMENTS_PATH = TEST_DATA_PATH + File.separator + "environments";

    // ==================== Features ====================
    public static final String FEATURES_PATH = TEST_RESOURCES_PATH + File.separator + "features";

    // ==================== Output Directories ====================
    public static final String TARGET_PATH = PROJECT_PATH + File.separator + "target";
    public static final String SCREENSHOTS_PATH = TARGET_PATH + File.separator + "screenshots";
    public static final String REPORTS_PATH = TARGET_PATH + File.separator + "reports";
    public static final String LOGS_PATH = TARGET_PATH + File.separator + "logs";
    public static final String EXTENT_REPORT_PATH = REPORTS_PATH + File.separator + "ExtentReport.html";

    // ==================== Timeouts (in seconds) ====================
    public static final int DEFAULT_IMPLICIT_WAIT = 10;
    public static final int DEFAULT_EXPLICIT_WAIT = 30;
    public static final int DEFAULT_PAGE_LOAD_TIMEOUT = 60;
    public static final int DEFAULT_SCRIPT_TIMEOUT = 30;
    public static final int POLLING_INTERVAL = 500; // milliseconds

    // ==================== Screenshot Naming ====================
    public static final String SCREENSHOT_DATE_FORMAT = "yyyyMMdd_HHmmss";
    public static final String SCREENSHOT_EXTENSION = ".png";

    // ==================== Excel Sheet Names ====================
    public static final String SHEET_TEST_CASES = "TestCases";
    public static final String SHEET_TEST_DATA = "prelogin";
    public static final String SHEET_RESULTS = "Results";

    // ==================== Excel Column Names - TestCases Sheet ====================
    public static final String COL_TEST_CASE_ID = "TestCaseId";
    public static final String COL_TITLE = "Title";
    public static final String COL_MODULE = "Module";
    public static final String COL_RUN = "Run";
    public static final String COL_ITERATIONS = "Iterations";
    public static final String COL_PRIORITY = "Priority";
    public static final String COL_TAGS = "Tags";
    public static final String COL_TOTAL_STEPS = "TotalSteps";
    public static final String COL_PASSED = "Passed";
    public static final String COL_FAILED = "Failed";
    public static final String COL_SKIPPED = "Skipped";
    public static final String COL_STATUS = "Status";
    public static final String COL_LAST_RUN = "LastRun";
    public static final String COL_LAST_ERROR = "LastError";
    public static final String COL_LAST_SCREENSHOT = "LastScreenshot";

    // ==================== Excel Column Names - TestData Sheet ====================
    public static final String COL_DATA_KEY = "DataKey";
    public static final String COL_VALUE = "Value";
    public static final String COL_NOTES = "Notes";

    // ==================== Test Status ====================
    public static final String STATUS_PASS = "PASS";
    public static final String STATUS_FAIL = "FAIL";
    public static final String STATUS_SKIP = "SKIP";
    public static final String STATUS_WARNING = "WARNING";
    public static final String STATUS_INFO = "INFO";

    // ==================== Run Modes ====================
    public static final String RUN_MODE_LOCAL = "local";
    public static final String RUN_MODE_GRID = "grid";
    public static final String RUN_MODE_BROWSERSTACK = "browserstack";
    public static final String RUN_MODE_SAUCELABS = "saucelabs";

    // ==================== Browser Names ====================
    public static final String BROWSER_CHROME = "chrome";
    public static final String BROWSER_EDGE = "edge";
    public static final String BROWSER_FIREFOX = "firefox";
    public static final String BROWSER_SAFARI = "safari";

    // ==================== Environment Names ====================
    public static final String ENV_QA = "qa";
    public static final String ENV_UAT = "uat";
    public static final String ENV_PROD = "prod";
    public static final String ENV_DEV = "dev";

    // ==================== Default Values ====================
    public static final String DEFAULT_BROWSER = BROWSER_CHROME;
    public static final String DEFAULT_RUN_MODE = RUN_MODE_LOCAL;
    public static final String DEFAULT_ENVIRONMENT = ENV_QA;
    public static final boolean DEFAULT_HEADLESS = false;
    public static final boolean DEFAULT_SCREENSHOT_ON_PASS = false;
    public static final boolean DEFAULT_SCREENSHOT_ON_FAIL = true;

    // ==================== Locator Types ====================
    public static final String LOCATOR_ID = "id";
    public static final String LOCATOR_NAME = "name";
    public static final String LOCATOR_XPATH = "xpath";
    public static final String LOCATOR_CSS = "css";
    public static final String LOCATOR_LINK_TEXT = "linkText";
    public static final String LOCATOR_PARTIAL_LINK_TEXT = "partialLinkText";
    public static final String LOCATOR_TAG_NAME = "tagName";
    public static final String LOCATOR_CLASS_NAME = "className";

    // ==================== Report Constants ====================
    public static final String REPORT_TITLE = "Automation Test Report";
    public static final String REPORT_NAME = "Test Execution Report";
    public static final String REPORT_THEME = "STANDARD"; // STANDARD or DARK

    // ==================== API Constants ====================
    public static final int API_DEFAULT_TIMEOUT = 30000; // milliseconds
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_XML = "application/xml";
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
}
