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
            logger.info("ManageBeneficiary page flag reset due to session re-login");
        });
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
        dashboardPage.waitForDashboardLoad();

        if (!isOnManageBeneficiaryPage) {
            try {
                dashboardPage.scrollToServices();
                dashboardPage.clickManageBeneficiary();
                sleep(1000);
                mbPage.waitForManageBeneficiaryPageLoad();
                isOnManageBeneficiaryPage = true;
            } catch (Exception e) {
                logger.warn("Service click failed, trying recovery: {}", e.getMessage());
                try {
                    mbPage.clickHomeNav();
                    sleep(500);
                    dashboardPage.scrollToServices();
                    dashboardPage.clickManageBeneficiary();
                    sleep(1000);
                    mbPage.waitForManageBeneficiaryPageLoad();
                    isOnManageBeneficiaryPage = true;
                } catch (Exception ex) {
                    logger.error("Failed to navigate to Manage Beneficiary page: {}", ex.getMessage());
                }
            }
        }

        if (isOnManageBeneficiaryPage) {
            try {
                if (!mbPage.isPageTitleDisplayed()) {
                    logger.info("MB page title not visible, re-navigating...");
                    navigateBackToManageBeneficiary();
                }
            } catch (Exception e) {
                logger.warn("Error checking MB page, attempting re-navigation...");
                navigateBackToManageBeneficiary();
            }
        }
    }

    private void navigateBackToManageBeneficiary() {
        logger.info("Attempting to navigate back to Manage Beneficiary page...");

        try {
            mbPage.clickBackArrow();
            sleep(500);
            if (mbPage.isPageTitleDisplayed()) {
                logger.info("Back arrow click successful - MB page title displayed");
                return;
            }
        } catch (Exception e) {
            logger.warn("Back arrow click failed: {}", e.getMessage());
        }

        try {
            mbPage.navigateBack();
            sleep(500);
            if (mbPage.isPageTitleDisplayed()) {
                logger.info("Browser back successful - MB page title displayed");
                return;
            }
        } catch (Exception e) {
            logger.warn("Browser back failed: {}", e.getMessage());
        }

        try {
            logger.info("Fallback: navigating via Home -> Manage Beneficiary");
            mbPage.clickHomeNav();
            sleep(500);
            dashboardPage.waitForDashboardLoad();
            dashboardPage.scrollToServices();
            dashboardPage.clickManageBeneficiary();
            sleep(1000);
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

        mbPage.clickShivalikBankRadio();

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
}
