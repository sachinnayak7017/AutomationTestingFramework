package org.example.validation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.reporting.ReportManager;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;

/**
 * CheckpointManager - Manages soft and hard assertions for test validations.
 * Provides checkpoint tracking and consolidated assertion reporting.
 *
 * Features:
 * - Soft assertions (continue on failure)
 * - Hard assertions (stop on failure)
 * - Checkpoint tracking with pass/fail counts
 * - Integration with ReportManager
 */
public class CheckpointManager {

    private static final Logger logger = LogManager.getLogger(CheckpointManager.class);
    private static final ThreadLocal<CheckpointManager> instance = new ThreadLocal<>();

    private final List<Checkpoint> checkpoints;
    private final SoftAssert softAssert;
    private int passCount;
    private int failCount;
    private int totalCount;

    /**
     * Checkpoint - Record of a single checkpoint
     */
    public static class Checkpoint {
        private final int number;
        private final String description;
        private final boolean passed;
        private final String expected;
        private final String actual;
        private final String message;
        private final long timestamp;

        public Checkpoint(int number, String description, boolean passed,
                          String expected, String actual, String message) {
            this.number = number;
            this.description = description;
            this.passed = passed;
            this.expected = expected;
            this.actual = actual;
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        public int getNumber() { return number; }
        public String getDescription() { return description; }
        public boolean isPassed() { return passed; }
        public String getExpected() { return expected; }
        public String getActual() { return actual; }
        public String getMessage() { return message; }
        public long getTimestamp() { return timestamp; }

        @Override
        public String toString() {
            return String.format("Checkpoint #%d [%s]: %s - Expected: '%s', Actual: '%s'",
                    number, passed ? "PASS" : "FAIL", description, expected, actual);
        }
    }

    /**
     * Private constructor
     */
    private CheckpointManager() {
        this.checkpoints = new ArrayList<>();
        this.softAssert = new SoftAssert();
        this.passCount = 0;
        this.failCount = 0;
        this.totalCount = 0;
    }

    /**
     * Get instance for current thread
     * @return CheckpointManager instance
     */
    public static CheckpointManager getInstance() {
        if (instance.get() == null) {
            instance.set(new CheckpointManager());
        }
        return instance.get();
    }

    /**
     * Reset checkpoints for new test
     */
    public static void reset() {
        instance.remove();
    }

    // ==================== Soft Assertion Methods ====================

    /**
     * Soft checkpoint - verify equals (continue on failure)
     * @param description Checkpoint description
     * @param expected Expected value
     * @param actual Actual value
     */
    public void softCheckpoint(String description, Object expected, Object actual) {
        totalCount++;
        boolean passed = (expected == null && actual == null) ||
                         (expected != null && expected.equals(actual));

        String expStr = String.valueOf(expected);
        String actStr = String.valueOf(actual);
        String message = passed ? "Checkpoint passed" : "Expected '" + expStr + "' but got '" + actStr + "'";

        recordCheckpoint(description, passed, expStr, actStr, message);

        // Use soft assert
        softAssert.assertEquals(actual, expected, description);
    }

    /**
     * Soft checkpoint - verify true (continue on failure)
     * @param description Checkpoint description
     * @param condition Condition to verify
     */
    public void softCheckpointTrue(String description, boolean condition) {
        totalCount++;
        String message = condition ? "Condition is true" : "Condition is false";

        recordCheckpoint(description, condition, "true", String.valueOf(condition), message);

        softAssert.assertTrue(condition, description);
    }

    /**
     * Soft checkpoint - verify false (continue on failure)
     * @param description Checkpoint description
     * @param condition Condition to verify
     */
    public void softCheckpointFalse(String description, boolean condition) {
        totalCount++;
        String message = !condition ? "Condition is false" : "Condition is true";

        recordCheckpoint(description, !condition, "false", String.valueOf(condition), message);

        softAssert.assertFalse(condition, description);
    }

    /**
     * Soft checkpoint - verify not null (continue on failure)
     * @param description Checkpoint description
     * @param object Object to check
     */
    public void softCheckpointNotNull(String description, Object object) {
        totalCount++;
        boolean passed = object != null;
        String message = passed ? "Object is not null" : "Object is null";

        recordCheckpoint(description, passed, "not null", object == null ? "null" : "not null", message);

        softAssert.assertNotNull(object, description);
    }

    /**
     * Soft checkpoint - verify contains (continue on failure)
     * @param description Checkpoint description
     * @param actual Actual string
     * @param expected Expected substring
     */
    public void softCheckpointContains(String description, String actual, String expected) {
        totalCount++;
        boolean passed = actual != null && actual.contains(expected);
        String message = passed ? "String contains expected value" : "String does not contain expected value";

        recordCheckpoint(description, passed, "contains '" + expected + "'", actual, message);

        softAssert.assertTrue(passed, description + " - Expected to contain: " + expected);
    }

    // ==================== Hard Assertion Methods ====================

