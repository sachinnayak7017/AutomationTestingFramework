package org.example.core.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.FrameworkConstants;
import org.example.reporting.ReportManager;
import org.testng.*;

import java.util.Arrays;

/**
 * TestNGListener - Implements TestNG listeners for test lifecycle events.
 * Handles reporting, screenshots, and logging at test execution points.
 */
public class TestNGListener implements ITestListener, ISuiteListener, IInvokedMethodListener {

    private static final Logger logger = LogManager.getLogger(TestNGListener.class);

    // ==================== ISuiteListener Methods ====================

    @Override
    public void onStart(ISuite suite) {
        logger.info("========================================");
        logger.info("Test Suite Started: {}", suite.getName());
        logger.info("========================================");

        // Initialize reports
        ReportManager.initReports();

        // Log suite parameters
        suite.getXmlSuite().getParameters().forEach((key, value) ->
                logger.info("Suite Parameter: {} = {}", key, value));
    }

    @Override
    public void onFinish(ISuite suite) {
        logger.info("========================================");
        logger.info("Test Suite Finished: {}", suite.getName());
        logger.info("========================================");

        // Flush reports
        ReportManager.flushReports();

        // Log summary
        logger.info("Report generated at: {}", ReportManager.getReportPath());
    }

    // ==================== ITestListener Methods ====================

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();
        String description = result.getMethod().getDescription();

        logger.info("----------------------------------------");
        logger.info("Test Started: {}.{}", className, testName);
        if (description != null && !description.isEmpty()) {
            logger.info("Description: {}", description);
        }
        logger.info("----------------------------------------");

        // Start test in report
        String reportTestName = testName;
        if (description != null && !description.isEmpty()) {
            ReportManager.startTest(reportTestName, description);
        } else {
            ReportManager.startTest(reportTestName);
        }

        // Assign categories from groups
        String[] groups = result.getMethod().getGroups();
        if (groups.length > 0) {
            ReportManager.assignCategory(groups);
            logger.info("Test Groups: {}", Arrays.toString(groups));
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        long duration = result.getEndMillis() - result.getStartMillis();

        logger.info("Test PASSED: {} (Duration: {}ms)", testName, duration);

        // Screenshots are captured per-step in Hooks.java @AfterStep — no duplicate here

        // End test in report
        ReportManager.endTest(FrameworkConstants.STATUS_PASS);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Throwable throwable = result.getThrowable();
        long duration = result.getEndMillis() - result.getStartMillis();

        logger.error("Test FAILED: {} (Duration: {}ms)", testName, duration);
        if (throwable != null) {
            logger.error("Failure Reason: {}", throwable.getMessage());
        }

        // Screenshots are captured per-step in Hooks.java @AfterStep — no duplicate here

        // Log failure in report
        if (throwable != null) {
            ReportManager.logFail("Test failed: " + throwable.getMessage(), throwable);
        }

        // End test in report
        ReportManager.endTest(FrameworkConstants.STATUS_FAIL);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Throwable throwable = result.getThrowable();

        logger.warn("Test SKIPPED: {}", testName);
        if (throwable != null) {
            logger.warn("Skip Reason: {}", throwable.getMessage());
        }

        // Start and end test in report (for skipped tests)
        ReportManager.startTest(testName);
        if (throwable != null) {
            ReportManager.logSkip("Test skipped: " + throwable.getMessage());
        } else {
            ReportManager.logSkip("Test skipped");
        }
        ReportManager.endTest(FrameworkConstants.STATUS_SKIP);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.warn("Test FAILED but within success percentage: {}", testName);
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.error("Test FAILED with timeout: {}", testName);
        onTestFailure(result);
    }

    @Override
    public void onStart(ITestContext context) {
        logger.info("Test Context Started: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Test Context Finished: {}", context.getName());

        // Log test context summary
        int passed = context.getPassedTests().size();
        int failed = context.getFailedTests().size();
        int skipped = context.getSkippedTests().size();
        int total = passed + failed + skipped;

        logger.info("Context Summary - Total: {}, Passed: {}, Failed: {}, Skipped: {}",
                total, passed, failed, skipped);
    }

    // ==================== IInvokedMethodListener Methods ====================

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        // Called before each method (including @Before/@After)
        if (method.isConfigurationMethod()) {
            logger.debug("Configuration method starting: {}", method.getTestMethod().getMethodName());
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        // Called after each method (including @Before/@After)
        if (method.isConfigurationMethod()) {
            logger.debug("Configuration method finished: {}", method.getTestMethod().getMethodName());

            // If configuration method failed, log it
            if (testResult.getStatus() == ITestResult.FAILURE) {
                logger.error("Configuration method failed: {}", method.getTestMethod().getMethodName());
                if (testResult.getThrowable() != null) {
                    logger.error("Failure: {}", testResult.getThrowable().getMessage());
                }
            }
        }
    }

    // ==================== Utility Methods ====================

    /**
     * Get test parameters as string
     * @param result Test result
     * @return Parameters string
     */
    private String getTestParameters(ITestResult result) {
        Object[] parameters = result.getParameters();
        if (parameters == null || parameters.length == 0) {
            return "";
        }
        return Arrays.toString(parameters);
    }

    /**
     * Get qualified test name
     * @param result Test result
     * @return Fully qualified test name
     */
    private String getQualifiedTestName(ITestResult result) {
        return result.getTestClass().getName() + "." + result.getMethod().getMethodName();
    }
}
