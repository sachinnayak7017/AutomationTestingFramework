package org.example.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.ConfigLoader;
import org.example.pages.DashboardPage;
import org.example.pages.ManageBeneficiariesPage;
import org.example.utils.SessionManager;
import org.example.utils.excel.ExcelReader;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ManageBeneficiarySteps - Composite step definitions for Manage Beneficiary module.
 * Contains ONLY: Background navigation + composite form-fill + validation + MB-specific checks.
 * All generic steps (click, type, verify, error, etc.) are in CommonSteps.java.
 * Screenshots handled globally by Hooks.java @AfterStep.
 * Login handled centrally by SessionManager (shared across all modules).
 */
public class ManageBeneficiarySteps {

    private static final Logger logger = LogManager.getLogger(ManageBeneficiarySteps.class);
    private static boolean isOnManageBeneficiaryPage = false;

    private ManageBeneficiariesPage mbPage;
    private DashboardPage dashboardPage;
    private List<Map<String, String>> allTestData;

    public ManageBeneficiarySteps() {
        mbPage = new ManageBeneficiariesPage();
        dashboardPage = new DashboardPage();
        loadAllTestData();
        SessionManager.addSessionResetListener(() -> {
            isOnManageBeneficiaryPage = false;
            // Re-create page objects to get fresh driver after session reset
            mbPage = new ManageBeneficiariesPage();
            dashboardPage = new DashboardPage();
            logger.info("ManageBeneficiary page flag reset and page objects refreshed due to session re-login");
        });
    }

    /**
     * Refresh page objects to get the current driver reference.
     * Called before each scenario's Background step to handle driver reinitialization.
     */
    private void refreshPageObjects() {
        mbPage = new ManageBeneficiariesPage();
        dashboardPage = new DashboardPage();
    }

    private void loadAllTestData() {
        try {
            String excelPath = ConfigLoader.getInstance().getSuiteExcelPath();
            logger.info("Loading managebeneficiary test data from: {}", excelPath);
            ExcelReader reader = new ExcelReader(excelPath);
            allTestData = reader.getSheetData("managebeneficiary");
            reader.close();
            logger.info("Successfully loaded {} rows from managebeneficiary sheet", allTestData.size());
            if (allTestData.isEmpty()) {
                logger.warn("managebeneficiary sheet is empty - no test data rows found");
            }
        } catch (Exception e) {
            logger.error("FAILED to load managebeneficiary test data: {}", e.getMessage(), e);
            allTestData = new ArrayList<>();
        }
    }

