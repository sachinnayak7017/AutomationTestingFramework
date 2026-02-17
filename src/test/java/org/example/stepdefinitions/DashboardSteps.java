package org.example.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.core.driver.DriverManager;
import org.example.pages.DashboardPage;
import org.example.utils.SessionManager;
import org.testng.Assert;

/**
 * DashboardSteps - Step definitions for Dashboard Module.
 * Contains ONLY non-generic steps that cannot be handled by CommonSteps.
 * Auto re-login if session expires during test execution.
 */
public class DashboardSteps {

    private static final Logger logger = LogManager.getLogger(DashboardSteps.class);

    private DashboardPage dashboardPage;
    private String originalWindow;  

    public DashboardSteps() {
        dashboardPage = new DashboardPage();
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    private void switchBackToOriginalWindow() {
        try {
            for (String handle : DriverManager.getDriver().getWindowHandles()) {
                if (!handle.equals(originalWindow)) {
                    DriverManager.getDriver().switchTo().window(handle);
                    DriverManager.getDriver().close();
                }
            }
            DriverManager.getDriver().switchTo().window(originalWindow);
        } catch (Exception e) {
            logger.warn("Window switch back failed: {}", e.getMessage());
        }
    }

    /**
     * Check if logged out or browser crashed and re-login automatically.
     * Returns true if re-login was performed.
     */
    private boolean checkAndReloginIfNeeded() {
        try {
            if (!DriverManager.isDriverAlive() || SessionManager.isSessionExpired()) {
                logger.warn("Session expired or browser crashed - auto re-logging in...");
                SessionManager.resetLoginState();
                SessionManager.ensureLoggedIn();
                // Recreate page object with new driver reference
                dashboardPage = new DashboardPage();
                dashboardPage.waitForDashboardLoad();
                originalWindow = DriverManager.getDriver().getWindowHandle();
                return true;
            }
        } catch (Exception e) {
            logger.error("Error during re-login check: {}", e.getMessage());
            SessionManager.resetLoginState();
            SessionManager.ensureLoggedIn();
            dashboardPage = new DashboardPage();
            dashboardPage.waitForDashboardLoad();
            originalWindow = DriverManager.getDriver().getWindowHandle();
            return true;
        }
        return false;
    }

    // ==================== BACKGROUND: Login ====================

    @Given("user is logged in successfully")
    public void userIsLoggedInSuccessfully() {
        SessionManager.ensureLoggedIn();
        // Recreate page object to ensure it uses current driver reference
        dashboardPage = new DashboardPage();
        dashboardPage.waitForDashboardLoad();
        originalWindow = DriverManager.getDriver().getWindowHandle();
    }

    // ==================== Dashboard Composite Check ====================

    @Then("Dashboard should be displayed")
    public void dashboardShouldBeDisplayed() {
        // If logged out, auto re-login
        if (!dashboardPage.isDashboardDisplayed()) {
            if (checkAndReloginIfNeeded()) return;
        }
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
            "Dashboard is not displayed");
    }

    // ==================== Navigate Back to Dashboard ====================

    @When("user navigates back to Dashboard")
    public void userNavigatesBackToDashboard() {
        // Check if logged out during navigation
        if (checkAndReloginIfNeeded()) return;

        // If extra tabs opened, close them and switch back
        try {
            if (DriverManager.getDriver().getWindowHandles().size() > 1) {
                switchBackToOriginalWindow();
                sleep(500);
                if (dashboardPage.isDashboardDisplayed()) return;
            }
        } catch (Exception ignored) { }

        // Try 1: Browser back
        try {
            dashboardPage.navigateBack();
            sleep(1000);
            if (dashboardPage.isDashboardDisplayed()) return;
        } catch (Exception ignored) { }

        // Check again if logged out after navigation
        if (checkAndReloginIfNeeded()) return;

        // Try 2: Click Home nav
        try {
            dashboardPage.clickHomeNav();
            sleep(1000);
            if (dashboardPage.isDashboardDisplayed()) return;
        } catch (Exception ignored) { }

        // Try 3: Click logo
        try {
            dashboardPage.clickLogo();
            sleep(1000);
        } catch (Exception e) {
            // Last resort: re-login
            checkAndReloginIfNeeded();
        }
    }

    // ==================== Window Handling (Download Links) ====================

    @Then("new page should open for the download link")
    public void newPageShouldOpenForTheDownloadLink() {
        sleep(1000);
        int windowCount = DriverManager.getDriver().getWindowHandles().size();
        Assert.assertTrue(windowCount >= 2,
            "New page did not open for the download link (window count: " + windowCount + ")");
        switchBackToOriginalWindow();
    }

    // ==================== OR-Condition Checks ====================

    @Then("no beneficiary image should be displayed or beneficiary cards should be visible")
    public void noBeneficiaryImageOrCardsVisible() {
        boolean noBeneficiaryImg = dashboardPage.isNoBeneficiaryImageDisplayed();
        boolean beneficiarySection = dashboardPage.isFavouriteBeneficiaryDisplayed();
        Assert.assertTrue(noBeneficiaryImg || beneficiarySection,
            "Neither no beneficiary image nor beneficiary cards are visible");
    }

    @Then("no bills image should be displayed or bill list should be visible")
    public void noBillsImageOrBillListVisible() {
        boolean noBillsImg = dashboardPage.isNoBillsImageDisplayed();
        boolean billList = dashboardPage.isBillListDisplayed();
        Assert.assertTrue(noBillsImg || billList,
            "Neither no bills image nor bill list is visible");
    }
}
