package org.example.pages;

/**
 * ViewChequeStatusPage - Page Object for View Cheque Status Page
 * Handles all interactions on the cheque status page
 */
public class ViewChequeStatusPage extends BasePage {

    public ViewChequeStatusPage() {
        super();
    }

    // Get locator key
    private String key(String objectName) {
        return objectName;
    }

    // ========== Navigation Actions ==========

    public void clickBackButton() {
        click(key("BackButton_Object"));
    }

    public void clickHomeNav() {
        click(key("HomeNavLink_Object"));
    }

    public void clickProfileButton() {
        click(key("ProfileButton_Object"));
    }

    // ========== Form Actions ==========

    public void selectAccount() {
        click(key("SelectAccountDropdown_Object"));
    }

    public void enterChequeNumber(String chequeNumber) {
        type(key("ChequeNumberInput_Object"), chequeNumber);
    }

    public void clearChequeNumber() {
        clear(key("ChequeNumberInput_Object"));
    }

    public void clickCancel() {
        click(key("CancelButton_Object"));
    }

    public void clickContinue() {
        click(key("ContinueButton_Object"));
    }

    // ========== Verification Methods ==========

    public boolean isPageTitleDisplayed() {
        return isDisplayed(key("PageTitle_Object"));
    }

    public boolean isSelectAccountDropdownDisplayed() {
        return isDisplayed(key("SelectAccountDropdown_Object"));
    }

    public boolean isChequeNumberInputDisplayed() {
        return isDisplayed(key("ChequeNumberInput_Object"));
    }

    public boolean isChequeNumberNoteDisplayed() {
        return isDisplayed(key("ChequeNumberNote_Object"));
    }

    public boolean isCancelButtonDisplayed() {
        return isDisplayed(key("CancelButton_Object"));
    }

    public boolean isContinueButtonDisplayed() {
        return isDisplayed(key("ContinueButton_Object"));
    }

    public boolean isEmptyBannerDisplayed() {
        return isDisplayed(key("EmptyBanner_Object"));
    }

    public boolean isChequeFormDisplayed() {
        return isDisplayed(key("ChequeForm_Object"));
    }

    public String getPageTitle() {
        return getText(key("PageTitle_Object"));
    }

    public String getChequeNumberNote() {
        return getText(key("ChequeNumberNote_Object"));
    }

    public String getChequeNumberValue() {
        return getAttribute(key("ChequeNumberInput_Object"), "value");
    }
}
