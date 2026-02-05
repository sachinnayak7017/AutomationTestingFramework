package org.example.core.keywords;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.ConfigLoader;
import org.example.core.driver.DriverManager;
import org.example.utils.json.ObjectRepositoryManager;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * WebActions - Contains all reusable web automation actions.
 * These methods can be used directly or through the KeywordEngine.
 */
public class WebActions {

    private static final Logger logger = LogManager.getLogger(WebActions.class);
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final ObjectRepositoryManager orManager;
    private final Actions actions;

    public WebActions() {
        this.driver = DriverManager.getDriver();
        int explicitWait = ConfigLoader.getInstance().getExplicitWait();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
        this.orManager = ObjectRepositoryManager.getInstance();
        this.actions = new Actions(driver);
    }

    // ==================== Navigation Actions ====================

    /**
     * Navigate to URL
     * @param url URL to navigate to
     */
    public void navigateTo(String url) {
        driver.get(url);
        logger.info("Navigated to: {}", url);
    }

    /**
     * Navigate to base URL from config
     */
    public void navigateToBaseUrl() {
        String baseUrl = ConfigLoader.getInstance().getBaseUrl();
        navigateTo(baseUrl);
    }

    /**
     * Refresh current page
     */
    public void refreshPage() {
        driver.navigate().refresh();
        logger.info("Page refreshed");
    }

    /**
     * Navigate back
     */
    public void navigateBack() {
        driver.navigate().back();
        logger.info("Navigated back");
    }

    /**
     * Navigate forward
     */
    public void navigateForward() {
        driver.navigate().forward();
        logger.info("Navigated forward");
    }

    // ==================== Element Finder Methods ====================

