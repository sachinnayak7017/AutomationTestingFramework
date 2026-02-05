package org.example.validation;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.FrameworkConstants;
import org.example.utils.json.JsonReader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * ValidationEngine - Validates data against JSON-driven rules.
 * Supports regex patterns, numeric ranges, password policies, and custom validations.
 *
 * validationRules.json structure:
 * {
 *   "email": { "type": "regex", "pattern": "^[\\w.-]+@[\\w.-]+\\.\\w+$", "message": "Invalid email format" },
 *   "password": { "type": "regex", "pattern": "^(?=.*[A-Z])(?=.*\\d).{8,}$", "message": "Password must have 8+ chars, 1 uppercase, 1 digit" },
 *   "age": { "type": "range", "min": 18, "max": 100, "message": "Age must be between 18 and 100" },
 *   "phone": { "type": "regex", "pattern": "^\\d{10}$", "message": "Phone must be 10 digits" }
 * }
 */
public class ValidationEngine {

    private static final Logger logger = LogManager.getLogger(ValidationEngine.class);
    private static ValidationEngine instance;
    private final Map<String, ValidationRule> rules;

    /**
     * ValidationRule - Inner class to hold validation rule details
     */
    public static class ValidationRule {
        private final String name;
        private final String type;
        private final String pattern;
        private final Double min;
        private final Double max;
        private final String message;
        private final Integer minLength;
        private final Integer maxLength;
        private final Boolean required;

        public ValidationRule(String name, String type, String pattern, Double min, Double max,
                              String message, Integer minLength, Integer maxLength, Boolean required) {
            this.name = name;
            this.type = type;
            this.pattern = pattern;
            this.min = min;
            this.max = max;
            this.message = message;
            this.minLength = minLength;
            this.maxLength = maxLength;
            this.required = required;
        }

        public String getName() { return name; }
        public String getType() { return type; }
        public String getPattern() { return pattern; }
        public Double getMin() { return min; }
        public Double getMax() { return max; }
        public String getMessage() { return message; }
        public Integer getMinLength() { return minLength; }
        public Integer getMaxLength() { return maxLength; }
        public Boolean isRequired() { return required != null && required; }
    }

    /**
     * ValidationResult - Result of validation
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String ruleName;
        private final String message;
        private final String actualValue;

        public ValidationResult(boolean valid, String ruleName, String message, String actualValue) {
            this.valid = valid;
            this.ruleName = ruleName;
            this.message = message;
            this.actualValue = actualValue;
        }

        public boolean isValid() { return valid; }
        public String getRuleName() { return ruleName; }
        public String getMessage() { return message; }
        public String getActualValue() { return actualValue; }

        @Override
        public String toString() {
            return String.format("[%s] %s: %s (Value: %s)",
                    valid ? "PASS" : "FAIL", ruleName, message, actualValue);
        }
    }

    /**
     * Private constructor - loads validation rules from JSON
     */
    private ValidationEngine() {
        this.rules = new HashMap<>();
        loadValidationRules();
    }

    /**
     * Get singleton instance
     * @return ValidationEngine instance
     */
    public static synchronized ValidationEngine getInstance() {
        if (instance == null) {
            instance = new ValidationEngine();
        }
        return instance;
    }

    /**
     * Reload validation rules
     */
    public static void reload() {
        instance = null;
        getInstance();
    }

    /**
     * Load validation rules from JSON file
     */
    private void loadValidationRules() {
        String rulesPath = FrameworkConstants.VALIDATION_RULES_PATH;
        File rulesFile = new File(rulesPath);

        if (!rulesFile.exists()) {
            logger.warn("Validation rules file not found: {}", rulesPath);
            return;
        }

        try {
            JsonNode rootNode = JsonReader.readJsonFile(rulesPath);

            rootNode.fields().forEachRemaining(entry -> {
                String ruleName = entry.getKey();
                JsonNode ruleNode = entry.getValue();

                ValidationRule rule = parseRule(ruleName, ruleNode);
                rules.put(ruleName, rule);
            });

            logger.info("Loaded {} validation rules from: {}", rules.size(), rulesPath);

        } catch (Exception e) {
            logger.error("Error loading validation rules: {}", e.getMessage());
        }
    }

