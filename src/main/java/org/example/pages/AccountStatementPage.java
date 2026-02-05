package org.example.pages;

/**
 * AccountStatementPage - Page Object for Account Statement Page
 * Handles all interactions on the account statement page
 */
public class AccountStatementPage extends BasePage {

    public AccountStatementPage() {
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
        click(key("HomeNavIcon_Object"));
    }

    public void clickBillsNav() {
        click(key("BillsNavIcon_Object"));
    }

    public void clickOffersNav() {
        click(key("OffersNavIcon_Object"));
    }

    public void clickProfileButton() {
        click(key("ProfileButton_Object"));
    }

    public void clickNotificationIcon() {
        click(key("NotificationIcon_Object"));
    }

    // ========== Tab Actions ==========

    public void clickAccountStatementTab() {
        click(key("AccountStatementTab_Object"));
    }

    public void clickAnnualStatementTab() {
        click(key("AnnualStatementTab_Object"));
    }

    // ========== Form Actions ==========

    public void selectAccount() {
        click(key("SelectAccountDropdown_Object"));
    }

    public void selectDuration() {
        click(key("DurationDropdown_Object"));
    }

    public void clickGenerateStatement() {
        click(key("GenerateStatementButton_Object"));
    }

    // ========== Verification Methods ==========

    public boolean isPageTitleDisplayed() {
        return isDisplayed(key("PageTitle_Object"));
    }

    public boolean isAccountStatementTabSelected() {
        return isDisplayed(key("AccountStatementTab_Object"));
    }

    public boolean isAnnualStatementTabDisplayed() {
        return isDisplayed(key("AnnualStatementTab_Object"));
    }

    public boolean isSelectAccountDropdownDisplayed() {
        return isDisplayed(key("SelectAccountDropdown_Object"));
    }

    public boolean isDurationDropdownDisplayed() {
        return isDisplayed(key("DurationDropdown_Object"));
    }

    public boolean isGenerateStatementButtonDisplayed() {
        return isDisplayed(key("GenerateStatementButton_Object"));
    }

    public boolean isNoTransactionsTextDisplayed() {
        return isDisplayed(key("NoTransactionsText_Object"));
    }

    public boolean isTransactionsContainerDisplayed() {
        return isDisplayed(key("TransactionContainer_Object"));
    }

    public boolean isEmptyBannerDisplayed() {
        return isDisplayed(key("EmptyBanner_Object"));
    }

    public String getPageTitle() {
        return getText(key("PageTitle_Object"));
    }
}
