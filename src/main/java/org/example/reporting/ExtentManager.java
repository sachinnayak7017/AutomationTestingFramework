package org.example.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.ConfigLoader;
import org.example.config.FrameworkConstants;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ExtentManager - Manages ExtentReports instance.
 * Configures and provides access to the report object.
 */
public class ExtentManager {

    private static final Logger logger = LogManager.getLogger(ExtentManager.class);
    private static ExtentReports extent;
    private static String reportPath;

    private ExtentManager() {
        // Private constructor - singleton
    }

    /**
     * Get ExtentReports instance
     * @return ExtentReports instance
     */
    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }

    /**
     * Create ExtentReports instance with configuration
     */
    private static void createInstance() {
        // Create reports directory
        createReportsDirectory();

        // Generate unique report name with timestamp
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        reportPath = FrameworkConstants.REPORTS_PATH + File.separator + "ExtentReport_" + timestamp + ".html";

        // Initialize ExtentSparkReporter
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);

        // Configure reporter
        configureReporter(sparkReporter);

        // Create ExtentReports and attach reporter
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // Set system information
        setSystemInfo();

        logger.info("ExtentReports initialized: {}", reportPath);
    }

    /**
     * Create reports directory if not exists
     */
    private static void createReportsDirectory() {
        File reportsDir = new File(FrameworkConstants.REPORTS_PATH);
        if (!reportsDir.exists()) {
            boolean created = reportsDir.mkdirs();
            if (created) {
                logger.debug("Created reports directory: {}", FrameworkConstants.REPORTS_PATH);
            }
        }
    }

    /**
     * Configure ExtentSparkReporter
     * @param reporter ExtentSparkReporter instance
     */
    private static void configureReporter(ExtentSparkReporter reporter) {
        reporter.config().setDocumentTitle(FrameworkConstants.REPORT_TITLE);
        reporter.config().setReportName(FrameworkConstants.REPORT_NAME);
        reporter.config().setTheme(getTheme());
        reporter.config().setEncoding("UTF-8");
        reporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");

        // Custom CSS for better styling
        reporter.config().setCss(
                ".badge-primary { background-color: #2196F3; }" +
                ".badge-success { background-color: #4CAF50; }" +
                ".badge-danger { background-color: #F44336; }" +
                ".badge-warning { background-color: #FF9800; }" +
                ".badge-info { background-color: #00BCD4; }"
        );
    }

    /**
     * Get theme from configuration
     * @return Theme enum
     */
    private static Theme getTheme() {
        String themeName = FrameworkConstants.REPORT_THEME;
        if ("DARK".equalsIgnoreCase(themeName)) {
            return Theme.DARK;
        }
        return Theme.STANDARD;
    }

    /**
     * Set system information in report
     */
    private static void setSystemInfo() {
        ConfigLoader config = ConfigLoader.getInstance();

        extent.setSystemInfo("Application", "Test Automation Framework");
        extent.setSystemInfo("Environment", config.getEnvironment().toUpperCase());
        extent.setSystemInfo("Browser", config.getBrowser());
        extent.setSystemInfo("Run Mode", config.getRunMode());
        extent.setSystemInfo("Headless", String.valueOf(config.isHeadless()));
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("OS Version", System.getProperty("os.version"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("User", System.getProperty("user.name"));

        if (!config.getBaseUrl().isEmpty()) {
            extent.setSystemInfo("Base URL", config.getBaseUrl());
        }
    }

    /**
     * Flush reports and write to file
     */
    public static void flushReports() {
        if (extent != null) {
            extent.flush();
            logger.info("Extent report flushed to: {}", reportPath);
        }
    }

    /**
     * Get report path
     * @return Path to generated report
     */
    public static String getReportPath() {
        return reportPath;
    }

    /**
     * Remove ExtentReports instance (for cleanup)
     */
    public static void removeInstance() {
        if (extent != null) {
            extent.flush();
            extent = null;
            logger.info("ExtentReports instance removed");
        }
    }

    /**
     * Add custom system info
     * @param key Info key
     * @param value Info value
     */
    public static void addSystemInfo(String key, String value) {
        if (extent != null) {
            extent.setSystemInfo(key, value);
        }
    }
}
