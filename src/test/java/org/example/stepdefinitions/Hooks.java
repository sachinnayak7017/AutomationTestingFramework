package org.example.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.ConfigLoader;
import org.example.core.driver.DriverManager;
import org.example.reporting.ReportManager;
import org.example.utils.screenshot.ScreenshotManager;

import java.net.URI;

/**
 * Hooks - Cucumber hooks for test lifecycle management.
 * Contains @Before, @After, @BeforeAll, @AfterAll hooks.
 * Captures screenshot on EVERY step: modulename_testcaseID_Step01_datetime.png, Step02, etc.
 * Also captures FINAL screenshot: modulename_testcaseID_FINAL_PASSED/FAILED_datetime.png
 */
public class Hooks {

    private static final Logger logger = LogManager.getLogger(Hooks.class);

    // Step counter for each scenario (reset per scenario)
    private int stepCounter = 0;

    // Current module and testcase info (extracted from scenario)
    private String currentModule = "General";
    private String currentTestCaseId = "TC";

    // Static scenario name accessible by step definition classes for TestCaseID extraction
    private static String currentScenarioName = "";

    public static String getCurrentScenarioName() {
        return currentScenarioName;
    }

    /**
     * Before all scenarios - Suite level setup
     * Browser opens once here and stays open for all scenarios
     */
    @BeforeAll
    public static void beforeAll() {
        logger.info("========================================");
        logger.info("Cucumber Test Suite Started");
        logger.info("========================================");

        // Initialize reports
        ReportManager.initReports();

        // Log configuration
        ConfigLoader config = ConfigLoader.getInstance();
        config.printAllProperties();

        // Initialize WebDriver once for all scenarios
        DriverManager.initDriver(config.getBrowser(), config.isHeadless());
        logger.info("Browser opened - will remain open for all scenarios");
    }

    /**
     * Before each scenario - Test setup
     * Browser is already open from @BeforeAll, no need to initialize here
     * Extracts module name and testcase ID from scenario for screenshot naming
     * @param scenario Current scenario
     */
    @Before
    public void beforeScenario(Scenario scenario) {
        logger.info("----------------------------------------");
        logger.info("Starting Scenario: {}", scenario.getName());
        logger.info("Tags: {}", scenario.getSourceTagNames());
        logger.info("----------------------------------------");

        // Store scenario name for step definition classes to access
        currentScenarioName = scenario.getName();

        // Reset step counter for new scenario
        stepCounter = 0;

        // Extract module name from feature file path
        currentModule = extractModuleName(scenario);

        // Extract testcase ID from scenario name or tags
        currentTestCaseId = extractTestCaseId(scenario);

        logger.info("Module: {}, TestCaseId: {}", currentModule, currentTestCaseId);
        logger.info("Using existing browser session");
    }

    /**
     * Before scenario with specific tag
     * @param scenario Current scenario
     */
    @Before("@api")
    public void beforeApiScenario(Scenario scenario) {
        logger.info("API Scenario - Skipping browser initialization");
        // API tests don't need browser
    }

    /**
     * After each step - capture screenshot for every step.
     * Screenshot naming: Module_TestCaseId_Step01_datetime.png, Step02, etc.
     */
    @AfterStep
    public void afterStep(Scenario scenario) {
        stepCounter++;
        String stepName = String.format("Step%02d", stepCounter);
        captureStepScreenshot(scenario, currentModule, currentTestCaseId, stepName);
    }

    /**
     * After each scenario - Test teardown
     * Browser stays open - will be closed in @AfterAll
     * Captures FINAL screenshot with proper naming: modulename_testcaseID_FINAL_PASSED/FAILED_datetime.png
     * @param scenario Current scenario
     */
    @After
    public void afterScenario(Scenario scenario) {
        logger.info("========== SCENARIO COMPLETED ==========");
        logger.info("Scenario: {}", scenario.getName());
        logger.info("Module: {}, TestCaseId: {}", currentModule, currentTestCaseId);
        logger.info("Status: {}", scenario.getStatus());
        logger.info("=========================================");

        // Capture FINAL screenshot after scenario completion
        String finalStepName = scenario.isFailed() ? "FINAL_FAILED" : "FINAL_PASSED";

        logger.info("Capturing final screenshot for: {}_{}", currentTestCaseId, finalStepName);

        // Save final screenshot to file with proper naming
        captureStepScreenshot(scenario, currentModule, currentTestCaseId, finalStepName);

        if (scenario.isFailed()) {
            logger.error("SCENARIO FAILED: {} - {}", currentTestCaseId, scenario.getName());
        } else {
            logger.info("SCENARIO PASSED: {} - {}", currentTestCaseId, scenario.getName());
        }

        // Browser stays open - do NOT quit here
        // Will be closed in @AfterAll after all scenarios complete
    }

