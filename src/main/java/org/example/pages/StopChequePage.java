package org.example.pages;

/**
 * StopChequePage - Page Object for Stop Cheque Page
 * Handles all interactions on the stop cheque page
 */
public class StopChequePage extends BasePage {

    public StopChequePage() {
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

    public void selectSingleCheque() {
        click(key("SingleChequeRadio_Object"));
    }

    public void selectMultipleCheque() {
        click(key("MultipleChequeRadio_Object"));
    }

    public void enterChequeNumber(String chequeNumber) {
        type(key("ChequeNumberInput_Object"), chequeNumber);
    }

    public void clearChequeNumber() {
        clear(key("ChequeNumberInput_Object"));
    }

    public void selectReason() {
        click(key("ReasonDropdown_Object"));
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

    public boolean isSingleChequeRadioDisplayed() {
        return isDisplayed(key("SingleChequeRadio_Object"));
    }

    public boolean isMultipleChequeRadioDisplayed() {
        return isDisplayed(key("MultipleChequeRadio_Object"));
    }

    public boolean isChequeNumberInputDisplayed() {
        return isDisplayed(key("ChequeNumberInput_Object"));
    }

    public boolean isReasonDropdownDisplayed() {
        return isDisplayed(key("ReasonDropdown_Object"));
    }

    public boolean isCancelButtonDisplayed() {
        return isDisplayed(key("CancelButton_Object"));
    }

    public boolean isContinueButtonDisplayed() {
        return isDisplayed(key("ContinueButton_Object"));
    }

    public boolean isNoticeTextDisplayed() {
        return isDisplayed(key("NoticeText_Object"));
    }

    public boolean isSingleChequeSelected() {
        return isSelected(key("SingleChequeRadio_Object"));
    }

    public boolean isMultipleChequeSelected() {
        return isSelected(key("MultipleChequeRadio_Object"));
    }

    public String getPageTitle() {
        return getText(key("PageTitle_Object"));
    }

    public String getNoticeText() {
        return getText(key("NoticeText_Object"));
    }

    public String getChequeNumberValue() {
        return getAttribute(key("ChequeNumberInput_Object"), "value");
    }
}
