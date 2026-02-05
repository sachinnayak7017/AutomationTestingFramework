package org.example.pages;

/**
 * ApplyForLoanPage - Page Object for Apply For Loan Page
 * Handles all interactions on the loan application page
 */
public class ApplyForLoanPage extends BasePage {

    public ApplyForLoanPage() {
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

    // ========== Form Actions ==========

    public void selectYes() {
        click(key("RadioYes_Object"));
    }

    public void selectNo() {
        click(key("RadioNo_Object"));
    }

    public void clickNext() {
        click(key("NextButton_Object"));
    }

    // ========== Verification Methods ==========

    public boolean isLogoDisplayed() {
        return isDisplayed(key("ShivalikLogo_Object"));
    }

    public boolean isProgressBarActiveDisplayed() {
        return isDisplayed(key("ProgressBarActive_Object"));
    }

    public boolean isQuestionTitleDisplayed() {
        return isDisplayed(key("QuestionTitle_Object"));
    }

    public boolean isCustomerQuestionDisplayed() {
        return isDisplayed(key("CustomerQuestion_Object"));
    }

    public boolean isYesRadioDisplayed() {
        return isDisplayed(key("RadioYes_Object"));
    }

    public boolean isNoRadioDisplayed() {
        return isDisplayed(key("RadioNo_Object"));
    }

    public boolean isNextButtonDisplayed() {
        return isDisplayed(key("NextButton_Object"));
    }

    public boolean isSpecialOffersTextDisplayed() {
        return isDisplayed(key("SpecialOffersText_Object"));
    }

    public boolean isYesSelected() {
        return isSelected(key("RadioYes_Object"));
    }

    public boolean isNoSelected() {
        return isSelected(key("RadioNo_Object"));
    }

    public String getQuestionTitle() {
        return getText(key("QuestionTitle_Object"));
    }

    public String getCustomerQuestion() {
        return getText(key("CustomerQuestion_Object"));
    }
}
