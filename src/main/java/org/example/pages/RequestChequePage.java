package org.example.pages;

/**
 * RequestChequePage - Page Object for Request Cheque Book Page
 * Handles all interactions on the cheque book request page
 */
public class RequestChequePage extends BasePage {

    public RequestChequePage() {
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

    // ========== Form Actions ==========

    public void selectAccount() {
        click(key("SelectAccountDropdown_Object"));
    }

    public void selectNumberOfLeaf() {
        click(key("SelectLeafDropdown_Object"));
    }

    public void clickShowLeafCharges() {
        click(key("ShowLeafChargesLink_Object"));
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

    public boolean isSelectLeafDropdownDisplayed() {
        return isDisplayed(key("SelectLeafDropdown_Object"));
    }

    public boolean isShowLeafChargesLinkDisplayed() {
        return isDisplayed(key("ShowLeafChargesLink_Object"));
    }

    public boolean isCancelButtonDisplayed() {
        return isDisplayed(key("CancelButton_Object"));
    }

    public boolean isContinueButtonDisplayed() {
        return isDisplayed(key("ContinueButton_Object"));
    }

    public boolean isAccountHolderNameDisplayed() {
        return isDisplayed(key("AccountHolderNameValue_Object"));
    }

    public boolean isMailingAddressDisplayed() {
        return isDisplayed(key("MailingAddressValue_Object"));
    }

    public String getPageTitle() {
        return getText(key("PageTitle_Object"));
    }

    public String getAccountHolderName() {
        return getText(key("AccountHolderNameValue_Object"));
    }

    public String getMailingAddress() {
        return getText(key("MailingAddressValue_Object"));
    }
}
