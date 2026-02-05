package org.example.pages;

/**
 * OpenDepositPage - Page Object for Open Deposit Page
 * Handles all interactions on the deposit pages including:
 * - Main deposit menu
 * - Fixed Deposit
 * - Recurring Flexi Deposit
 * - Tax Saving Fixed Deposit
 */
public class OpenDepositPage extends BasePage {

    public OpenDepositPage() {
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

    // ========== Main Menu Actions ==========

    public void clickFixedDeposit() {
        click(key("FixedDepositCard_Object"));
    }

    public void clickRecurringFlexi() {
        click(key("RecurringFlexiCard_Object"));
    }

    public void clickTaxSavingFD() {
        click(key("TaxSavingFDCard_Object"));
    }

    // ========== Calculator Actions ==========

    public void enterInvestmentAmount(String amount) {
        type(key("InvestmentAmountInput_Object"), amount);
    }

    public void clearInvestmentAmount() {
        clear(key("InvestmentAmountInput_Object"));
    }

    public void selectTenureYear() {
        click(key("TenureYearDropdown_Object"));
    }

    public void selectTenureMonth() {
        click(key("TenureMonthDropdown_Object"));
    }

    public void selectTenureDay() {
        click(key("TenureDayDropdown_Object"));
    }

    public void clickViewInterestRates() {
        click(key("ViewInterestRatesLink_Object"));
    }

    public void clickOpenFDButton() {
        click(key("OpenFDButton_Object"));
    }

    public void enterSearchDeposits(String searchText) {
        type(key("SearchDepositsInput_Object"), searchText);
    }

    // ========== Deposit Form Actions ==========

    public void selectTransferFromAccount() {
        click(key("TransferFromDropdown_Object"));
    }

    public void enterDepositAmount(String amount) {
        type(key("DepositAmountInput_Object"), amount);
    }

    public void clearDepositAmount() {
        clear(key("DepositAmountInput_Object"));
    }

    public void selectInterestPaymentInstructions() {
        click(key("InterestPaymentInstructionsDropdown_Object"));
    }

    public void selectMaturityInstructions() {
        click(key("MaturityInstructionsDropdown_Object"));
    }

    public void clickDurationDropdown() {
        click(key("DurationInput_Object"));
    }

    public void toggleAddNominee() {
        click(key("AddNomineeSwitch_Object"));
    }

    // ========== Recurring Deposit Actions ==========

    public void enterRecurringDepositAmount(String amount) {
        type(key("RecurringDepositAmountInput_Object"), amount);
    }

    public void clearRecurringDepositAmount() {
        clear(key("RecurringDepositAmountInput_Object"));
    }

    public void selectPaymentType() {
        click(key("PaymentTypeDropdown_Object"));
    }

    public void selectRecurringDuration() {
        click(key("RecurringDurationDropdown_Object"));
    }

    // ========== Common Actions ==========

    public void clickCancel() {
        click(key("CancelButton_Object"));
    }

    public void clickContinue() {
        click(key("ContinueButton_Object"));
    }

    // ========== Verification Methods ==========

    public boolean isOpenFixedDepositsPageDisplayed() {
        return isDisplayed(key("OpenFixedDepositsPageTitle_Object"));
    }

    public boolean isFixedDepositCardDisplayed() {
        return isDisplayed(key("FixedDepositCard_Object"));
    }

    public boolean isRecurringFlexiCardDisplayed() {
        return isDisplayed(key("RecurringFlexiCard_Object"));
    }

    public boolean isTaxSavingFDCardDisplayed() {
        return isDisplayed(key("TaxSavingFDCard_Object"));
    }

    public boolean isFDCalculatorDisplayed() {
        return isDisplayed(key("FDCalculatorTitle_Object"));
    }

    public boolean isRecommendedDepositsDisplayed() {
        return isDisplayed(key("RecommendedDepositsTitle_Object"));
    }

    public boolean isOpenFixedDepositPageDisplayed() {
        return isDisplayed(key("OpenFixedDepositPageTitle_Object"));
    }

    public boolean isOpenRecurringFlexiPageDisplayed() {
        return isDisplayed(key("OpenRecurringFlexiPageTitle_Object"));
    }

    public boolean isOpenTaxSavingPageDisplayed() {
        return isDisplayed(key("OpenTaxSavingPageTitle_Object"));
    }

    public boolean isDepositPreferencesDisplayed() {
        return isDisplayed(key("DepositPreferencesTitle_Object"));
    }

    public boolean isNomineeDetailsDisplayed() {
        return isDisplayed(key("NomineeDetailsTitle_Object"));
    }

    public boolean isSeniorCitizenNoticeDisplayed() {
        return isDisplayed(key("SeniorCitizenNotice_Object"));
    }

    public boolean isCancelButtonDisplayed() {
        return isDisplayed(key("CancelButton_Object"));
    }

    public boolean isContinueButtonDisplayed() {
        return isDisplayed(key("ContinueButton_Object"));
    }

    public String getDepositAmountValue() {
        return getText(key("DepositAmountValue_Object"));
    }

    public String getInterestAmountValue() {
        return getText(key("InterestAmountValue_Object"));
    }

    public String getMonthlyAmountValue() {
        return getText(key("MonthlyAmountValue_Object"));
    }

    public String getAvailableBalanceText() {
        return getText(key("AvailableBalanceText_Object"));
    }

    public String getTaxSavingDurationValue() {
        return getText(key("TaxSavingDurationValue_Object"));
    }

    public String getRateOfInterestValue() {
        return getAttribute(key("RateOfInterestInput_Object"), "value");
    }
}
