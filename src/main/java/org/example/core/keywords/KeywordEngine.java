
package org.example.core.keywords;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.ConfigLoader;
import org.example.config.EnvironmentConfig;
import org.example.config.FrameworkConstants;
import org.example.core.driver.DriverManager;
import org.example.reporting.ReportManager;
import org.example.utils.excel.ExcelReader;
import org.example.utils.screenshot.ScreenshotManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * KeywordEngine - Executes keywords mapped to WebActions methods.
 * Reads test steps from Excel and executes corresponding actions.
 *
 * Supported Keywords:
 * - openBrowser, closeBrowser
 * - navigateTo, navigateBack, navigateForward, refreshPage
 * - click, doubleClick, rightClick, jsClick
 * - type, clear, sendKeys, pressEnter, pressTab
 * - selectByText, selectByValue, selectByIndex
 * - check, uncheck
 * - getText, getAttribute, getTitle, getCurrentUrl
 * - verifyElementPresent, verifyText, verifyTextContains, verifyTitle
 * - waitForElement, waitForElementClickable, waitForElementInvisible
 * - scrollToElement, scrollToTop, scrollToBottom
 * - hover, dragAndDrop
 * - switchToWindow, switchToFrame, switchToDefaultContent
 * - acceptAlert, dismissAlert, getAlertText
 * - captureScreenshot
 */
public class KeywordEngine {

    private static final Logger logger = LogManager.getLogger(KeywordEngine.class);
    private WebActions webActions;
    private Map<String, String> testData;
    private String currentTestCaseId;
    private String currentModule;
    private int stepCount;
    private int passedSteps;
    private int failedSteps;
    private int skippedSteps;
    private String lastError;
    private String lastScreenshot;

    public KeywordEngine() {
        this.testData = new HashMap<>();
        this.stepCount = 0;
        this.passedSteps = 0;
        this.failedSteps = 0;
        this.skippedSteps = 0;
    }

    /**
     * Load test data from Excel
     * @param testDataMap Test data map (key -> value)
     */
    public void loadTestData(Map<String, String> testDataMap) {
        this.testData = testDataMap;
        logger.info("Test data loaded: {} entries", testData.size());
    }

    /**
     * Get test data value by key
     * @param key Data key
     * @return Value or empty string
     */
    public String getTestDataValue(String key) {
        // First check if key exists in test data
        if (testData.containsKey(key)) {
            return testData.get(key);
        }
        // Then check environment config
        String envValue = EnvironmentConfig.getInstance().get(key);
        if (envValue != null) {
            return envValue;
        }
        // Return the key itself if not found (might be a literal value)
        return key;
    }

    /**
     * Execute a single keyword
     * @param keyword Keyword to execute
     * @param elementKey Element key from OR (can be null)
     * @param data Data/value to use (can be null)
     * @return true if execution successful
     */
    public boolean executeKeyword(String keyword, String elementKey, String data) {
        stepCount++;
        String resolvedData = data != null ? getTestDataValue(data) : null;
        String stepDescription = buildStepDescription(keyword, elementKey, resolvedData);

        logger.info("Executing Step {}: {} - Element: {} - Data: {}",
                    stepCount, keyword, elementKey, resolvedData);

        try {
            boolean result = performKeywordAction(keyword.toLowerCase().trim(), elementKey, resolvedData);

            if (result) {
                passedSteps++;
                ReportManager.logPass(stepDescription);
                logger.info("Step {} PASSED", stepCount);
            } else {
                failedSteps++;
                lastError = "Verification failed";
                ReportManager.logFail(stepDescription + " - Verification failed");
                captureScreenshotOnFail();
                logger.error("Step {} FAILED - Verification failed", stepCount);
            }
            return result;

        } catch (Exception e) {
            failedSteps++;
            lastError = e.getMessage();
            ReportManager.logFail(stepDescription + " - " + e.getMessage());
            captureScreenshotOnFail();
            logger.error("Step {} FAILED with exception: {}", stepCount, e.getMessage());
            return false;
        }
    }

