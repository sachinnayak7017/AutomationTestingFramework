package org.example.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.ConfigLoader;
import org.example.config.FrameworkConstants;
import org.example.core.driver.DriverManager;
import org.example.core.keywords.KeywordEngine;
import org.example.core.keywords.WebActions;
import org.example.reporting.ReportManager;
import org.example.utils.excel.ExcelReader;
import org.example.utils.screenshot.ScreenshotManager;
import org.example.validation.CheckpointManager;
import org.testng.annotations.*;

import java.util.Map;

/**
 * KeywordDrivenTest - Sample test class demonstrating keyword-driven execution.
 * Shows how to use WebActions, KeywordEngine, and test data from Excel.
 */
@Listeners(org.example.core.listeners.TestNGListener.class)
public class KeywordDrivenTest {

    private static final Logger logger = LogManager.getLogger(KeywordDrivenTest.class);
    private WebActions webActions;
    private KeywordEngine keywordEngine;
    private CheckpointManager checkpointManager;
    private Map<String, String> testData;

    @BeforeClass
    public void beforeClass() {
        logger.info("KeywordDrivenTest class setup");

        // Load test data
        try {
            String suiteExcelPath = ConfigLoader.getInstance().getSuiteExcelPath();
            ExcelReader excelReader = new ExcelReader(suiteExcelPath);
            testData = excelReader.getTestData();
            excelReader.close();
        } catch (Exception e) {
            logger.warn("Could not load test data: {}", e.getMessage());
            testData = new java.util.HashMap<>();
        }
    }

    @BeforeMethod
    @Parameters({"browser", "headless"})
    public void setUp(
            @Optional("chrome") String browser,
            @Optional("false") String headless) {

        logger.info("Test setup - Browser: {}, Headless: {}", browser, headless);

        // Initialize WebDriver
        DriverManager.initDriver(browser, Boolean.parseBoolean(headless));

        // Initialize WebActions
        webActions = new WebActions();

        // Initialize Keyword Engine
        keywordEngine = new KeywordEngine();
        keywordEngine.loadTestData(testData);

        // Initialize Checkpoint Manager
        checkpointManager = CheckpointManager.getInstance();
    }

    @AfterMethod
    public void tearDown() {
        logger.info("Test teardown");

        // Reset checkpoint manager
        CheckpointManager.reset();

        // Quit driver
        DriverManager.quitDriver();
    }

    @AfterClass
    public void afterClass() {
        logger.info("KeywordDrivenTest class teardown");
    }

    // ==================== Sample Test Methods ====================

    @Test(groups = {"smoke", "sanity"}, description = "Verify application is accessible")
    public void testApplicationAccessibility() {
        ReportManager.startTest("TC001_AppAccess", "Verify application is accessible");
        ReportManager.assignCategory("Smoke");

        // Navigate to application
        String baseUrl = ConfigLoader.getInstance().getBaseUrl();
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = "https://example.com";
        }
        webActions.navigateTo(baseUrl);
        ReportManager.logInfo("Navigated to: " + baseUrl);

        // Wait for page load
        webActions.waitForPageLoad();
        ReportManager.logInfo("Page loaded successfully");

        // Verify page title is not empty
        String title = webActions.getPageTitle();
        checkpointManager.softCheckpointTrue("Page title should not be empty", !title.isEmpty());
        ReportManager.logInfo("Page title: " + title);

        // Capture screenshot
        String screenshot = ScreenshotManager.captureScreenshot("Smoke", "TC001_AppAccess");
        ReportManager.attachScreenshot(screenshot, "Application Home Page");

        // Assert all checkpoints
        checkpointManager.assertAll();

