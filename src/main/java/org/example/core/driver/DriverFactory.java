package org.example.core.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.ConfigLoader;
import org.example.config.FrameworkConstants;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * DriverFactory - Creates and configures WebDriver instances.
 * Supports local execution, Selenium Grid, and cloud platforms.
 */
public class DriverFactory {

    private static final Logger logger = LogManager.getLogger(DriverFactory.class);
    private final ConfigLoader config;

    public DriverFactory() {
        this.config = ConfigLoader.getInstance();
    }

    /**
     * Create WebDriver instance based on configuration
     * @return Configured WebDriver instance
     */
    public WebDriver createDriver() {
        String browser = config.getBrowser();
        boolean headless = config.isHeadless();
        BrowserType browserType = BrowserType.fromString(browser, headless);

        logger.info("Creating WebDriver for browser: {}", browserType);

        WebDriver driver;

        if (config.isGridExecution()) {
            driver = createRemoteDriver(browserType);
        } else {
            driver = createLocalDriver(browserType);
        }

        configureDriver(driver);
        return driver;
    }

    /**
     * Create WebDriver instance with specific browser type
     * @param browserType Browser type enum
     * @return Configured WebDriver instance
     */
    public WebDriver createDriver(BrowserType browserType) {
        logger.info("Creating WebDriver for browser: {}", browserType);

        WebDriver driver;

        if (config.isGridExecution()) {
            driver = createRemoteDriver(browserType);
        } else {
            driver = createLocalDriver(browserType);
        }

        configureDriver(driver);
        return driver;
    }

    /**
     * Create local WebDriver instance
     * @param browserType Browser type
     * @return WebDriver instance
     */
    private WebDriver createLocalDriver(BrowserType browserType) {
        switch (browserType) {
            case CHROME:
            case CHROME_HEADLESS:
                return createChromeDriver(browserType.isHeadless());
            case EDGE:
            case EDGE_HEADLESS:
                return createEdgeDriver(browserType.isHeadless());
            case FIREFOX:
            case FIREFOX_HEADLESS:
                return createFirefoxDriver(browserType.isHeadless());
            case SAFARI:
                return createSafariDriver();
            default:
                logger.warn("Unknown browser type: {}. Defaulting to Chrome.", browserType);
                return createChromeDriver(false);
        }
    }

    /**
     * Create Chrome WebDriver
     * @param headless Whether to run in headless mode
     * @return ChromeDriver instance
     */
    private WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        if (headless) {
            options.addArguments("--headless=new");
        }

        // Common Chrome options
        options.addArguments("--start-maximized");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        // Disable automation flags
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);

        // Set download preferences
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("download.prompt_for_download", false);
        prefs.put("credentials_enable_service", false);
        prefs.put("password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        logger.info("Chrome WebDriver created successfully (headless: {})", headless);
        return new ChromeDriver(options);
    }

    /**
     * Create Edge WebDriver
     * @param headless Whether to run in headless mode
     * @return EdgeDriver instance
     */
    private WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();

        EdgeOptions options = new EdgeOptions();

        if (headless) {
            options.addArguments("--headless=new");
        }

        // Common Edge options
        options.addArguments("--start-maximized");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        // Disable automation flags
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);

        logger.info("Edge WebDriver created successfully (headless: {})", headless);
        return new EdgeDriver(options);
    }

    /**
     * Create Firefox WebDriver
     * @param headless Whether to run in headless mode
     * @return FirefoxDriver instance
     */
    private WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("--headless");
        }

        // Common Firefox options
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");

        // Firefox preferences
        options.addPreference("browser.download.folderList", 2);
        options.addPreference("browser.download.useDownloadDir", true);
        options.addPreference("browser.helperApps.neverAsk.saveToDisk", "application/pdf,application/octet-stream");
        options.addPreference("pdfjs.disabled", true);

        logger.info("Firefox WebDriver created successfully (headless: {})", headless);
        return new FirefoxDriver(options);
    }

    /**
     * Create Safari WebDriver
     * @return SafariDriver instance
     */
    private WebDriver createSafariDriver() {
        logger.info("Safari WebDriver created successfully");
        return new SafariDriver();
    }

    /**
     * Create Remote WebDriver for Grid execution
     * @param browserType Browser type
     * @return RemoteWebDriver instance
     */
    private WebDriver createRemoteDriver(BrowserType browserType) {
        String gridUrl = config.getGridUrl();

        try {
            URL hubUrl = new URL(gridUrl);

            switch (browserType) {
                case CHROME:
                case CHROME_HEADLESS:
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (browserType.isHeadless()) {
                        chromeOptions.addArguments("--headless=new");
                    }
                    chromeOptions.addArguments("--start-maximized");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    logger.info("Remote Chrome WebDriver created for Grid: {}", gridUrl);
                    return new RemoteWebDriver(hubUrl, chromeOptions);

                case EDGE:
                case EDGE_HEADLESS:
                    EdgeOptions edgeOptions = new EdgeOptions();
                    if (browserType.isHeadless()) {
                        edgeOptions.addArguments("--headless=new");
                    }
                    edgeOptions.addArguments("--start-maximized");
                    logger.info("Remote Edge WebDriver created for Grid: {}", gridUrl);
                    return new RemoteWebDriver(hubUrl, edgeOptions);

                case FIREFOX:
                case FIREFOX_HEADLESS:
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (browserType.isHeadless()) {
                        firefoxOptions.addArguments("--headless");
                    }
                    logger.info("Remote Firefox WebDriver created for Grid: {}", gridUrl);
                    return new RemoteWebDriver(hubUrl, firefoxOptions);

                default:
                    ChromeOptions defaultOptions = new ChromeOptions();
                    defaultOptions.addArguments("--start-maximized");
                    logger.info("Remote Chrome WebDriver (default) created for Grid: {}", gridUrl);
                    return new RemoteWebDriver(hubUrl, defaultOptions);
            }
        } catch (MalformedURLException e) {
            logger.error("Invalid Grid URL: {}", gridUrl);
            throw new RuntimeException("Invalid Grid URL: " + gridUrl, e);
        }
    }

    /**
     * Configure WebDriver with timeouts
     * @param driver WebDriver instance
     */
    private void configureDriver(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(FrameworkConstants.DEFAULT_SCRIPT_TIMEOUT));

        // Maximize window only for non-headless mode
        if (!config.isHeadless()) {
            driver.manage().window().maximize();
        }

        logger.info("WebDriver configured with implicit wait: {}s, page load timeout: {}s",
                config.getImplicitWait(), config.getPageLoadTimeout());
    }
}