    /**
     * Perform the actual keyword action
     * @param keyword Lowercase keyword
     * @param elementKey Element key
     * @param data Data value
     * @return true if action successful
     */
    private boolean performKeywordAction(String keyword, String elementKey, String data) {
        switch (keyword) {
            // Browser Actions
            case "openbrowser":
                DriverManager.initDriver();
                webActions = new WebActions();
                return true;

            case "closebrowser":
                DriverManager.quitDriver();
                return true;

            // Navigation Actions
            case "navigateto":
            case "gotourl":
            case "openurl":
                webActions.navigateTo(data);
                return true;

            case "navigatetobaseurl":
            case "openapplication":
                webActions.navigateToBaseUrl();
                return true;

            case "navigateback":
            case "goback":
                webActions.navigateBack();
                return true;

            case "navigateforward":
            case "goforward":
                webActions.navigateForward();
                return true;

            case "refreshpage":
            case "refresh":
                webActions.refreshPage();
                return true;

            // Click Actions
            case "click":
                webActions.click(elementKey);
                return true;

            case "doubleclick":
                webActions.doubleClick(elementKey);
                return true;

            case "rightclick":
            case "contextclick":
                webActions.rightClick(elementKey);
                return true;

            case "jsclick":
            case "javascriptclick":
                webActions.jsClick(elementKey);
                return true;

            // Input Actions
            case "type":
            case "entertext":
            case "input":
            case "sendkeys":
                webActions.type(elementKey, data);
                return true;

            case "clear":
            case "cleartext":
                webActions.clear(elementKey);
                return true;

            case "pressenter":
            case "enter":
                webActions.pressEnter(elementKey);
                return true;

            case "presstab":
            case "tab":
                webActions.pressTab(elementKey);
                return true;

            // Dropdown Actions
            case "selectbytext":
            case "selectbyvisibletext":
            case "select":
                webActions.selectByText(elementKey, data);
                return true;

            case "selectbyvalue":
                webActions.selectByValue(elementKey, data);
                return true;

            case "selectbyindex":
                webActions.selectByIndex(elementKey, Integer.parseInt(data));
                return true;

            // Checkbox Actions
            case "check":
            case "checkbox":
                webActions.check(elementKey);
                return true;

            case "uncheck":
            case "uncheckbox":
                webActions.uncheck(elementKey);
                return true;

            // Get Actions
            case "gettext":
                String text = webActions.getText(elementKey);
                logger.info("Got text: {}", text);
                return true;

            case "getattribute":
                String attr = webActions.getAttribute(elementKey, data);
                logger.info("Got attribute {}: {}", data, attr);
                return true;

            case "gettitle":
                String title = webActions.getPageTitle();
                logger.info("Page title: {}", title);
                return true;

            case "getcurrenturl":
                String url = webActions.getCurrentUrl();
                logger.info("Current URL: {}", url);
                return true;

            // Verification Actions
            case "verifyelementpresent":
            case "iselementpresent":
                return webActions.isElementPresent(elementKey);

            case "verifyelementdisplayed":
            case "iselementdisplayed":
                return webActions.isDisplayed(elementKey);

            case "verifyelementenabled":
            case "iselementenabled":
                return webActions.isEnabled(elementKey);

            case "verifytext":
            case "asserttext":
                return webActions.verifyText(elementKey, data);

            case "verifytextcontains":
            case "asserttextcontains":
                return webActions.verifyTextContains(elementKey, data);

            case "verifytitle":
            case "asserttitle":
                return webActions.verifyTitle(data);

            case "verifytitlecontains":
            case "asserttitlecontains":
                return webActions.verifyTitleContains(data);

            case "verifyselected":
            case "isselected":
                return webActions.isSelected(elementKey);

            // Wait Actions
            case "waitforelement":
            case "waitforvisible":
                int waitTime = data != null ? Integer.parseInt(data) : ConfigLoader.getInstance().getExplicitWait();
                webActions.waitForElementVisible(elementKey, waitTime);
                return true;

            case "waitforclickable":
                int clickWait = data != null ? Integer.parseInt(data) : ConfigLoader.getInstance().getExplicitWait();
                webActions.waitForElementClickable(elementKey, clickWait);
                return true;

            case "waitforinvisible":
            case "waitforelementdisappear":
                int invisibleWait = data != null ? Integer.parseInt(data) : ConfigLoader.getInstance().getExplicitWait();
                webActions.waitForElementInvisible(elementKey, invisibleWait);
                return true;

            case "waitforpageload":
                webActions.waitForPageLoad();
                return true;

            case "wait":
            case "sleep":
            case "pause":
                webActions.staticWait(Integer.parseInt(data));
                return true;

            // Scroll Actions
            case "scrolltoelement":
            case "scrollto":
                webActions.scrollToElement(elementKey);
                return true;

            case "scrolltotop":
                webActions.scrollToTop();
                return true;

            case "scrolltobottom":
                webActions.scrollToBottom();
                return true;

            // Mouse Actions
            case "hover":
            case "mouseover":
            case "moveto":
                webActions.hover(elementKey);
                return true;

            case "draganddrop":
                // data contains target element key
                webActions.dragAndDrop(elementKey, data);
                return true;

            // Window Actions
            case "switchtowindow":
                webActions.switchToWindow(Integer.parseInt(data));
                return true;

            case "switchtonewwindow":
                webActions.switchToNewWindow();
                return true;

            case "closewindow":
                webActions.closeCurrentWindow();
                return true;

            case "maximizewindow":
                DriverManager.maximizeWindow();
                return true;

            // Frame Actions
            case "switchtoframe":
                if (elementKey != null && !elementKey.isEmpty()) {
                    webActions.switchToFrame(elementKey);
                } else {
                    webActions.switchToFrameByIndex(Integer.parseInt(data));
                }
                return true;

            case "switchtodefaultcontent":
            case "switchtomaincontent":
                webActions.switchToDefaultContent();
                return true;

            case "switchtoparentframe":
                webActions.switchToParentFrame();
                return true;

            // Alert Actions
            case "acceptalert":
            case "alertaccept":
                webActions.acceptAlert();
                return true;

            case "dismissalert":
            case "alertdismiss":
                webActions.dismissAlert();
                return true;

            case "getalerttext":
                String alertText = webActions.getAlertText();
                logger.info("Alert text: {}", alertText);
                return true;

            case "typeinalert":
            case "alertsendkeys":
                webActions.typeInAlert(data);
                webActions.acceptAlert();
                return true;

            // Screenshot
            case "capturescreenshot":
            case "screenshot":
            case "takescreenshot":
                String screenshotPath = ScreenshotManager.captureScreenshot(currentModule, currentTestCaseId);
                ReportManager.attachScreenshot(screenshotPath);
                return true;

            // Highlight (debugging)
            case "highlight":
            case "highlightelement":
                webActions.highlightElement(elementKey);
                return true;

            default:
                logger.warn("Unknown keyword: {}", keyword);
                throw new RuntimeException("Unknown keyword: " + keyword);
        }
    }