    /**
     * Find element using Object Repository key
     * @param elementKey OR element key
     * @return WebElement
     */
    public WebElement findElement(String elementKey) {
        By locator = orManager.getLocator(elementKey);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Find element using By locator
     * @param locator By locator
     * @return WebElement
     */
    public WebElement findElement(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Find multiple elements using OR key
     * @param elementKey OR element key
     * @return List of WebElements
     */
    public List<WebElement> findElements(String elementKey) {
        By locator = orManager.getLocator(elementKey);
        return driver.findElements(locator);
    }

    /**
     * Find clickable element
     * @param elementKey OR element key
     * @return WebElement that is clickable
     */
    public WebElement findClickableElement(String elementKey) {
        By locator = orManager.getLocator(elementKey);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Find visible element
     * @param elementKey OR element key
     * @return WebElement that is visible
     */
    public WebElement findVisibleElement(String elementKey) {
        By locator = orManager.getLocator(elementKey);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // ==================== Click Actions ====================

    /**
     * Click on element
     * @param elementKey OR element key
     */
    public void click(String elementKey) {
        WebElement element = findClickableElement(elementKey);
        element.click();
        logger.info("Clicked on element: {}", elementKey);
    }

    /**
     * Double click on element
     * @param elementKey OR element key
     */
    public void doubleClick(String elementKey) {
        WebElement element = findClickableElement(elementKey);
        actions.doubleClick(element).perform();
        logger.info("Double clicked on element: {}", elementKey);
    }

    /**
     * Right click on element
     * @param elementKey OR element key
     */
    public void rightClick(String elementKey) {
        WebElement element = findClickableElement(elementKey);
        actions.contextClick(element).perform();
        logger.info("Right clicked on element: {}", elementKey);
    }

    /**
     * Click using JavaScript
     * @param elementKey OR element key
     */
    public void jsClick(String elementKey) {
        WebElement element = findElement(elementKey);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        logger.info("JS clicked on element: {}", elementKey);
    }

    // ==================== Input Actions ====================

    /**
     * Type text into element
     * @param elementKey OR element key
     * @param text Text to type
     */
    public void type(String elementKey, String text) {
        WebElement element = findVisibleElement(elementKey);
        element.clear();
        element.sendKeys(text);
        logger.info("Typed '{}' into element: {}", text, elementKey);
    }

    /**
     * Type text without clearing
     * @param elementKey OR element key
     * @param text Text to type
     */
    public void typeWithoutClear(String elementKey, String text) {
        WebElement element = findVisibleElement(elementKey);
        element.sendKeys(text);
        logger.info("Typed '{}' into element (without clear): {}", text, elementKey);
    }

    /**
     * Clear element
     * @param elementKey OR element key
     */
    public void clear(String elementKey) {
        WebElement element = findVisibleElement(elementKey);
        element.clear();
        logger.info("Cleared element: {}", elementKey);
    }

    /**
     * Send keys to element
     * @param elementKey OR element key
     * @param keys Keys to send
     */
    public void sendKeys(String elementKey, Keys keys) {
        WebElement element = findVisibleElement(elementKey);
        element.sendKeys(keys);
        logger.info("Sent keys {} to element: {}", keys.name(), elementKey);
    }

    /**
     * Press Enter key on element
     * @param elementKey OR element key
     */
    public void pressEnter(String elementKey) {
        sendKeys(elementKey, Keys.ENTER);
    }

    /**
     * Press Tab key on element
     * @param elementKey OR element key
     */
    public void pressTab(String elementKey) {
        sendKeys(elementKey, Keys.TAB);
    }

    // ==================== Dropdown Actions ====================

    /**
     * Select dropdown by visible text
     * @param elementKey OR element key
     * @param visibleText Text to select
     */
    public void selectByText(String elementKey, String visibleText) {
        WebElement element = findVisibleElement(elementKey);
        Select select = new Select(element);
        select.selectByVisibleText(visibleText);
        logger.info("Selected '{}' from dropdown: {}", visibleText, elementKey);
    }

    /**
     * Select dropdown by value
     * @param elementKey OR element key
     * @param value Value to select
     */
    public void selectByValue(String elementKey, String value) {
        WebElement element = findVisibleElement(elementKey);
        Select select = new Select(element);
        select.selectByValue(value);
        logger.info("Selected value '{}' from dropdown: {}", value, elementKey);
    }

    /**
     * Select dropdown by index
     * @param elementKey OR element key
     * @param index Index to select (0-based)
     */
    public void selectByIndex(String elementKey, int index) {
        WebElement element = findVisibleElement(elementKey);
        Select select = new Select(element);
        select.selectByIndex(index);
        logger.info("Selected index {} from dropdown: {}", index, elementKey);
    }

    /**
     * Get all options from dropdown
     * @param elementKey OR element key
     * @return List of option texts
     */
    public List<String> getDropdownOptions(String elementKey) {
        WebElement element = findVisibleElement(elementKey);
        Select select = new Select(element);
        List<String> options = new ArrayList<>();
        for (WebElement option : select.getOptions()) {
            options.add(option.getText());
        }
        return options;
    }

    /**
     * Get selected option text from dropdown
     * @param elementKey OR element key
     * @return Selected option text
     */
    public String getSelectedOption(String elementKey) {
        WebElement element = findVisibleElement(elementKey);
        Select select = new Select(element);
        return select.getFirstSelectedOption().getText();
    }

    // ==================== Checkbox and Radio Button Actions ====================

    /**
     * Check checkbox (if not already checked)
     * @param elementKey OR element key
     */
    public void check(String elementKey) {
        WebElement element = findClickableElement(elementKey);
        if (!element.isSelected()) {
            element.click();
            logger.info("Checked element: {}", elementKey);
        }
    }

    /**
     * Uncheck checkbox (if checked)
     * @param elementKey OR element key
     */
    public void uncheck(String elementKey) {
        WebElement element = findClickableElement(elementKey);
        if (element.isSelected()) {
            element.click();
            logger.info("Unchecked element: {}", elementKey);
        }
    }

    /**
     * Check if element is selected
     * @param elementKey OR element key
     * @return true if selected
     */
    public boolean isSelected(String elementKey) {
        WebElement element = findElement(elementKey);
        return element.isSelected();
    }

    // ==================== Get Text/Attribute Actions ====================

    /**
     * Get text from element
     * @param elementKey OR element key
     * @return Element text
     */
    public String getText(String elementKey) {
        WebElement element = findVisibleElement(elementKey);
        String text = element.getText();
        logger.info("Got text '{}' from element: {}", text, elementKey);
        return text;
    }

    /**
     * Get attribute value
     * @param elementKey OR element key
     * @param attributeName Attribute name
     * @return Attribute value
     */
    public String getAttribute(String elementKey, String attributeName) {
        WebElement element = findElement(elementKey);
        return element.getAttribute(attributeName);
    }

    /**
     * Get CSS value
     * @param elementKey OR element key
     * @param propertyName CSS property name
     * @return CSS value
     */
    public String getCssValue(String elementKey, String propertyName) {
        WebElement element = findElement(elementKey);
        return element.getCssValue(propertyName);
    }

    /**
     * Get page title
     * @return Page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Get current URL
     * @return Current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    // ==================== Verification Actions ====================

    /**
     * Verify element is displayed
     * @param elementKey OR element key
     * @return true if displayed
     */
    public boolean isDisplayed(String elementKey) {
        try {
            WebElement element = findElement(elementKey);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verify element is enabled
     * @param elementKey OR element key
     * @return true if enabled
     */
    public boolean isEnabled(String elementKey) {
        try {
            WebElement element = findElement(elementKey);
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verify element exists in DOM
     * @param elementKey OR element key
     * @return true if exists
     */
    public boolean isElementPresent(String elementKey) {
        try {
            By locator = orManager.getLocator(elementKey);
            return !driver.findElements(locator).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verify text is present in element
     * @param elementKey OR element key
     * @param expectedText Expected text
     * @return true if text matches
     */
    public boolean verifyText(String elementKey, String expectedText) {
        String actualText = getText(elementKey);
        boolean result = actualText.equals(expectedText);
        logger.info("Text verification: expected='{}', actual='{}', result={}", expectedText, actualText, result);
        return result;
    }

    /**
     * Verify text contains
     * @param elementKey OR element key
     * @param expectedText Expected text to contain
     * @return true if contains
     */
    public boolean verifyTextContains(String elementKey, String expectedText) {
        String actualText = getText(elementKey);
        boolean result = actualText.contains(expectedText);
        logger.info("Text contains verification: expected='{}', actual='{}', result={}", expectedText, actualText, result);
        return result;
    }

    /**
     * Verify page title
     * @param expectedTitle Expected title
     * @return true if title matches
     */
    public boolean verifyTitle(String expectedTitle) {
        String actualTitle = getPageTitle();
        boolean result = actualTitle.equals(expectedTitle);
        logger.info("Title verification: expected='{}', actual='{}', result={}", expectedTitle, actualTitle, result);
        return result;
    }

    /**
     * Verify page title contains
     * @param expectedText Text to contain
     * @return true if contains
     */
    public boolean verifyTitleContains(String expectedText) {
        String actualTitle = getPageTitle();
        boolean result = actualTitle.contains(expectedText);
        logger.info("Title contains verification: expected='{}', actual='{}', result={}", expectedText, actualTitle, result);
        return result;
    }

    // ==================== Wait Actions ====================

    /**
     * Wait for element to be visible
     * @param elementKey OR element key
     * @param timeoutSeconds Timeout in seconds
     */
    public void waitForElementVisible(String elementKey, int timeoutSeconds) {
        By locator = orManager.getLocator(elementKey);
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
        logger.info("Waited for element to be visible: {}", elementKey);
    }

    /**
     * Wait for element to be clickable
     * @param elementKey OR element key
     * @param timeoutSeconds Timeout in seconds
     */
    public void waitForElementClickable(String elementKey, int timeoutSeconds) {
        By locator = orManager.getLocator(elementKey);
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.elementToBeClickable(locator));
        logger.info("Waited for element to be clickable: {}", elementKey);
    }

    /**
     * Wait for element to disappear
     * @param elementKey OR element key
     * @param timeoutSeconds Timeout in seconds
     */
    public void waitForElementInvisible(String elementKey, int timeoutSeconds) {
        By locator = orManager.getLocator(elementKey);
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
        logger.info("Waited for element to be invisible: {}", elementKey);
    }

    /**
     * Wait for page load complete
     */
    public void waitForPageLoad() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
        logger.info("Page load complete");
    }

    /**
     * Static wait (not recommended, use with caution)
     * @param seconds Seconds to wait
     */
    public void staticWait(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ==================== Scroll Actions ====================

    /**
     * Scroll to element
     * @param elementKey OR element key
     */
    public void scrollToElement(String elementKey) {
        WebElement element = findElement(elementKey);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        logger.info("Scrolled to element: {}", elementKey);
    }

    /**
     * Scroll to top of page
     */
    public void scrollToTop() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
        logger.info("Scrolled to top of page");
    }

    /**
     * Scroll to bottom of page
     */
    public void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        logger.info("Scrolled to bottom of page");
    }

    /**
     * Scroll by pixels
     * @param x Horizontal pixels
     * @param y Vertical pixels
     */
    public void scrollByPixels(int x, int y) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(" + x + ", " + y + ");");
        logger.info("Scrolled by pixels: x={}, y={}", x, y);
    }

    // ==================== Mouse Actions ====================

    /**
     * Hover over element
     * @param elementKey OR element key
     */
    public void hover(String elementKey) {
        WebElement element = findVisibleElement(elementKey);
        actions.moveToElement(element).perform();
        logger.info("Hovered over element: {}", elementKey);
    }

    /**
     * Drag and drop
     * @param sourceKey Source element OR key
     * @param targetKey Target element OR key
     */
    public void dragAndDrop(String sourceKey, String targetKey) {
        WebElement source = findVisibleElement(sourceKey);
        WebElement target = findVisibleElement(targetKey);
        actions.dragAndDrop(source, target).perform();
        logger.info("Dragged {} to {}", sourceKey, targetKey);
    }

    // ==================== Window/Frame Actions ====================

    /**
     * Switch to window by index
     * @param index Window index (0-based)
     */
    public void switchToWindow(int index) {
        Set<String> handles = driver.getWindowHandles();
        String[] handleArray = handles.toArray(new String[0]);
        if (index < handleArray.length) {
            driver.switchTo().window(handleArray[index]);
            logger.info("Switched to window index: {}", index);
        }
    }

    /**
     * Switch to new window (most recent)
     */
    public void switchToNewWindow() {
        String currentHandle = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(currentHandle)) {
                driver.switchTo().window(handle);
                logger.info("Switched to new window");
                break;
            }
        }
    }

    /**
     * Close current window and switch back
     */
    public void closeCurrentWindow() {
        String currentHandle = driver.getWindowHandle();
        driver.close();
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(currentHandle)) {
                driver.switchTo().window(handle);
                break;
            }
        }
        logger.info("Closed current window and switched back");
    }

    /**
     * Switch to frame by element key
     * @param elementKey OR element key
     */
    public void switchToFrame(String elementKey) {
        WebElement frame = findElement(elementKey);
        driver.switchTo().frame(frame);
        logger.info("Switched to frame: {}", elementKey);
    }

    /**
     * Switch to frame by index
     * @param index Frame index
     */
    public void switchToFrameByIndex(int index) {
        driver.switchTo().frame(index);
        logger.info("Switched to frame index: {}", index);
    }

    /**
     * Switch to default content
     */
    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
        logger.info("Switched to default content");
    }

    /**
     * Switch to parent frame
     */
    public void switchToParentFrame() {
        driver.switchTo().parentFrame();
        logger.info("Switched to parent frame");
    }

    // ==================== Alert Actions ====================

    /**
     * Accept alert
     */
    public void acceptAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        logger.info("Alert accepted");
    }