    /**
     * Hard checkpoint - verify equals (stop on failure)
     * @param description Checkpoint description
     * @param expected Expected value
     * @param actual Actual value
     */
    public void hardCheckpoint(String description, Object expected, Object actual) {
        totalCount++;
        boolean passed = (expected == null && actual == null) ||
                         (expected != null && expected.equals(actual));

        String expStr = String.valueOf(expected);
        String actStr = String.valueOf(actual);
        String message = passed ? "Checkpoint passed" : "Expected '" + expStr + "' but got '" + actStr + "'";

        recordCheckpoint(description, passed, expStr, actStr, message);

        if (!passed) {
            throw new AssertionError("Hard checkpoint failed: " + description + " - " + message);
        }
    }

    /**
     * Hard checkpoint - verify true (stop on failure)
     * @param description Checkpoint description
     * @param condition Condition to verify
     */
    public void hardCheckpointTrue(String description, boolean condition) {
        totalCount++;
        String message = condition ? "Condition is true" : "Condition is false";

        recordCheckpoint(description, condition, "true", String.valueOf(condition), message);

        if (!condition) {
            throw new AssertionError("Hard checkpoint failed: " + description + " - Condition is false");
        }
    }

    /**
     * Hard checkpoint - verify false (stop on failure)
     * @param description Checkpoint description
     * @param condition Condition to verify
     */
    public void hardCheckpointFalse(String description, boolean condition) {
        totalCount++;
        String message = !condition ? "Condition is false" : "Condition is true";

        recordCheckpoint(description, !condition, "false", String.valueOf(condition), message);

        if (condition) {
            throw new AssertionError("Hard checkpoint failed: " + description + " - Condition is true");
        }
    }

    // ==================== Validation Checkpoints ====================

    /**
     * Checkpoint using ValidationEngine
     * @param description Checkpoint description
     * @param ruleName Validation rule name
     * @param value Value to validate
     * @param soft Use soft assertion if true
     */
    public void validationCheckpoint(String description, String ruleName, String value, boolean soft) {
        ValidationEngine.ValidationResult result = ValidationEngine.getInstance().validate(ruleName, value);

        totalCount++;
        recordCheckpoint(description + " [" + ruleName + "]", result.isValid(),
                "valid", result.isValid() ? "valid" : "invalid", result.getMessage());

        if (!result.isValid()) {
            if (soft) {
                softAssert.fail(description + " - " + result.getMessage());
            } else {
                throw new AssertionError("Validation checkpoint failed: " + description + " - " + result.getMessage());
            }
        }
    }

    // ==================== Helper Methods ====================

    /**
     * Record checkpoint in list and report
     */
    private void recordCheckpoint(String description, boolean passed, String expected, String actual, String message) {
        if (passed) {
            passCount++;
        } else {
            failCount++;
        }

        Checkpoint checkpoint = new Checkpoint(totalCount, description, passed, expected, actual, message);
        checkpoints.add(checkpoint);

        // Log to report
        String logMessage = String.format("Checkpoint #%d: %s - %s", totalCount, description, message);

        if (passed) {
            ReportManager.logPass(logMessage);
            logger.info("[CHECKPOINT PASS] {}", logMessage);
        } else {
            ReportManager.logFail(logMessage);
            logger.error("[CHECKPOINT FAIL] {}", logMessage);
        }
    }

    /**
     * Assert all soft checkpoints (call at end of test)
     * Throws AssertionError if any soft checkpoint failed
     */
    public void assertAll() {
        logger.info("========== Checkpoint Summary ==========");
        logger.info("Total: {}, Passed: {}, Failed: {}", totalCount, passCount, failCount);
        logger.info("========================================");

        ReportManager.logInfo(String.format("Checkpoint Summary - Total: %d, Passed: %d, Failed: %d",
                totalCount, passCount, failCount));

        softAssert.assertAll();
    }

    /**
     * Check if all checkpoints passed
     * @return true if all passed
     */
    public boolean allPassed() {
        return failCount == 0;
    }

    /**
     * Get all checkpoints
     * @return List of checkpoints
     */
    public List<Checkpoint> getCheckpoints() {
        return new ArrayList<>(checkpoints);
    }

    /**
     * Get failed checkpoints
     * @return List of failed checkpoints
     */
    public List<Checkpoint> getFailedCheckpoints() {
        List<Checkpoint> failed = new ArrayList<>();
        for (Checkpoint cp : checkpoints) {
            if (!cp.isPassed()) {
                failed.add(cp);
            }
        }
        return failed;
    }

    // Getters for counts
    public int getPassCount() { return passCount; }
    public int getFailCount() { return failCount; }
    public int getTotalCount() { return totalCount; }

    /**
     * Print checkpoint summary
     */
    public void printSummary() {
        logger.info("========== Checkpoint Summary ==========");
        for (Checkpoint cp : checkpoints) {
            logger.info(cp.toString());
        }
        logger.info("Total: {}, Passed: {}, Failed: {}", totalCount, passCount, failCount);
        logger.info("========================================");
    }
}
