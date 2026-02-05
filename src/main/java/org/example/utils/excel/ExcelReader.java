package org.example.utils.excel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.config.FrameworkConstants;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * ExcelReader - Utility class for reading Excel files (.xlsx).
 * Supports reading test cases, test data, and object repository from Excel.
 */
public class ExcelReader {

    private static final Logger logger = LogManager.getLogger(ExcelReader.class);
    private final String filePath;
    private Workbook workbook;

    /**
     * Constructor with file path
     * @param filePath Path to Excel file
     */
    public ExcelReader(String filePath) {
        this.filePath = filePath;
        loadWorkbook();
    }

    /**
     * Load Excel workbook
     */
    private void loadWorkbook() {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            workbook = new XSSFWorkbook(fis);
            logger.info("Excel workbook loaded: {}", filePath);
        } catch (IOException e) {
            logger.error("Error loading Excel file: {}", e.getMessage());
            throw new RuntimeException("Error loading Excel file: " + filePath, e);
        }
    }

    /**
     * Get sheet by name
     * @param sheetName Sheet name
     * @return Sheet object
     */
    public Sheet getSheet(String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            logger.error("Sheet not found: {}", sheetName);
            throw new RuntimeException("Sheet not found: " + sheetName);
        }
        return sheet;
    }

    /**
     * Get sheet by index
     * @param index Sheet index (0-based)
     * @return Sheet object
     */
    public Sheet getSheet(int index) {
        return workbook.getSheetAt(index);
    }

    /**
     * Get all sheet names
     * @return List of sheet names
     */
    public List<String> getSheetNames() {
        List<String> sheetNames = new ArrayList<>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            sheetNames.add(workbook.getSheetAt(i).getSheetName());
        }
        return sheetNames;
    }

    /**
     * Get row count for a sheet
     * @param sheetName Sheet name
     * @return Number of rows
     */
    public int getRowCount(String sheetName) {
        Sheet sheet = getSheet(sheetName);
        return sheet.getLastRowNum() + 1;
    }

    /**
     * Get column count for a sheet
     * @param sheetName Sheet name
     * @return Number of columns
     */
    public int getColumnCount(String sheetName) {
        Sheet sheet = getSheet(sheetName);
        Row headerRow = sheet.getRow(0);
        return headerRow != null ? headerRow.getLastCellNum() : 0;
    }

    /**
     * Get cell value as string
     * @param sheetName Sheet name
     * @param rowNum Row number (0-based)
     * @param colNum Column number (0-based)
     * @return Cell value as string
     */
    public String getCellValue(String sheetName, int rowNum, int colNum) {
        Sheet sheet = getSheet(sheetName);
        Row row = sheet.getRow(rowNum);
        if (row == null) return "";

        Cell cell = row.getCell(colNum);
        return getCellValueAsString(cell);
    }

    /**
     * Get cell value by column name
     * @param sheetName Sheet name
     * @param rowNum Row number (0-based)
     * @param columnName Column header name
     * @return Cell value as string
     */
    public String getCellValue(String sheetName, int rowNum, String columnName) {
        int colIndex = getColumnIndex(sheetName, columnName);
        if (colIndex == -1) {
            logger.warn("Column not found: {}", columnName);
            return "";
        }
        return getCellValue(sheetName, rowNum, colIndex);
    }

    /**
     * Get column index by header name
     * @param sheetName Sheet name
     * @param columnName Column header name
     * @return Column index or -1 if not found
     */
    public int getColumnIndex(String sheetName, String columnName) {
        Sheet sheet = getSheet(sheetName);
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) return -1;

        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null && getCellValueAsString(cell).equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Get cell value as string (handles different cell types)
     * @param cell Cell object
     * @return Cell value as string
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                double numValue = cell.getNumericCellValue();
                if (numValue == Math.floor(numValue)) {
                    return String.valueOf((long) numValue);
                }
                return String.valueOf(numValue);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    /**
     * Get row data as map (column name -> value)
     * @param sheetName Sheet name
     * @param rowNum Row number (0-based, excluding header)
     * @return Map of column name to value
     */
    public Map<String, String> getRowAsMap(String sheetName, int rowNum) {
        Map<String, String> rowData = new LinkedHashMap<>();
        Sheet sheet = getSheet(sheetName);
        Row headerRow = sheet.getRow(0);
        Row dataRow = sheet.getRow(rowNum);

        if (headerRow == null || dataRow == null) return rowData;

        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            String columnName = getCellValueAsString(headerRow.getCell(i));
            String cellValue = getCellValueAsString(dataRow.getCell(i));
            if (!columnName.isEmpty()) {
                rowData.put(columnName, cellValue);
            }
        }
        return rowData;
    }

    /**
     * Get all data from sheet as list of maps
     * @param sheetName Sheet name
     * @return List of maps (each map represents a row)
     */
    public List<Map<String, String>> getSheetData(String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();
        int rowCount = getRowCount(sheetName);

        for (int i = 1; i < rowCount; i++) { // Start from 1 to skip header
            Map<String, String> rowData = getRowAsMap(sheetName, i);
            if (!rowData.isEmpty() && !isRowEmpty(rowData)) {
                data.add(rowData);
            }
        }
        logger.info("Loaded {} rows from sheet: {}", data.size(), sheetName);
        return data;
    }

    /**
     * Check if row is empty
     * @param rowData Row data map
     * @return true if all values are empty
     */
    private boolean isRowEmpty(Map<String, String> rowData) {
        return rowData.values().stream().allMatch(String::isEmpty);
    }

    /**
     * Get test data as key-value map from TestData sheet
     * @return Map of DataKey -> Value
     */
    public Map<String, String> getTestData() {
        return getTestData(FrameworkConstants.SHEET_TEST_DATA);
    }

    /**
     * Get test data as key-value map from specified sheet
     * @param sheetName Sheet name
     * @return Map of DataKey -> Value
     */
    public Map<String, String> getTestData(String sheetName) {
        Map<String, String> testData = new HashMap<>();
        List<Map<String, String>> sheetData = getSheetData(sheetName);

        for (Map<String, String> row : sheetData) {
            String key = row.get(FrameworkConstants.COL_DATA_KEY);
            String value = row.get(FrameworkConstants.COL_VALUE);
            if (key != null && !key.isEmpty()) {
                testData.put(key, value != null ? value : "");
            }
        }
        logger.info("Loaded {} test data entries", testData.size());
        return testData;
    }

    /**
     * Get test cases to execute (where Run = Yes/Y/True)
     * @return List of test case data
     */
    public List<Map<String, String>> getTestCasesToExecute() {
        List<Map<String, String>> allTestCases = getSheetData(FrameworkConstants.SHEET_TEST_CASES);
        List<Map<String, String>> testCasesToRun = new ArrayList<>();

        for (Map<String, String> testCase : allTestCases) {
            String runFlag = testCase.get(FrameworkConstants.COL_RUN);
            if (isYes(runFlag)) {
                testCasesToRun.add(testCase);
            }
        }
        logger.info("Found {} test cases to execute out of {}", testCasesToRun.size(), allTestCases.size());
        return testCasesToRun;
    }

    /**
     * Check if value represents "Yes"
     * @param value Value to check
     * @return true if yes/y/true
     */
    private boolean isYes(String value) {
        if (value == null) return false;
        String normalized = value.trim().toLowerCase();
        return normalized.equals("yes") || normalized.equals("y") || normalized.equals("true") || normalized.equals("1");
    }

    /**
     * Get test case by ID
     * @param testCaseId Test case ID
     * @return Test case data map or null if not found
     */
    public Map<String, String> getTestCaseById(String testCaseId) {
        List<Map<String, String>> allTestCases = getSheetData(FrameworkConstants.SHEET_TEST_CASES);

        for (Map<String, String> testCase : allTestCases) {
            String id = testCase.get(FrameworkConstants.COL_TEST_CASE_ID);
            if (testCaseId.equals(id)) {
                return testCase;
            }
        }
        logger.warn("Test case not found: {}", testCaseId);
        return null;
    }

    /**
     * Get test data value by key
     * @param key Data key
     * @return Value or empty string if not found
     */
    public String getTestDataValue(String key) {
        Map<String, String> testData = getTestData();
        return testData.getOrDefault(key, "");
    }

    /**
     * Get 2D array of sheet data (for TestNG DataProvider)
     * @param sheetName Sheet name
     * @return 2D Object array
     */
    public Object[][] getSheetDataAs2DArray(String sheetName) {
        List<Map<String, String>> data = getSheetData(sheetName);
        if (data.isEmpty()) return new Object[0][0];

        Object[][] result = new Object[data.size()][1];
        for (int i = 0; i < data.size(); i++) {
            result[i][0] = data.get(i);
        }
        return result;
    }

    /**
     * Close workbook
     */
    public void close() {
        try {
            if (workbook != null) {
                workbook.close();
                logger.info("Excel workbook closed");
            }
        } catch (IOException e) {
            logger.error("Error closing workbook: {}", e.getMessage());
        }
    }
}
