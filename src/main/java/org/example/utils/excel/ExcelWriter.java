package org.example.utils.excel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.config.FrameworkConstants;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * ExcelWriter - Utility class for writing results to Excel files.
 * Supports step-wise result writing and test case status aggregation.
 */
public class ExcelWriter {

    private static final Logger logger = LogManager.getLogger(ExcelWriter.class);
    private final String filePath;
    private Workbook workbook;

    /**
     * Constructor with file path
     * @param filePath Path to Excel file
     */
    public ExcelWriter(String filePath) {
        this.filePath = filePath;
        loadWorkbook();
    }

    /**
     * Load existing workbook or create new one
     */
    private void loadWorkbook() {
        try {
            java.io.File file = new java.io.File(filePath);
            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(filePath)) {
                    workbook = new XSSFWorkbook(fis);
                    logger.info("Excel workbook loaded for writing: {}", filePath);
                }
            } else {
                workbook = new XSSFWorkbook();
                logger.info("New Excel workbook created: {}", filePath);
            }
        } catch (IOException e) {
            logger.error("Error loading Excel file: {}", e.getMessage());
            throw new RuntimeException("Error loading Excel file: " + filePath, e);
        }
    }

    /**
     * Get or create sheet
     * @param sheetName Sheet name
     * @return Sheet object
     */
    public Sheet getOrCreateSheet(String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
            logger.info("Created new sheet: {}", sheetName);
        }
        return sheet;
    }

    /**
     * Write cell value
     * @param sheetName Sheet name
     * @param rowNum Row number (0-based)
     * @param colNum Column number (0-based)
     * @param value Value to write
     */
    public void setCellValue(String sheetName, int rowNum, int colNum, String value) {
        Sheet sheet = getOrCreateSheet(sheetName);
        Row row = sheet.getRow(rowNum);
        if (row == null) {
            row = sheet.createRow(rowNum);
        }
        Cell cell = row.getCell(colNum);
        if (cell == null) {
            cell = row.createCell(colNum);
        }
        cell.setCellValue(value != null ? value : "");
    }

    /**
     * Write cell value by column name
     * @param sheetName Sheet name
     * @param rowNum Row number (0-based)
     * @param columnName Column header name
     * @param value Value to write
     */
    public void setCellValue(String sheetName, int rowNum, String columnName, String value) {
        int colIndex = getColumnIndex(sheetName, columnName);
        if (colIndex == -1) {
            logger.warn("Column not found: {}. Creating it.", columnName);
            colIndex = createColumn(sheetName, columnName);
        }
        setCellValue(sheetName, rowNum, colIndex, value);
    }

    /**
     * Get column index by header name
     * @param sheetName Sheet name
     * @param columnName Column header name
     * @return Column index or -1 if not found
     */
    public int getColumnIndex(String sheetName, String columnName) {
        Sheet sheet = getOrCreateSheet(sheetName);
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) return -1;

        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null && cell.getStringCellValue().equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Create a new column with header
     * @param sheetName Sheet name
     * @param columnName Column header name
     * @return New column index
     */
    public int createColumn(String sheetName, String columnName) {
        Sheet sheet = getOrCreateSheet(sheetName);
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            headerRow = sheet.createRow(0);
        }
        int newColIndex = headerRow.getLastCellNum();
        if (newColIndex < 0) newColIndex = 0;

        Cell cell = headerRow.createCell(newColIndex);
        cell.setCellValue(columnName);
        return newColIndex;
    }

    /**
     * Create header row with column names
     * @param sheetName Sheet name
     * @param headers Array of header names
     */
    public void createHeaderRow(String sheetName, String... headers) {
        Sheet sheet = getOrCreateSheet(sheetName);
        Row headerRow = sheet.createRow(0);

        CellStyle headerStyle = createHeaderStyle();

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        logger.info("Header row created in sheet: {}", sheetName);
    }

    /**
     * Create header cell style
     * @return CellStyle for headers
     */
    private CellStyle createHeaderStyle() {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    /**
     * Write row data from map
     * @param sheetName Sheet name
     * @param rowNum Row number
     * @param data Map of column name to value
     */
    public void writeRow(String sheetName, int rowNum, Map<String, String> data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            setCellValue(sheetName, rowNum, entry.getKey(), entry.getValue());
        }
    }

    /**
     * Update test case status in TestCases sheet
     * @param testCaseId Test case ID
     * @param status Status (PASS/FAIL/SKIP)
     * @param passed Number of passed steps
     * @param failed Number of failed steps
     * @param skipped Number of skipped steps
     * @param lastError Last error message (if any)
     * @param lastScreenshot Path to last screenshot
     */
    public void updateTestCaseStatus(String testCaseId, String status, int passed, int failed, int skipped,
                                     String lastError, String lastScreenshot) {
        Sheet sheet = getOrCreateSheet(FrameworkConstants.SHEET_TEST_CASES);
        int rowIndex = findRowByTestCaseId(sheet, testCaseId);

        if (rowIndex == -1) {
            logger.warn("Test case not found for update: {}", testCaseId);
            return;
        }

        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        setCellValue(FrameworkConstants.SHEET_TEST_CASES, rowIndex, FrameworkConstants.COL_PASSED, String.valueOf(passed));
        setCellValue(FrameworkConstants.SHEET_TEST_CASES, rowIndex, FrameworkConstants.COL_FAILED, String.valueOf(failed));
        setCellValue(FrameworkConstants.SHEET_TEST_CASES, rowIndex, FrameworkConstants.COL_SKIPPED, String.valueOf(skipped));
        setCellValue(FrameworkConstants.SHEET_TEST_CASES, rowIndex, FrameworkConstants.COL_STATUS, status);
        setCellValue(FrameworkConstants.SHEET_TEST_CASES, rowIndex, FrameworkConstants.COL_LAST_RUN, currentTime);

        if (lastError != null && !lastError.isEmpty()) {
            setCellValue(FrameworkConstants.SHEET_TEST_CASES, rowIndex, FrameworkConstants.COL_LAST_ERROR, lastError);
        }
        if (lastScreenshot != null && !lastScreenshot.isEmpty()) {
            setCellValue(FrameworkConstants.SHEET_TEST_CASES, rowIndex, FrameworkConstants.COL_LAST_SCREENSHOT, lastScreenshot);
        }

        logger.info("Updated test case status: {} = {}", testCaseId, status);
    }

    /**
     * Find row index by test case ID
     * @param sheet Sheet object
     * @param testCaseId Test case ID to find
     * @return Row index or -1 if not found
     */
    private int findRowByTestCaseId(Sheet sheet, String testCaseId) {
        int testCaseIdCol = getColumnIndex(FrameworkConstants.SHEET_TEST_CASES, FrameworkConstants.COL_TEST_CASE_ID);
        if (testCaseIdCol == -1) return -1;

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cell = row.getCell(testCaseIdCol);
                if (cell != null && testCaseId.equals(cell.getStringCellValue())) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Write step result to Results sheet
     * @param testCaseId Test case ID
     * @param stepNumber Step number
     * @param stepDescription Step description
     * @param status Step status
     * @param actualResult Actual result
     * @param screenshotPath Screenshot path (if any)
     */
    public void writeStepResult(String testCaseId, int stepNumber, String stepDescription,
                                String status, String actualResult, String screenshotPath) {
        Sheet sheet = getOrCreateSheet(FrameworkConstants.SHEET_RESULTS);

        // Create header if sheet is new
        if (sheet.getLastRowNum() == 0 && sheet.getRow(0) == null) {
            createHeaderRow(FrameworkConstants.SHEET_RESULTS,
                    "TestCaseId", "StepNo", "Description", "Status", "ActualResult", "Screenshot", "Timestamp");
        }

        int newRowNum = sheet.getLastRowNum() + 1;
        Row row = sheet.createRow(newRowNum);

        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        row.createCell(0).setCellValue(testCaseId);
        row.createCell(1).setCellValue(stepNumber);
        row.createCell(2).setCellValue(stepDescription);
        row.createCell(3).setCellValue(status);
        row.createCell(4).setCellValue(actualResult != null ? actualResult : "");
        row.createCell(5).setCellValue(screenshotPath != null ? screenshotPath : "");
        row.createCell(6).setCellValue(timestamp);

        // Apply color based on status
        applyStatusColor(row.getCell(3), status);

        logger.debug("Step result written: {} - Step {} - {}", testCaseId, stepNumber, status);
    }

    /**
     * Apply color to status cell
     * @param cell Cell to style
     * @param status Status value
     */
    private void applyStatusColor(Cell cell, String status) {
        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(cell.getCellStyle());

        if (FrameworkConstants.STATUS_PASS.equalsIgnoreCase(status)) {
            style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        } else if (FrameworkConstants.STATUS_FAIL.equalsIgnoreCase(status)) {
            style.setFillForegroundColor(IndexedColors.RED.getIndex());
        } else if (FrameworkConstants.STATUS_SKIP.equalsIgnoreCase(status)) {
            style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        } else {
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        }
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell.setCellStyle(style);
    }

    /**
     * Auto-size all columns in a sheet
     * @param sheetName Sheet name
     */
    public void autoSizeColumns(String sheetName) {
        Sheet sheet = getOrCreateSheet(sheetName);
        Row headerRow = sheet.getRow(0);
        if (headerRow != null) {
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                sheet.autoSizeColumn(i);
            }
        }
    }

    /**
     * Save workbook to file
     */
    public void save() {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
            logger.info("Excel workbook saved: {}", filePath);
        } catch (IOException e) {
            logger.error("Error saving Excel file: {}", e.getMessage());
            throw new RuntimeException("Error saving Excel file: " + filePath, e);
        }
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

    /**
     * Save and close workbook
     */
    public void saveAndClose() {
        save();
        close();
    }
}
