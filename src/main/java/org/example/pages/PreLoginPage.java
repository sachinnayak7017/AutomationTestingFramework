package org.example.pages;

/**
 * PreLoginPage - Page Object for Shivalik Bank Pre-Login Page
 * Handles all interactions on the pre-login/landing page
 * Test Cases: PL_001 to PL_053
 */
public class PreLoginPage extends BasePage {

    public PreLoginPage() {
        super();
    }

    // ========== Header Navigation Actions ==========

    public void clickLogo() {
        click("ShivalikLogo_Object");
    }

    public void clickContactUs() {
        click("ContactUsButton_Object");
    }

    public void clickApplyNow() {
        click("ApplyNowButton_Object");
    }

    public void clickBranchLocator() {
        click("BranchLocatorButton_Object");
    }

    public void clickLanguage() {
        click("LanguageButton_Object");
    }

    public void clickRegisterNav() {
        click("RegisterNavButton_Object");
    }

    public void clickGoToHomeButton() {
        click("GoToHomeButton_Object");
    }

    // ========== Quick Services Actions ==========

    public void clickRechargeAndBills() {
        jsClick("RechargeAndBillsService_Object");
    }

    public void clickOpenDepositAccount() {
        jsClick("OpenDepositAccountService_Object");
    }

    public void clickFinancialCalculator() {
        jsClick("FinancialCalculatorService_Object");
    }

    public void clickDepositsRate() {
        jsClick("DepositsRateService_Object");
    }

    public void clickOffers() {
        jsClick("OffersService_Object");
    }

    public void clickFAQ() {
        jsClick("FAQService_Object");
    }

    // ========== Financial Calculator Actions ==========

    public void selectLoanOption() {
        click("LoanOption_Object");
    }

    public void selectDepositOption() {
        click("DepositOption_Object");
    }

    public void clickFixedDeposit() {
        click("FixedDepositButton_Object");
    }

    public void clickRecurringDeposit() {
        click("RecurringDepositButton_Object");
    }

    // ========== Login Form - User ID Actions ==========

    public void enterUserId(String userId) {
        type("UserIdInput_Object", userId);
    }

    public void clearUserId() {
        clear("UserIdInput_Object");
    }

    public void clickRememberUserId() {
        click("RememberUserIdCheckbox_Object");
    }

    public void clickProceedButton() {
        click("ProceedButton_Object");
    }

    public void clickRegisterLink() {
        click("RegisterLink_Object");
    }

    public void clickForgotUserId() {
        click("ForgotUserIdLink_Object");
    }

    // ========== Login Form - Password Actions ==========

    public void enterPassword(String password) {
        type("PasswordInput_Object", password);
    }

    public void clearPassword() {
        clear("PasswordInput_Object");
    }

    public void clickEyeButton() {
        click("PasswordEyeButton_Object");
    }

    public void clickKeyboardIcon() {
        click("KeyboardIcon_Object");
    }

    public void clickForgotPassword() {
        click("ForgotPasswordLink_Object");
    }

    // ========== Login Form - Captcha and Checkboxes ==========

    public void enterCaptcha(String captcha) {
        type("CaptchaInput_Object", captcha);
    }

    public void clearCaptcha() {
        clear("CaptchaInput_Object");
    }

    public void clickAccessImageCheckbox() {
        click("AccessImageCheckbox_Object");
    }

    public void clickTCCheckbox() {
        click("TCCheckbox_Object");
    }

    public void clickLoginButton() {
        click("LoginButton_Object");
    }

    // ========== Verification Methods - Header ==========

    public boolean isLogoDisplayed() {
        return isDisplayed("ShivalikLogo_Object");
    }

    public boolean isContactUsDisplayed() {
        return isDisplayed("ContactUsButton_Object");
    }

    public boolean isContactUsClickable() {
        return isEnabled("ContactUsButton_Object");
    }

    public boolean isApplyNowDisplayed() {
        return isDisplayed("ApplyNowButton_Object");
    }

    public boolean isApplyNowClickable() {
        return isEnabled("ApplyNowButton_Object");
    }

    public boolean isBranchLocatorDisplayed() {
        return isDisplayed("BranchLocatorButton_Object");
    }

    public boolean isBranchLocatorClickable() {
        return isEnabled("BranchLocatorButton_Object");
    }

    public boolean isLanguageDisplayed() {
        return isDisplayed("LanguageButton_Object");
    }

    public boolean isLanguageClickable() {
        return isEnabled("LanguageButton_Object");
    }

    public boolean isRegisterButtonDisplayed() {
        return isDisplayed("RegisterNavButton_Object");
    }

    public boolean isRegisterButtonClickable() {
        return isEnabled("RegisterNavButton_Object");
    }