    /**
     * After all scenarios - Suite level teardown
     * Browser closes here after all scenarios are complete
     */
    @AfterAll
    public static void afterAll() {
        logger.info("========================================");
        logger.info("Cucumber Test Suite Finished");
        logger.info("========================================");

        // Reset session state before quitting driver
        org.example.utils.SessionManager.resetLoginState();

        // Quit WebDriver after all scenarios are done
        if (DriverManager.isDriverInitialized()) {
            try {
                DriverManager.quitDriver();
                logger.info("Browser closed after all scenarios completed");
            } catch (Exception e) {
                logger.warn("Browser was already closed: {}", e.getMessage());
            }
        }

        // Flush reports
        ReportManager.flushReports();
        logger.info("Report generated at: {}", ReportManager.getReportPath());
    }

    /**
     * Capture step screenshot with proper naming and attach to scenario + report.
     * Optimized: single capture reused for file save + Cucumber attach + Extent attach.
     * @param scenario Current scenario
     * @param module Module name
     * @param testCaseId Test case ID
     * @param stepName Step name (e.g., "Step01", "Step02", "FINAL_PASSED")
     */
    private void captureStepScreenshot(Scenario scenario, String module, String testCaseId, String stepName) {
        try {
            if (DriverManager.isDriverInitialized() && DriverManager.isDriverAlive()) {
                // Single capture as bytes - reuse for all attach targets
                byte[] screenshot = ScreenshotManager.captureScreenshotAsBytes();
                if (screenshot == null || screenshot.length == 0) {
                    logger.warn("Screenshot capture returned empty for {}_{}_{}", module, testCaseId, stepName);
                    return;
                }

                // Save to file from bytes (avoids second WebDriver capture)
                String screenshotPath = ScreenshotManager.saveScreenshotBytes(
                        screenshot, module, testCaseId, stepName);

                // Attach to Cucumber scenario report (reuse same bytes)
                try {
                    scenario.attach(screenshot, "image/png", module + "_" + testCaseId + "_" + stepName);
                } catch (Exception attachError) {
                    logger.warn("Could not attach screenshot to Cucumber report: {}", attachError.getMessage());
                }

                // Attach to Extent report
                try {
                    if (screenshotPath != null) {
                        ReportManager.attachScreenshot(screenshotPath, stepName);
                    }
                } catch (Exception reportError) {
                    logger.warn("Could not attach screenshot to Extent report: {}", reportError.getMessage());
                }

                logger.debug("Screenshot captured: {}_{}_{}",  module, testCaseId, stepName);
            }
        } catch (Exception e) {
            logger.error("Failed to capture screenshot [{}]: {}", stepName, e.getMessage());
        }
    }

    /**
     * Extract module name from feature file path
     * e.g., "features/login/Login.feature" -> "Login"
     * e.g., "features/DemoLogin.feature" -> "DemoLogin"
     * @param scenario Current scenario
     * @return Module name
     */
    private String extractModuleName(Scenario scenario) {
        try {
            URI uri = scenario.getUri();
            String path = uri.getPath();

            // Get filename without extension
            String fileName = path.substring(path.lastIndexOf('/') + 1);
            if (fileName.contains(".")) {
                fileName = fileName.substring(0, fileName.lastIndexOf('.'));
            }

            return fileName;
        } catch (Exception e) {
            logger.warn("Could not extract module name, using 'General'");
            return "General";
        }
    }

    /**
     * Extract test case ID from scenario
     * Priority: 1. Tag starting with @PL_ or @DB_ or @MB_ 2. Scenario ID 3. Scenario name (sanitized)
     * @param scenario Current scenario
     * @return Test case ID
     */
    private String extractTestCaseId(Scenario scenario) {
        try {
            // First, check for tag starting with module prefixes: @PL_ @DB_ @MB_ @FT_
            for (String tag : scenario.getSourceTagNames()) {
                if (tag.toUpperCase().startsWith("@PL_") || tag.toUpperCase().startsWith("@PL-")
                    || tag.toUpperCase().startsWith("@DB_") || tag.toUpperCase().startsWith("@DB-")
                    || tag.toUpperCase().startsWith("@MB_") || tag.toUpperCase().startsWith("@MB-")
                    || tag.toUpperCase().startsWith("@FT_") || tag.toUpperCase().startsWith("@FT-")) {
                    return tag.substring(1); // Remove @ prefix
                }
            }

            // Second, use scenario ID if available
            String scenarioId = scenario.getId();
            if (scenarioId != null && !scenarioId.isEmpty()) {
                // Extract last part of ID (usually unique identifier)
                String[] parts = scenarioId.split(";");
                if (parts.length > 0) {
                    String lastPart = parts[parts.length - 1];
                    // Clean up and limit length
                    lastPart = lastPart.replaceAll("[^a-zA-Z0-9]", "_");
                    if (lastPart.length() > 30) {
                        lastPart = lastPart.substring(0, 30);
                    }
                    return "TC_" + lastPart;
                }
            }

            // Third, use scenario name (sanitized)
            String name = scenario.getName();
            name = name.replaceAll("[^a-zA-Z0-9]", "_");
            if (name.length() > 30) {
                name = name.substring(0, 30);
            }
            return "TC_" + name;

        } catch (Exception e) {
            logger.warn("Could not extract testcase ID, using 'TC_Unknown'");
            return "TC_Unknown";
        }
    }
}