        ReportManager.logPass("Application is accessible");
        ReportManager.endTest(FrameworkConstants.STATUS_PASS);
    }

    @Test(groups = {"smoke"}, description = "Sample keyword-driven login test")
    public void testKeywordDrivenLogin() {
        ReportManager.startTest("TC002_KeywordLogin", "Keyword-driven login test");
        ReportManager.assignCategory("Smoke", "Login");

        keywordEngine.setCurrentModule("Login");

        // Execute keywords
        keywordEngine.executeKeyword("navigateToBaseUrl", null, null);
        keywordEngine.executeKeyword("waitForPageLoad", null, null);

        // Verify page loaded
        String title = webActions.getPageTitle();
        ReportManager.logInfo("Page title: " + title);

        // Take screenshot
        keywordEngine.executeKeyword("captureScreenshot", null, null);

        ReportManager.logPass("Keyword-driven test completed");
        ReportManager.endTest(FrameworkConstants.STATUS_PASS);
    }

    @Test(groups = {"regression"}, description = "Verify element visibility using WebActions")
    public void testElementVisibility() {
        ReportManager.startTest("TC003_ElementVisibility", "Test element visibility checks");
        ReportManager.assignCategory("Regression", "UI");

        // Navigate to application
        webActions.navigateToBaseUrl();
        webActions.waitForPageLoad();

        // Get current URL and title
        String currentUrl = webActions.getCurrentUrl();
        String pageTitle = webActions.getPageTitle();

        ReportManager.logInfo("Current URL: " + currentUrl);
        ReportManager.logInfo("Page Title: " + pageTitle);

        // Checkpoints
        checkpointManager.softCheckpointTrue("URL should not be empty", !currentUrl.isEmpty());
        checkpointManager.softCheckpointTrue("Title should not be empty", !pageTitle.isEmpty());

        // Capture screenshot
        ScreenshotManager.captureScreenshot("Regression", "TC003_ElementVisibility");

        checkpointManager.assertAll();

        ReportManager.logPass("Element visibility test completed");
        ReportManager.endTest(FrameworkConstants.STATUS_PASS);
    }

    @Test(groups = {"api"}, description = "Sample API test")
    public void testApiCall() {
        ReportManager.startTest("TC004_APITest", "Sample API test");
        ReportManager.assignCategory("API");

        // API tests don't need browser - this is just a placeholder
        ReportManager.logInfo("API test would go here");
        ReportManager.logInfo("Use APIUtils class for REST API testing");

        ReportManager.logPass("API test placeholder completed");
        ReportManager.endTest(FrameworkConstants.STATUS_PASS);
    }

    @Test(groups = {"regression"}, description = "Test with multiple checkpoints")
    public void testMultipleCheckpoints() {
        ReportManager.startTest("TC005_Checkpoints", "Test with multiple checkpoints");
        ReportManager.assignCategory("Regression");

        // Navigate
        webActions.navigateToBaseUrl();
        webActions.waitForPageLoad();

        // Multiple soft checkpoints
        String url = webActions.getCurrentUrl();
        String title = webActions.getPageTitle();

        checkpointManager.softCheckpointNotNull("URL should not be null", url);
        checkpointManager.softCheckpointNotNull("Title should not be null", title);
        checkpointManager.softCheckpointTrue("URL should contain http", url.contains("http"));

        // Print checkpoint summary
        checkpointManager.printSummary();

        // Assert all at end
        checkpointManager.assertAll();

        ReportManager.logPass("Multiple checkpoints test completed");
        ReportManager.endTest(FrameworkConstants.STATUS_PASS);
    }

    @Test(groups = {"demo"}, description = "Demo test showing all WebActions")
    public void testWebActionsDemo() {
        ReportManager.startTest("TC006_WebActionsDemo", "Demo of WebActions methods");
        ReportManager.assignCategory("Demo");

        // This is a demo test showing how to use various WebActions
        // In real tests, you would interact with actual elements

        ReportManager.logInfo("WebActions Demo Test");
        ReportManager.logInfo("This test demonstrates the framework capabilities");

        // Navigate
        webActions.navigateToBaseUrl();
        ReportManager.logInfo("Navigation completed");

        // Wait
        webActions.waitForPageLoad();
        ReportManager.logInfo("Page load wait completed");

        // Get info
        String title = webActions.getPageTitle();
        String url = webActions.getCurrentUrl();
        ReportManager.logInfo("Title: " + title);
        ReportManager.logInfo("URL: " + url);

        // Scroll
        webActions.scrollToBottom();
        webActions.scrollToTop();
        ReportManager.logInfo("Scroll actions completed");

        // Screenshot
        String screenshot = ScreenshotManager.captureScreenshot("Demo", "TC006");
        ReportManager.attachScreenshot(screenshot);

        ReportManager.logPass("WebActions demo completed successfully");
        ReportManager.endTest(FrameworkConstants.STATUS_PASS);
    }

    /**
     * Data provider example
     * @return Test data array
     */
    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        return new Object[][] {
                {"user1", "pass1", true},
                {"user2", "pass2", true},
                {"invalid", "invalid", false}
        };
    }

    @Test(dataProvider = "loginData", groups = {"datadriven"}, description = "Data-driven test example")
    public void testDataDriven(String username, String password, boolean expectedResult) {
        ReportManager.startTest("TC_DD_" + username, "Data-driven test for: " + username);
        ReportManager.assignCategory("DataDriven");

        ReportManager.logInfo("Testing with username: " + username);
        ReportManager.logInfo("Expected result: " + (expectedResult ? "Success" : "Failure"));

        // Navigate
        webActions.navigateToBaseUrl();

        // In real test, you would:
        // webActions.type("txt_username", username);
        // webActions.type("txt_password", password);
        // webActions.click("btn_login");

        ReportManager.logPass("Data-driven test completed for: " + username);
        ReportManager.endTest(FrameworkConstants.STATUS_PASS);
    }
}
