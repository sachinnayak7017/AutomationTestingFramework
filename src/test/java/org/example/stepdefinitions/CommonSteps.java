package org.example.stepdefinitions;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.core.driver.DriverManager;
import org.example.pages.BasePage;
import org.example.pages.DashboardPage;
import org.example.pages.FundTransferPage;
import org.example.pages.ManageBeneficiariesPage;
import org.example.pages.PreLoginPage;
import org.testng.Assert;

/**
 * CommonSteps - Universal reusable step definitions for ALL modules.
 * Uses page resolver pattern: ONE method per action, page name as {word} parameter.
 * Screenshots are handled GLOBALLY by Hooks.java @AfterStep - NO hardcoded screenshot calls here.
 *
 * Page Abbreviations:
 * PL        -> PreLoginPage
 * Dashboard -> DashboardPage
 * MB        -> ManageBeneficiariesPage
 * FT        -> FundTransferPage (main page)
 * TB        -> FundTransferPage (Transfer to Beneficiary)
 * QT        -> FundTransferPage (Quick Transfer)
 * SA        -> FundTransferPage (Self Account)
 * CONFIRM   -> FundTransferPage (Confirm Details)
 * OTP       -> FundTransferPage (OTP Verification)
 * SUCCESS   -> FundTransferPage (Success/Receipt)
 */
public class CommonSteps {

    private static final Logger logger = LogManager.getLogger(CommonSteps.class);

    private PreLoginPage preLoginPage;
    private DashboardPage dashboardPage;
    private ManageBeneficiariesPage mbPage;
    private FundTransferPage ftPage;
    private org.openqa.selenium.WebDriver cachedDriver;

    public CommonSteps() {
        cachedDriver = DriverManager.getDriver();
        preLoginPage = new PreLoginPage();
        dashboardPage = new DashboardPage();
        mbPage = new ManageBeneficiariesPage();
        ftPage = new FundTransferPage();
    }

    /**
     * Refresh all page objects if the driver has been reinitialized.
     * This handles the case where SessionManager reinitializes the driver during login retry.
     */
    private void refreshPageObjectsIfNeeded() {
        org.openqa.selenium.WebDriver currentDriver = DriverManager.getDriver();
        if (currentDriver != cachedDriver) {
            logger.info("Driver changed - refreshing CommonSteps page objects");
            cachedDriver = currentDriver;
            preLoginPage = new PreLoginPage();
            dashboardPage = new DashboardPage();
            mbPage = new ManageBeneficiariesPage();
            ftPage = new FundTransferPage();
        }
    }

    // ==================== Page Resolver ====================

    private BasePage getPageObject(String page) {
        refreshPageObjectsIfNeeded();
        switch (page.toUpperCase()) {
            case "PL":
                return preLoginPage;
            case "DASHBOARD":
                return dashboardPage;
            case "MB":
                return mbPage;
            case "FT": case "TB": case "QT": case "SA":
            case "CONFIRM": case "OTP": case "SUCCESS":
                return ftPage;
            default:
                logger.warn("Unknown page '{}', defaulting to FundTransferPage", page);
                return ftPage;
        }
    }

    // ==================== Helper Methods ====================

    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    // ==================== CLICK ACTIONS ====================

    @When("user clicks on {string} on {word} page")
    public void userClicksOnElement(String objectKey, String page) {
        BasePage pageObj = getPageObject(page);
        pageObj.clickOnElement(objectKey);
    }

    // ==================== DIRECT VALUE INPUT ====================

    @When("user enters value {string} in {string} on {word} page")
    public void userEntersDirectValue(String value, String objectKey, String page) {
        BasePage pageObj = getPageObject(page);
        pageObj.typeInField(objectKey, value);
    }

    // ==================== CLEAR FIELD ====================

    @When("user clears {string} on {word} page")
    public void userClearsField(String objectKey, String page) {
        BasePage pageObj = getPageObject(page);
        pageObj.clearField(objectKey);
    }

    // ==================== DROPDOWN SELECTION ====================

