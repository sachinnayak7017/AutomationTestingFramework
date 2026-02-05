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
      tags = "@prelogin and not @manual and not @skip"
     //   tags = "@PL_053"
)
public class CucumberRunner extends AbstractTestNGCucumberTests {

}
