package org.example.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.utils.screenshot.ScreenshotManager;

/**
 * ReportManager - Manages test reporting using ExtentReports.
 * Provides methods to log test steps, results, and screenshots.
 * Thread-safe for parallel execution.
 */
public class ReportManager {

    private static final Logger logger = LogManager.getLogger(ReportManager.class);
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static ExtentReports extent;

    private ReportManager() {
        // Private constructor - utility class
    }

    /**
     * Initialize report manager
     */
    public static void initReports() {
        extent = ExtentManager.getInstance();
        logger.info("ReportManager initialized");
    }

    /**
     * Start a new test in the report
     * @param testName Test name
     */
    public static void startTest(String testName) {
        if (extent == null) {
            initReports();
        }
        ExtentTest test = extent.createTest(testName);
        extentTest.set(test);
        logger.info("Test started in report: {}", testName);
    }

    /**
     * Start a new test with description
     * @param testName Test name
     * @param description Test description
     */
    public static void startTest(String testName, String description) {
        if (extent == null) {
            initReports();
        }
        ExtentTest test = extent.createTest(testName, description);
        extentTest.set(test);
        logger.info("Test started in report: {} - {}", testName, description);
    }

    /**
     * End current test with status
     * @param status Test status (PASS/FAIL/SKIP)
     */
    public static void endTest(String status) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            switch (status.toUpperCase()) {
                case "PASS":
                    test.pass(MarkupHelper.createLabel("TEST PASSED", ExtentColor.GREEN));
                    break;
                case "FAIL":
                    test.fail(MarkupHelper.createLabel("TEST FAILED", ExtentColor.RED));
                    break;
                case "SKIP":
                    test.skip(MarkupHelper.createLabel("TEST SKIPPED", ExtentColor.YELLOW));
                    break;
                default:
                    test.info("Test completed with status: " + status);
            }
        }
        flushReports();
    }

    /**
     * Get current ExtentTest instance
     * @return ExtentTest for current thread
     */
    public static ExtentTest getTest() {
        return extentTest.get();
    }

    /**
     * Remove test from ThreadLocal
     */
    public static void removeTest() {
        extentTest.remove();
    }

    // ==================== Logging Methods ====================

    /**
     * Log info message
     * @param message Message to log
     */
    public static void logInfo(String message) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.info(message);
        }
        logger.info(message);
    }

    /**
     * Log pass message
     * @param message Message to log
     */
    public static void logPass(String message) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.pass(message);
        }
        logger.info("[PASS] {}", message);
    }

    /**
     * Log fail message
     * @param message Message to log
     */
    public static void logFail(String message) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.fail(message);
        }
        logger.error("[FAIL] {}", message);
    }

    /**
     * Log fail message with exception
     * @param message Message to log
     * @param throwable Exception
     */
    public static void logFail(String message, Throwable throwable) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.fail(message);
            test.fail(throwable);
        }
        logger.error("[FAIL] {} - {}", message, throwable.getMessage());
    }

    /**
     * Log skip message
     * @param message Message to log
     */
    public static void logSkip(String message) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.skip(message);
        }
        logger.warn("[SKIP] {}", message);
    }

    /**
     * Log warning message
     * @param message Message to log
     */
    public static void logWarning(String message) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.warning(message);
        }
        logger.warn("[WARNING] {}", message);
    }

    /**
     * Log debug message (only to logger, not to report)
     * @param message Message to log
     */
    public static void logDebug(String message) {
        logger.debug(message);
    }

    // ==================== Screenshot Methods ====================

    /**
     * Attach screenshot to report
     * @param screenshotPath Path to screenshot file
     */
    public static void attachScreenshot(String screenshotPath) {
        if (screenshotPath == null || screenshotPath.isEmpty()) {
            return;
        }

        ExtentTest test = extentTest.get();
        if (test != null) {
            try {
                test.addScreenCaptureFromPath(screenshotPath);
                logger.debug("Screenshot attached to report: {}", screenshotPath);
            } catch (Exception e) {
                logger.error("Failed to attach screenshot: {}", e.getMessage());
            }
        }
    }

    /**
     * Attach screenshot with title
     * @param screenshotPath Path to screenshot file
     * @param title Screenshot title
     */
    public static void attachScreenshot(String screenshotPath, String title) {
        if (screenshotPath == null || screenshotPath.isEmpty()) {
            return;
        }

        ExtentTest test = extentTest.get();
        if (test != null) {
            try {
                test.addScreenCaptureFromPath(screenshotPath, title);
                logger.debug("Screenshot '{}' attached to report", title);
            } catch (Exception e) {
                logger.error("Failed to attach screenshot: {}", e.getMessage());
            }
        }
    }

    /**
     * Attach screenshot as Base64
     * @param base64Screenshot Base64 encoded screenshot
     */
    public static void attachScreenshotBase64(String base64Screenshot) {
        if (base64Screenshot == null || base64Screenshot.isEmpty()) {
            return;
        }

        ExtentTest test = extentTest.get();
        if (test != null) {
            try {
                test.addScreenCaptureFromBase64String(base64Screenshot);
                logger.debug("Base64 screenshot attached to report");
            } catch (Exception e) {
                logger.error("Failed to attach Base64 screenshot: {}", e.getMessage());
            }
        }
    }

    /**
     * Log pass with screenshot
     * @param message Message to log
     * @param module Module name for screenshot
     * @param testCaseId Test case ID for screenshot
     */
    public static void logPassWithScreenshot(String message, String module, String testCaseId) {
        String screenshotPath = ScreenshotManager.captureScreenshot(module, testCaseId);
        ExtentTest test = extentTest.get();
        if (test != null) {
            try {
                test.pass(message, MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            } catch (Exception e) {
                test.pass(message);
                attachScreenshot(screenshotPath);
            }
        }
        logger.info("[PASS] {} - Screenshot: {}", message, screenshotPath);
    }

    /**
     * Log fail with screenshot
     * @param message Message to log
     * @param module Module name for screenshot
     * @param testCaseId Test case ID for screenshot
     */
    public static void logFailWithScreenshot(String message, String module, String testCaseId) {
        String screenshotPath = ScreenshotManager.captureScreenshot(module, testCaseId);
        ExtentTest test = extentTest.get();
        if (test != null) {
            try {
                test.fail(message, MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            } catch (Exception e) {
                test.fail(message);
                attachScreenshot(screenshotPath);
            }
        }
        logger.error("[FAIL] {} - Screenshot: {}", message, screenshotPath);
    }

    // ==================== Category and Author ====================

    /**
     * Assign category/tag to test
     * @param categories Categories to assign
     */
    public static void assignCategory(String... categories) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.assignCategory(categories);
        }
    }

    /**
     * Assign author to test
     * @param authors Authors to assign
     */
    public static void assignAuthor(String... authors) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.assignAuthor(authors);
        }
    }

    /**
     * Assign device to test
     * @param devices Devices to assign
     */
    public static void assignDevice(String... devices) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.assignDevice(devices);
        }
    }

    // ==================== Child Tests (for BDD) ====================

    /**
     * Create child node for step
     * @param stepName Step name
     * @return ExtentTest child node
     */
    public static ExtentTest createNode(String stepName) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            return test.createNode(stepName);
        }
        return null;
    }

    /**
     * Create child node with description
     * @param stepName Step name
     * @param description Step description
     * @return ExtentTest child node
     */
    public static ExtentTest createNode(String stepName, String description) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            return test.createNode(stepName, description);
        }
        return null;
    }

    // ==================== Flush and Cleanup ====================

    /**
     * Flush reports to file
     */
    public static void flushReports() {
        ExtentManager.flushReports();
    }

    /**
     * Get report file path
     * @return Path to report file
     */
    public static String getReportPath() {
        return ExtentManager.getReportPath();
    }

    /**
     * Add system info to report
     * @param key Info key
     * @param value Info value
     */
    public static void addSystemInfo(String key, String value) {
        ExtentManager.addSystemInfo(key, value);
    }

    /**
     * Cleanup resources
     */
    public static void cleanup() {
        removeTest();
        ExtentManager.removeInstance();
        logger.info("ReportManager cleanup completed");
    }
}