    /**
     * Dismiss alert
     */
    public void dismissAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().dismiss();
        logger.info("Alert dismissed");
    }

    /**
     * Get alert text
     * @return Alert text
     */
    public String getAlertText() {
        wait.until(ExpectedConditions.alertIsPresent());
        return driver.switchTo().alert().getText();
    }

    /**
     * Type into alert prompt
     * @param text Text to type
     */
    public void typeInAlert(String text) {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().sendKeys(text);
        logger.info("Typed '{}' into alert", text);
    }

    /**
     * Check if alert is present
     * @return true if alert is present
     */
    public boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    // ==================== JavaScript Actions ====================

    /**
     * Execute JavaScript
     * @param script JavaScript to execute
     * @param args Arguments
     * @return Result of execution
     */
    public Object executeJavaScript(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    /**
     * Highlight element (for debugging)
     * @param elementKey OR element key
     */
    public void highlightElement(String elementKey) {
        WebElement element = findElement(elementKey);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].style.border='3px solid red'", element);
    }

    /**
     * Set value using JavaScript
     * @param elementKey OR element key
     * @param value Value to set
     */
    public void setValueByJS(String elementKey, String value) {
        WebElement element = findElement(elementKey);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value='" + value + "'", element);
        logger.info("Set value '{}' to element {} using JS", value, elementKey);
    }
}
