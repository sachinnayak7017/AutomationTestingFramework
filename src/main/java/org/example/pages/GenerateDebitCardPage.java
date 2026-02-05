package org.example.pages;

/**
 * GenerateDebitCardPage - Page Object for Generate Debit Card Green PIN Page
 * Handles all interactions on the debit card PIN generation page
 */
public class GenerateDebitCardPage extends BasePage {

    public GenerateDebitCardPage() {
        super();
    }

    // Get locator key
    private String key(String objectName) {
        return objectName;
    }

    // ========== Form Actions ==========

    public void enterMobileNumber(String mobileNumber) {
        type(key("MobileNumberInput_Object"), mobileNumber);
    }

    public void clearMobileNumber() {
        clear(key("MobileNumberInput_Object"));
    }

    public void enterCustomerId(String customerId) {
        type(key("CustomerIdInput_Object"), customerId);
    }

    public void clearCustomerId() {
        clear(key("CustomerIdInput_Object"));
    }

    public void enterCaptcha(String captcha) {
        type(key("CaptchaInput_Object"), captcha);
    }

    public void clearCaptcha() {
        clear(key("CaptchaInput_Object"));
    }

    public void clickRefreshCaptcha() {
        click(key("CaptchaRefreshLink_Object"));
    }

    public void clickNext() {
        click(key("NextButton_Object"));
    }

    public void clickCustomerIdInfo() {
        click(key("CustomerIdInfoIcon_Object"));
    }

    // ========== Verification Methods ==========

    public boolean isLogoDisplayed() {
        return isDisplayed(key("ShivalikBankLogo_Object"));
    }

    public boolean isPageTitleDisplayed() {
        return isDisplayed(key("PageTitle_Object"));
    }

    public boolean isMobileNumberInputDisplayed() {
        return isDisplayed(key("MobileNumberInput_Object"));
    }

    public boolean isCustomerIdInputDisplayed() {
        return isDisplayed(key("CustomerIdInput_Object"));
    }

    public boolean isCaptchaImageDisplayed() {
        return isDisplayed(key("CaptchaImage_Object"));
    }

    public boolean isCaptchaInputDisplayed() {
        return isDisplayed(key("CaptchaInput_Object"));
    }

    public boolean isNextButtonDisplayed() {
        return isDisplayed(key("NextButton_Object"));
    }

    public boolean isCustomerIdTooltipDisplayed() {
        return isDisplayed(key("CustomerIdTooltip_Object"));
    }

    public String getMobileNumberValue() {
        return getAttribute(key("MobileNumberInput_Object"), "value");
    }

    public String getCustomerIdValue() {
        return getAttribute(key("CustomerIdInput_Object"), "value");
    }

    public String getPageTitle() {
        return getText(key("PageTitle_Object"));
    }
}
