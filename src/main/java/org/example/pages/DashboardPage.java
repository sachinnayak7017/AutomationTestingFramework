package org.example.pages;

/**
 * DashboardPage - Page Object for RIB Dashboard Page
 * Handles all interactions on the main dashboard after login
 */
public class DashboardPage extends BasePage {

    public DashboardPage() {
        super();
    }

    // Get locator key
    private String key(String objectName) {
        return objectName;
    }

    // ========== Navigation Actions ==========

    public void clickLogo() {
        click(key("ShivalikLogo_Object"));
    }

    public void clickBurgerMenu() {
        click(key("BurgerMenuIcon_Object"));
    }

    public void clickHomeNav() {
        click(key("HomeNavLink_Object"));
    }

    public void clickBillsNav() {
        click(key("BillsNavLink_Object"));
    }

    public void clickOffersNav() {
        click(key("OffersNavLink_Object"));
    }

    public void clickProfileButton() {
        click(key("ProfileButton_Object"));
    }

    public void clickNotificationIcon() {
        click(key("NotificationIcon_Object"));
    }

    public void clickSearchIcon() {
        click(key("SearchIcon_Object"));
    }

    public void clickHelpIcon() {
        click(key("HelpIcon_Object"));
    }

    // ========== Quick Services Actions ==========

    public void clickViewChequeStatus() {
        click(key("ViewChequeStatusService_Object"));
    }

    public void clickRequestChequeBook() {
        click(key("RequestChequeBookService_Object"));
    }

    public void clickStopCheque() {
        click(key("StopChequeService_Object"));
    }

    public void clickPositivePay() {
        click(key("PositivePayService_Object"));
    }

    public void clickApplyForLoan() {
        click(key("ApplyForLoanService_Object"));
    }

    public void clickApplyForLocker() {
        click(key("ApplyForLockerService_Object"));
    }

    public void clickGenerateDebitCardPin() {
        click(key("GenerateDebitCardPinService_Object"));
    }

    // ========== Services Actions ==========

    public void clickFundTransfer() {
        click(key("FundTransferService_Object"));
    }

    public void clickOpenFDRD() {
        click(key("OpenFDRDService_Object"));
    }

    public void clickAccountStatement() {
        click(key("AccountStatementService_Object"));
    }

    public void clickManageBeneficiary() {
        click(key("ManageBeneficiaryService_Object"));
    }

    // ========== Account Tab Actions ==========

    public void clickSavingsTab() {
        click(key("SavingsTab_Object"));
    }

    public void clickCurrentTab() {
        click(key("CurrentTab_Object"));
    }

    public void clickDepositTab() {
        click(key("DepositTab_Object"));
    }

    public void clickLoanTab() {
        click(key("LoanTab_Object"));
    }

    public void clickODCCTab() {
        click(key("ODCCTab_Object"));
    }

    // ========== Beneficiary Actions ==========

    public void clickSeeAllBeneficiary() {
        click(key("SeeAllBeneficiary_Object"));
    }

    public void clickAddNewBeneficiary() {
        click(key("AddNewBeneficiary_Object"));
    }

    // ========== Other Actions ==========

    public void clickAccountCard() {
        click(key("AccountCard_Object"));
    }

    public void clickShowDetails() {
        click(key("ShowDetailsIcon_Object"));
    }

    public void clickLeftScrollArrow() {
        click(key("LeftScrollArrow_Object"));
    }

    public void clickRightScrollArrow() {
        click(key("RightScrollArrow_Object"));
    }

    public void clickOpenFDButton() {
        click(key("OpenFDButton_Object"));
    }

    public void selectTransactionAccount() {
        click(key("TransactionAccountDropdown_Object"));
    }

    // ========== Verification Methods ==========

    public boolean isLogoDisplayed() {
        return isDisplayed(key("ShivalikLogo_Object"));
    }

    public boolean isLatestUpdatesDisplayed() {
        return isDisplayed(key("LatestUpdatesTitle_Object"));
    }

    public boolean isQuickServicesDisplayed() {
        return isDisplayed(key("QuickServicesTitle_Object"));
    }

    public boolean isAccountsDisplayed() {
        return isDisplayed(key("AccountsTitle_Object"));
    }

    public boolean isServicesDisplayed() {
        return isDisplayed(key("ServicesTitle_Object"));
    }

    public boolean isRecentTransactionsDisplayed() {
        return isDisplayed(key("RecentTransactionsTitle_Object"));
    }

    public boolean isFavouriteBeneficiaryDisplayed() {
        return isDisplayed(key("FavouriteBeneficiaryTitle_Object"));
    }

    public boolean isFastagDisplayed() {
        return isDisplayed(key("FastagTitle_Object"));
    }

    public boolean isMyInvestmentsDisplayed() {
        return isDisplayed(key("MyInvestmentsTitle_Object"));
    }

    public boolean isUpcomingBillsDisplayed() {
        return isDisplayed(key("UpcomingBillsTitle_Object"));
    }

    public String getProfileName() {
        return getText(key("ProfileName_Object"));
    }

    public String getLastLoginText() {
        return getText(key("LastLoginText_Object"));
    }

    public String getMarqueeText() {
        return getText(key("MarqueeText_Object"));
    }
}
