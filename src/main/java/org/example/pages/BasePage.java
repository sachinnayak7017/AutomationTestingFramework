package org.example.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.ConfigLoader;
import org.example.core.driver.DriverManager;
import org.example.utils.json.ObjectRepositoryManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * BasePage - Base class for all Page Object Model classes.
 * Provides common methods for interacting with web elements.
 */
public abstract class BasePage {

    protected final Logger logger;
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final ObjectRepositoryManager orManager;

    /**
     * Constructor - initializes driver and wait
     */
    public BasePage() {
        this.logger = LogManager.getLogger(this.getClass());
        this.driver = DriverManager.getDriver();
        int explicitWait = ConfigLoader.getInstance().getExplicitWait();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
        this.orManager = ObjectRepositoryManager.getInstance();
    }

    // ==================== Element Finder Methods ====================

    /**
     * Find element using OR key
     * @param elementKey OR element key
     * @return WebElement
     */
    protected WebElement findElement(String elementKey) {
        By locator = orManager.getLocator(elementKey);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Find element using By locator
     * @param locator By locator
     * @return WebElement
     */
    protected WebElement findElement(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Find clickable element
     * @param elementKey OR element key
     * @return WebElement
     */
    protected WebElement findClickableElement(String elementKey) {
        By locator = orManager.getLocator(elementKey);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Find visible element
     * @param elementKey OR element key
     * @return WebElement
     */
    protected WebElement findVisibleElement(String elementKey) {
        By locator = orManager.getLocator(elementKey);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Find multiple elements
     * @param elementKey OR element key
     * @return List of WebElements
     */
    protected List<WebElement> findElements(String elementKey) {
        By locator = orManager.getLocator(elementKey);
        return driver.findElements(locator);
    }

    // ==================== Click Methods ====================

    /**
     * Click on element
     * @param elementKey OR element key
     */
    protected void click(String elementKey) {
        findClickableElement(elementKey).click();
        logger.debug("Clicked on element: {}", elementKey);
    }

    /**
     * Click using JavaScript
     * @param elementKey OR element key
     */
    protected void jsClick(String elementKey) {
        WebElement element = findElement(elementKey);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        logger.debug("JS clicked on element: {}", elementKey);
    }

    // ==================== Input Methods ====================

    /**
     * Type text into element
     * @param elementKey OR element key
     * @param text Text to type
     */
    protected void type(String elementKey, String text) {
        WebElement element = findVisibleElement(elementKey);
        element.clear();
        element.sendKeys(text);
        logger.debug("Typed '{}' into element: {}", text, elementKey);
    }

    /**
     * Clear element
     * @param elementKey OR element key
     */
    protected void clear(String elementKey) {
        findVisibleElement(elementKey).clear();
        logger.debug("Cleared element: {}", elementKey);
    }

    /**
     * Send keys to element
     * @param elementKey OR element key
     * @param keys Keys to send
     */
    protected void sendKeys(String elementKey, Keys keys) {
        findVisibleElement(elementKey).sendKeys(keys);
    }

    // ==================== Dropdown Methods ====================

    /**
     * Select dropdown by visible text
     * @param elementKey OR element key
     * @param text Text to select
     */
    protected void selectByText(String elementKey, String text) {
        WebElement element = findVisibleElement(elementKey);
        new Select(element).selectByVisibleText(text);
        logger.debug("Selected '{}' from dropdown: {}", text, elementKey);
    }

    /**
     * Select dropdown by value
     * @param elementKey OR element key
     * @param value Value to select
     */
    protected void selectByValue(String elementKey, String value) {
        WebElement element = findVisibleElement(elementKey);
        new Select(element).selectByValue(value);
    }

    /**
     * Select dropdown by index
     * @param elementKey OR element key
     * @param index Index to select
     */
    protected void selectByIndex(String elementKey, int index) {
        WebElement element = findVisibleElement(elementKey);
        new Select(element).selectByIndex(index);
    }

    // ==================== Get Methods ====================

    /**
     * Get text from element
     * @param elementKey OR element key
     * @return Element text
     */
    protected String getText(String elementKey) {
        return findVisibleElement(elementKey).getText();
    }

    /**
     * Get attribute value
     * @param elementKey OR element key
     * @param attributeName Attribute name
     * @return Attribute value
     */
    protected String getAttribute(String elementKey, String attributeName) {
        return findElement(elementKey).getAttribute(attributeName);
    }

    /**
     * Get page title
     * @return Page title
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Get current URL
     * @return Current URL
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    // ==================== Verification Methods ====================

    /**
     * Check if element is displayed
     * @param elementKey OR element key
     * @return true if displayed
     */
    protected boolean isDisplayed(String elementKey) {
        try {
            return findElement(elementKey).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if element is enabled
     * @param elementKey OR element key
     * @return true if enabled
     */
    protected boolean isEnabled(String elementKey) {
        try {
            return findElement(elementKey).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if element is selected
     * @param elementKey OR element key
     * @return true if selected
     */
    protected boolean isSelected(String elementKey) {
        try {
            return findElement(elementKey).isSelected();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if element is present in DOM
     * @param elementKey OR element key
     * @return true if present
     */
    protected boolean isElementPresent(String elementKey) {
        try {
            By locator = orManager.getLocator(elementKey);
            return !driver.findElements(locator).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== Wait Methods ====================

    /**
     * Wait for element to be visible
     * @param elementKey OR element key
     * @param timeoutSeconds Timeout in seconds
     */
    protected void waitForElementVisible(String elementKey, int timeoutSeconds) {
        By locator = orManager.getLocator(elementKey);
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait for element to be clickable
     * @param elementKey OR element key
     * @param timeoutSeconds Timeout in seconds
     */
    protected void waitForElementClickable(String elementKey, int timeoutSeconds) {
        By locator = orManager.getLocator(elementKey);
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait for element to be invisible
     * @param elementKey OR element key
     * @param timeoutSeconds Timeout in seconds
     */
    protected void waitForElementInvisible(String elementKey, int timeoutSeconds) {
        By locator = orManager.getLocator(elementKey);
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Wait for page load complete with timeout handling
     */
    public void waitForPageLoad() {
        try {
            WebDriverWait pageLoadWait = new WebDriverWait(driver, Duration.ofSeconds(30));
            pageLoadWait.until(webDriver -> {
                try {
                    return ((JavascriptExecutor) webDriver)
                            .executeScript("return document.readyState").equals("complete");
                } catch (Exception e) {
                    return true; // Continue if script execution fails
                }
            });
        } catch (Exception e) {
            // Log and continue - page may already be loaded or partially loaded
            logger.warn("Page load wait timed out, continuing...");
        }
    }

    // ==================== Scroll Methods ====================

    /**
     * Scroll to element
     * @param elementKey OR element key
     */
    protected void scrollToElement(String elementKey) {
        WebElement element = findElement(elementKey);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Scroll to top of page
     */
    protected void scrollToTop() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
    }

    /**
     * Scroll to bottom of page
     */
    protected void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    // ==================== JavaScript Methods ====================

    /**
     * Execute JavaScript
     * @param script JavaScript to execute
     * @param args Arguments
     * @return Result of execution
     */
    protected Object executeJavaScript(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    /**
     * Set value using JavaScript
     * @param elementKey OR element key
     * @param value Value to set
     */
    protected void setValueByJS(String elementKey, String value) {
        WebElement element = findElement(elementKey);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + value + "'", element);
    }

    // ==================== Alert Methods ====================

    /**
     * Accept alert
     */
    protected void acceptAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
    }

    /**
     * Dismiss alert
     */
    protected void dismissAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().dismiss();
    }

    /**
     * Get alert text
     * @return Alert text
     */
    protected String getAlertText() {
        wait.until(ExpectedConditions.alertIsPresent());
        return driver.switchTo().alert().getText();
    }

    // ==================== Frame Methods ====================

    /**
     * Switch to frame by element key
     * @param elementKey OR element key
     */
    protected void switchToFrame(String elementKey) {
        WebElement frame = findElement(elementKey);
        driver.switchTo().frame(frame);
    }

    /**
     * Switch to frame by index
     * @param index Frame index
     */
    protected void switchToFrameByIndex(int index) {
        driver.switchTo().frame(index);
    }

    /**
     * Switch to default content
     */
    protected void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    // ==================== Window Methods ====================

    /**
     * Switch to new window
     */
    public void switchToNewWindow() {
        String currentHandle = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(currentHandle)) {
                driver.switchTo().window(handle);
                break;
            }
        }
    }

    /**
     * Close current window and switch back
     */
    protected void closeCurrentWindow() {
        String currentHandle = driver.getWindowHandle();
        driver.close();
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(currentHandle)) {
                driver.switchTo().window(handle);
                break;
            }
        }
    }

    // ==================== Highlight Method (for debugging) ====================

    /**
     * Highlight element
     * @param elementKey OR element key
     */
    protected void highlightElement(String elementKey) {
        WebElement element = findElement(elementKey);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].style.border='3px solid red'", element);
    }
}