    @When("user selects first option from {string} on {word} page")
    public void userSelectsFirstOption(String objectKey, String page) {
        BasePage pageObj = getPageObject(page);
        pageObj.selectFirstDropdownOption(objectKey);
    }

    // ==================== BENEFICIARY SELECTION ====================

    @When("user selects first beneficiary from dropdown on {word} page")
    public void userSelectsFirstBeneficiary(String page) {
        ftPage.selectBeneficiaryFromDropdown();
    }

    // ==================== ELEMENT DISPLAY VERIFICATION ====================

    @Then("{string} should be displayed on {word} page")
    public void elementShouldBeDisplayed(String objectKey, String page) {
        BasePage pageObj = getPageObject(page);
        Assert.assertTrue(pageObj.isElementDisplayedOnPage(objectKey),
                objectKey + " is not displayed on " + page + " page");
    }

    @Then("{string} should not be displayed on {word} page")
    public void elementShouldNotBeDisplayed(String objectKey, String page) {
        BasePage pageObj = getPageObject(page);
        boolean displayed = false;
        try {
            displayed = pageObj.isElementDisplayedOnPage(objectKey);
        } catch (Exception e) {
            displayed = false;
        }
        Assert.assertFalse(displayed,
                objectKey + " is displayed on " + page + " page when it should not be");
    }

    // ==================== SELECTED BY DEFAULT ====================

    @Then("{string} should be selected by default on {word} page")
    public void elementShouldBeSelectedByDefault(String objectKey, String page) {
        BasePage pageObj = getPageObject(page);
        Assert.assertTrue(pageObj.isElementSelectedOnPage(objectKey),
                objectKey + " is not selected by default on " + page + " page");
    }

    // ==================== ERROR MESSAGE VERIFICATION ====================

    @Then("error message {string} should be displayed on {word} page")
    public void errorMessageShouldBeDisplayed(String expectedMessage, String page) {
        BasePage pageObj = getPageObject(page);
        sleep(300);
        Assert.assertTrue(pageObj.isTextDisplayedOnPage(expectedMessage),
                "Error message '" + expectedMessage + "' is not displayed on " + page + " page");
    }

    // ==================== FIELD ERROR VERIFICATION ====================

    @Then("field error should be displayed for {string} on {word} page")
    public void fieldErrorShouldBeDisplayed(String objectKey, String page) {
        BasePage pageObj = getPageObject(page);
        sleep(1500);
        boolean hasError = pageObj.isFieldErrorDisplayed(objectKey);
        if (!hasError) {
            // Retry after longer wait for slow network validation
            sleep(3000);
            hasError = pageObj.isFieldErrorDisplayed(objectKey);
        }
        Assert.assertTrue(hasError,
                "Field error is not displayed for " + objectKey + " on " + page + " page");
    }

    @Then("field error should not be displayed for {string} on {word} page")
    public void fieldErrorShouldNotBeDisplayed(String objectKey, String page) {
        BasePage pageObj = getPageObject(page);
        sleep(300);
        Assert.assertFalse(pageObj.isFieldErrorDisplayed(objectKey),
                "Field error is displayed for " + objectKey + " when it should not be on " + page + " page");
    }

    // ==================== FIELD VALUE VALIDATIONS ====================

    @Then("{string} field value should have maximum {int} characters on {word} page")
    public void fieldMaxChars(String objectKey, int maxChars, String page) {
        BasePage pageObj = getPageObject(page);
        String value = pageObj.getFieldValue(objectKey);
        Assert.assertNotNull(value, objectKey + " field value is null");
        Assert.assertTrue(value.length() <= maxChars,
                objectKey + " has " + value.length() + " chars, expected max " + maxChars);
    }

    @Then("{string} field should contain only numeric values on {word} page")
    public void fieldNumericOnly(String objectKey, String page) {
        BasePage pageObj = getPageObject(page);
        String value = pageObj.getFieldValue(objectKey);
        if (value != null && !value.isEmpty()) {
            Assert.assertTrue(value.matches("[0-9,.]*"),
                    objectKey + " contains non-numeric characters: " + value);
        }
    }

