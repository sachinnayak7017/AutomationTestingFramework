package org.example.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.example.config.ConfigLoader;
import org.example.core.driver.DriverManager;
import org.example.pages.PreLoginPage;
import org.example.pages.DashboardPage;
import org.example.utils.excel.ExcelReader;
import org.testng.Assert;

import java.util.Map;

/**
 * PreLoginSteps - Step definitions for PreLogin Module (PL_001 to PL_053)
 * Only Excel-driven and PreLogin-specific steps remain here.
 * All generic steps (click, display, clear, wait, URL, etc.) are in CommonSteps.java.
 */
public class PreLoginSteps {

    private PreLoginPage preLoginPage;
    private DashboardPage dashboardPage;
    private Map<String, String> testData;

    public PreLoginSteps() {
        preLoginPage = new PreLoginPage();
        dashboardPage = new DashboardPage();
        loadTestData();
    }

    private void loadTestData() {
        try {
            String excelPath = ConfigLoader.getInstance().getSuiteExcelPath();
            ExcelReader reader = new ExcelReader(excelPath);
            testData = reader.getTestData();
        } catch (Exception e) {
            testData = new java.util.HashMap<>();
        }
    }

    private String getData(String key) {
        return testData.getOrDefault(key, "");
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    // ==================== BACKGROUND ====================

    @Given("user navigates to PreLogin URL from Excel key {string}")
    public void userNavigatesToPreLoginUrl(String key) {
        String url = getData(key);
        if (url.isEmpty()) {
            url = ConfigLoader.getInstance().getBaseUrl();
        }
        DriverManager.navigateTo(url);
        preLoginPage.waitForPreLoginPageLoad();
    }

    // ==================== EXCEL-DRIVEN INPUT ====================

    @When("user enters User ID from Excel key {string}")
    public void userEntersUserIdFromExcelKey(String key) {
        String userId = getData(key);
        preLoginPage.enterUserId(userId);
    }

    @When("user enters Password from Excel key {string}")
    public void userEntersPasswordFromExcelKey(String key) {
        String password = getData(key);
        preLoginPage.enterPassword(password);
    }

    @When("user enters Captcha from Excel key {string}")
    public void userEntersCaptchaFromExcelKey(String key) {
        String captcha = getData(key);
        preLoginPage.enterCaptcha(captcha);
    }

    // ==================== PASSWORD VERIFICATION ====================

    @Then("Password field should be masked")
    public void passwordFieldShouldBeMasked() {
        Assert.assertTrue(preLoginPage.isPasswordMasked(),
                "Password field is not masked");
    }

    @Then("Password should be visible")
    public void passwordShouldBeVisible() {
        Assert.assertTrue(preLoginPage.isPasswordVisible(),
                "Password is not visible");
    }

    // ==================== LOGIN SUCCESS ====================

    @Then("user should login successfully")
    public void userShouldLoginSuccessfully() {
        sleep(5000);
        boolean isDashboardDisplayed = dashboardPage.isQuickServicesDisplayed()
                || dashboardPage.isAccountsDisplayed()
                || dashboardPage.isLogoDisplayed();
        Assert.assertTrue(isDashboardDisplayed,
                "User login was not successful - Dashboard page is not displayed");
    }
}
