package org.example.stepdefinitions;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.ConfigLoader;
import org.example.pages.DashboardPage;
import org.example.pages.FundTransferPage;
import org.example.utils.SessionManager;
import org.example.utils.excel.ExcelReader;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * FundTransferSteps - Composite step definitions for Fund Transfer module.
 * Contains ONLY: Dashboard navigation + composite form-fill methods.
 * All generic steps (click, type, verify, error, etc.) are in CommonSteps.java.
 * Screenshots handled globally by Hooks.java @AfterStep.
 * Login handled centrally by SessionManager (shared across all modules).
 */
public class FundTransferSteps {

    private static final Logger logger = LogManager.getLogger(FundTransferSteps.class);
    private static boolean isOnFundTransferPage = false;

    private FundTransferPage ftPage;
    private DashboardPage dashboardPage;
    private List<Map<String, String>> allTestData;

    public FundTransferSteps() {
        ftPage = new FundTransferPage();
        dashboardPage = new DashboardPage();
        loadAllTestData();
        // Register session reset listener: when session expires and re-login happens,
        // reset the page flag so we re-navigate to Fund Transfer page
        SessionManager.addSessionResetListener(() -> {
            isOnFundTransferPage = false;
            logger.info("FundTransfer page flag reset due to session re-login");
        });
    }

