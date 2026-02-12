package org.example.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.example.core.listeners.TestNGListener;
import org.testng.annotations.Listeners;

/**
 * CucumberRunner - Main Runner Class for BDD Tests
 *
 * HOW TO RUN:
 * 1. Right-click this file -> Run 'CucumberRunner'
 * 2. Or use Maven: mvn test
 * 3. Or use tags: mvn test -Dcucumber.filter.tags="@smoke"
 */
@Listeners(TestNGListener.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"org.example.stepdefinitions"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json"
        },
        monochrome = true,
        // Run ALL modules (Dashboard → FundTransfer → ManageBeneficiary):
        //tags = "(@dashboard or @fundtransfer or @managebeneficiary) and not @manual and not @skip"
        // Single module runs (uncomment one at a time):
        // tags = "@dashboard and not @manual and not @skip"
         tags = "@fundtransfer and not @manual and not @skip"
        // tags = "@managebeneficiary and not @manual and not @skip"
        // tags = "@prelogin and not @manual and not @skip"
        // Sub-module tags:
        // tags = "@fundtransfer_mainpage and not @manual and not @skip"
        // tags = "@transfer_to_beneficiary and not @manual and not @skip"
        // tags = "@quick_transfer and not @manual and not @skip"
        // tags = "@self_account and not @manual and not @skip"
        
)
public class CucumberRunner extends AbstractTestNGCucumberTests {

}