    /**
     * Parse rule from JSON node
     * @param ruleName Rule name
     * @param node JSON node
     * @return ValidationRule object
     */
    private ValidationRule parseRule(String ruleName, JsonNode node) {
        String type = node.has("type") ? node.get("type").asText() : "regex";
        String pattern = node.has("pattern") ? node.get("pattern").asText() : null;
        Double min = node.has("min") ? node.get("min").asDouble() : null;
        Double max = node.has("max") ? node.get("max").asDouble() : null;
        String message = node.has("message") ? node.get("message").asText() : "Validation failed for " + ruleName;
        Integer minLength = node.has("minLength") ? node.get("minLength").asInt() : null;
        Integer maxLength = node.has("maxLength") ? node.get("maxLength").asInt() : null;
        Boolean required = node.has("required") ? node.get("required").asBoolean() : false;

        return new ValidationRule(ruleName, type, pattern, min, max, message, minLength, maxLength, required);
    }

    /**
     * Validate value against a named rule
     * @param ruleName Rule name from validationRules.json
     * @param value Value to validate
     * @return ValidationResult
     */
    public ValidationResult validate(String ruleName, String value) {
        ValidationRule rule = rules.get(ruleName);

        if (rule == null) {
            logger.warn("Validation rule not found: {}", ruleName);
            return new ValidationResult(true, ruleName, "Rule not found - skipped", value);
        }

        return validateWithRule(rule, value);
    }

    /**
     * Validate value with a specific rule
     * @param rule ValidationRule
     * @param value Value to validate
     * @return ValidationResult
     */
    private ValidationResult validateWithRule(ValidationRule rule, String value) {
        // Check required
        if (rule.isRequired() && (value == null || value.trim().isEmpty())) {
            return new ValidationResult(false, rule.getName(), rule.getMessage() + " (Required field is empty)", value);
        }

        // Skip further validation if value is empty and not required
        if (value == null || value.trim().isEmpty()) {
            return new ValidationResult(true, rule.getName(), "Empty value - skipped", value);
        }

        String type = rule.getType().toLowerCase();

        switch (type) {
            case "regex":
                return validateRegex(rule, value);
            case "range":
                return validateRange(rule, value);
            case "length":
                return validateLength(rule, value);
            case "email":
                return validateEmail(rule, value);
            case "phone":
                return validatePhone(rule, value);
            case "password":
                return validatePassword(rule, value);
            case "alphanumeric":
                return validateAlphanumeric(rule, value);
            case "numeric":
                return validateNumeric(rule, value);
            case "alpha":
                return validateAlpha(rule, value);
            default:
                logger.warn("Unknown validation type: {}", type);
                return new ValidationResult(true, rule.getName(), "Unknown type - skipped", value);
        }
    }

    /**
     * Validate with regex pattern
     */
    private ValidationResult validateRegex(ValidationRule rule, String value) {
        if (rule.getPattern() == null) {
            return new ValidationResult(true, rule.getName(), "No pattern defined", value);
        }

        boolean matches = Pattern.matches(rule.getPattern(), value);
        return new ValidationResult(matches, rule.getName(),
                matches ? "Valid" : rule.getMessage(), value);
    }

    /**
     * Validate numeric range
     */
    private ValidationResult validateRange(ValidationRule rule, String value) {
        try {
            double numValue = Double.parseDouble(value);

            if (rule.getMin() != null && numValue < rule.getMin()) {
                return new ValidationResult(false, rule.getName(),
                        rule.getMessage() + " (Value " + numValue + " is less than " + rule.getMin() + ")", value);
            }

            if (rule.getMax() != null && numValue > rule.getMax()) {
                return new ValidationResult(false, rule.getName(),
                        rule.getMessage() + " (Value " + numValue + " is greater than " + rule.getMax() + ")", value);
            }

            return new ValidationResult(true, rule.getName(), "Valid range", value);

        } catch (NumberFormatException e) {
            return new ValidationResult(false, rule.getName(),
                    rule.getMessage() + " (Not a valid number)", value);
        }
    }

