package org.example.utils.screenshot;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.FrameworkConstants;
import org.example.core.driver.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ScreenshotManager - Manages screenshot capture and storage.
 * Supports full page screenshots and element-specific screenshots.
 *
 * Screenshot naming format: <SequenceNo>_<Module>_<TestCaseId>_<yyyyMMdd_HHmmss>.png
 * Storage location: target/screenshots/<Module>/
 */
public class ScreenshotManager {

    private static final Logger logger = LogManager.getLogger(ScreenshotManager.class);
    private static final AtomicInteger sequenceCounter = new AtomicInteger(0);

    private ScreenshotManager() {
        // Private constructor - utility class
    }

    /**
     * Capture screenshot with module and test case info
     * @param module Module name
     * @param testCaseId Test case ID
     * @return Path to saved screenshot
     */
    public static String captureScreenshot(String module, String testCaseId) {
        WebDriver driver = DriverManager.getDriver();
        return captureScreenshot(driver, module, testCaseId);
    }

    /**
     * Capture screenshot with specific driver
     * @param driver WebDriver instance
     * @param module Module name
     * @param testCaseId Test case ID
     * @return Path to saved screenshot
     */
    public static String captureScreenshot(WebDriver driver, String module, String testCaseId) {
        try {
            // Create directory structure
            String modulePath = FrameworkConstants.SCREENSHOTS_PATH + File.separator +
                               (module != null ? module : "General");
            createDirectoryIfNotExists(modulePath);

            // Generate filename
            String fileName = generateFileName(module, testCaseId);
            String fullPath = modulePath + File.separator + fileName;

            // Capture screenshot
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(fullPath);
            FileUtils.copyFile(srcFile, destFile);

            logger.info("Screenshot captured: {}", fullPath);
            return fullPath;

        } catch (IOException e) {
            logger.error("Failed to capture screenshot: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Capture screenshot with action name for step-level screenshots
     * Pattern: modulename_testcaseID_action_datetime.png
     * @param module Module name
     * @param testCaseId Test case ID
     * @param action Action name (e.g., "EnterUsername", "ClickLogin")
     * @return Path to saved screenshot
     */
    public static String captureScreenshotWithAction(String module, String testCaseId, String action) {
        WebDriver driver = DriverManager.getDriver();
        return captureScreenshotWithAction(driver, module, testCaseId, action);
    }

    /**
     * Capture screenshot with action name using specific driver
     * @param driver WebDriver instance
     * @param module Module name
     * @param testCaseId Test case ID
     * @param action Action name
     * @return Path to saved screenshot
     */
    public static String captureScreenshotWithAction(WebDriver driver, String module, String testCaseId, String action) {
        try {
            // Create directory structure
            String modulePath = FrameworkConstants.SCREENSHOTS_PATH + File.separator +
                               (module != null ? module : "General");
            createDirectoryIfNotExists(modulePath);

            // Generate filename with action
            String fileName = generateFileNameWithAction(module, testCaseId, action);
            String fullPath = modulePath + File.separator + fileName;

            // Capture screenshot
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(fullPath);
            FileUtils.copyFile(srcFile, destFile);

            logger.info("Screenshot captured [{}]: {}", action, fullPath);
            return fullPath;

        } catch (IOException e) {
            logger.error("Failed to capture screenshot: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Capture full page screenshot (including scrollable areas)
     * @param module Module name
     * @param testCaseId Test case ID
     * @return Path to saved screenshot
     */
    public static String captureFullPageScreenshot(String module, String testCaseId) {
        WebDriver driver = DriverManager.getDriver();
        return captureFullPageScreenshot(driver, module, testCaseId);
    }

    /**
     * Capture full page screenshot with specific driver
     * @param driver WebDriver instance
     * @param module Module name
     * @param testCaseId Test case ID
     * @return Path to saved screenshot
     */
    public static String captureFullPageScreenshot(WebDriver driver, String module, String testCaseId) {
        try {
            // Create directory structure
            String modulePath = FrameworkConstants.SCREENSHOTS_PATH + File.separator +
                               (module != null ? module : "General");
            createDirectoryIfNotExists(modulePath);

            // Generate filename
            String fileName = generateFileName(module, testCaseId);
            String fullPath = modulePath + File.separator + fileName;

            // Capture full page screenshot using AShot
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(100))
                    .takeScreenshot(driver);

            ImageIO.write(screenshot.getImage(), "PNG", new File(fullPath));

            logger.info("Full page screenshot captured: {}", fullPath);
            return fullPath;

        } catch (IOException e) {
            logger.error("Failed to capture full page screenshot: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Capture screenshot of specific element
     * @param element WebElement to capture
     * @param module Module name
     * @param testCaseId Test case ID
     * @return Path to saved screenshot
     */
    public static String captureElementScreenshot(WebElement element, String module, String testCaseId) {
        try {
            // Create directory structure
            String modulePath = FrameworkConstants.SCREENSHOTS_PATH + File.separator +
                               (module != null ? module : "General");
            createDirectoryIfNotExists(modulePath);

            // Generate filename
            String fileName = generateFileName(module, testCaseId);
            String fullPath = modulePath + File.separator + fileName;

            // Capture element screenshot
            File srcFile = element.getScreenshotAs(OutputType.FILE);
            File destFile = new File(fullPath);
            FileUtils.copyFile(srcFile, destFile);

            logger.info("Element screenshot captured: {}", fullPath);
            return fullPath;

        } catch (IOException e) {
            logger.error("Failed to capture element screenshot: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Capture screenshot and return as Base64 string
     * @return Base64 encoded screenshot
     */
    public static String captureScreenshotAsBase64() {
        WebDriver driver = DriverManager.getDriver();
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    /**
     * Capture screenshot and return as byte array
     * @return Screenshot as byte array
     */
    public static byte[] captureScreenshotAsBytes() {
        WebDriver driver = DriverManager.getDriver();
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    /**
     * Generate screenshot filename
     * Pattern: modulename_testcaseID_datetime.png
     * @param module Module name
     * @param testCaseId Test case ID
     * @return Filename string
     */
    private static String generateFileName(String module, String testCaseId) {
        String timestamp = new SimpleDateFormat(FrameworkConstants.SCREENSHOT_DATE_FORMAT).format(new Date());
        String moduleName = module != null ? module : "General";
        String tcId = testCaseId != null ? testCaseId : "TC";

        // Pattern: modulename_testcaseID_datetime.png
        return String.format("%s_%s_%s%s",
                sanitizeFileName(moduleName),
                sanitizeFileName(tcId),
                timestamp,
                FrameworkConstants.SCREENSHOT_EXTENSION);
    }

    /**
     * Generate screenshot filename with action name
     * Pattern: modulename_testcaseID_action_datetime.png
     * @param module Module name
     * @param testCaseId Test case ID
     * @param action Action name (e.g., "EnterUsername", "ClickLogin")
     * @return Filename string
     */
    private static String generateFileNameWithAction(String module, String testCaseId, String action) {
        String timestamp = new SimpleDateFormat(FrameworkConstants.SCREENSHOT_DATE_FORMAT).format(new Date());
        String moduleName = module != null ? module : "General";
        String tcId = testCaseId != null ? testCaseId : "TC";
        String actionName = action != null ? action : "Step";

        // Pattern: modulename_testcaseID_action_datetime.png
        return String.format("%s_%s_%s_%s%s",
                sanitizeFileName(moduleName),
                sanitizeFileName(tcId),
                sanitizeFileName(actionName),
                timestamp,
                FrameworkConstants.SCREENSHOT_EXTENSION);
    }

    /**
     * Sanitize filename by removing invalid characters
     * @param fileName Original filename
     * @return Sanitized filename
     */
    private static String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
    }

    /**
     * Create directory if it doesn't exist
     * @param path Directory path
     */
    private static void createDirectoryIfNotExists(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                logger.debug("Created directory: {}", path);
            }
        }
    }

    /**
     * Reset sequence counter (useful between test runs)
     */
    public static void resetSequenceCounter() {
        sequenceCounter.set(0);
    }

    /**
     * Get current sequence number
     * @return Current sequence number
     */
    public static int getCurrentSequence() {
        return sequenceCounter.get();
    }

    /**
     * Delete all screenshots in target folder
     */
    public static void clearScreenshots() {
        try {
            File screenshotsDir = new File(FrameworkConstants.SCREENSHOTS_PATH);
            if (screenshotsDir.exists()) {
                FileUtils.cleanDirectory(screenshotsDir);
                logger.info("Screenshots folder cleared");
            }
        } catch (IOException e) {
            logger.error("Failed to clear screenshots folder: {}", e.getMessage());
        }
    }

    /**
     * Get screenshot folder path for module
     * @param module Module name
     * @return Folder path
     */
    public static String getScreenshotFolderPath(String module) {
        return FrameworkConstants.SCREENSHOTS_PATH + File.separator +
               (module != null ? module : "General");
    }
}