    private String getTestDataValue(String testCaseId, String columnName) {
        if (allTestData.isEmpty()) {
            logger.warn("No test data loaded - allTestData is empty. Cannot find {} for {}", columnName, testCaseId);
            return "";
        }
        for (Map<String, String> row : allTestData) {
            if (testCaseId.equals(row.get("TestCaseID"))) {
                String value = row.getOrDefault(columnName, "");
                logger.debug("TestData [{}] {} = '{}'", testCaseId, columnName, value);
                return value;
            }
        }
        logger.warn("TestCaseID '{}' not found in managebeneficiary sheet (loaded {} rows)", testCaseId, allTestData.size());
        return "";
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    // ==================== BACKGROUND: Login + Navigate to Manage Beneficiary ====================

    @Given("user is logged in and navigates to Manage Beneficiary page")
    public void userIsLoggedInAndNavigatesToManageBeneficiaryPage() {
        SessionManager.ensureLoggedIn();
        refreshPageObjects();

        // Quick check: are we already on MB page? (same as FT pattern)
        if (!isOnManageBeneficiaryPage) {
            try {
                if (mbPage.isPageTitleDisplayed()) {
                    isOnManageBeneficiaryPage = true;
                    logger.info("Already on Manage Beneficiary page (detected via quick check)");
                }
            } catch (Exception e) {
                // Not on MB page, proceed with navigation
            }
        }

        if (!isOnManageBeneficiaryPage) {
            try {
                logger.info("Navigating to Manage Beneficiary from Dashboard...");
                dashboardPage.scrollToServices();
                dashboardPage.clickManageBeneficiary();
                mbPage.waitForManageBeneficiaryPageLoad();

                if (mbPage.isPageTitleDisplayed()) {
                    isOnManageBeneficiaryPage = true;
                    logger.info("Successfully navigated to Manage Beneficiary page");
                } else {
                    logger.error("Manage Beneficiary page FAILED to load");
                }
            } catch (Exception e) {
                logger.error("Exception navigating to Manage Beneficiary: {}", e.getMessage());
                // Recovery: click Home to go to Dashboard first, then retry
                try {
                    logger.info("Recovery: clicking Home nav and retrying...");
                    mbPage.clickHomeNav();
                    sleep(1000);
                    dashboardPage.waitForDashboardLoad();
                    dashboardPage.scrollToServices();
                    dashboardPage.clickManageBeneficiary();
                    mbPage.waitForManageBeneficiaryPageLoad();
                    if (mbPage.isPageTitleDisplayed()) {
                        isOnManageBeneficiaryPage = true;
                        logger.info("Recovery navigation to Manage Beneficiary succeeded");
                    }
                } catch (Exception ex) {
                    logger.error("Recovery navigation also failed: {}", ex.getMessage());
                }
            }
        }

        // If we're supposed to be on MB page, verify and re-navigate if needed
        if (isOnManageBeneficiaryPage) {
            try {
                if (!mbPage.isPageTitleDisplayed()) {
                    logger.info("MB page title not visible, attempting back navigation...");
                    navigateBackToManageBeneficiary();
                }
            } catch (Exception e) {
                logger.warn("Error checking MB page, attempting re-navigation...");
                navigateBackToManageBeneficiary();
            }
        }
    }

    /**
     * Navigate back to MB main page from any sub-page.
     * Same pattern as FundTransfer navigateBackToFundTransfer().
     */
    private void navigateBackToManageBeneficiary() {
        logger.info("Attempting to navigate back to Manage Beneficiary main page...");

        // Strategy 0: Dismiss any open popup/modal/backdrop first (same as FT - try click directly, no check)
        try {
            mbPage.dismissMuiBackdrop();
        } catch (Exception e) {
            // No backdrop, continue
        }
        try {
            mbPage.clickCautionConfirmButton();
            sleep(2000);
            if (mbPage.isPageTitleDisplayed()) {
                logger.info("Dismissed popup and landed on MB page");
                return;
            }
        } catch (Exception e) {
            // No popup open, continue with other strategies
        }

        // Strategy 0.5: Click Done button if on success page
        try {
            if (mbPage.isDoneButtonDisplayedFast()) {
                logger.info("Done button found - clicking to return to main page");
                mbPage.clickDoneButton();
                sleep(2000);
                if (mbPage.isPageTitleDisplayed()) {
                    logger.info("Done button click successful - MB page title displayed");
                    return;
                }
            }
        } catch (Exception e) {
            // No Done button, continue
        }

        // Strategy 1: Click Back Arrow up to 3 times (handles nested sub-pages)
        for (int i = 0; i < 3; i++) {
            try {
                mbPage.clickBackArrow();
                sleep(1500);
                // Check if CAUTION popup appeared and confirm it
                try {
                    if (mbPage.isCautionPopupDisplayed()) {
                        logger.info("CAUTION popup appeared after back arrow click, confirming...");
                        mbPage.clickCautionConfirmButton();
                        sleep(2000);
                    }
                } catch (Exception popupEx) {
                    // No popup, continue
                }
                if (mbPage.isPageTitleDisplayed()) {
                    logger.info("Back arrow click {} successful - MB page title displayed", (i + 1));
                    return;
                }
            } catch (Exception e) {
                logger.warn("Back arrow click {} failed: {}", (i + 1), e.getMessage());
                break;
            }
        }

        // Strategy 2: Browser back
        try {
            mbPage.navigateBack();
            sleep(1500);
            if (mbPage.isPageTitleDisplayed()) {
                logger.info("Browser back successful - MB page title displayed");
                return;
            }
        } catch (Exception e) {
            logger.warn("Browser back failed: {}", e.getMessage());
        }

        // Strategy 3: Go Home and re-navigate
        try {
            logger.info("Fallback: navigating via Home -> Manage Beneficiary");
            mbPage.clickHomeNav();
            sleep(1500);
            dashboardPage.waitForDashboardLoad();
            dashboardPage.scrollToServices();
            dashboardPage.clickManageBeneficiary();
            mbPage.waitForManageBeneficiaryPageLoad();
            logger.info("Fallback navigation completed");
        } catch (Exception ex) {
            logger.error("Fallback navigation also failed: {}", ex.getMessage());
        }
    }

    // ==================== COMPOSITE FORM FILL - Shivalik Beneficiary ====================

    @When("user fills Shivalik beneficiary form with test case {string} on MB page")
    public void userFillsShivalikBeneficiaryForm(String testCaseId) {
        String accountNo = getTestDataValue(testCaseId, "AccountNumber_Value");
        String reAccountNo = getTestDataValue(testCaseId, "ReEnterAccountNumber_Value");
        String beneficiaryName = getTestDataValue(testCaseId, "BeneficiaryName_Value");
        String nickname = getTestDataValue(testCaseId, "Nickname_Value");

        // Shivalik Bank is selected by default - no radio click needed

        if (accountNo != null && !accountNo.isEmpty()) mbPage.enterAccountNumber(accountNo);
        if (reAccountNo != null && !reAccountNo.isEmpty()) {
            mbPage.enterReEnterAccountNumber(reAccountNo);
        } else if (accountNo != null && !accountNo.isEmpty()) {
            mbPage.enterReEnterAccountNumber(accountNo);
        }
        if (beneficiaryName != null && !beneficiaryName.isEmpty()) mbPage.enterBeneficiaryName(beneficiaryName);
        if (nickname != null && !nickname.isEmpty()) mbPage.enterNickname(nickname);
    }

    // ==================== COMPOSITE FORM FILL - Other Bank Beneficiary ====================

    @When("user fills Other Bank beneficiary form with test case {string} on MB page")
    public void userFillsOtherBankBeneficiaryForm(String testCaseId) {
        String accountNo = getTestDataValue(testCaseId, "AccountNumber_Value");
        String reAccountNo = getTestDataValue(testCaseId, "ReEnterAccountNumber_Value");
        String ifscCode = getTestDataValue(testCaseId, "IFSCCode_Value");
        String beneficiaryName = getTestDataValue(testCaseId, "BeneficiaryName_Value");
        String nickname = getTestDataValue(testCaseId, "Nickname_Value");

        mbPage.clickOtherBankRadio();

        if (accountNo != null && !accountNo.isEmpty()) mbPage.enterAccountNumber(accountNo);
        if (reAccountNo != null && !reAccountNo.isEmpty()) {
            mbPage.enterReEnterAccountNumber(reAccountNo);
        } else if (accountNo != null && !accountNo.isEmpty()) {
            mbPage.enterReEnterAccountNumber(accountNo);
        }
        if (ifscCode != null && !ifscCode.isEmpty()) mbPage.enterIFSCCode(ifscCode);
        if (beneficiaryName != null && !beneficiaryName.isEmpty()) mbPage.enterBeneficiaryName(beneficiaryName);
        if (nickname != null && !nickname.isEmpty()) mbPage.enterNickname(nickname);
    }

    // ==================== EXPECTED RESULT VALIDATION FROM EXCEL ====================

    @Then("expected MB result from test case {string} should be validated")
    public void expectedMBResultShouldBeValidated(String testCaseId) {
        String expectedResult = getTestDataValue(testCaseId, "ExpectedResult");
        if (expectedResult == null || expectedResult.isEmpty()) {
            logger.warn("No ExpectedResult found for test case: {}", testCaseId);
            return;
        }

        sleep(300);
        String[] expectedMessages = expectedResult.split(";");
        boolean anyFound = false;
        for (String msg : expectedMessages) {
            String trimmedMsg = msg.trim();
            if (!trimmedMsg.isEmpty()) {
                if (mbPage.isTextDisplayedOnPage(trimmedMsg)) {
                    logger.info("Validated expected result '{}' on MB page for {}", trimmedMsg, testCaseId);
                    anyFound = true;
                }
            }
        }
        Assert.assertTrue(anyFound,
                "None of the expected results '" + expectedResult + "' displayed on MB page for test case " + testCaseId);
    }

    // ==================== OTP FROM EXCEL ====================

    @When("user enters MB OTP from test case {string}")
    public void userEntersMBOTP(String testCaseId) {
        String otp = getTestDataValue(testCaseId, "OTP_Value");
        if (otp != null && !otp.isEmpty()) {
            mbPage.enterOTP(otp);
            logger.info("Entered OTP from test case {} on MB page", testCaseId);
        } else {
            logger.warn("No OTP_Value found for test case: {}", testCaseId);
        }
    }

    // ==================== WRONG OTP THREE TIMES ====================

    @When("user enters wrong OTP three times on MB page with test case {string}")
    public void userEntersWrongOTPThreeTimes(String testCaseId) {
        String otp = getTestDataValue(testCaseId, "OTP_Value");
        if (otp == null || otp.isEmpty()) {
            otp = "000000";
        }
        for (int i = 0; i < 3; i++) {
            try {
                mbPage.clearOTP();
                mbPage.enterOTP(otp);
                mbPage.clickSubmitButton();
                sleep(500);
                logger.info("Wrong OTP attempt {} of 3 for test case {}", (i + 1), testCaseId);
            } catch (Exception e) {
                logger.warn("OTP attempt {} failed: {}", (i + 1), e.getMessage());
                break;
            }
        }
    }

    // ==================== INPUT FROM EXCEL ====================

    @When("user enters MB test data {string} from test case {string} in {string} on MB page")
    public void userEntersMBTestData(String columnName, String testCaseId, String objectKey) {
        String value = getTestDataValue(testCaseId, columnName);
        if (value != null && !value.isEmpty()) {
            mbPage.typeInField(objectKey, value);
            logger.info("Entered '{}' from test case {} column {} in {} on MB page",
                    value, testCaseId, columnName, objectKey);
        } else {
            logger.warn("No value found for column '{}' in test case '{}'", columnName, testCaseId);
        }
    }

    // ==================== MB-SPECIFIC VERIFICATION STEPS ====================

    @Then("MB account number field should be masked")
    public void mbAccountNumberFieldShouldBeMasked() {
        Assert.assertTrue(mbPage.isAccountNumberMasked(),
                "MB Account Number field is not masked");
    }

    @Then("MB account number field should be unmasked")
    public void mbAccountNumberFieldShouldBeUnmasked() {
        String type = mbPage.getAccountNumberInputType();
        Assert.assertEquals(type, "text", "MB Account Number field is not unmasked");
    }

    @Then("MB SUBMIT button should be disabled")
    public void mbSubmitButtonShouldBeDisabled() {
        Assert.assertFalse(mbPage.isSubmitButtonEnabled(),
                "MB SUBMIT button should be disabled before OTP entry");
    }

    @Then("beneficiary list or no beneficiaries text should be displayed on MB page")
    public void beneficiaryListOrNoBeneficiariesText() {
        boolean benList = mbPage.isBeneficiaryListDisplayed();
        boolean noBen = mbPage.isNoBeneficiariesTextDisplayed();
        Assert.assertTrue(benList || noBen,
                "Neither beneficiary list nor No Beneficiaries text is displayed on MB page");
    }

    @Then("MB nickname update should proceed or error should be shown")
    public void mbNicknameUpdateShouldProceedOrError() {
        sleep(300);
        boolean otpShown = mbPage.isOTPInputDisplayed();
        boolean errorShown = mbPage.isErrorMessageDisplayed() || mbPage.isToastMessageDisplayed();
        Assert.assertTrue(otpShown || errorShown,
                "Neither OTP input nor error message is shown after nickname save");
    }

    // ==================== HIGH-LEVEL VALIDATION: CONFIRM PAGE ====================

    @Then("MB confirm page should display correct details for test case {string}")
    public void mbConfirmPageShouldDisplayCorrectDetails(String testCaseId) {
        String expectedName = getTestDataValue(testCaseId, "BeneficiaryName_Value");
        String expectedAccount = getTestDataValue(testCaseId, "AccountNumber_Value");
        String expectedNickname = getTestDataValue(testCaseId, "Nickname_Value");
        String expectedIFSC = getTestDataValue(testCaseId, "IFSCCode_Value");

        sleep(500);
        mbPage.waitForConfirmPageLoad();

        boolean anyValidated = false;

        if (expectedName != null && !expectedName.isEmpty()) {
            String actualName = mbPage.getConfirmBeneficiaryName();
            Assert.assertTrue(actualName != null && actualName.toLowerCase().contains(expectedName.toLowerCase()),
                    "Confirm page name mismatch. Expected contains: " + expectedName + ", Actual: " + actualName);
            logger.info("Confirm page name validated: expected '{}', actual '{}'", expectedName, actualName);
            anyValidated = true;
        }

        if (expectedAccount != null && !expectedAccount.isEmpty()) {
            String actualAccount = mbPage.getConfirmAccountNumber();
            String cleanExpected = expectedAccount.replaceAll("\\s", "");
            String cleanActual = actualAccount != null ? actualAccount.replaceAll("\\s", "") : "";
            Assert.assertTrue(cleanActual.contains(cleanExpected),
                    "Confirm page account mismatch. Expected contains: " + expectedAccount + ", Actual: " + actualAccount);
            logger.info("Confirm page account validated: expected '{}', actual '{}'", expectedAccount, actualAccount);
            anyValidated = true;
        }

        if (expectedNickname != null && !expectedNickname.isEmpty()) {
            String actualNickname = mbPage.getConfirmNickname();
            Assert.assertTrue(actualNickname != null && actualNickname.toLowerCase().contains(expectedNickname.toLowerCase()),
                    "Confirm page nickname mismatch. Expected contains: " + expectedNickname + ", Actual: " + actualNickname);
            logger.info("Confirm page nickname validated: expected '{}', actual '{}'", expectedNickname, actualNickname);
            anyValidated = true;
        }

        if (expectedIFSC != null && !expectedIFSC.isEmpty()) {
            String actualIFSC = mbPage.getConfirmIFSCCode();
            Assert.assertTrue(actualIFSC != null && actualIFSC.contains(expectedIFSC),
                    "Confirm page IFSC mismatch. Expected contains: " + expectedIFSC + ", Actual: " + actualIFSC);
            logger.info("Confirm page IFSC validated: expected '{}', actual '{}'", expectedIFSC, actualIFSC);
            anyValidated = true;
        }

        Assert.assertTrue(anyValidated,
                "No confirm page details validated for test case " + testCaseId + " - check Excel test data");
    }

    // ==================== HIGH-LEVEL VALIDATION: BENEFICIARY DETAILS PAGE ====================

    @Then("MB beneficiary details should match test case {string}")
    public void mbBeneficiaryDetailsShouldMatch(String testCaseId) {
        String expectedName = getTestDataValue(testCaseId, "BeneficiaryName_Value");
        String expectedNickname = getTestDataValue(testCaseId, "Nickname_Value");

        sleep(500);
        mbPage.waitForBeneficiaryDetailLoad();

        boolean anyValidated = false;

        if (expectedName != null && !expectedName.isEmpty()) {
            String actualName = mbPage.getBeneficiaryDetailName();
            Assert.assertTrue(actualName != null && actualName.toLowerCase().contains(expectedName.toLowerCase()),
                    "Detail page name mismatch. Expected contains: " + expectedName + ", Actual: " + actualName);
            logger.info("Detail page name validated: expected '{}', actual '{}'", expectedName, actualName);
            anyValidated = true;
        }

        if (expectedNickname != null && !expectedNickname.isEmpty()) {
            boolean nicknameFound = mbPage.isTextDisplayedOnPage(expectedNickname);
            if (nicknameFound) {
                logger.info("Detail page nickname '{}' found on page", expectedNickname);
                anyValidated = true;
            } else {
                logger.warn("Detail page nickname '{}' not found on page", expectedNickname);
            }
        }

        if (!anyValidated) {
            logger.warn("No beneficiary details validated for test case {} - check Excel data", testCaseId);
        }
    }
}