    private void loadAllTestData() {
        try {
            String excelPath = ConfigLoader.getInstance().getSuiteExcelPath();
            logger.info("Loading fundtransfer test data from: {}", excelPath);
            ExcelReader reader = new ExcelReader(excelPath);
            allTestData = reader.getSheetData("fundtransfer");
            reader.close();
            logger.info("Successfully loaded {} rows from fundtransfer sheet", allTestData.size());
            if (allTestData.isEmpty()) {
                logger.warn("fundtransfer sheet is empty - no test data rows found");
            }
        } catch (Exception e) {
            logger.error("FAILED to load fundtransfer test data: {}", e.getMessage(), e);
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
        logger.warn("TestCaseID '{}' not found in fundtransfer sheet (loaded {} rows)", testCaseId, allTestData.size());
        return "";
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    private void navigateBackToFundTransfer() {
        logger.info("Attempting to navigate back to Fund Transfer page...");

        // Strategy 0: Dismiss any open popup/modal/backdrop first
        try {
            ftPage.dismissMuiBackdrop();
        } catch (Exception e) {
            // No backdrop, continue
        }
        try {
            ftPage.clickConfirmOnPopup();
            sleep(1500);
            if (ftPage.isPageTitleDisplayed()) {
                logger.info("Dismissed popup and landed on FT page");
                return;
            }
        } catch (Exception e) {
            // No popup open, continue with other strategies
        }

        // Strategy 1: Click Back Arrow up to 3 times (handles nested sub-pages like QT -> By Account)
        for (int i = 0; i < 3; i++) {
            try {
                ftPage.clickBackArrow();
                sleep(300);
                if (ftPage.isPageTitleDisplayed()) {
                    logger.info("Back arrow click {} successful - FT page title displayed", (i + 1));
                    return;
                }
            } catch (Exception e) {
                logger.warn("Back arrow click {} failed: {}", (i + 1), e.getMessage());
                break;
            }
        }

        // Strategy 2: Browser back
        try {
            ftPage.navigateBack();
            sleep(300);
            if (ftPage.isPageTitleDisplayed()) {
                logger.info("Browser back successful - FT page title displayed");
                return;
            }
        } catch (Exception e) {
            logger.warn("Browser back failed: {}", e.getMessage());
        }

        // Strategy 3: Go Home and re-navigate
        try {
            logger.info("Fallback: navigating via Home -> Fund Transfer");
            ftPage.clickHomeNav();
            sleep(300);
            dashboardPage.waitForDashboardLoad();
            dashboardPage.scrollToServices();
            dashboardPage.clickFundTransfer();
            ftPage.waitForFundTransferPageLoad();
            logger.info("Fallback navigation completed");
        } catch (Exception ex) {
            logger.error("Fallback navigation also failed: {}", ex.getMessage());
        }
    }

    // ==================== BACKGROUND - Dashboard Navigation ====================

    @And("user navigates to Fund Transfer from Dashboard")
    public void userNavigatesToFundTransferFromDashboard() {
        // Quick check: are we already on FT page? (handles case where previous timeout missed detection)
        if (!isOnFundTransferPage) {
            try {
                if (ftPage.isPageTitleDisplayed()) {
                    isOnFundTransferPage = true;
                    logger.info("Already on Fund Transfer page (detected via quick check)");
                }
            } catch (Exception e) {
                // Not on FT page, proceed with navigation
            }
        }

        if (!isOnFundTransferPage) {
            try {
                logger.info("Navigating to Fund Transfer from Dashboard...");
                dashboardPage.scrollToServices();
                dashboardPage.clickFundTransfer();
                ftPage.waitForFundTransferPageLoad();

                if (ftPage.isPageTitleDisplayed()) {
                    isOnFundTransferPage = true;
                    logger.info("Successfully navigated to Fund Transfer page");
                } else {
                    logger.error("Fund Transfer page FAILED to load");
                }
            } catch (Exception e) {
                logger.error("Exception navigating to Fund Transfer: {}", e.getMessage());
                // Recovery: click Home to go to Dashboard first, then retry
                try {
                    logger.info("Recovery: clicking Home nav and retrying...");
                    ftPage.clickHomeNav();
                    sleep(1000);
                    dashboardPage.waitForDashboardLoad();
                    dashboardPage.scrollToServices();
                    dashboardPage.clickFundTransfer();
                    ftPage.waitForFundTransferPageLoad();
                    if (ftPage.isPageTitleDisplayed()) {
                        isOnFundTransferPage = true;
                        logger.info("Recovery navigation to Fund Transfer succeeded");
                    }
                } catch (Exception ex) {
                    logger.error("Recovery navigation also failed: {}", ex.getMessage());
                }
            }
        }

        // If we're supposed to be on FT page, verify and re-navigate if needed
        if (isOnFundTransferPage) {
            try {
                if (!ftPage.isPageTitleDisplayed()) {
                    logger.info("FT page title not visible, attempting back navigation...");
                    navigateBackToFundTransfer();
                }
            } catch (Exception e) {
                logger.warn("Error checking FT page, attempting re-navigation...");
                navigateBackToFundTransfer();
            }
        }
    }

    // ==================== COMPOSITE FORM FILL - Quick Transfer ====================

    @When("user fills Quick Transfer form with test case {string} on QT page")
    public void userFillsQuickTransferFormOnQTPage(String testCaseId) {
        String bankType = getTestDataValue(testCaseId, "BankType_Value");
        String accountNo = getTestDataValue(testCaseId, "BankAccountNumber_Value");
        String reAccountNo = getTestDataValue(testCaseId, "ReEnterAccountNumber_Value");
        String recipientName = getTestDataValue(testCaseId, "RecipientName_Value");
        String amount = getTestDataValue(testCaseId, "Amount_Value");
        String remarks = getTestDataValue(testCaseId, "Remarks_Value");

        if ("Other Bank".equalsIgnoreCase(bankType)) {
            ftPage.selectOtherBank();
            String ifsc = getTestDataValue(testCaseId, "IFSCCode_Value");
            if (accountNo != null && !accountNo.isEmpty()) ftPage.enterAccountNumber(accountNo);
            if (reAccountNo != null && !reAccountNo.isEmpty()) ftPage.enterReEnterAccountNumber(reAccountNo);
            if (ifsc != null && !ifsc.isEmpty()) ftPage.enterIFSCCode(ifsc);
            if (recipientName != null && !recipientName.isEmpty()) ftPage.enterRecipientName(recipientName);
        } else {
            ftPage.selectShivalikBank();
            if (accountNo != null && !accountNo.isEmpty()) ftPage.enterAccountNumber(accountNo);
            if (reAccountNo != null && !reAccountNo.isEmpty()) ftPage.enterReEnterAccountNumber(reAccountNo);
            if (recipientName != null && !recipientName.isEmpty()) ftPage.enterRecipientName(recipientName);
        }

        if (amount != null && !amount.isEmpty()) ftPage.enterAmount(amount);
        ftPage.selectFirstTransferFromAccount();
        if (remarks != null && !remarks.isEmpty()) ftPage.enterRemarks(remarks);
    }

    // ==================== COMPOSITE FORM FILL - Transfer to Beneficiary ====================

    @When("user fills Transfer to Beneficiary form with test case {string} on TB page")
    public void userFillsTransferToBeneficiaryFormOnTBPage(String testCaseId) {
        String bankType = getTestDataValue(testCaseId, "BankType_Value");
        String beneficiary = getTestDataValue(testCaseId, "BeneficiarySearch_Value");
        String amount = getTestDataValue(testCaseId, "Amount_Value");
        String remarks = getTestDataValue(testCaseId, "Remarks_Value");
        String transactionType = getTestDataValue(testCaseId, "TransactionType_Value");

        if ("Other Bank".equalsIgnoreCase(bankType)) {
            ftPage.selectOtherBank();
            sleep(1000); // Wait for Other Bank mode to render
            if ("IMPS".equalsIgnoreCase(transactionType)) {
                ftPage.selectIMPS();
            } else if ("RTGS".equalsIgnoreCase(transactionType)) {
                ftPage.selectRTGS();
            }
            sleep(1000); // Wait for transaction type to settle
        } else {
            ftPage.selectShivalikBank();
        }

        if (beneficiary != null && !beneficiary.isEmpty()) {
            ftPage.enterBeneficiarySearch(beneficiary);
            sleep(3000); // Wait for API to return beneficiary search results
            ftPage.selectBeneficiaryFromDropdown();
            sleep(1000); // Wait for selection to register in React form
        }

        if (amount != null && !amount.isEmpty()) ftPage.enterAmount(amount);
        ftPage.selectFirstTransferFromAccount();

        // IMPS mode does NOT have Remarks field
        if (!"IMPS".equalsIgnoreCase(transactionType)) {
            if (remarks != null && !remarks.isEmpty()) ftPage.enterRemarks(remarks);
        }
    }

    // ==================== COMPOSITE FORM FILL - Self Account ====================

    @When("user fills Self Account form with test case {string} on SA page")
    public void userFillsSelfAccountFormOnSAPage(String testCaseId) {
        String amount = getTestDataValue(testCaseId, "Amount_Value");
        String remarks = getTestDataValue(testCaseId, "Remarks_Value");

        if (amount != null && !amount.isEmpty()) ftPage.enterSelfTransferAmount(amount);
        ftPage.selectFirstTransferFromAccount();
        if (remarks != null && !remarks.isEmpty()) ftPage.enterRemarks(remarks);
    }

    // ==================== EXPECTED RESULT VALIDATION FROM EXCEL ====================

    @Then("expected result from test case {string} should be validated on {word} page")
    public void expectedResultShouldBeValidated(String testCaseId, String page) {
        String expectedResult = getTestDataValue(testCaseId, "ExpectedResult");
        if (expectedResult == null || expectedResult.isEmpty()) {
            logger.warn("No ExpectedResult found for test case: {}", testCaseId);
            return;
        }

        // Support multiple expected results separated by semicolon (;)
        String[] expectedMessages = expectedResult.split(";");
        for (String msg : expectedMessages) {
            String trimmedMsg = msg.trim();
            if (!trimmedMsg.isEmpty()) {
                Assert.assertTrue(ftPage.isTextDisplayedOnPage(trimmedMsg),
                        "Expected result '" + trimmedMsg + "' is not displayed on " + page
                                + " page for test case " + testCaseId);
                logger.info("Validated expected result '{}' on {} page for {}", trimmedMsg, page, testCaseId);
            }
        }
    }

    // ==================== INPUT FROM EXCEL ====================

    @When("user enters {string} from test case {string} in {string} on {word} page")
    public void userEntersFromTestCase(String columnName, String testCaseId, String objectKey, String page) {
        String value = getTestDataValue(testCaseId, columnName);
        if (value != null && !value.isEmpty()) {
            ftPage.typeInField(objectKey, value);
            logger.info("Entered '{}' from test case {} column {} in {} on {} page",
                    value, testCaseId, columnName, objectKey, page);
        } else {
            logger.warn("No value found for column '{}' in test case '{}'", columnName, testCaseId);
        }
    }

    // ==================== CONFIRM PAGE DYNAMIC VALIDATION ====================

    @Then("confirm page should display correct details for test case {string}")
    public void confirmPageShouldDisplayCorrectDetails(String testCaseId) {
        ftPage.waitForConfirmPageLoad();
        logger.info("Validating Confirm page details for test case: {}", testCaseId);

        String expectedAmount = getTestDataValue(testCaseId, "Amount_Value");
        String expectedBeneficiary = getTestDataValue(testCaseId, "BeneficiaryName_Value");
        if (expectedBeneficiary.isEmpty()) {
            expectedBeneficiary = getTestDataValue(testCaseId, "BeneficiarySearch_Value");
        }
        if (expectedBeneficiary.isEmpty()) {
            expectedBeneficiary = getTestDataValue(testCaseId, "RecipientName_Value");
        }
        String expectedTransferType = getTestDataValue(testCaseId, "TransactionType_Value");
        String expectedRemarks = getTestDataValue(testCaseId, "Remarks_Value");
        String expectedTransferFrom = getTestDataValue(testCaseId, "TransferFromAccount_Value");
        String expectedTransferTo = getTestDataValue(testCaseId, "BeneficiaryAccountNo_Value");
        String expectedBankName = getTestDataValue(testCaseId, "BankName_Value");

        // Validate amount (contains check to handle currency formatting like ₹1,000.00)
        if (!expectedAmount.isEmpty()) {
            String actualAmount = ftPage.getConfirmTotalAmount();
            Assert.assertTrue(actualAmount.contains(expectedAmount) || actualAmount.replaceAll("[^0-9.]", "").contains(expectedAmount.replaceAll("[^0-9.]", "")),
                    "Confirm page amount mismatch: expected contains '" + expectedAmount + "' but got '" + actualAmount + "'");
            logger.info("Confirm amount validated: {} contains {}", actualAmount, expectedAmount);
        }

        // Validate transfer from (handles masked format like "SHIVALIK XX3763")
        String actualTransferFrom = ftPage.getConfirmTransferFrom();
        if (!expectedTransferFrom.isEmpty()) {
            String expectedLast4 = expectedTransferFrom.length() >= 4 ? expectedTransferFrom.substring(expectedTransferFrom.length() - 4) : expectedTransferFrom;
            String actualDigits = actualTransferFrom.replaceAll("[^0-9]", "");
            Assert.assertTrue(
                    actualTransferFrom.contains(expectedTransferFrom) || actualDigits.contains(expectedTransferFrom) || actualDigits.contains(expectedLast4),
                    "Confirm page Transfer From mismatch: expected '" + expectedTransferFrom + "' (last4: " + expectedLast4 + ") but got '" + actualTransferFrom + "'");
            logger.info("Confirm Transfer From validated: {} matches last4 of {}", actualTransferFrom, expectedTransferFrom);
        } else {
            Assert.assertFalse(actualTransferFrom.isEmpty(), "Confirm page Transfer From should not be empty");
            logger.info("Confirm Transfer From: {}", actualTransferFrom);
        }

        // Validate transfer to (handles masked format like "XX1234")
        String actualTransferTo = ftPage.getConfirmTransferTo();
        if (!expectedTransferTo.isEmpty()) {
            String expectedLast4 = expectedTransferTo.length() >= 4 ? expectedTransferTo.substring(expectedTransferTo.length() - 4) : expectedTransferTo;
            String actualDigits = actualTransferTo.replaceAll("[^0-9]", "");
            Assert.assertTrue(
                    actualTransferTo.contains(expectedTransferTo) || actualDigits.contains(expectedTransferTo) || actualDigits.contains(expectedLast4),
                    "Confirm page Transfer To mismatch: expected '" + expectedTransferTo + "' (last4: " + expectedLast4 + ") but got '" + actualTransferTo + "'");
            logger.info("Confirm Transfer To validated: {} matches last4 of {}", actualTransferTo, expectedTransferTo);
        } else {
            Assert.assertFalse(actualTransferTo.isEmpty(), "Confirm page Transfer To should not be empty");
            logger.info("Confirm Transfer To: {}", actualTransferTo);
        }

        // Validate transfer type if expected
        if (!expectedTransferType.isEmpty()) {
            String actualTransferType = ftPage.getConfirmTransferType();
            Assert.assertTrue(actualTransferType.toLowerCase().contains(expectedTransferType.toLowerCase()),
                    "Confirm page transfer type mismatch: expected contains '" + expectedTransferType + "' but got '" + actualTransferType + "'");
            logger.info("Confirm Transfer Type validated: {} contains {}", actualTransferType, expectedTransferType);
        }

        // Validate bank name if expected
        if (!expectedBankName.isEmpty()) {
            String actualBankName = ftPage.getConfirmBankName();
            Assert.assertTrue(actualBankName.toLowerCase().contains(expectedBankName.toLowerCase()),
                    "Confirm page bank name mismatch: expected contains '" + expectedBankName + "' but got '" + actualBankName + "'");
            logger.info("Confirm Bank Name validated: {} contains {}", actualBankName, expectedBankName);
        }

        // Validate remarks if expected
        if (!expectedRemarks.isEmpty()) {
            String actualRemarks = ftPage.getConfirmRemarks();
            Assert.assertTrue(actualRemarks.toLowerCase().contains(expectedRemarks.toLowerCase()),
                    "Confirm page remarks mismatch: expected contains '" + expectedRemarks + "' but got '" + actualRemarks + "'");
            logger.info("Confirm Remarks validated: {} contains {}", actualRemarks, expectedRemarks);
        }

        logger.info("Confirm page validation PASSED for test case: {}", testCaseId);
    }

    // ==================== OTP ENTRY FROM EXCEL ====================

    @When("user enters OTP from test case {string} on OTP page")
    public void userEntersOTPFromTestCase(String testCaseId) {
        String otp = getTestDataValue(testCaseId, "OTP_Value");
        if (otp.isEmpty()) {
            otp = "222222"; // fallback default OTP
            logger.warn("OTP_Value not found in Excel for {}, using default: {}", testCaseId, otp);
        }
        ftPage.waitForOTPInput();
        ftPage.enterOTP(otp);
        sleep(500);
        logger.info("Entered OTP '{}' for test case: {}", otp, testCaseId);
    }

    // ==================== SUCCESS PAGE DYNAMIC VALIDATION ====================

    @Then("success page should display correct details for test case {string}")
    public void successPageShouldDisplayCorrectDetails(String testCaseId) {
        ftPage.waitForSuccessPageLoad();
        logger.info("Validating Success page details for test case: {}", testCaseId);

        String expectedAmount = getTestDataValue(testCaseId, "Amount_Value");
        String expectedBeneficiary = getTestDataValue(testCaseId, "BeneficiaryName_Value");
        if (expectedBeneficiary.isEmpty()) {
            expectedBeneficiary = getTestDataValue(testCaseId, "BeneficiarySearch_Value");
        }
        if (expectedBeneficiary.isEmpty()) {
            expectedBeneficiary = getTestDataValue(testCaseId, "RecipientName_Value");
        }
        String expectedTransferType = getTestDataValue(testCaseId, "TransactionType_Value");
        String expectedRemarks = getTestDataValue(testCaseId, "Remarks_Value");
        String expectedTransferFrom = getTestDataValue(testCaseId, "TransferFromAccount_Value");
        String expectedTransferTo = getTestDataValue(testCaseId, "BeneficiaryAccountNo_Value");
        String expectedBankName = getTestDataValue(testCaseId, "BankName_Value");

        // Validate Payment Successful text is displayed
        Assert.assertTrue(ftPage.isPaymentSuccessfulTextDisplayed(),
                "Payment Successful text should be displayed on success page");
        logger.info("Payment Successful text displayed");

        // Validate amount
        if (!expectedAmount.isEmpty()) {
            String actualAmount = ftPage.getSuccessAmount();
            Assert.assertTrue(actualAmount.contains(expectedAmount) || actualAmount.replaceAll("[^0-9.]", "").contains(expectedAmount.replaceAll("[^0-9.]", "")),
                    "Success page amount mismatch: expected contains '" + expectedAmount + "' but got '" + actualAmount + "'");
            logger.info("Success amount validated: {} contains {}", actualAmount, expectedAmount);
        }

        // Validate transfer from (handles masked format like "SHIVALIK XX3763")
        if (!expectedTransferFrom.isEmpty()) {
            String actualTransferFrom = ftPage.getSuccessTransferFrom();
            String expectedLast4 = expectedTransferFrom.length() >= 4 ? expectedTransferFrom.substring(expectedTransferFrom.length() - 4) : expectedTransferFrom;
            String actualDigits = actualTransferFrom.replaceAll("[^0-9]", "");
            Assert.assertTrue(
                    actualTransferFrom.contains(expectedTransferFrom) || actualDigits.contains(expectedTransferFrom) || actualDigits.contains(expectedLast4),
                    "Success page Transfer From mismatch: expected contains '" + expectedTransferFrom + "' (last4: " + expectedLast4 + ") but got '" + actualTransferFrom + "'");
            logger.info("Success Transfer From validated: {} matches last4 of {}", actualTransferFrom, expectedTransferFrom);
        }

        // Validate transfer to (handles masked format like "XX1234")
        if (!expectedTransferTo.isEmpty()) {
            String actualTransferTo = ftPage.getSuccessTransferTo();
            String expectedLast4 = expectedTransferTo.length() >= 4 ? expectedTransferTo.substring(expectedTransferTo.length() - 4) : expectedTransferTo;
            String actualDigits = actualTransferTo.replaceAll("[^0-9]", "");
            Assert.assertTrue(
                    actualTransferTo.contains(expectedTransferTo) || actualDigits.contains(expectedTransferTo) || actualDigits.contains(expectedLast4),
                    "Success page Transfer To mismatch: expected contains '" + expectedTransferTo + "' (last4: " + expectedLast4 + ") but got '" + actualTransferTo + "'");
            logger.info("Success Transfer To validated: {} matches last4 of {}", actualTransferTo, expectedTransferTo);
        }

        // Validate beneficiary name
        if (!expectedBeneficiary.isEmpty()) {
            String actualBenName = ftPage.getSuccessBeneficiaryName();
            Assert.assertTrue(actualBenName.toLowerCase().contains(expectedBeneficiary.toLowerCase()),
                    "Success page beneficiary name mismatch: expected contains '" + expectedBeneficiary + "' but got '" + actualBenName + "'");
            logger.info("Success Beneficiary Name validated: {} contains {}", actualBenName, expectedBeneficiary);
        }

        // Validate bank name
        if (!expectedBankName.isEmpty()) {
            String actualBankName = ftPage.getSuccessBankName();
            Assert.assertTrue(actualBankName.toLowerCase().contains(expectedBankName.toLowerCase()),
                    "Success page bank name mismatch: expected contains '" + expectedBankName + "' but got '" + actualBankName + "'");
            logger.info("Success Bank Name validated: {} contains {}", actualBankName, expectedBankName);
        }

        // Validate transfer type if expected
        if (!expectedTransferType.isEmpty()) {
            String actualTransferType = ftPage.getSuccessTransferType();
            Assert.assertTrue(actualTransferType.toLowerCase().contains(expectedTransferType.toLowerCase()),
                    "Success page transfer type mismatch: expected contains '" + expectedTransferType + "' but got '" + actualTransferType + "'");
            logger.info("Success Transfer Type validated: {} contains {}", actualTransferType, expectedTransferType);
        }

        // Validate remarks if expected
        if (!expectedRemarks.isEmpty()) {
            String actualRemark = ftPage.getSuccessRemark();
            Assert.assertTrue(actualRemark.toLowerCase().contains(expectedRemarks.toLowerCase()),
                    "Success page remark mismatch: expected contains '" + expectedRemarks + "' but got '" + actualRemark + "'");
            logger.info("Success Remark validated: {} contains {}", actualRemark, expectedRemarks);
        }

        // Validate Reference ID is not empty (proves transaction completed)
        String referenceId = ftPage.getSuccessReferenceId();
        Assert.assertFalse(referenceId.isEmpty(), "Success page Reference ID should not be empty - transaction must have a reference");
        logger.info("Success Reference ID: {}", referenceId);

        logger.info("Success page validation PASSED for test case: {}", testCaseId);
    }

    // ==================== SA SIDEBAR ACCOUNT SELECTION ====================

    @When("user selects account from sidebar for test case {string}")
    public void userSelectsAccountFromSidebar(String testCaseId) {
        ftPage.waitForSASidebar();
        Assert.assertTrue(ftPage.isSASidebarDisplayed(),
                "Self Account sidebar should be displayed after clicking Self Account card");
        logger.info("SA Sidebar displayed, selecting account for test case: {}", testCaseId);

        ftPage.selectFirstAccountFromSidebar();
        ftPage.waitForSelfAccountPageLoad();
        logger.info("Account selected from SA sidebar for test case: {}", testCaseId);
    }
}