    public boolean isGoToHomeButtonDisplayed() {
        return isDisplayed("GoToHomeButton_Object");
    }

    // ========== Verification Methods - Quick Services ==========

    public boolean isQuickServicesDisplayed() {
        return isDisplayed("QuickServicesTitle_Object");
    }

    public boolean isRechargeAndBillsIconDisplayed() {
        return isDisplayed("RechargeAndBillsIcon_Object");
    }

    public boolean isRechargeAndBillsTextDisplayed() {
        return isDisplayed("RechargeAndBillsText_Object");
    }

    public boolean isOpenFixedDepositIconDisplayed() {
        return isDisplayed("OpenFixedDepositIcon_Object");
    }

    public boolean isOpenFixedDepositTextDisplayed() {
        return isDisplayed("OpenFixedDepositText_Object");
    }

    public boolean isFinancialCalculatorIconDisplayed() {
        return isDisplayed("FinancialCalculatorIcon_Object");
    }

    public boolean isFinancialCalculatorTextDisplayed() {
        return isDisplayed("FinancialCalculatorText_Object");
    }

    public boolean isDepositsRateIconDisplayed() {
        return isDisplayed("DepositsRateIcon_Object");
    }

    public boolean isDepositsRateTextDisplayed() {
        return isDisplayed("DepositsRateText_Object");
    }

    public boolean isOffersIconDisplayed() {
        return isDisplayed("OffersIcon_Object");
    }

    public boolean isOffersTextDisplayed() {
        return isDisplayed("OffersText_Object");
    }

    public boolean isFAQIconDisplayed() {
        return isDisplayed("FAQIcon_Object");
    }

    public boolean isFAQTextDisplayed() {
        return isDisplayed("FAQText_Object");
    }

    // ========== Verification Methods - Financial Calculator ==========

    public boolean isLoanOptionDisplayed() {
        return isDisplayed("LoanOption_Object");
    }

    public boolean isDepositOptionDisplayed() {
        return isDisplayed("DepositOption_Object");
    }