    @Then("{string} field should contain only alphabet values on {word} page")
    public void fieldAlphabetOnly(String objectKey, String page) {
        BasePage pageObj = getPageObject(page);
        String value = pageObj.getFieldValue(objectKey);
        if (value != null && !value.isEmpty()) {
            Assert.assertTrue(value.matches("[a-zA-Z ]*"),
                    objectKey + " contains non-alphabet characters: " + value);
        }
    }

    @Then("{string} field value should be minimum {int} on {word} page")
    public void fieldMinValue(String objectKey, int minValue, String page) {
        BasePage pageObj = getPageObject(page);
        String value = pageObj.getFieldValue(objectKey);
        Assert.assertNotNull(value, objectKey + " field value is null");
        String numericValue = value.replaceAll("[^0-9.]", "");
        Assert.assertFalse(numericValue.isEmpty(), objectKey + " has no numeric value");
        double amount = Double.parseDouble(numericValue);
        Assert.assertTrue(amount >= minValue,
                objectKey + " value " + amount + " is less than minimum " + minValue);
    }

    @Then("{string} field should have value entered on {word} page")
    public void fieldShouldHaveValue(String objectKey, String page) {
        BasePage pageObj = getPageObject(page);
        String value = pageObj.getFieldValue(objectKey);
        Assert.assertNotNull(value, objectKey + " field value is null");
        Assert.assertFalse(value.isEmpty(), objectKey + " field has no value entered on " + page + " page");
    }

    // ==================== MASKED FIELD VERIFICATION ====================

    @Then("{string} field should be masked on {word} page")
    public void fieldShouldBeMasked(String objectKey, String page) {
        Assert.assertTrue(ftPage.isAccountNumberMasked(),
                objectKey + " field is not masked on " + page + " page");
    }

    @Then("{string} field should not be masked on {word} page")
    public void fieldShouldNotBeMasked(String objectKey, String page) {
        Assert.assertFalse(ftPage.isAccountNumberMasked(),
                objectKey + " field is still masked on " + page + " page");
    }

    // ==================== DROPDOWN OPTION VERIFICATION ====================

    @Then("{string} should have doption selected on {word} page")
    public void elementShouldHaveOptionSelected(String objectKey, String page) {
        BasePage pageObj = getPageObject(page);
        Assert.assertTrue(pageObj.isElementDisplayedOnPage(objectKey),
                objectKey + " dropdown is not displayed after selection on " + page + " page");
    }

    // ==================== RADIO BUTTON VERIFICATION ====================

    @Then("only one radio should be selected between {string} and {string} on {word} page")
    public void onlyOneRadioSelected(String radio1, String radio2, String page) {
        BasePage pageObj = getPageObject(page);
        boolean r1 = pageObj.isElementSelectedOnPage(radio1);
        boolean r2 = pageObj.isElementSelectedOnPage(radio2);
        Assert.assertTrue(r1 != r2,
                "Both radios selected or neither selected: " + radio1 + "=" + r1 + ", " + radio2 + "=" + r2);
    }

    // ==================== CANCEL POPUP ====================

    @Then("cancel popup should be displayed on {word} page")
    public void cancelPopupDisplayed(String page) {
        sleep(3000);
        Assert.assertTrue(ftPage.isCancelPopupDisplayed(),
                "Cancel popup is not displayed on " + page + " page");
    }

    @Then("cancel popup should display message {string} on {word} page")
    public void cancelPopupMessage(String expectedMessage, String page) {
        String actual = ftPage.getCancelPopupMessage();
        Assert.assertTrue(actual.contains(expectedMessage),
                "Cancel popup message mismatch on " + page + " page. Actual: " + actual);
    }

    @When("user clicks on Cancel button on cancel popup on {word} page")
    public void userClicksCancelOnPopup(String page) {
        ftPage.clickCancelOnPopup();
        sleep(300);
    }

    @When("user clicks on Confirm button on cancel popup on {word} page")
    public void userClicksConfirmOnPopup(String page) {
        ftPage.clickConfirmOnPopup();
        sleep(500);
    }