    /**
     * Build step description for reporting
     */
    private String buildStepDescription(String keyword, String elementKey, String data) {
        StringBuilder sb = new StringBuilder();
        sb.append(keyword);
        if (elementKey != null && !elementKey.isEmpty()) {
            sb.append(" on '").append(elementKey).append("'");
        }
        if (data != null && !data.isEmpty()) {
            sb.append(" with value '").append(data).append("'");
        }
        return sb.toString();
    }

    /**
     * Capture screenshot on failure
     */
    private void captureScreenshotOnFail() {
        if (ConfigLoader.getInstance().isScreenshotOnFail()) {
            try {
                lastScreenshot = ScreenshotManager.captureScreenshot(currentModule, currentTestCaseId);
                ReportManager.attachScreenshot(lastScreenshot);
            } catch (Exception e) {
                logger.error("Failed to capture screenshot: {}", e.getMessage());
            }
        }
    }

    /**
     * Execute test case from Excel
     * @param testCaseId Test case ID
     * @param steps List of step maps (Keyword, ElementKey, Data)
     * @return true if all steps passed
     */
    public boolean executeTestCase(String testCaseId, List<Map<String, String>> steps) {
        this.currentTestCaseId = testCaseId;
        resetCounters();

        logger.info("========== Starting Test Case: {} ==========", testCaseId);
        ReportManager.startTest(testCaseId);

        boolean allPassed = true;

        for (Map<String, String> step : steps) {
            String keyword = step.get("Keyword");
            String elementKey = step.get("ElementKey");
            String data = step.get("Data");

            if (keyword == null || keyword.trim().isEmpty()) {
                continue;
            }

            boolean result = executeKeyword(keyword, elementKey, data);
            if (!result) {
                allPassed = false;
                // Continue executing remaining steps or break based on config
                // For now, we continue to see all failures
            }
        }

        String status = allPassed ? FrameworkConstants.STATUS_PASS : FrameworkConstants.STATUS_FAIL;
        ReportManager.endTest(status);

        logger.info("========== Test Case {} Completed: {} ==========", testCaseId, status);
        logger.info("Results - Passed: {}, Failed: {}, Skipped: {}", passedSteps, failedSteps, skippedSteps);

        return allPassed;
    }

    /**
     * Set current module name
     * @param module Module name
     */
    public void setCurrentModule(String module) {
        this.currentModule = module;
    }

    /**
     * Reset step counters
     */
    private void resetCounters() {
        stepCount = 0;
        passedSteps = 0;
        failedSteps = 0;
        skippedSteps = 0;
        lastError = null;
        lastScreenshot = null;
    }

    // Getters for results
    public int getStepCount() { return stepCount; }
    public int getPassedSteps() { return passedSteps; }
    public int getFailedSteps() { return failedSteps; }
    public int getSkippedSteps() { return skippedSteps; }
    public String getLastError() { return lastError; }
    public String getLastScreenshot() { return lastScreenshot; }
    public String getCurrentTestCaseId() { return currentTestCaseId; }
}