    /**
     * Validate string length
     */
    private ValidationResult validateLength(ValidationRule rule, String value) {
        int length = value.length();

        if (rule.getMinLength() != null && length < rule.getMinLength()) {
            return new ValidationResult(false, rule.getName(),
                    rule.getMessage() + " (Length " + length + " is less than " + rule.getMinLength() + ")", value);
        }

        if (rule.getMaxLength() != null && length > rule.getMaxLength()) {
            return new ValidationResult(false, rule.getName(),
                    rule.getMessage() + " (Length " + length + " is greater than " + rule.getMaxLength() + ")", value);
        }

        return new ValidationResult(true, rule.getName(), "Valid length", value);
    }

    /**
     * Validate email format
     */
    private ValidationResult validateEmail(ValidationRule rule, String value) {
        String emailPattern = "^[\\w.-]+@[\\w.-]+\\.\\w{2,}$";
        boolean matches = Pattern.matches(emailPattern, value);
        return new ValidationResult(matches, rule.getName(),
                matches ? "Valid email" : rule.getMessage(), value);
    }

    /**
     * Validate phone number
     */
    private ValidationResult validatePhone(ValidationRule rule, String value) {
        String phonePattern = rule.getPattern() != null ? rule.getPattern() : "^\\d{10}$";
        boolean matches = Pattern.matches(phonePattern, value.replaceAll("[\\s-()]", ""));
        return new ValidationResult(matches, rule.getName(),
                matches ? "Valid phone" : rule.getMessage(), value);
    }

    /**
     * Validate password strength
     */
    private ValidationResult validatePassword(ValidationRule rule, String value) {
        // Default password pattern: min 8 chars, 1 uppercase, 1 lowercase, 1 digit
        String passwordPattern = rule.getPattern() != null ? rule.getPattern() :
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        boolean matches = Pattern.matches(passwordPattern, value);
        return new ValidationResult(matches, rule.getName(),
                matches ? "Valid password" : rule.getMessage(), value);
    }

    /**
     * Validate alphanumeric
     */
    private ValidationResult validateAlphanumeric(ValidationRule rule, String value) {
        boolean matches = Pattern.matches("^[a-zA-Z0-9]+$", value);
        return new ValidationResult(matches, rule.getName(),
                matches ? "Valid alphanumeric" : rule.getMessage(), value);
    }

    /**
     * Validate numeric only
     */
    private ValidationResult validateNumeric(ValidationRule rule, String value) {
        boolean matches = Pattern.matches("^\\d+$", value);
        return new ValidationResult(matches, rule.getName(),
                matches ? "Valid numeric" : rule.getMessage(), value);
    }

    /**
     * Validate alphabetic only
     */
    private ValidationResult validateAlpha(ValidationRule rule, String value) {
        boolean matches = Pattern.matches("^[a-zA-Z]+$", value);
        return new ValidationResult(matches, rule.getName(),
                matches ? "Valid alphabetic" : rule.getMessage(), value);
    }

    // ==================== Quick Validation Methods ====================

    /**
     * Quick validation for email
     * @param email Email to validate
     * @return true if valid
     */
    public static boolean isValidEmail(String email) {
        return Pattern.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$", email);
    }

    /**
     * Quick validation for phone
     * @param phone Phone to validate
     * @return true if valid
     */
    public static boolean isValidPhone(String phone) {
        return Pattern.matches("^\\d{10}$", phone.replaceAll("[\\s-()]", ""));
    }

    /**
     * Quick validation for numeric
     * @param value Value to validate
     * @return true if numeric
     */
    public static boolean isNumeric(String value) {
        return Pattern.matches("^-?\\d+(\\.\\d+)?$", value);
    }

    /**
     * Check if rule exists
     * @param ruleName Rule name
     * @return true if rule exists
     */
    public boolean hasRule(String ruleName) {
        return rules.containsKey(ruleName);
    }

    /**
     * Get all rule names
     * @return Array of rule names
     */
    public String[] getRuleNames() {
        return rules.keySet().toArray(new String[0]);
    }

    /**
     * Print all loaded rules (for debugging)
     */
    public void printAllRules() {
        logger.info("========== Validation Rules ==========");
        for (Map.Entry<String, ValidationRule> entry : rules.entrySet()) {
            ValidationRule rule = entry.getValue();
            logger.info("{}: type={}, pattern={}", entry.getKey(), rule.getType(), rule.getPattern());
        }
        logger.info("======================================");
    }
}
