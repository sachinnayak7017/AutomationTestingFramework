package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

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
        click(key("Db_HomeNavLink_Object"));
    }

    public void clickBillsNav() {
        click(key("Db_BillsNavLink_Object"));
    }

    public void clickOffersNav() {
        click(key("Db_OffersNavLink_Object"));
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
        // Wait for loading overlay to disappear (returns instantly if not present)
        waitForLoadingOverlay();
        // Scroll to center of viewport to avoid fixed header
        scrollToElement(key("FundTransferService_Object"));
        // Normal click, fallback to JS click if intercepted by header
        try {
            click(key("FundTransferService_Object"));
        } catch (Exception e) {
            logger.warn("Normal click intercepted, using JS click");
            jsClick(key("FundTransferService_Object"));
        }
        logger.info("Clicked on Fund Transfer service");
    }

    public void clickOpenFDRD() {
        click(key("OpenFDRDService_Object"));
    }

    public void clickAccountStatement() {
        click(key("AccountStatementService_Object"));
    }

    public void clickManageBeneficiary() {
        // Wait for loading overlay to disappear
        waitForLoadingOverlay();
        // Scroll to center of viewport to avoid fixed header
        scrollToElement(key("Db_ManageBeneficiaryService_Object"));
        try {
            click(key("Db_ManageBeneficiaryService_Object"));
        } catch (Exception e) {
            logger.warn("Normal click intercepted for ManageBeneficiary, using JS click");
            jsClick(key("Db_ManageBeneficiaryService_Object"));
        }
        logger.info("Clicked on Manage Beneficiary service");
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
        click(key("Db_OpenFDButton_Object"));
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
        return isDisplayed(key("Db_QuickServicesTitle_Object"));
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

    // ========== Additional Verification Methods ==========

    public boolean isBurgerMenuDisplayed() {
        return isDisplayed(key("BurgerMenuIcon_Object"));
    }

    public boolean isHomeNavDisplayed() {
        return isDisplayed(key("Db_HomeNavLink_Object"));
    }

    public boolean isBillsNavDisplayed() {
        return isDisplayed(key("Db_BillsNavLink_Object"));
    }

    public boolean isOffersNavDisplayed() {
        return isDisplayed(key("Db_OffersNavLink_Object"));
    }

    public boolean isProfileButtonDisplayed() {
        return isDisplayed(key("ProfileButton_Object"));
    }

    public boolean isNotificationIconDisplayed() {
        return isDisplayed(key("NotificationIcon_Object"));
    }

    public boolean isSearchIconDisplayed() {
        return isDisplayed(key("SearchIcon_Object"));
    }

    public boolean isHelpIconDisplayed() {
        return isDisplayed(key("HelpIcon_Object"));
    }

    public boolean isHomeNavClickable() {
        return isEnabled(key("Db_HomeNavLink_Object"));
    }

    public boolean isBillsNavClickable() {
        return isEnabled(key("Db_BillsNavLink_Object"));
    }

    public boolean isOffersNavClickable() {
        return isEnabled(key("Db_OffersNavLink_Object"));
    }

    public boolean isProfileButtonClickable() {
        return isEnabled(key("ProfileButton_Object"));
    }

    public boolean isNotificationIconClickable() {
        return isEnabled(key("NotificationIcon_Object"));
    }

    public boolean isSearchIconClickable() {
        return isEnabled(key("SearchIcon_Object"));
    }

    public boolean isHelpIconClickable() {
        return isEnabled(key("HelpIcon_Object"));
    }

    // Quick Services verifications
    public boolean isViewChequeStatusDisplayed() {
        return isDisplayed(key("ViewChequeStatusService_Object"));
    }

    public boolean isRequestChequeBookDisplayed() {
        return isDisplayed(key("RequestChequeBookService_Object"));
    }

    public boolean isStopChequeDisplayed() {
        return isDisplayed(key("StopChequeService_Object"));
    }

    public boolean isPositivePayDisplayed() {
        return isDisplayed(key("PositivePayService_Object"));
    }

    public boolean isApplyForLoanDisplayed() {
        return isDisplayed(key("ApplyForLoanService_Object"));
    }

    public boolean isApplyForLockerDisplayed() {
        return isDisplayed(key("ApplyForLockerService_Object"));
    }

    public boolean isGenerateDebitCardPinDisplayed() {
        return isDisplayed(key("GenerateDebitCardPinService_Object"));
    }

    // Account tab verifications
    public boolean isSavingsTabDisplayed() {
        return isDisplayed(key("SavingsTab_Object"));
    }

    public boolean isCurrentTabDisplayed() {
        return isDisplayed(key("CurrentTab_Object"));
    }

    public boolean isDepositTabDisplayed() {
        return isDisplayed(key("DepositTab_Object"));
    }

    public boolean isLoanTabDisplayed() {
        return isDisplayed(key("LoanTab_Object"));
    }

    public boolean isODCCTabDisplayed() {
        return isDisplayed(key("ODCCTab_Object"));
    }

    public boolean isAccountCardDisplayed() {
        return isDisplayed(key("AccountCard_Object"));
    }

    public boolean isAccountNumberDisplayed() {
        return isDisplayed(key("AccountNumber_Object"));
    }

    public boolean isShowDetailsIconDisplayed() {
        return isDisplayed(key("ShowDetailsIcon_Object"));
    }

    // Services verifications
    public boolean isFundTransferServiceDisplayed() {
        return isDisplayed(key("FundTransferService_Object"));
    }

    public boolean isOpenFDRDServiceDisplayed() {
        return isDisplayed(key("OpenFDRDService_Object"));
    }

    public boolean isAccountStatementServiceDisplayed() {
        return isDisplayed(key("AccountStatementService_Object"));
    }

    public boolean isManageBeneficiaryServiceDisplayed() {
        return isDisplayed(key("Db_ManageBeneficiaryService_Object"));
    }

    public boolean isApplyForLoanServiceCardDisplayed() {
        return isDisplayed(key("ApplyForLoanServiceCard_Object"));
    }

    public boolean isApplyForLockerServiceCardDisplayed() {
        return isDisplayed(key("ApplyForLockerServiceCard_Object"));
    }

    // Beneficiary verifications
    public boolean isSeeAllBeneficiaryDisplayed() {
        return isDisplayed(key("SeeAllBeneficiary_Object"));
    }

    public boolean isAddNewBeneficiaryDisplayed() {
        return isDisplayed(key("AddNewBeneficiary_Object"));
    }

    public boolean isNoBeneficiaryImageDisplayed() {
        try {
            return isDisplayed(key("NoBeneficiaryImage_Object"));
        } catch (Exception e) {
            return false;
        }
    }

    // Recent Transactions verifications
    public boolean isTransactionAccountDropdownDisplayed() {
        return isDisplayed(key("TransactionAccountDropdown_Object"));
    }

    public boolean isTransactionListDisplayed() {
        return isDisplayed(key("TransactionList_Object"));
    }

    public boolean isTransactionItemDisplayed() {
        try {
            return isDisplayed(key("TransactionItem_Object"));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTransactionAmountDisplayed() {
        try {
            return isDisplayed(key("TransactionAmount_Object"));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTransactionDateDisplayed() {
        try {
            return isDisplayed(key("TransactionDate_Object"));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTransactionNameDisplayed() {
        try {
            return isDisplayed(key("TransactionName_Object"));
        } catch (Exception e) {
            return false;
        }
    }

    // Download verifications
    public boolean isPlayStoreIconDisplayed() {
        return isDisplayed(key("PlayStoreIcon_Object"));
    }

    public boolean isAppStoreIconDisplayed() {
        return isDisplayed(key("AppStoreIcon_Object"));
    }

    // Other section verifications
    public boolean isBBPSBannerDisplayed() {
        try {
            return isDisplayed(key("BBPSBanner_Object"));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isOpenFDButtonDisplayed() {
        return isDisplayed(key("Db_OpenFDButton_Object"));
    }

    public boolean isLeftScrollArrowDisplayed() {
        try {
            return isDisplayed(key("LeftScrollArrow_Object"));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRightScrollArrowDisplayed() {
        try {
            return isDisplayed(key("RightScrollArrow_Object"));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCustomerCarePhoneDisplayed() {
        return isDisplayed(key("CustomerCarePhone_Object"));
    }

    public boolean isCustomerCareEmailDisplayed() {
        return isDisplayed(key("CustomerCareEmail_Object"));
    }

    public boolean isCustomerCareSectionDisplayed() {
        try {
            return isDisplayed(key("CustomerCareSection_Object"));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDownloadAppSectionDisplayed() {
        try {
            return isDisplayed(key("DownloadAppSection_Object"));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDownloadAppTextDisplayed() {
        try {
            return isDisplayed(key("DownloadAppText_Object"));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFastagRegisterTextDisplayed() {
        try {
            return isDisplayed(key("FastagRegisterText_Object"));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFDInvestmentTextDisplayed() {
        try {
            return isDisplayed(key("FDInvestmentText_Object"));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFDInterestRateTextDisplayed() {
        try {
            return isDisplayed(key("FDInterestRateText_Object"));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNoBillsImageDisplayed() {
        try {
            return isDisplayed(key("NoBillsImage_Object"));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isBillListDisplayed() {
        try {
            return isDisplayed(key("BillList_Object"));
        } catch (Exception e) {
            return false;
        }
    }

    // getText methods
    public String getAccountNumber() {
        return getText(key("AccountNumber_Object"));
    }

    // ========== Click Actions for Download Links ==========

    public void clickPlayStoreIcon() {
        click(key("PlayStoreIcon_Object"));
    }

    public void clickAppStoreIcon() {
        click(key("AppStoreIcon_Object"));
    }

    // ========== Wait and Scroll Methods ==========

    public void waitForDashboardLoad() {
        try {
            waitForPageLoad();
            waitForElementVisible("ProfileName_Object", 30);
        } catch (Exception e) {
            logger.warn("Dashboard page load wait encountered issue: " + e.getMessage());
        }
    }

    public void scrollToQuickServices() {
        scrollToElement(key("Db_QuickServicesTitle_Object"));
    }

    public void scrollToAccounts() {
        scrollToElement(key("AccountsTitle_Object"));
    }

    public void scrollToServices() {
        scrollToElement(key("ServicesTitle_Object"));
    }

    public void scrollToRecentTransactions() {
        scrollToElement(key("RecentTransactionsTitle_Object"));
    }

    public void scrollToFastag() {
        scrollToElement(key("FastagTitle_Object"));
    }

    public void scrollToMyInvestments() {
        scrollToElement(key("MyInvestmentsTitle_Object"));
    }

    public void scrollToUpcomingBills() {
        scrollToElement(key("UpcomingBillsTitle_Object"));
    }

    public void scrollToFavouriteBeneficiary() {
        scrollToElement(key("FavouriteBeneficiaryTitle_Object"));
    }

    public void navigateBack() {
        try {
            executeJavaScript("window.history.back()");
            waitForPageLoad();
        } catch (Exception e) {
            logger.warn("Navigate back failed: " + e.getMessage());
        }
    }

    public boolean isDashboardDisplayed() {
        try {
            return isQuickServicesDisplayed() || isAccountsDisplayed() || isLogoDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Wait for loading overlay to disappear. Returns instantly if not present.
     */
    private void waitForLoadingOverlay() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(30))
                    .until(ExpectedConditions.invisibilityOfElementLocated(
                            By.cssSelector("div.MuiBox-root[style*='height: 100vh']")));
        } catch (Exception e) {
            logger.debug("Loading overlay check completed");
        }
    }
}
