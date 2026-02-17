package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

/**
 * FundTransferPage - Page Object for Fund Transfer Page
 * Handles all interactions on the fund transfer pages including:
 * - Main fund transfer menu
 * - Transfer to beneficiary (Shivalik / Other Bank)
 * - Quick transfer (By Mobile Number / By Account Details)
 * - Self account transfer
 * - Confirm page, OTP, Success flows
 */
public class FundTransferPage extends BasePage {

    public FundTransferPage() {
        super();
    }

    private String key(String objectName) {
        return objectName;
    }

    // ========== Wait / Load Methods ==========

    public void waitForFundTransferPageLoad() {
        try {
            // Use presence check (not visibility) because the fixed-position header
            // may not pass Selenium's visibility check due to CSS rendering
            By locator = orManager.getLocator(key("FT_PageTitle_Object"));
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(60))
                    .until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(locator));
            logger.info("Fund Transfer page loaded successfully");
        } catch (Exception e) {
            logger.warn("Fund Transfer page load wait timed out after 60s");
        }
    }

    public void waitForTransferToBeneficiaryPageLoad() {
        try {
            waitForElementVisible(key("TransferToBeneficiaryPageTitle_Object"), 15);
            logger.info("Transfer to Beneficiary page loaded successfully");
        } catch (Exception e) {
            logger.warn("Transfer to beneficiary page load wait timed out after 15s");
        }
    }

    public void waitForQuickTransferPageLoad() {
        try {
            waitForElementVisible(key("QuickTransferPageTitle_Object"), 15);
            logger.info("Quick Transfer page loaded successfully");
        } catch (Exception e) {
            logger.warn("Quick transfer page load wait timed out after 15s");
        }
    }

    public void waitForSelfAccountPageLoad() {
        try {
            waitForElementVisible(key("SelfAccountPageTitle_Object"), 15);
            logger.info("Self Account page loaded successfully");
        } catch (Exception e) {
            logger.warn("Self account page load wait timed out after 15s");
        }
    }

    public void waitForConfirmPageLoad() {
        try {
            waitForElementVisible(key("ConfirmPageTitle_Object"), 15);
        } catch (Exception e) {
            logger.warn("Confirm page load wait timed out");
        }
    }

    public void waitForOTPInput() {
        try {
            waitForElementVisible(key("OTPInput_Object"), 15);
        } catch (Exception e) {
            logger.warn("OTP input wait timed out");
        }
    }

    public void waitForSuccessPageLoad() {
        try {
            waitForElementVisible(key("PaymentSuccessfulText_Object"), 30);
        } catch (Exception e) {
            logger.warn("Success page load wait timed out, retrying with SuccessTitle");
            try {
                waitForElementVisible(key("SuccessTitle_Object"), 10);
            } catch (Exception ex) {
                logger.warn("Success page not found with fallback either");
            }
        }
    }

    // ========== Navigation Actions ==========

    public void clickBackArrow() {
        try {
            click(key("GoBackButton_Object"));
        } catch (Exception e) {
            jsClick(key("FT_BackArrow_Object"));
        }
    }

    public void clickGoBackButton() {
        click(key("GoBackButton_Object"));
    }

    public void clickHomeNav() {
        click(key("HomeNavLink_Object"));
    }

    public void clickProfileButton() {
        click(key("ProfileButton_Object"));
    }

    public void navigateBack() {
        driver.navigate().back();
    }

    // ========== Main Menu Actions ==========

    public void clickTransferToBeneficiary() {
        click(key("TransferToBeneficiaryCard_Object"));
    }

    public void clickQuickTransfer() {
        click(key("QuickTransferCard_Object"));
    }

    public void clickSelfAccount() {
        click(key("SelfAccountCard_Object"));
    }

    // ========== Services Actions ==========

    public void clickScheduleTransactions() {
        click(key("ScheduleTransactionsService_Object"));
    }

    public void clickAccountStatement() {
        click(key("AccountStatementService_Object"));
    }

    public void clickAccountDetails() {
        click(key("AccountDetailsService_Object"));
    }

    public void clickFavoriteTransactions() {
        click(key("FavoriteTransactionsService_Object"));
    }

    public void clickMiniStatement() {
        click(key("MiniStatementService_Object"));
    }

    public void clickEmailStatement() {
        click(key("EmailStatementService_Object"));
    }

    public void clickManageBeneficiary() {
        click(key("ManageBeneficiaryService_Object"));
    }

    public void clickRaiseDispute() {
        click(key("RaiseDisputeService_Object"));
    }

    public void clickModifyTransactionLimit() {
        click(key("ModifyTransactionLimitService_Object"));
    }

    // ========== Transfer to Beneficiary Actions ==========

    public void selectShivalikBank() {
        jsClick(key("ShivalikBankLabel_Object"));
    }

    public void selectOtherBank() {
        jsClick(key("OtherBankLabel_Object"));
    }

    public void clickSelectBeneficiary() {
        click(key("SelectBeneficiaryInput_Object"));
    }

    public void enterBeneficiarySearch(String text) {
        // Step 1: Click the main beneficiary input to open the dropdown panel
        click(key("SelectBeneficiaryInput_Object"));
        try { Thread.sleep(1500); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }

        // Step 2: Wait for the dropdown panel to appear
        String dropdownPanel = "//div[contains(@class,'absolute') and contains(@class,'z-20') and contains(@class,'bg-white')]";
        try {
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath(dropdownPanel)));
            logger.info("Beneficiary dropdown panel opened");
        } catch (Exception e) {
            logger.warn("Dropdown panel did not appear, trying to type in main input instead");
            type(key("SelectBeneficiaryInput_Object"), text);
            return;
        }

        // Step 3: Find and type in the dropdown's internal Search input
        String searchInput = "//div[contains(@class,'absolute') and contains(@class,'z-20')]//input[@placeholder='Search']";
        try {
            WebElement searchField = driver.findElement(By.xpath(searchInput));
            searchField.clear();
            searchField.sendKeys(text);
            logger.info("Typed '{}' in dropdown search input", text);
            try { Thread.sleep(2000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
        } catch (Exception e) {
            logger.warn("Could not find dropdown search input, falling back to main input");
            type(key("SelectBeneficiaryInput_Object"), text);
        }
    }

    public void selectBeneficiaryFromDropdown() {
        // The beneficiary dropdown panel structure:
        // <div class="absolute z-20 w-full bg-white ...">
        //   <input placeholder="Search">
        //   <ul><li class="cursor-pointer"><div class="beneficiaryAvtar">...</div>...</li></ul>
        // </div>
        String selector = "//li[contains(@class,'cursor-pointer') and .//div[contains(@class,'beneficiaryAvtar')]]";

        try {
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath(selector)));
            try { Thread.sleep(500); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }

            List<WebElement> items = driver.findElements(By.xpath(selector));
            if (!items.isEmpty()) {
                WebElement item = items.get(0);
                String itemText = item.getText();
                logger.info("Found beneficiary item: {}", itemText);

                try {
                    new org.openqa.selenium.interactions.Actions(driver)
                            .moveToElement(item).pause(java.time.Duration.ofMillis(200)).click().perform();
                } catch (Exception actionsEx) {
                    try {
                        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", item);
                    } catch (Exception jsEx) {
                        item.click();
                    }
                }

                try { Thread.sleep(1000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                logger.info("Beneficiary selected successfully");
                return;
            }
        } catch (Exception e) {
            logger.warn("Could not find beneficiary items: {}", e.getMessage());
        }

        logger.warn("Could not select beneficiary from dropdown - no items found");
    }

    public void enterAmount(String amount) {
        typeInField(key("EnterAmountInput_Object"), amount);
    }

    public void clearAmount() {
        clear(key("EnterAmountInput_Object"));
    }

    public void selectTransferFromAccount() {
        click(key("TransferFromDropdown_Object"));
    }

    public void selectFirstTransferFromAccount() {
        click(key("TransferFromDropdown_Object"));
        try {
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(3))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[@role='listbox']//li")));
            List<WebElement> items = driver.findElements(By.xpath("//ul[@role='listbox']//li"));
            if (!items.isEmpty()) {
                items.get(0).click();
            }
        } catch (Exception e) {
            logger.warn("Could not select transfer from account");
        }
    }

    public void clickPaymentDateField() {
        click(key("PaymentDateInput_Object"));
    }

    public void clickCalendarIcon() {
        click(key("CalendarIcon_Object"));
    }

    public void enterRemarks(String remarks) {
        type(key("RemarksInput_Object"), remarks);
    }

    public void clearRemarks() {
        clear(key("RemarksInput_Object"));
    }

    // ========== Transaction Type Actions (IMPS/NEFT/RTGS) ==========

    public void selectIMPS() {
        jsClick(key("IMPSLabel_Object"));
    }

    public void selectNEFT() {
        jsClick(key("NEFTLabel_Object"));
    }

    public void selectRTGS() {
        jsClick(key("RTGSLabel_Object"));
    }

    // ========== Quick Transfer Actions ==========

    public void clickByMobileNumber() {
        click(key("ByMobileNumberCard_Object"));
    }

    public void clickByAccountDetails() {
        click(key("ByAccountDetailsCard_Object"));
    }

    public void enterRecipientName(String name) {
        typeInField(key("QT_RecipientNameInput_Object"), name);
    }

    public void clearRecipientName() {
        clear(key("QT_RecipientNameInput_Object"));
    }

    public void enterAccountNumber(String accountNumber) {
        typeInField(key("QT_AccountNumberInput_Object"), accountNumber);
    }

    public void clearAccountNumber() {
        clear(key("QT_AccountNumberInput_Object"));
    }

    public void clickAccountNumberEyeIcon() {
        click(key("QT_AccountNumberEyeIcon_Object"));
    }

    public void enterReEnterAccountNumber(String accountNumber) {
        typeInField(key("QT_ReEnterAccountInput_Object"), accountNumber);
    }

    public void clearReEnterAccountNumber() {
        clear(key("QT_ReEnterAccountInput_Object"));
    }

    public void enterIFSCCode(String ifscCode) {
        typeInField(key("QT_IFSCCodeInput_Object"), ifscCode);
    }

    public void clearIFSCCode() {
        clear(key("QT_IFSCCodeInput_Object"));
    }

    public void clickSearchIFSCLink() {
        click(key("QT_SearchIFSCLink_Object"));
    }

    // ========== Self Account Actions ==========

    public void clickChangeAccount() {
        click(key("ChangeAccountButton_Object"));
    }

    public void enterSelfTransferAmount(String amount) {
        type(key("SelfTransferAmountInput_Object"), amount);
    }

    public void clearSelfTransferAmount() {
        clear(key("SelfTransferAmountInput_Object"));
    }

    // ========== SA Sidebar Actions ==========

    public boolean isSASidebarDisplayed() {
        return isDisplayedWithin(key("SA_SidebarTitle_Object"), 5);
    }

    public void selectFirstAccountFromSidebar() {
        try {
            waitForElementVisible(key("SA_SidebarAccountItem_Object"), 5);
            click(key("SA_SidebarAccountItem_Object"));
            logger.info("Selected first account from SA sidebar");
        } catch (Exception e) {
            logger.warn("Could not select account from SA sidebar");
        }
    }

    public void closeSASidebar() {
        click(key("SA_SidebarCloseButton_Object"));
    }

    public void waitForSASidebar() {
        try {
            waitForElementVisible(key("SA_SidebarTitle_Object"), 8);
        } catch (Exception e) {
            logger.warn("SA Sidebar did not appear within 8s");
        }
    }

    // ========== Common Actions ==========

    public void clickCancel() {
        click(key("FT_CancelButton_Object"));
    }

    public void clickContinue() {
        click(key("FT_ContinueButton_Object"));
    }

    public void selectTransactionAccount() {
        click(key("FT_TransactionAccountDropdown_Object"));
    }

    public void clickSeeMore() {
        click(key("SeeMoreLink_Object"));
    }

    // ========== Confirm Page Actions ==========

    public void clickConfirmButton() {
        click(key("ConfirmButton_Object"));
    }

    // ========== OTP Actions ==========

    public void enterOTP(String otp) {
        type(key("OTPInput_Object"), otp);
    }

    public void clearOTP() {
        clear(key("OTPInput_Object"));
    }

    public void clickResendOTPLink() {
        click(key("ResendOTPLink_Object"));
    }

    // ========== Submit & Success Page Actions ==========

    public void clickSubmitButton() {
        click(key("FT_SubmitButton_Object"));
    }

    public void clickDoneButton() {
        click(key("FT_DoneButton_Object"));
    }

    public void clickRepeatTransactionsButton() {
        click(key("RepeatTransactionsButton_Object"));
    }

    // ========== Get Text Methods ==========

    public String getPageTitle() {
        return getText(key("FT_PageTitle_Object"));
    }

    public String getTransferToBeneficiaryPageTitle() {
        return getText(key("TransferToBeneficiaryPageTitle_Object"));
    }

    public String getQuickTransferPageTitle() {
        return getText(key("QuickTransferPageTitle_Object"));
    }

    public String getSelfAccountPageTitle() {
        return getText(key("SelfAccountPageTitle_Object"));
    }

    public String getAvailableBalanceText() {
        return getText(key("AvailableBalanceText_Object"));
    }

    public String getTransferToValue() {
        return getAttribute(key("TransferToInput_Object"), "value");
    }

    public String getAmountInputValue() {
        return getAttribute(key("EnterAmountInput_Object"), "value");
    }

    public String getSelfTransferAmountValue() {
        return getAttribute(key("SelfTransferAmountInput_Object"), "value");
    }

    public String getSelectBeneficiaryValue() {
        return getAttribute(key("SelectBeneficiaryInput_Object"), "value");
    }

    public String getAccountNumberInputValue() {
        return getAttribute(key("QT_AccountNumberInput_Object"), "value");
    }

    public String getReEnterAccountInputValue() {
        return getAttribute(key("QT_ReEnterAccountInput_Object"), "value");
    }

    public String getIFSCCodeInputValue() {
        return getAttribute(key("QT_IFSCCodeInput_Object"), "value");
    }

    public String getRecipientNameInputValue() {
        return getAttribute(key("QT_RecipientNameInput_Object"), "value");
    }

    public String getRemarksInputValue() {
        return getAttribute(key("RemarksInput_Object"), "value");
    }

    public String getPaymentDateValue() {
        return getAttribute(key("PaymentDateInput_Object"), "value");
    }

    // ========== Confirm Page Get Text ==========

    public String getConfirmPageTitle() {
        return getText(key("ConfirmPageTitle_Object"));
    }

    public String getConfirmBeneficiaryName() {
        return getText(key("ConfirmBeneficiaryName_Object"));
    }

    public String getConfirmAccountNumber() {
        return getText(key("ConfirmAccountNumber_Object"));
    }

    public String getConfirmTransferFrom() {
        return getText(key("ConfirmTransferFrom_Object"));
    }

    public String getConfirmTransferTo() {
        return getText(key("ConfirmTransferTo_Object"));
    }

    public String getConfirmTotalAmount() {
        return getText(key("ConfirmTotalAmount_Object"));
    }

    public String getConfirmTransferType() {
        return getText(key("ConfirmTransferType_Object"));
    }

    public String getConfirmIFSC() {
        return getText(key("ConfirmIFSC_Object"));
    }

    public String getConfirmBankName() {
        return getText(key("ConfirmBankName_Object"));
    }

    public String getConfirmBranchName() {
        return getText(key("ConfirmBranchName_Object"));
    }

    public String getConfirmRemarks() {
        return getText(key("ConfirmRemarks_Object"));
    }

    // ========== Success Page Get Text ==========

    public String getSuccessTitle() {
        return getText(key("SuccessTitle_Object"));
    }

    public String getSuccessMessage() {
        return getText(key("SuccessMessage_Object"));
    }

    public String getSuccessAmount() {
        return getText(key("SuccessAmount_Object"));
    }

    public String getSuccessTransferTo() {
        return getText(key("SuccessTransferTo_Object"));
    }

    public String getSuccessTransferFrom() {
        return getText(key("SuccessTransferFrom_Object"));
    }

    public String getSuccessBankName() {
        return getText(key("SuccessBankName_Object"));
    }

    public String getSuccessBeneficiaryName() {
        return getText(key("SuccessBeneficiaryName_Object"));
    }

    public String getSuccessTransferType() {
        return getText(key("SuccessTransferType_Object"));
    }

    public String getSuccessRemark() {
        try {
            return getText(key("SuccessRemark_Object"));
        } catch (Exception e) {
            return "";
        }
    }

    public String getSuccessReferenceId() {
        try {
            return getText(key("SuccessReferenceId_Object"));
        } catch (Exception e) {
            return "";
        }
    }

    // ========== Error Get Text ==========

    public String getErrorMessage() {
        try {
            return getText(key("FT_ErrorMessage_Object"));
        } catch (Exception e) {
            return "";
        }
    }

    public String getToastMessage() {
        try {
            return getText(key("FT_ToastMessage_Object"));
        } catch (Exception e) {
            return "";
        }
    }

    // ========== Verification - Main Page ==========

    public boolean isPageTitleDisplayed() {
        try {
            By locator = orManager.getLocator(key("FT_PageTitle_Object"));
            org.openqa.selenium.WebElement el = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                    .until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(locator));
            return el != null;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTransferToBeneficiaryCardDisplayed() {
        return isDisplayed(key("TransferToBeneficiaryCard_Object"));
    }

    public boolean isQuickTransferCardDisplayed() {
        return isDisplayed(key("QuickTransferCard_Object"));
    }

    public boolean isSelfAccountCardDisplayed() {
        return isDisplayed(key("SelfAccountCard_Object"));
    }

    public boolean isRecentTransactionsDisplayed() {
        return isDisplayed(key("FT_RecentTransactionsTitle_Object"));
    }

    public boolean isTransactionAccountDropdownDisplayed() {
        return isDisplayed(key("FT_TransactionAccountDropdown_Object"));
    }

    public boolean isTransactionListDisplayed() {
        return isDisplayed(key("FT_TransactionList_Object"));
    }

    public boolean isServicesDisplayed() {
        return isDisplayed(key("FT_ServicesTitle_Object"));
    }

    public boolean isSeeMoreLinkDisplayed() {
        return isDisplayed(key("SeeMoreLink_Object"));
    }

    // ========== Verification - Services ==========

    public boolean isScheduleTransactionsServiceDisplayed() {
        return isDisplayed(key("ScheduleTransactionsService_Object"));
    }

    public boolean isAccountStatementServiceDisplayed() {
        return isDisplayed(key("AccountStatementService_Object"));
    }

    public boolean isAccountDetailsServiceDisplayed() {
        return isDisplayed(key("AccountDetailsService_Object"));
    }

    public boolean isFavoriteTransactionsServiceDisplayed() {
        return isDisplayed(key("FavoriteTransactionsService_Object"));
    }

    public boolean isMiniStatementServiceDisplayed() {
        return isDisplayed(key("MiniStatementService_Object"));
    }

    public boolean isEmailStatementServiceDisplayed() {
        return isDisplayed(key("EmailStatementService_Object"));
    }

    public boolean isManageBeneficiaryServiceDisplayed() {
        return isDisplayed(key("ManageBeneficiaryService_Object"));
    }

    public boolean isRaiseDisputeServiceDisplayed() {
        return isDisplayed(key("RaiseDisputeService_Object"));
    }

    public boolean isModifyTransactionLimitServiceDisplayed() {
        return isDisplayed(key("ModifyTransactionLimitService_Object"));
    }

    // ========== Verification - Transfer to Beneficiary ==========

    public boolean isTransferToBeneficiaryPageDisplayed() {
        return isDisplayed(key("TransferToBeneficiaryPageTitle_Object"));
    }

    public boolean isShivalikBankRadioDisplayed() {
        return isDisplayed(key("ShivalikBankLabel_Object"));
    }

    public boolean isOtherBankRadioDisplayed() {
        return isDisplayed(key("OtherBankLabel_Object"));
    }

    public boolean isShivalikBankSelected() {
        return isSelected(key("ShivalikBankRadio_Object"));
    }

    public boolean isOtherBankSelected() {
        return isSelected(key("OtherBankRadio_Object"));
    }

    public boolean isSelectBeneficiaryDisplayed() {
        return isDisplayed(key("SelectBeneficiaryInput_Object"));
    }

    public boolean isEnterAmountDisplayed() {
        return isDisplayed(key("EnterAmountInput_Object"));
    }

    public boolean isTransferFromDropdownDisplayed() {
        return isDisplayed(key("TransferFromDropdown_Object"));
    }

    public boolean isPaymentDateDisplayed() {
        return isDisplayed(key("PaymentDateInput_Object"));
    }

    public boolean isRemarksDisplayed() {
        return isDisplayed(key("RemarksInput_Object"));
    }

    public boolean isCancelButtonDisplayed() {
        return isDisplayed(key("FT_CancelButton_Object"));
    }

    public boolean isContinueButtonDisplayed() {
        return isDisplayed(key("FT_ContinueButton_Object"));
    }

    // ========== Verification - Transaction Type (IMPS/NEFT/RTGS) ==========

    public boolean isIMPSRadioDisplayed() {
        return isDisplayed(key("IMPSLabel_Object"));
    }

    public boolean isNEFTRadioDisplayed() {
        return isDisplayed(key("NEFTLabel_Object"));
    }

    public boolean isRTGSRadioDisplayed() {
        return isDisplayed(key("RTGSLabel_Object"));
    }

    public boolean isIMPSSelected() {
        return isSelected(key("IMPSRadio_Object"));
    }

    public boolean isNEFTSelected() {
        return isSelected(key("NEFTRadio_Object"));
    }

    public boolean isRTGSSelected() {
        return isSelected(key("RTGSRadio_Object"));
    }

    // ========== Verification - Quick Transfer ==========

    public boolean isQuickTransferPageDisplayed() {
        return isDisplayed(key("QuickTransferPageTitle_Object"));
    }

    public boolean isByMobileNumberCardDisplayed() {
        return isDisplayed(key("ByMobileNumberCard_Object"));
    }

    public boolean isByAccountDetailsCardDisplayed() {
        return isDisplayed(key("ByAccountDetailsCard_Object"));
    }

    public boolean isRecipientNameDisplayed() {
        return isDisplayed(key("QT_RecipientNameInput_Object"));
    }

    public boolean isAccountNumberDisplayed() {
        return isDisplayed(key("QT_AccountNumberInput_Object"));
    }

    public boolean isReEnterAccountDisplayed() {
        return isDisplayed(key("QT_ReEnterAccountInput_Object"));
    }

    public boolean isIFSCCodeDisplayed() {
        return isDisplayed(key("QT_IFSCCodeInput_Object"));
    }

    public boolean isAccountNumberMasked() {
        try {
            String type = getAttribute(key("QT_AccountNumberInput_Object"), "type");
            if ("password".equalsIgnoreCase(type)) return true;
            // Also check CSS text-security masking
            By locator = orManager.getLocator(key("QT_AccountNumberInput_Object"));
            WebElement el = driver.findElement(locator);
            String cssMask = (String) ((JavascriptExecutor) driver).executeScript(
                    "return window.getComputedStyle(arguments[0]).getPropertyValue('-webkit-text-security') || window.getComputedStyle(arguments[0]).getPropertyValue('text-security') || '';", el);
            return "disc".equals(cssMask) || "circle".equals(cssMask) || "square".equals(cssMask);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isReEnterAccountMasked() {
        try {
            String type = getAttribute(key("QT_ReEnterAccountInput_Object"), "type");
            return "password".equalsIgnoreCase(type);
        } catch (Exception e) {
            return false;
        }
    }

    // ========== Verification - Self Account ==========

    public boolean isSelfAccountPageDisplayed() {
        return isDisplayed(key("SelfAccountPageTitle_Object"));
    }

    public boolean isTransferToFieldDisplayed() {
        return isDisplayed(key("TransferToInput_Object"));
    }

    public boolean isChangeAccountButtonDisplayed() {
        return isDisplayed(key("ChangeAccountButton_Object"));
    }

    public boolean isSelfTransferAmountDisplayed() {
        return isDisplayed(key("SelfTransferAmountInput_Object"));
    }

    // ========== Verification - Confirm Page ==========

    public boolean isConfirmPageDisplayed() {
        return isDisplayed(key("ConfirmPageTitle_Object"));
    }

    public boolean isConfirmBeneficiaryNameDisplayed() {
        return isDisplayed(key("ConfirmBeneficiaryName_Object"));
    }

    public boolean isConfirmAccountNumberDisplayed() {
        return isDisplayed(key("ConfirmAccountNumber_Object"));
    }

    public boolean isConfirmTransferFromDisplayed() {
        return isDisplayed(key("ConfirmTransferFrom_Object"));
    }

    public boolean isConfirmTransferToDisplayed() {
        return isDisplayed(key("ConfirmTransferTo_Object"));
    }

    public boolean isConfirmTotalAmountDisplayed() {
        return isDisplayed(key("ConfirmTotalAmount_Object"));
    }

    public boolean isConfirmTransferTypeDisplayed() {
        return isDisplayed(key("ConfirmTransferType_Object"));
    }

    public boolean isConfirmIFSCDisplayed() {
        return isDisplayed(key("ConfirmIFSC_Object"));
    }

    public boolean isConfirmBankNameDisplayed() {
        return isDisplayed(key("ConfirmBankName_Object"));
    }

    public boolean isConfirmBranchNameDisplayed() {
        return isDisplayed(key("ConfirmBranchName_Object"));
    }

    public boolean isConfirmRemarksDisplayed() {
        return isDisplayed(key("ConfirmRemarks_Object"));
    }

    public boolean isConfirmButtonDisplayed() {
        return isDisplayed(key("ConfirmButton_Object"));
    }

    // ========== Verification - OTP ==========

    public boolean isOTPInputDisplayed() {
        return isDisplayed(key("OTPInput_Object"));
    }

    public boolean isResendOTPLinkDisplayed() {
        return isDisplayed(key("ResendOTPLink_Object"));
    }

    public boolean isOTPTimerDisplayed() {
        return isDisplayed(key("OTPTimerText_Object"));
    }

    public boolean isOTPMessageDisplayed() {
        return isDisplayed(key("OTPMessage_Object"));
    }

    // ========== Verification - Success ==========

    public boolean isSuccessTitleDisplayed() {
        return isDisplayed(key("SuccessTitle_Object"));
    }

    public boolean isSuccessMessageDisplayed() {
        return isDisplayed(key("SuccessMessage_Object"));
    }

    public boolean isDoneButtonDisplayed() {
        return isDisplayed(key("FT_DoneButton_Object"));
    }

    // ========== Verification - Success Page Details ==========

    public boolean isPaymentSuccessfulTextDisplayed() {
        return isDisplayed(key("PaymentSuccessfulText_Object"));
    }

    public boolean isSuccessAmountDisplayed() {
        return isDisplayed(key("SuccessAmount_Object"));
    }

    public boolean isRepeatTransactionsButtonDisplayed() {
        return isDisplayed(key("RepeatTransactionsButton_Object"));
    }

    // ========== Verification - SA Sidebar ==========

    public boolean isSASidebarCloseButtonDisplayed() {
        return isDisplayed(key("SA_SidebarCloseButton_Object"));
    }

    public boolean isSASidebarSearchInputDisplayed() {
        return isDisplayed(key("SA_SidebarSearchInput_Object"));
    }

    // ========== Verification - Error / Toast ==========

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(key("FT_ErrorMessage_Object"));
    }

    public boolean isToastMessageDisplayed() {
        return isDisplayed(key("FT_ToastMessage_Object"));
    }

    // ========== Scroll Methods ==========

    public void scrollToServices() {
        scrollToElement(key("FT_ServicesTitle_Object"));
    }

    public void scrollToRecentTransactions() {
        scrollToElement(key("FT_RecentTransactionsTitle_Object"));
    }

    public void scrollToAmount() {
        scrollToElement(key("EnterAmountInput_Object"));
    }

    public void scrollToRemarks() {
        scrollToElement(key("RemarksInput_Object"));
    }

    // ========== Utility Methods ==========

    public int getTransactionListCount() {
        try {
            List<WebElement> items = driver.findElements(By.xpath("//ul[contains(@class,'transactionList')]//li"));
            return items.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public int getBeneficiaryDropdownCount() {
        try {
            List<WebElement> items = driver.findElements(By.xpath("//ul[@role='listbox']//li"));
            return items.size();
        } catch (Exception e) {
            return 0;
        }
    }

    // ========== Composite / Helper Methods ==========

    public void fillTransferToBeneficiaryShivalikForm(String beneficiary, String amount, String remarks) {
        selectShivalikBank();
        clickSelectBeneficiary();
        selectBeneficiaryFromDropdown();
        enterAmount(amount);
        selectFirstTransferFromAccount();
        if (remarks != null && !remarks.isEmpty()) {
            enterRemarks(remarks);
        }
    }

    public void fillTransferToBeneficiaryOtherBankForm(String beneficiary, String amount, String remarks) {
        selectOtherBank();
        clickSelectBeneficiary();
        selectBeneficiaryFromDropdown();
        enterAmount(amount);
        selectFirstTransferFromAccount();
        if (remarks != null && !remarks.isEmpty()) {
            enterRemarks(remarks);
        }
    }

    public void fillQuickTransferShivalikForm(String recipientName, String accountNo, String reAccountNo, String amount) {
        selectShivalikBank();
        enterRecipientName(recipientName);
        enterAccountNumber(accountNo);
        enterReEnterAccountNumber(reAccountNo);
        enterAmount(amount);
        selectFirstTransferFromAccount();
    }

    public void fillQuickTransferOtherBankForm(String recipientName, String accountNo, String reAccountNo, String ifsc, String amount) {
        selectOtherBank();
        enterRecipientName(recipientName);
        enterAccountNumber(accountNo);
        enterReEnterAccountNumber(reAccountNo);
        enterIFSCCode(ifsc);
        enterAmount(amount);
        selectFirstTransferFromAccount();
    }

    public void fillSelfAccountTransferForm(String amount, String remarks) {
        enterSelfTransferAmount(amount);
        selectFirstTransferFromAccount();
        if (remarks != null && !remarks.isEmpty()) {
            enterRemarks(remarks);
        }
    }

    // ========== Cancel Popup Methods ==========

    public boolean isCancelPopupDisplayed() {
        return isDisplayedWithin(key("CancelPopupContainer_Object"), 10);
    }

    public String getCancelPopupMessage() {
        try {
            return getText(key("CancelPopupMessage_Object"));
        } catch (Exception e) {
            return "";
        }
    }

    public void clickCancelOnPopup() {
        try {
            waitForElementVisible(key("CancelPopupCancelBtn_Object"), 5);
            click(key("CancelPopupCancelBtn_Object"));
            logger.info("Clicked Cancel button on cancel popup");
            waitForPopupToDismiss();
        } catch (Exception e) {
            logger.warn("Cancel popup Cancel button not found - popup may not have appeared");
        }
    }

    public void clickConfirmOnPopup() {
        try {
            waitForElementVisible(key("CancelPopupConfirmBtn_Object"), 5);
            click(key("CancelPopupConfirmBtn_Object"));
            logger.info("Clicked Confirm button on cancel popup");
            waitForPopupToDismiss();
        } catch (Exception e) {
            logger.warn("Cancel popup Confirm button not found - popup may not have appeared");
        }
    }

    private void waitForPopupToDismiss() {
        try {
            // Wait for popup container to disappear from DOM
            By popupLocator = orManager.getLocator(key("CancelPopupContainer_Object"));
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                    .until(org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated(popupLocator));
            logger.info("Popup dismissed successfully");
        } catch (Exception e) {
            logger.debug("Popup dismiss wait timed out, using fallback sleep");
        }
        // Extra sleep to let animation complete
        try { Thread.sleep(1000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
    }

    public void dismissMuiBackdrop() {
        try {
            List<WebElement> backdrops = driver.findElements(By.cssSelector(".MuiBackdrop-root"));
            if (!backdrops.isEmpty()) {
                // Try pressing Escape to close any modal
                driver.findElement(By.tagName("body")).sendKeys(org.openqa.selenium.Keys.ESCAPE);
                try { Thread.sleep(500); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                logger.info("Pressed Escape to dismiss MuiBackdrop");
                // If backdrop still exists, remove it via JS
                backdrops = driver.findElements(By.cssSelector(".MuiBackdrop-root"));
                if (!backdrops.isEmpty()) {
                    ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                            "document.querySelectorAll('.MuiBackdrop-root').forEach(el => el.parentElement.remove());"
                    );
                    try { Thread.sleep(300); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                    logger.info("Removed MuiBackdrop via JavaScript");
                }
            }
        } catch (Exception e) {
            logger.debug("No MuiBackdrop to dismiss");
        }
    }
}