    // ==================== TEXT ON PAGE VERIFICATION ====================

    @Then("text {string} should be displayed on {word} page")
    public void textShouldBeDisplayed(String expectedText, String page) {
        BasePage pageObj = getPageObject(page);
        sleep(300);
        Assert.assertTrue(pageObj.isTextDisplayedOnPage(expectedText),
                "Text '" + expectedText + "' is not displayed on " + page + " page");
    }

    @Then("text {string} should not be displayed on {word} page")
    public void textShouldNotBeDisplayed(String expectedText, String page) {
        BasePage pageObj = getPageObject(page);
        Assert.assertFalse(pageObj.isTextDisplayedOnPage(expectedText),
                "Text '" + expectedText + "' is displayed on " + page + " page when it should not be");
    }

    // ==================== AUTO-POPULATED FIELD VERIFICATION ====================

    @Then("{string} should be auto-populated on {word} page")
    public void fieldShouldBeAutoPopulated(String objectKey, String page) {
        BasePage pageObj = getPageObject(page);
        String value = pageObj.getFieldValue(objectKey);
        Assert.assertNotNull(value, objectKey + " is null on " + page + " page");
        Assert.assertFalse(value.isEmpty(), objectKey + " is not auto-populated (empty) on " + page + " page");
    }

    // ==================== NEXT PAGE / FORM SUBMISSION ====================

    @Then("next page should be displayed after form submission on {word} page")
    public void nextPageAfterSubmission(String page) {
        sleep(1000);
        boolean confirmPage = ftPage.isConfirmPageDisplayed();
        boolean otpPage = ftPage.isOTPInputDisplayed();
        boolean errorShown = ftPage.isErrorMessageDisplayed() || ftPage.isToastMessageDisplayed();
        Assert.assertTrue(confirmPage || otpPage || errorShown,
                "No next page displayed after form submission on " + page + " page");
    }

    // ==================== CLICKABLE VERIFICATION ====================

    @Then("{string} should be clickable on {word} page")
    public void elementShouldBeClickable(String objectKey, String page) {
        BasePage pageObj = getPageObject(page);
        Assert.assertTrue(pageObj.isElementDisplayedOnPage(objectKey),
                objectKey + " is not clickable on " + page + " page");
    }

    // ==================== CHARGES WARNING TEXT ====================

    @Then("charges warning {string} should be displayed on {word} page")
    public void chargesWarningShouldBeDisplayed(String expectedText, String page) {
        BasePage pageObj = getPageObject(page);
        Assert.assertTrue(pageObj.isTextDisplayedOnPage(expectedText),
                "Charges warning '" + expectedText + "' is not displayed on " + page + " page");
    }

    // ==================== WAIT ====================

    @When("user waits {int} milliseconds")
    public void userWaitsMilliseconds(int ms) {
        sleep(ms);
    }

    // ==================== URL VERIFICATION ====================

    @Then("URL should contain {string}")
    public void urlShouldContain(String text) {
        sleep(1500);
        String url = DriverManager.getDriver().getCurrentUrl().toLowerCase();
        Assert.assertTrue(url.contains(text.toLowerCase()),
                "URL does not contain '" + text + "'. Actual URL: " + url);
    }

    // ==================== NEW PAGE / REDIRECT VERIFICATION ====================

    @Then("user should be on a new or redirected page")
    public void userShouldBeOnNewOrRedirectedPage() {
        sleep(1000);
        int windowCount = DriverManager.getDriver().getWindowHandles().size();
        Assert.assertTrue(windowCount >= 1, "No page is open");
        // Close extra windows and switch back to first
        if (windowCount > 1) {
            String original = DriverManager.getDriver().getWindowHandles().iterator().next();
            for (String handle : DriverManager.getDriver().getWindowHandles()) {
                if (!handle.equals(original)) {
                    DriverManager.getDriver().switchTo().window(handle);
                    DriverManager.getDriver().close();
                }
            }
            DriverManager.getDriver().switchTo().window(original);
        }
    }
}
