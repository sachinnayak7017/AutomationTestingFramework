package org.example.core.driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * DriverManager - Manages WebDriver lifecycle using ThreadLocal.
 * Ensures thread-safe driver management for parallel test execution.
 */
public class DriverManager {

    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<String> browserNameThreadLocal = new ThreadLocal<>();

    private DriverManager() {
        // Private constructor to prevent instantiation
    }

    /**
     * Initialize WebDriver for current thread
     */
    public static void initDriver() {
        if (driverThreadLocal.get() == null) {
            DriverFactory factory = new DriverFactory();
            WebDriver driver = factory.createDriver();
            driverThreadLocal.set(driver);
            browserNameThreadLocal.set(driver.getClass().getSimpleName());
            logger.info("WebDriver initialized for thread: {}", Thread.currentThread().getName());
        } else {
            logger.warn("WebDriver already initialized for thread: {}", Thread.currentThread().getName());
        }
    }

    /**
     * Initialize WebDriver with specific browser type
     * @param browserType Browser type enum
     */
    public static void initDriver(BrowserType browserType) {
        if (driverThreadLocal.get() == null) {
            DriverFactory factory = new DriverFactory();
            WebDriver driver = factory.createDriver(browserType);
            driverThreadLocal.set(driver);
            browserNameThreadLocal.set(browserType.toString());
            logger.info("WebDriver ({}) initialized for thread: {}", browserType, Thread.currentThread().getName());
        } else {
            logger.warn("WebDriver already initialized for thread: {}", Thread.currentThread().getName());
        }
    }

    /**
     * Initialize WebDriver with browser name string
     * @param browserName Browser name
     * @param headless Whether to run headless
     */
    public static void initDriver(String browserName, boolean headless) {
        BrowserType browserType = BrowserType.fromString(browserName, headless);
        initDriver(browserType);
    }

    /**
     * Get WebDriver for current thread
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            logger.warn("WebDriver not initialized for thread: {}. Initializing now.", Thread.currentThread().getName());
            initDriver();
            driver = driverThreadLocal.get();
        }
        return driver;
    }

    /**
     * Check if WebDriver is initialized for current thread
     * @return true if driver is initialized
     */
    public static boolean isDriverInitialized() {
        return driverThreadLocal.get() != null;
    }

    /**
     * Get browser name for current thread
     * @return Browser name
     */
    public static String getBrowserName() {
        return browserNameThreadLocal.get();
    }

    /**
     * Quit WebDriver and remove from ThreadLocal
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver quit successfully for thread: {}", Thread.currentThread().getName());
            } catch (Exception e) {
                logger.error("Error quitting WebDriver: {}", e.getMessage());
            } finally {
                driverThreadLocal.remove();
                browserNameThreadLocal.remove();
            }
        } else {
            logger.warn("No WebDriver to quit for thread: {}", Thread.currentThread().getName());
        }
    }

    /**
     * Close current window (not quit driver)
     */
    public static void closeWindow() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.close();
                logger.info("Browser window closed for thread: {}", Thread.currentThread().getName());
            } catch (Exception e) {
                logger.error("Error closing window: {}", e.getMessage());
            }
        }
    }

    /**
     * Navigate to URL
     * @param url URL to navigate to
     */
    public static void navigateTo(String url) {
        WebDriver driver = getDriver();
        driver.get(url);
        logger.info("Navigated to: {}", url);
    }

    /**
     * Get current URL
     * @return Current URL
     */
    public static String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    /**
     * Get page title
     * @return Page title
     */
    public static String getTitle() {
        return getDriver().getTitle();
    }

    /**
     * Refresh current page
     */
    public static void refresh() {
        getDriver().navigate().refresh();
        logger.info("Page refreshed");
    }

    /**
     * Navigate back
     */
    public static void navigateBack() {
        getDriver().navigate().back();
        logger.info("Navigated back");
    }

    /**
     * Navigate forward
     */
    public static void navigateForward() {
        getDriver().navigate().forward();
        logger.info("Navigated forward");
    }

    /**
     * Delete all cookies
     */
    public static void deleteAllCookies() {
        getDriver().manage().deleteAllCookies();
        logger.info("All cookies deleted");
    }

    /**
     * Get current window handle
     * @return Window handle
     */
    public static String getWindowHandle() {
        return getDriver().getWindowHandle();
    }

    /**
     * Get all window handles
     * @return Set of window handles
     */
    public static java.util.Set<String> getWindowHandles() {
        return getDriver().getWindowHandles();
    }

    /**
     * Switch to window by handle
     * @param handle Window handle
     */
    public static void switchToWindow(String handle) {
        getDriver().switchTo().window(handle);
        logger.info("Switched to window: {}", handle);
    }

    /**
     * Switch to new window (latest opened)
     */
    public static void switchToNewWindow() {
        String currentHandle = getWindowHandle();
        for (String handle : getWindowHandles()) {
            if (!handle.equals(currentHandle)) {
                switchToWindow(handle);
                break;
            }
        }
    }

    /**
     * Switch to frame by index
     * @param index Frame index
     */
    public static void switchToFrame(int index) {
        getDriver().switchTo().frame(index);
        logger.info("Switched to frame by index: {}", index);
    }

    /**
     * Switch to frame by name or id
     * @param nameOrId Frame name or id
     */
    public static void switchToFrame(String nameOrId) {
        getDriver().switchTo().frame(nameOrId);
        logger.info("Switched to frame: {}", nameOrId);
    }

    /**
     * Switch to default content (out of all frames)
     */
    public static void switchToDefaultContent() {
        getDriver().switchTo().defaultContent();
        logger.info("Switched to default content");
    }

    /**
     * Switch to parent frame
     */
    public static void switchToParentFrame() {
        getDriver().switchTo().parentFrame();
        logger.info("Switched to parent frame");
    }

    /**
     * Accept alert
     */
    public static void acceptAlert() {
        getDriver().switchTo().alert().accept();
        logger.info("Alert accepted");
    }

    /**
     * Dismiss alert
     */
    public static void dismissAlert() {
        getDriver().switchTo().alert().dismiss();
        logger.info("Alert dismissed");
    }

    /**
     * Get alert text
     * @return Alert text
     */
    public static String getAlertText() {
        return getDriver().switchTo().alert().getText();
    }

    /**
     * Send keys to alert
     * @param text Text to send
     */
    public static void sendKeysToAlert(String text) {
        getDriver().switchTo().alert().sendKeys(text);
        logger.info("Sent text to alert: {}", text);
    }

    /**
     * Maximize window
     */
    public static void maximizeWindow() {
        getDriver().manage().window().maximize();
        logger.info("Window maximized");
    }

    /**
     * Minimize window
     */
    public static void minimizeWindow() {
        getDriver().manage().window().minimize();
        logger.info("Window minimized");
    }

    /**
     * Set window size
     * @param width Width in pixels
     * @param height Height in pixels
     */
    public static void setWindowSize(int width, int height) {
        getDriver().manage().window().setSize(new org.openqa.selenium.Dimension(width, height));
        logger.info("Window size set to: {}x{}", width, height);
    }
}
