package org.example.core.driver;

/**
 * BrowserType - Enumeration of supported browser types.
 * Includes both standard and headless variants.
 */
public enum BrowserType {

    CHROME("chrome", false),
    CHROME_HEADLESS("chrome", true),
    EDGE("edge", false),
    EDGE_HEADLESS("edge", true),
    FIREFOX("firefox", false),
    FIREFOX_HEADLESS("firefox", true),
    SAFARI("safari", false);

    private final String browserName;
    private final boolean headless;

    BrowserType(String browserName, boolean headless) {
        this.browserName = browserName;
        this.headless = headless;
    }

    public String getBrowserName() {
        return browserName;
    }

    public boolean isHeadless() {
        return headless;
    }

    /**
     * Get BrowserType from string name
     * @param browserName Browser name string
     * @param headless Whether headless mode is enabled
     * @return Matching BrowserType
     */
    public static BrowserType fromString(String browserName, boolean headless) {
        if (browserName == null || browserName.isEmpty()) {
            return headless ? CHROME_HEADLESS : CHROME;
        }

        String normalizedName = browserName.toLowerCase().trim();

        switch (normalizedName) {
            case "chrome":
                return headless ? CHROME_HEADLESS : CHROME;
            case "edge":
            case "msedge":
            case "microsoftedge":
                return headless ? EDGE_HEADLESS : EDGE;
            case "firefox":
            case "ff":
                return headless ? FIREFOX_HEADLESS : FIREFOX;
            case "safari":
                return SAFARI;
            default:
                return headless ? CHROME_HEADLESS : CHROME;
        }
    }

    /**
     * Check if browser is Chrome-based
     * @return true if Chrome
     */
    public boolean isChrome() {
        return this == CHROME || this == CHROME_HEADLESS;
    }

    /**
     * Check if browser is Edge-based
     * @return true if Edge
     */
    public boolean isEdge() {
        return this == EDGE || this == EDGE_HEADLESS;
    }

    /**
     * Check if browser is Firefox-based
     * @return true if Firefox
     */
    public boolean isFirefox() {
        return this == FIREFOX || this == FIREFOX_HEADLESS;
    }

    /**
     * Check if browser is Safari
     * @return true if Safari
     */
    public boolean isSafari() {
        return this == SAFARI;
    }

    @Override
    public String toString() {
        return browserName + (headless ? " (headless)" : "");
    }
}
