package org.example.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.ConfigLoader;

import java.util.Map;

/**
 * APIUtils - Utility class for REST API testing using RestAssured.
 * Provides methods for GET, POST, PUT, PATCH, DELETE operations.
 */
public class APIUtils {

    private static final Logger logger = LogManager.getLogger(APIUtils.class);
    private String baseUrl;
    private RequestSpecification requestSpec;
    private Response lastResponse;

    /**
     * Constructor using config properties
     */
    public APIUtils() {
        this.baseUrl = ConfigLoader.getInstance().getApiBaseUrl();
        initRequestSpec();
    }

    /**
     * Constructor with custom base URL
     * @param baseUrl API base URL
     */
    public APIUtils(String baseUrl) {
        this.baseUrl = baseUrl;
        initRequestSpec();
    }

    /**
     * Initialize request specification
     */
    private void initRequestSpec() {
        RestAssured.baseURI = baseUrl;
        requestSpec = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
        logger.info("APIUtils initialized with base URL: {}", baseUrl);
    }

    /**
     * Set base URL
     * @param baseUrl Base URL
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        RestAssured.baseURI = baseUrl;
    }

    /**
     * Get base URL
     * @return Base URL
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    // ==================== Headers and Authentication ====================

    /**
     * Add header to request
     * @param key Header name
     * @param value Header value
     * @return this for chaining
     */
    public APIUtils addHeader(String key, String value) {
        requestSpec = requestSpec.header(key, value);
        return this;
    }

    /**
     * Add multiple headers
     * @param headers Map of headers
     * @return this for chaining
     */
    public APIUtils addHeaders(Map<String, String> headers) {
        requestSpec = requestSpec.headers(headers);
        return this;
    }

    /**
     * Set Bearer token authorization
     * @param token Bearer token
     * @return this for chaining
     */
    public APIUtils setBearerToken(String token) {
        requestSpec = requestSpec.header("Authorization", "Bearer " + token);
        return this;
    }

    /**
     * Set Basic authentication
     * @param username Username
     * @param password Password
     * @return this for chaining
     */
    public APIUtils setBasicAuth(String username, String password) {
        requestSpec = requestSpec.auth().basic(username, password);
        return this;
    }

    /**
     * Set content type
     * @param contentType Content type
     * @return this for chaining
     */
    public APIUtils setContentType(ContentType contentType) {
        requestSpec = requestSpec.contentType(contentType);
        return this;
    }

    /**
     * Add query parameter
     * @param key Parameter name
     * @param value Parameter value
     * @return this for chaining
     */
    public APIUtils addQueryParam(String key, Object value) {
        requestSpec = requestSpec.queryParam(key, value);
        return this;
    }

    /**
     * Add multiple query parameters
     * @param params Map of parameters
     * @return this for chaining
     */
    public APIUtils addQueryParams(Map<String, Object> params) {
        requestSpec = requestSpec.queryParams(params);
        return this;
    }

    /**
     * Add path parameter
     * @param key Parameter name
     * @param value Parameter value
     * @return this for chaining
     */
    public APIUtils addPathParam(String key, Object value) {
        requestSpec = requestSpec.pathParam(key, value);
        return this;
    }

    // ==================== GET Requests ====================

    /**
     * Send GET request
     * @param endpoint API endpoint
     * @return Response object
     */
    public Response get(String endpoint) {
        logger.info("Sending GET request to: {}{}", baseUrl, endpoint);
        lastResponse = requestSpec.get(endpoint);
        logResponse();
        resetRequestSpec();
        return lastResponse;
    }

    /**
     * Send GET request with query parameters
     * @param endpoint API endpoint
     * @param queryParams Query parameters
     * @return Response object
     */
    public Response get(String endpoint, Map<String, Object> queryParams) {
        addQueryParams(queryParams);
        return get(endpoint);
    }

    // ==================== POST Requests ====================

    /**
     * Send POST request with JSON body
     * @param endpoint API endpoint
     * @param body Request body (will be serialized to JSON)
     * @return Response object
     */
    public Response post(String endpoint, Object body) {
        logger.info("Sending POST request to: {}{}", baseUrl, endpoint);
        lastResponse = requestSpec.body(body).post(endpoint);
        logResponse();
        resetRequestSpec();
        return lastResponse;
    }

    /**
     * Send POST request with string body
     * @param endpoint API endpoint
     * @param jsonBody JSON string body
     * @return Response object
     */
    public Response post(String endpoint, String jsonBody) {
        logger.info("Sending POST request to: {}{}", baseUrl, endpoint);
        lastResponse = requestSpec.body(jsonBody).post(endpoint);
        logResponse();
        resetRequestSpec();
        return lastResponse;
    }

    /**
     * Send POST request without body
     * @param endpoint API endpoint
     * @return Response object
     */
    public Response post(String endpoint) {
        logger.info("Sending POST request to: {}{}", baseUrl, endpoint);
        lastResponse = requestSpec.post(endpoint);
        logResponse();
        resetRequestSpec();
        return lastResponse;
    }

    // ==================== PUT Requests ====================

    /**
     * Send PUT request with JSON body
     * @param endpoint API endpoint
     * @param body Request body
     * @return Response object
     */
    public Response put(String endpoint, Object body) {
        logger.info("Sending PUT request to: {}{}", baseUrl, endpoint);
        lastResponse = requestSpec.body(body).put(endpoint);
        logResponse();
        resetRequestSpec();
        return lastResponse;
    }