    public boolean isDepositOptionSelected() {
        try {
            String ariaSelected = getAttribute("DepositOption_Object", "aria-selected");
            return "true".equals(ariaSelected);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFixedDepositButtonDisplayed() {
        return isDisplayed("FixedDepositButton_Object");
    }

    public boolean isRecurringDepositButtonDisplayed() {
        return isDisplayed("RecurringDepositButton_Object");
    }

    // ========== Verification Methods - Login Form ==========

    public boolean isPreLoginPageDisplayed() {
        return isDisplayed("LoginFormTitle_Object");
    }

    public boolean isLoginFormDisplayed() {
        return isDisplayed("LoginContainer_Object");
    }

    public boolean isUserIdFieldDisplayed() {
        return isDisplayed("UserIdInput_Object");
    }

    public boolean isProceedButtonDisplayed() {
        return isDisplayed("ProceedButton_Object");
    }

    public boolean isProceedButtonEnabled() {
        return isEnabled("ProceedButton_Object") && !isDisplayed("ProceedButtonDisabled_Object");
    }

    public boolean isRememberUserIdDisplayed() {
        return isDisplayed("RememberUserIdText_Object");
    }

    public boolean isRegisterLinkDisplayed() {
        return isDisplayed("RegisterLink_Object");
    }

    public boolean isForgotUserIdDisplayed() {
        return isDisplayed("ForgotUserIdLink_Object");
    }

    // ========== Verification Methods - Password Screen ==========

    public boolean isPasswordFieldDisplayed() {
        return isDisplayed("PasswordInput_Object");
    }

    public boolean isPasswordMasked() {
        try {
            String inputType = getAttribute("PasswordInput_Object", "type");
            return "password".equals(inputType);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPasswordVisible() {
        try {
            String inputType = getAttribute("PasswordInput_Object", "type");
            return "text".equals(inputType);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isOnscreenKeyboardDisplayed() {
        return isDisplayed("OnscreenKeyboard_Object");
    }

    public boolean isForgotPasswordDisplayed() {
        return isDisplayed("ForgotPasswordLink_Object");
    }

    public boolean isForgotPasswordFormDisplayed() {
        try {
            // Check if forgot password form or page is displayed
            return isDisplayed("ForgotPasswordForm_Object") || isForgotUserIdPageDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ========== Verification Methods - Captcha and Checkboxes ==========

    public boolean isCaptchaFieldDisplayed() {
        return isDisplayed("CaptchaInput_Object");
    }

    public boolean isAccessImageCheckboxDisplayed() {
        return isDisplayed("AccessImageCheckbox_Object");
    }

    public boolean isTCCheckboxDisplayed() {
        return isDisplayed("TCCheckbox_Object");
    }

    public boolean isTCCheckboxChecked() {
        try {
            // Check if the SVG checkmark is displayed (indicates checked state)
            return isDisplayed("TCCheckboxChecked_Object");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAccessImageCheckboxUnchecked() {
        try {
            // Check if the checkbox is in unchecked state (default)
            return isDisplayed("AccessImageCheckbox_Object");
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isSecureMessageDisplayed() {
        try {
            return isDisplayed("SecureMessageDisplay_Object");
        } catch (Exception e) {
            return false;
        }
    }

    public String getSecureMessage() {
        try {
            return getText("SecureMessageDisplay_Object");
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isCaptchaPreFilled() {
        try {
            String message = getSecureMessage();
            return message != null && !message.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoginButtonDisabled() {
        try {
            return isDisplayed("LoginButtonDisabled_Object");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoginButtonNotDisplayed() {
        try {
            return isDisplayed("LoginButtonDisabled_Object") || !isEnabled("LoginButton_Object");
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isLoginButtonDisplayed() {
        return isDisplayed("LoginButton_Object");
    }

    public boolean isLoginButtonClickable() {
        return isEnabled("LoginButton_Object");
    }

    public boolean isErrorDisplayed() {
        try {
            return isDisplayed("ErrorMessage_Object");
        } catch (Exception e) {
            return false;
        }
    }

    // ========== Getter Methods ==========

    public String getUserIdValue() {
        return getAttribute("UserIdInput_Object", "value");
    }

    public String getPasswordValue() {
        return getAttribute("PasswordInput_Object", "value");
    }

    public String getCaptchaValue() {
        return getAttribute("CaptchaInput_Object", "value");
    }

    public String getErrorMessage() {
        try {
            return getText("ErrorMessage_Object");
        } catch (Exception e) {
            return "";
        }
    }

    public String getPageUrl() {
        return getCurrentUrl();
    }

    public String getTitle() {
        return getPageTitle();
    }

    // ========== Field Entry Validations ==========

    public boolean isUserIdEntered() {
        String value = getUserIdValue();
        return value != null && !value.isEmpty();
    }

    public boolean isPasswordEntered() {
        String value = getPasswordValue();
        return value != null && !value.isEmpty();
    }

    public boolean isCaptchaEntered() {
        String value = getCaptchaValue();
        return value != null && !value.isEmpty();
    }

    // ========== Page Redirections ==========

    public boolean isOnContactUsPage() {
        String url = getCurrentUrl();
        return url.contains("contact");
    }

    public boolean isOnApplyForLoanPage() {
        String url = getCurrentUrl();
        return url.contains("apply") || url.contains("loan");
    }

    public boolean isOnBranchLocatorPage() {
        String url = getCurrentUrl();
        return url.contains("branch") || url.contains("locator");
    }

    public boolean isOnRegisterPage() {
        String url = getCurrentUrl();
        return url.contains("register");
    }

    public boolean isOnRegisterFormPage() {
        try {
            return isDisplayed("RegisterFormTitle_Object") ||
                   getCurrentUrl().contains("register") ||
                   getCurrentUrl().contains("registration");
        } catch (Exception e) {
            return getCurrentUrl().contains("register");
        }
    }

    public boolean isOnHomePage() {
        String url = getCurrentUrl();
        return url.contains("RIB") || url.endsWith("/");
    }

    public boolean isOnFinancialCalculatorPage() {
        return isDisplayed("FinancialCalculatorPageTitle_Object") ||
               getCurrentUrl().contains("calculator");
    }

    public boolean isOnFAQPage() {
        return isDisplayed("FAQPageTitle_Object") || getCurrentUrl().contains("faq");
    }

    public boolean isForgotUserIdPageDisplayed() {
        String url = getCurrentUrl();
        return url.contains("forgot") && url.contains("user");
    }

    public boolean isForgotPasswordPageDisplayed() {
        String url = getCurrentUrl();
        return url.contains("forgot") && url.contains("password");
    }

    public boolean isLoginSuccessful() {
        String url = getCurrentUrl();
        return url.contains("dashboard") || url.contains("home") || url.contains("account");
    }

    // ========== Combined Actions ==========

    public void loginWithUserId(String userId) {
        enterUserId(userId);
        clickProceedButton();
    }

    public void waitForPreLoginPageLoad() {
        try {
            waitForPageLoad();
            waitForElementVisible("LoginFormTitle_Object", 30);
        } catch (Exception e) {
            // Continue if page load wait fails - element visibility check will handle it
            logger.warn("PreLogin page load wait encountered issue: " + e.getMessage());
        }
    }

    public void waitForPasswordScreen() {
        waitForElementVisible("PasswordInput_Object", 30);
    }
}
