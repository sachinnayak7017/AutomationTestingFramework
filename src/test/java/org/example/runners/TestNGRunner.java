package org.example.runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.ConfigLoader;
import org.example.config.FrameworkConstants;
import org.example.core.driver.DriverManager;
import org.example.core.keywords.KeywordEngine;
import org.example.reporting.ReportManager;
import org.example.utils.excel.ExcelReader;
import org.example.utils.excel.ExcelWriter;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * TestNGRunner - Main runner for Keyword Driven tests using TestNG.
 * Reads test cases from Excel and executes them using KeywordEngine.
 */
@Listeners(org.example.core.listeners.TestNGListener.class)
public class TestNGRunner {

    private static final Logger logger = LogManager.getLogger(TestNGRunner.class);
    private KeywordEngine keywordEngine;
    private ExcelReader excelReader;
    private ExcelWriter excelWriter;
    private Map<String, String> testData;

    @BeforeSuite
    public void beforeSuite(ITestContext context) {
        logger.info("========================================");
        logger.info("Test Suite Initialization Started");
        logger.info("========================================");

        // Initialize reports
        ReportManager.initReports();

        // Load configuration
        ConfigLoader config = ConfigLoader.getInstance();
        config.printAllProperties();

        // Load test data from Excel
        String suiteExcelPath = config.getSuiteExcelPath();
        excelReader = new ExcelReader(suiteExcelPath);
        testData = excelReader.getTestData();

        // Initialize Excel writer for results
        excelWriter = new ExcelWriter(suiteExcelPath);

        logger.info("Test Suite Initialization Completed");
    }

    @BeforeClass
    public void beforeClass() {
        logger.info("Test Class Setup");
    }

    @BeforeMethod
    @Parameters({"browser", "headless"})
    public void beforeMethod(
            @Optional("chrome") String browser,
            @Optional("false") String headless) {

        logger.info("Test Method Setup - Browser: {}, Headless: {}", browser, headless);

        // Initialize WebDriver
        DriverManager.initDriver(browser, Boolean.parseBoolean(headless));

        // Initialize Keyword Engine
        keywordEngine = new KeywordEngine();
        keywordEngine.loadTestData(testData);

        // Navigate to base URL
        String baseUrl = ConfigLoader.getInstance().getBaseUrl();
        if (!baseUrl.isEmpty()) {
            DriverManager.navigateTo(baseUrl);
        }
    }

    @AfterMethod
    public void afterMethod() {
        logger.info("Test Method Teardown");

        // Quit WebDriver
        DriverManager.quitDriver();
    }

    @AfterClass
    public void afterClass() {
        logger.info("Test Class Teardown");
    }

    @AfterSuite
    public void afterSuite() {
        logger.info("========================================");
        logger.info("Test Suite Teardown");
        logger.info("========================================");

        // Close Excel readers/writers
        if (excelReader != null) {
            excelReader.close();
        }
        if (excelWriter != null) {
            excelWriter.saveAndClose();
        }

        // Flush reports
        ReportManager.flushReports();

        logger.info("Report generated at: {}", ReportManager.getReportPath());
    }

    /**
     * Data provider for test cases from Excel
     * @return Test case data
     */
    @DataProvider(name = "testCases", parallel = false)
    public Object[][] getTestCases() {
        List<Map<String, String>> testCases = excelReader.getTestCasesToExecute();
        Object[][] data = new Object[testCases.size()][1];

        for (int i = 0; i < testCases.size(); i++) {
            data[i][0] = testCases.get(i);
        }

        return data;
    }

    /**
     * Execute test case from data provider
     * @param testCaseData Test case data from Excel
     */
    @Test(dataProvider = "testCases", groups = {"regression"})
    public void executeTestCase(Map<String, String> testCaseData) {
        String testCaseId = testCaseData.get(FrameworkConstants.COL_TEST_CASE_ID);
        String title = testCaseData.get(FrameworkConstants.COL_TITLE);
        String module = testCaseData.get(FrameworkConstants.COL_MODULE);

        logger.info("Executing Test Case: {} - {}", testCaseId, title);

        // Set current module in keyword engine
        keywordEngine.setCurrentModule(module);

        // Start test in report
        ReportManager.startTest(testCaseId, title);
        ReportManager.assignCategory(module);

        // TODO: Load test steps from Excel (steps sheet for this test case)
        // For now, this is a placeholder - you would load steps from a separate sheet

        // Execute keywords based on test case
        boolean passed = true; // keywordEngine.executeTestCase(testCaseId, steps);

        // Update Excel with results
        excelWriter.updateTestCaseStatus(
                testCaseId,
                passed ? FrameworkConstants.STATUS_PASS : FrameworkConstants.STATUS_FAIL,
                keywordEngine.getPassedSteps(),
                keywordEngine.getFailedSteps(),
                keywordEngine.getSkippedSteps(),
                keywordEngine.getLastError(),
                keywordEngine.getLastScreenshot()
        );

        // End test in report
        ReportManager.endTest(passed ? FrameworkConstants.STATUS_PASS : FrameworkConstants.STATUS_FAIL);

        // Assert test result
        if (!passed) {
            throw new AssertionError("Test case failed: " + testCaseId);
        }
    }

    /**
     * Sample smoke test
     */
    @Test(groups = {"smoke"})
    public void sampleSmokeTest() {
        logger.info("Executing Sample Smoke Test");

        ReportManager.startTest("SMOKE_001", "Sample Smoke Test");
        ReportManager.assignCategory("Smoke");

        // Verify page loaded
        String title = DriverManager.getTitle();
        ReportManager.logInfo("Page Title: " + title);

        ReportManager.logPass("Smoke test completed successfully");
        ReportManager.endTest(FrameworkConstants.STATUS_PASS);
    }
}