    /**
     * Send PUT request with string body
     * @param endpoint API endpoint
     * @param jsonBody JSON string body
     * @return Response object
     */
    public Response put(String endpoint, String jsonBody) {
        logger.info("Sending PUT request to: {}{}", baseUrl, endpoint);
        lastResponse = requestSpec.body(jsonBody).put(endpoint);
        logResponse();
        resetRequestSpec();
        return lastResponse;
    }

    // ==================== PATCH Requests ====================

    /**
     * Send PATCH request with JSON body
     * @param endpoint API endpoint
     * @param body Request body
     * @return Response object
     */
    public Response patch(String endpoint, Object body) {
        logger.info("Sending PATCH request to: {}{}", baseUrl, endpoint);
        lastResponse = requestSpec.body(body).patch(endpoint);
        logResponse();
        resetRequestSpec();
        return lastResponse;
    }

    // ==================== DELETE Requests ====================

    /**
     * Send DELETE request
     * @param endpoint API endpoint
     * @return Response object
     */
    public Response delete(String endpoint) {
        logger.info("Sending DELETE request to: {}{}", baseUrl, endpoint);
        lastResponse = requestSpec.delete(endpoint);
        logResponse();
        resetRequestSpec();
        return lastResponse;
    }

    /**
     * Send DELETE request with body
     * @param endpoint API endpoint
     * @param body Request body
     * @return Response object
     */
    public Response delete(String endpoint, Object body) {
        logger.info("Sending DELETE request to: {}{}", baseUrl, endpoint);
        lastResponse = requestSpec.body(body).delete(endpoint);
        logResponse();
        resetRequestSpec();
        return lastResponse;
    }

    // ==================== Response Helpers ====================

    /**
     * Get last response
     * @return Last Response object
     */
    public Response getLastResponse() {
        return lastResponse;
    }

    /**
     * Get status code from last response
     * @return Status code
     */
    public int getStatusCode() {
        return lastResponse != null ? lastResponse.getStatusCode() : -1;
    }

    /**
     * Get response body as string
     * @return Response body string
     */
    public String getResponseBody() {
        return lastResponse != null ? lastResponse.getBody().asString() : null;
    }

    /**
     * Get response body as object
     * @param clazz Target class
     * @param <T> Type
     * @return Deserialized object
     */
    public <T> T getResponseAs(Class<T> clazz) {
        return lastResponse != null ? lastResponse.getBody().as(clazz) : null;
    }

    /**
     * Get value from JSON response by path
     * @param jsonPath JSON path (e.g., "data.id", "users[0].name")
     * @param <T> Type
     * @return Value at path
     */
    public <T> T getJsonPathValue(String jsonPath) {
        return lastResponse != null ? lastResponse.jsonPath().get(jsonPath) : null;
    }

    /**
     * Get response header
     * @param headerName Header name
     * @return Header value
     */
    public String getResponseHeader(String headerName) {
        return lastResponse != null ? lastResponse.getHeader(headerName) : null;
    }

    /**
     * Get response time in milliseconds
     * @return Response time
     */
    public long getResponseTime() {
        return lastResponse != null ? lastResponse.getTime() : -1;
    }

    // ==================== Validation Helpers ====================

    /**
     * Verify status code
     * @param expectedStatusCode Expected status code
     * @return true if matches
     */
    public boolean verifyStatusCode(int expectedStatusCode) {
        int actualStatusCode = getStatusCode();
        boolean result = actualStatusCode == expectedStatusCode;
        logger.info("Status code verification: expected={}, actual={}, result={}",
                expectedStatusCode, actualStatusCode, result);
        return result;
    }

    /**
     * Verify response contains key
     * @param jsonPath JSON path to check
     * @return true if path exists
     */
    public boolean verifyJsonPathExists(String jsonPath) {
        try {
            Object value = getJsonPathValue(jsonPath);
            return value != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verify JSON path value
     * @param jsonPath JSON path
     * @param expectedValue Expected value
     * @return true if matches
     */
    public boolean verifyJsonPathValue(String jsonPath, Object expectedValue) {
        Object actualValue = getJsonPathValue(jsonPath);
        boolean result = expectedValue != null ? expectedValue.equals(actualValue) : actualValue == null;
        logger.info("JSON path verification: path={}, expected={}, actual={}, result={}",
                jsonPath, expectedValue, actualValue, result);
        return result;
    }

    /**
     * Verify response body contains text
     * @param text Text to check
     * @return true if contains
     */
    public boolean verifyResponseContains(String text) {
        String body = getResponseBody();
        boolean result = body != null && body.contains(text);
        logger.info("Response contains verification: text='{}', result={}", text, result);
        return result;
    }

    // ==================== Private Helpers ====================

    /**
     * Log response details
     */
    private void logResponse() {
        if (lastResponse != null) {
            logger.info("Response Status: {}", lastResponse.getStatusCode());
            logger.debug("Response Body: {}", lastResponse.getBody().asString());
            logger.debug("Response Time: {}ms", lastResponse.getTime());
        }
    }

    /**
     * Reset request specification for next request
     */
    private void resetRequestSpec() {
        requestSpec = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }
}
