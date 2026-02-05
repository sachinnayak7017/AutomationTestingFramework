package org.example.pages;

/**
 * ManageBeneficiariesPage - Page Object for Manage Beneficiaries Page
 * Handles all interactions on the beneficiary management page
 */
public class ManageBeneficiariesPage extends BasePage {

    public ManageBeneficiariesPage() {
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

    // ========== Tab Actions ==========

    public void clickAllTab() {
        click(key("AllTab_Object"));
    }

    public void clickShivalikTab() {
        click(key("ShivalikTab_Object"));
    }

    public void clickOtherBankTab() {
        click(key("OtherBankTab_Object"));
    }

    // ========== Form Actions ==========

    public void enterSearchBeneficiary(String searchText) {
        type(key("SearchBeneficiaryInput_Object"), searchText);
    }

    public void clearSearchBeneficiary() {
        clear(key("SearchBeneficiaryInput_Object"));
    }

    public void clickSearchButton() {
        click(key("SearchButton_Object"));
    }

    public void clickAddButton() {
        click(key("AddButton_Object"));
    }

    public void clickFilterIcon() {
        click(key("FilterIcon_Object"));
    }

    // ========== Verification Methods ==========

    public boolean isPageTitleDisplayed() {
        return isDisplayed(key("PageTitle_Object"));
    }

    public boolean isSearchInputDisplayed() {
        return isDisplayed(key("SearchBeneficiaryInput_Object"));
    }

    public boolean isAddButtonDisplayed() {
        return isDisplayed(key("AddButton_Object"));
    }

    public boolean isAllTabDisplayed() {
        return isDisplayed(key("AllTab_Object"));
    }

    public boolean isShivalikTabDisplayed() {
        return isDisplayed(key("ShivalikTab_Object"));
    }

    public boolean isOtherBankTabDisplayed() {
        return isDisplayed(key("OtherBankTab_Object"));
    }

    public boolean isCoolingPeriodTextDisplayed() {
        return isDisplayed(key("CoolingPeriodText_Object"));
    }

    public boolean isNoBeneficiariesCoolingTextDisplayed() {
        return isDisplayed(key("NoBeneficiariesCoolingText_Object"));
    }

    public boolean isFavoriteTextDisplayed() {
        return isDisplayed(key("FavoriteText_Object"));
    }

    public boolean isNoFavoriteBeneficiariesTextDisplayed() {
        return isDisplayed(key("NoFavoriteBeneficiariesText_Object"));
    }

    public boolean isNoBeneficiariesTextDisplayed() {
        return isDisplayed(key("NoBeneficiariesText_Object"));
    }

    public boolean isBeneficiaryListDisplayed() {
        return isDisplayed(key("BeneficiaryList_Object"));
    }

    public String getPageTitle() {
        return getText(key("PageTitle_Object"));
    }

    public String getCoolingPeriodMessage() {
        return getText(key("CoolingPeriodMessage_Object"));
    }
}
