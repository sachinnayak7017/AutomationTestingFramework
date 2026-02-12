package org.example.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.ConfigLoader;
import org.example.core.driver.DriverManager;
import org.example.pages.PreLoginPage;
import org.example.utils.excel.ExcelReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * SessionManager - Centralized login and session management for all modules.
 * Handles:
 * - Single login across all modules (login once, share session)
 * - Session expiry detection (instant check, no 30s wait)
 * - Auto-re-login when session expires at runtime
 * - Reset module page flags on re-login
 *
 * Usage: All step definition classes should call SessionManager.ensureLoggedIn()
 * instead of maintaining their own login logic.
 */
public class SessionManager {

    private static final Logger logger = LogManager.getLogger(SessionManager.class);

    // Shared login state across ALL modules
    private static boolean isLoggedIn = false;

    // Listeners for session reset (modules register to get notified on re-login)
    private static final java.util.List<Runnable> sessionResetListeners = new java.util.ArrayList<>();

    // Cached test data for login credentials
    private static Map<String, String> loginData;

    private SessionManager() {
        // Prevent instantiation
    }

    /**
     * Ensure user is logged in. If session expired, auto-re-login.
     * If browser crashed, reinitialize driver and login.
     * Call this from every module's Background step.
     */
    public static void ensureLoggedIn() {
        // First check if browser is still alive
        if (!DriverManager.isDriverAlive()) {
            logger.warn("Browser window is not alive - reinitializing driver...");
            ConfigLoader config = ConfigLoader.getInstance();
            DriverManager.reinitializeDriver(config.getBrowser(), config.isHeadless());
            isLoggedIn = false;
            notifySessionReset();
        }

        // Check if session expired (redirected to login page)
        if (isLoggedIn && isSessionExpired()) {
            logger.warn("Session expired - detected login page. Re-logging in...");
            isLoggedIn = false;
            notifySessionReset();
        }

        if (!isLoggedIn) {
            performLogin();
        }
    }

    /**
     * Perform login using credentials from Excel test data.
     * Retries up to 2 times - if browser crashes, reinitializes driver and retries.
     */
    private static void performLogin() {
        loadLoginData();

        String url = getLoginData("BASE_URL");
        if (url.isEmpty()) {
            url = ConfigLoader.getInstance().getBaseUrl();
        }

        for (int attempt = 1; attempt <= 2; attempt++) {
            try {
                // Check driver health before each attempt
                if (!DriverManager.isDriverAlive()) {
                    logger.warn("Driver not alive before login attempt {} - reinitializing", attempt);
                    ConfigLoader config = ConfigLoader.getInstance();
                    DriverManager.reinitializeDriver(config.getBrowser(), config.isHeadless());
                }

                PreLoginPage preLoginPage = new PreLoginPage();

                // Navigate to login page
                DriverManager.navigateTo(url);
                preLoginPage.waitForPreLoginPageLoad();

                // Perform login flow
                preLoginPage.enterUserId(getLoginData("USER_ID_VALID"));
                preLoginPage.clickProceedButton();
                sleep(500);

                // Wait for password field to be interactable
                waitForPasswordField();
                preLoginPage.enterPassword(getLoginData("PASSWORD_VALID"));
                sleep(300);
                preLoginPage.clickAccessImageCheckbox();
                preLoginPage.clickLoginButton();
                sleep(1000);

                // Verify dashboard loaded
                preLoginPage.waitForPageLoad();
                isLoggedIn = true;
                logger.info("Login successful - session established (attempt {})", attempt);
                return;

            } catch (Exception e) {
                logger.error("Login attempt {} failed: {}", attempt, e.getMessage());
                if (attempt < 2) {
                    logger.info("Retrying login with fresh driver...");
                    sleep(2000);
                } else {
                    logger.error("Login failed after 2 attempts");
                    throw new RuntimeException("Login failed after 2 attempts: " + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Quick check if session expired (login page visible).
     * Uses findElements() which returns instantly - no 30s wait.
     * Returns true if browser crashed (treat as expired).
     */
    public static boolean isSessionExpired() {
        try {
            if (!DriverManager.isDriverAlive()) {
                return true; // Browser crashed = session expired
            }
            return !DriverManager.getDriver().findElements(
                    By.xpath("//input[@placeholder='Enter your user ID']")).isEmpty();
        } catch (Exception e) {
            return true; // Any error means session is gone
        }
    }

    /**
     * Check if currently logged in.
     */
    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    /**
     * Force reset login state (e.g., after explicit logout).
     */
    public static void resetLoginState() {
        isLoggedIn = false;
        notifySessionReset();
    }

    /**
     * Register a listener to be notified when session resets (re-login).
     * Modules can use this to reset their page navigation flags.
     */
    public static void addSessionResetListener(Runnable listener) {
        sessionResetListeners.add(listener);
    }

    /**
     * Notify all registered listeners that session was reset.
     */
    private static void notifySessionReset() {
        logger.info("Notifying {} module(s) of session reset", sessionResetListeners.size());
        for (Runnable listener : sessionResetListeners) {
            try {
                listener.run();
            } catch (Exception e) {
                logger.warn("Error notifying session reset listener: {}", e.getMessage());
            }
        }
    }

    /**
     * Load login credentials from Excel (cached).
     */
    private static void loadLoginData() {
        if (loginData == null) {
            try {
                String excelPath = ConfigLoader.getInstance().getSuiteExcelPath();
                ExcelReader reader = new ExcelReader(excelPath);
                loginData = reader.getTestData(); // reads default "prelogin" sheet
                reader.close();
            } catch (Exception e) {
                logger.error("Failed to load login data: {}", e.getMessage());
                loginData = new HashMap<>();
            }
        }
    }

    private static String getLoginData(String key) {
        if (loginData == null) loadLoginData();
        return loginData.getOrDefault(key, "");
    }

    /**
     * Wait for password field to be clickable/interactable.
     * After clicking Proceed, the page transitions and password field needs time.
     */
    private static void waitForPasswordField() {
        try {
            By passwordLocator = By.xpath(
                    "//input[@type='password' or @placeholder='Enter your password' or @placeholder='Enter Password']");
            new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(30))
                    .until(ExpectedConditions.elementToBeClickable(passwordLocator));
            logger.debug("Password field is interactable");
        } catch (Exception e) {
            logger.warn("Password field wait timed out, attempting anyway: {}", e.getMessage());
        }
    }

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
