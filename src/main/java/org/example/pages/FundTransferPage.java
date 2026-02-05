package org.example.pages;

/**
 * FundTransferPage - Page Object for Fund Transfer Page
 * Handles all interactions on the fund transfer pages including:
 * - Main fund transfer menu
 * - Transfer to beneficiary
 * - Quick transfer
 * - Self account transfer
 */
public class FundTransferPage extends BasePage {

    public FundTransferPage() {
        super();
    }

    // Get locator key
    private String key(String objectName) {
        return objectName;
    }

    // ========== Navigation Actions ==========

    public void clickBackArrow() {
        click(key("BackArrow_Object"));
    }

    public void clickHomeNav() {
        click(key("HomeNavLink_Object"));
    }

    public void clickProfileButton() {
        click(key("ProfileButton_Object"));
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
        click(key("ShivalikBankRadio_Object"));
    }

    public void selectOtherBank() {
        click(key("OtherBankRadio_Object"));
    }

    public void clickSelectBeneficiary() {
        click(key("SelectBeneficiaryInput_Object"));
    }

    public void enterAmount(String amount) {
        type(key("EnterAmountInput_Object"), amount);
    }

    public void clearAmount() {
        clear(key("EnterAmountInput_Object"));
    }

    public void selectTransferFromAccount() {
        click(key("TransferFromDropdown_Object"));
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

    // ========== Quick Transfer Actions ==========

    public void clickByMobileNumber() {
        click(key("ByMobileNumberCard_Object"));
    }

    public void clickByAccountDetails() {
        click(key("ByAccountDetailsCard_Object"));
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

    // ========== Common Actions ==========

    public void clickCancel() {
        click(key("CancelButton_Object"));
    }

    public void clickContinue() {
        click(key("ContinueButton_Object"));
    }

    public void selectTransactionAccount() {
        click(key("TransactionAccountDropdown_Object"));
    }

    // ========== Verification Methods ==========

    public boolean isPageTitleDisplayed() {
        return isDisplayed(key("PageTitle_Object"));
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
        return isDisplayed(key("RecentTransactionsTitle_Object"));
    }

    public boolean isServicesDisplayed() {
        return isDisplayed(key("ServicesTitle_Object"));
    }

    public boolean isTransferToBeneficiaryPageDisplayed() {
        return isDisplayed(key("TransferToBeneficiaryPageTitle_Object"));
    }

    public boolean isQuickTransferPageDisplayed() {
        return isDisplayed(key("QuickTransferPageTitle_Object"));
    }

    public boolean isSelfAccountPageDisplayed() {
        return isDisplayed(key("SelfAccountPageTitle_Object"));
    }

    public boolean isShivalikBankSelected() {
        return isSelected(key("ShivalikBankRadio_Object"));
    }

    public boolean isOtherBankSelected() {
        return isSelected(key("OtherBankRadio_Object"));
    }

    public boolean isByMobileNumberCardDisplayed() {
        return isDisplayed(key("ByMobileNumberCard_Object"));
    }

    public boolean isByAccountDetailsCardDisplayed() {
        return isDisplayed(key("ByAccountDetailsCard_Object"));
    }

    public boolean isCancelButtonDisplayed() {
        return isDisplayed(key("CancelButton_Object"));
    }

    public boolean isContinueButtonDisplayed() {
        return isDisplayed(key("ContinueButton_Object"));
    }

    public String getPageTitle() {
        return getText(key("PageTitle_Object"));
    }

    public String getAvailableBalanceText() {
        return getText(key("AvailableBalanceText_Object"));
    }

    public String getTransferToValue() {
        return getAttribute(key("TransferToInput_Object"), "value");
    }
}
