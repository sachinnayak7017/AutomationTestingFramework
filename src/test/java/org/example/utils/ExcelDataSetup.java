package org.example.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * ExcelDataSetup - Standalone utility to create/update test data sheets in TestSuite.xlsx.
 *
 * HOW TO RUN:
 * 1. Right-click this file in IntelliJ -> Run 'ExcelDataSetup.main()'
 * 2. With --force flag to recreate existing sheets:
 *    Run Configuration -> Program Arguments: --force
 * 3. Recreate a specific sheet:
 *    Run Configuration -> Program Arguments: --force managebeneficiary
 *
 * Sheets:
 * 1. prelogin           - Login credentials ONLY (DataKey | Value) - common for all modules
 * 2. dashboard          - Dashboard-specific test data (DataKey | Value)
 * 3. fundtransfer       - Fund Transfer (row-based: TestCaseID | columns | ExpectedResult)
 * 4. managebeneficiary  - Manage Beneficiary (row-based: TestCaseID | columns | ExpectedResult)
 *
 * NOTE: Login data (BASE_URL, USER_ID, PASSWORD) is ONLY in prelogin sheet.
 * All modules read login data from prelogin via SessionManager.
 */
public class ExcelDataSetup {

    private static final String EXCEL_PATH = "src/test/resources/testdata/TestSuite.xlsx";

    public static void main(String[] args) {
        boolean forceRecreate = false;
        String targetSheet = null;

        for (String arg : args) {
            if ("--force".equals(arg)) {
                forceRecreate = true;
            } else {
                targetSheet = arg.toLowerCase();
            }
        }

        try {
            File file = new File(EXCEL_PATH);
            Workbook workbook;

            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                workbook = new XSSFWorkbook(fis);
                fis.close();
                System.out.println("Opened existing workbook: " + EXCEL_PATH);
            } else {
                file.getParentFile().mkdirs();
                workbook = new XSSFWorkbook();
                System.out.println("Created new workbook: " + EXCEL_PATH);
            }

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            if (forceRecreate) {
                System.out.println("--force: will recreate " +
                    (targetSheet != null ? "'" + targetSheet + "'" : "ALL") + " sheets");
            }

            // 1. Pre-login (ONLY place for login data)
            if (shouldProcess(targetSheet, "prelogin")) {
                forceDeleteSheet(workbook, "prelogin", forceRecreate);
                createPreloginSheet(workbook, headerStyle);
            }

            // 2. Dashboard (NO login data - only dashboard-specific)
            if (shouldProcess(targetSheet, "dashboard")) {
                forceDeleteSheet(workbook, "dashboard", forceRecreate);
                createDashboardSheet(workbook, headerStyle);
            }

            // 3. Manage Beneficiary (row-based like fundtransfer)
            if (shouldProcess(targetSheet, "managebeneficiary")) {
                forceDeleteSheet(workbook, "managebeneficiary", forceRecreate);
                createManageBeneficiarySheet(workbook, headerStyle);
            }

            // 4. Fund Transfer (row-based)
            if (shouldProcess(targetSheet, "fundtransfer")) {
                forceDeleteSheet(workbook, "fundtransfer", forceRecreate);
                createFundTransferSheet(workbook, headerStyle);
            }

            FileOutputStream fos = new FileOutputStream(EXCEL_PATH);
            workbook.write(fos);
            fos.close();
            workbook.close();

            System.out.println("\n=== Test data sheets created/updated successfully! ===");
            System.out.println("File: " + new File(EXCEL_PATH).getAbsolutePath());
            System.out.println("IMPORTANT: Replace REPLACE_* placeholder values with real test data.");

        } catch (Exception e) {
            System.err.println("Error setting up Excel data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean shouldProcess(String targetSheet, String sheetName) {
        return targetSheet == null || targetSheet.equals(sheetName);
    }

    private static void forceDeleteSheet(Workbook workbook, String sheetName, boolean force) {
        if (force && workbook.getSheet(sheetName) != null) {
            workbook.removeSheetAt(workbook.getSheetIndex(sheetName));
            System.out.println("Deleted existing sheet: " + sheetName);
        }
    }

    // ==================== 1. PRELOGIN SHEET (Login data - common for ALL modules) ====================

    private static void createPreloginSheet(Workbook workbook, CellStyle headerStyle) {
        createKeyValueSheet(workbook, headerStyle, "prelogin", new String[][]{
            {"BASE_URL", "https://eohs-uat.shivalik.bank.in/RIB/"},
            {"USER_ID_VALID", "995050"},
            {"USER_ID_SAMPLE", "995050"},
            {"USER_ID_ALL_CHARS", "test_user123"},
            {"USER_ID_INVALID", "invalidUser123"},
            {"USER_ID_EMPTY", ""},
            {"PASSWORD_VALID", "Shivalik@1"},
            {"PASSWORD_SAMPLE", "Test@1234"},
            {"PASSWORD_WRONG", "WrongPass@123"},
            {"PASSWORD_INVALID", "wrongPassword"},
            {"PASSWORD_EMPTY", ""},
            {"CAPTCHA_VALID", "222222"},
            {"CAPTCHA_INVALID", "000000"}
        });
    }

    // ==================== 2. DASHBOARD SHEET (dashboard-specific only, NO login data) ====================

    private static void createDashboardSheet(Workbook workbook, CellStyle headerStyle) {
        createKeyValueSheet(workbook, headerStyle, "dashboard", new String[][]{
            {"EXPECTED_PROFILE_NAME", "Nitish singh"},
            {"EXPECTED_ACCOUNT_NUMBER", "100110513763"}
        });
    }

    // ==================== 3. MANAGE BENEFICIARY SHEET (row-based like fundtransfer) ====================

    private static void createManageBeneficiarySheet(Workbook workbook, CellStyle headerStyle) {
        String sheetName = "managebeneficiary";
        if (workbook.getSheet(sheetName) != null) {
            System.out.println("Sheet '" + sheetName + "' already exists - skipping");
            return;
        }

        Sheet sheet = workbook.createSheet(sheetName);

        // Headers (same pattern as fundtransfer)
        String[] headers = {
            "TestCaseID", "Description", "BankType_Value", "AccountNumber_Value",
            "ReEnterAccountNumber_Value", "IFSCCode_Value", "BeneficiaryName_Value",
            "Nickname_Value", "OTP_Value", "SearchText_Value", "ExpectedResult"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            createCell(headerRow, i, headers[i], headerStyle);
        }

        // ===== Real data from Application (DOM-verified) =====
        String SH = "Shivalik Bank";
        String OB = "Other Bank";
        // Shivalik beneficiary accounts
        String ACC2 = "100110042018";      // MR. GAURAV MISHRA - Shivalik beneficiary for add
        String ACC3 = "100112510009";      // HARSHIT GOYAL - existing Shivalik beneficiary
        // Invalid / negative test data
        String ACC_MM = "9999999999";       // Mismatch account
        String ACC_INV = "ABC123";          // Invalid format (non-numeric)
        String IFSC = "PUNB0206500";        // Punjab National Bank IFSC (Other Bank)
        String IFSC_INV = "INVALID1234";    // Invalid IFSC
        // Names from actual application (DOM-verified)
        String NM_S = "GAURAV MISHRA";      // Shivalik beneficiary name for add
        String NK_S = "gaurav";             // Shivalik beneficiary nickname
        String NM_O = "sachin nayak";       // Other Bank beneficiary name
        String NK_O = "sachin";             // Other Bank nickname
        String NK_E = "EditedNick";         // Edited nickname value
        // OTP values (222222 = UAT environment fixed OTP)
        String OTP_V = "222222";            // Valid OTP (UAT environment)
        // Search values
        String SEARCH_VALID = "harshit";    // Real beneficiary name in app
        String SEARCH_INVALID = "ZZZZNONEXISTENT"; // Non-existent beneficiary

        // ===== Expected messages (DOM-verified from actual application) =====
        String ERR_EMPTY = "Bank account number is required;Please re-enter your bank account number;Beneficiary name is required";
        String ERR_MISMATCH = "Account numbers do not match";
        String ERR_FORMAT = "Bank Account number should be 12 digits";
        String ERR_IFSC = "Please enter valid IFSC code";
        String SUC_ADD = "Beneficiary added successfully!";
        String SUC_EDIT = "Beneficiary details updated successfully!";
        String SUC_DEL = "Beneficiary deleted successfully!";
        String MSG_COOL_NONE = "No beneficiaries under cooling period";

        // =====================================================================
        // ONLY rows referenced by ManageBeneficiary.feature (22 merged scenarios)
        // =====================================================================
        // Columns: TestCaseID, Description, BankType, Account, ReEnter, IFSC, BenName, Nick, OTP, Search, Expected
        String[][] data = {

            // === MODULE 1: MAIN PAGE (MB_001) ===
            {"MB_001", "Verify Manage Beneficiaries main page elements", "", "", "", "", "", "", "", "", ""},

            // === MODULE 2: ADD SHIVALIK (MB_011 - MB_017) ===
            {"MB_011", "Click ADD and verify add form with all elements", SH, "", "", "", "", "", "", "", ""},
            {"MB_012", "Click CONTINUE with empty Shivalik form - validate errors", SH, "", "", "", "", "", "", "", ERR_EMPTY},
            {"MB_013", "Enter mismatched re-enter account - validate error", SH, ACC2, ACC_MM, "", "", "", "", "", ERR_MISMATCH},
            {"MB_014", "Enter invalid account format - validate error", SH, ACC_INV, ACC_INV, "", NM_S, "", "", "", ERR_FORMAT},
            {"MB_015", "Complete Shivalik form - verify confirm page and OTP section", SH, ACC2, ACC2, "", NM_S, NK_S, "", "", ""},
            {"MB_016", "E2E Add Shivalik: form -> confirm -> OTP -> success -> Done -> main", SH, ACC2, ACC2, "", NM_S, NK_S, OTP_V, "", SUC_ADD},
            {"MB_017", "Back arrow on add form returns to main page", SH, "", "", "", "", "", "", "", ""},

            // === MODULE 3: MANAGE BENEFICIARY LIST (MB_030 - MB_031) ===
            {"MB_030", "Search beneficiary, verify tabs and filter with A-Z sort", "", "", "", "", "", "", "", SEARCH_VALID, ""},
            {"MB_031", "Search non-existent beneficiary - no results", "", "", "", "", "", "", "", SEARCH_INVALID, MSG_COOL_NONE},

            // === MODULE 4: ADD OTHER BANK (MB_040 - MB_045) ===
            {"MB_040", "Click ADD, select Other Bank and verify form elements", OB, "", "", "", "", "", "", "", ""},
            {"MB_041", "Click CONTINUE with empty Other Bank form - validate errors", OB, "", "", "", "", "", "", "", ERR_EMPTY},
            {"MB_042", "Enter invalid IFSC code - validate error", OB, ACC3, ACC3, IFSC_INV, NM_O, "", "", "", ERR_IFSC},
            {"MB_043", "Complete Other Bank form - verify confirm page and OTP section", OB, ACC3, ACC3, IFSC, NM_O, NK_O, "", "", ""},
            {"MB_044", "E2E Add Other Bank: form -> confirm -> OTP -> success -> Done -> main", OB, ACC3, ACC3, IFSC, NM_O, NK_O, OTP_V, "", SUC_ADD},
            {"MB_045", "Open Search IFSC sidebar, verify elements and close", OB, "", "", "", "", "", "", "", ""},

            // === MODULE 5: EDIT BENEFICIARY (MB_060 - MB_062) ===
            {"MB_060", "Click beneficiary, verify details page, 3-dot menu and Edit form", "", ACC3, "", "", "Harshit", "harshit", "", "", ""},
            {"MB_061", "E2E Edit: beneficiary -> 3-dot -> Edit -> nickname -> SAVE -> OTP -> success -> Done", "", "", "", "", "", NK_E, OTP_V, "", SUC_EDIT},
            {"MB_062", "Edit nickname with empty value", "", "", "", "", "", "", "", "", ""},

            // === MODULE 6: DELETE BENEFICIARY (MB_080 - MB_082) ===
            {"MB_080", "Click beneficiary, 3-dot, Delete and verify delete page elements", "", "", "", "", "", "", "", "", ""},
            {"MB_081", "E2E Delete: beneficiary -> 3-dot -> Delete -> DELETE -> OTP -> success -> Done", "", "", "", "", "", "", OTP_V, "", SUC_DEL},
            {"MB_082", "Verify page after all operations", "", "", "", "", "", "", "", "", ""},
        };

        for (int i = 0; i < data.length; i++) {
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < data[i].length; j++) {
                row.createCell(j).setCellValue(data[i][j]);
            }
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        System.out.println("Created sheet: " + sheetName + " (" + data.length + " rows, row-based format)");
    }

    // ==================== 4. FUND TRANSFER SHEET (row-based) ====================

    private static void createFundTransferSheet(Workbook workbook, CellStyle headerStyle) {
        String sheetName = "fundtransfer";
        if (workbook.getSheet(sheetName) != null) {
            System.out.println("Sheet '" + sheetName + "' already exists - skipping");
            return;
        }

        Sheet sheet = workbook.createSheet(sheetName);

        String[] headers = {
            "TestCaseID", "Description", "BankType_Value", "BeneficiarySearch_Value",
            "BankAccountNumber_Value", "ReEnterAccountNumber_Value", "IFSCCode_Value",
            "RecipientName_Value", "Amount_Value", "Remarks_Value",
            "TransactionType_Value", "ExpectedResult",
            // E2E complete transaction columns
            "BeneficiaryName_Value", "BeneficiaryAccountNo_Value",
            "TransferFromAccount_Value", "AccountHolderName_Value",
            "BankName_Value", "OTP_Value"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            createCell(headerRow, i, headers[i], headerStyle);
        }

        // ===== Real data from AccountInfo sheet =====
        String SH = "Shivalik Bank";
        String OB = "Other Bank";
        String ACC1 = "100110513763";      // saving, high balance
        String ACC2 = "100010510088";      // saving, 100
        String ACC3 = "100012250035";      // current, 200
        String ACC4 = "100012250034";      // current, 500
        String ACC5 = "100012510000";      // saving, 0 balance
        String ACC6 = "100012250036";      // current, 0 balance
        String ACC_MM = "9999999999";       // Mismatch account
        String IFSC = "PUNB0206500";        // Real IFSC
        String IFSC_SHORT = "ABC";          // Too short IFSC
        // Beneficiary accounts (corrected 12-digit numbers)
        // Shivalik beneficiary accounts (from actual system)
        String BEN_GAURAV_ACC = "100110042018";  // MR. GAURAV MISHRA
        String BEN_HARSHIT_ACC = "100112510009"; // HARSHIT GOYAL
        String BEN_RUBY_ACC = "100110513099";    // RUBY TASNEEM
        String BEN_RAFEEQ_ACC = "100110513185";  // RAFEEQ AHMAD
        String BEN_RAMESH_ACC = "100110513229";  // RAMESH KUMAR
        String BEN_SHAMA_ACC = "100110513325";   // SHAMA PRAVEEN
        String BEN_SARANG_ACC = "100110028240";  // MR. SARANG SINGHAL
        String BEN_RAJNI_ACC = "100110510872";   // RAJNI DEVI
        String BEN_DEEPA_ACC = "100110512811";   // DEEPA JAIN
        String BEN_SHANKY_ACC = "100112510010";  // SHANKY JAIN
        String BEN_AARYA_ACC = "100110513167";   // AARYA MAHESHWARI
        // Aliases for QT scenarios
        String BEN_NAITIK = BEN_GAURAV_ACC;      // Primary QT account
        String BEN_ACC1 = BEN_HARSHIT_ACC;
        String BEN_ACC2 = BEN_RUBY_ACC;
        String BEN_ACC3 = BEN_RAFEEQ_ACC;
        String BEN_ACC4 = BEN_RAMESH_ACC;
        String BEN_ACC5 = BEN_SHAMA_ACC;
        String BEN_ACC6 = BEN_SARANG_ACC;
        String BEN_ACC7 = BEN_RAJNI_ACC;
        String BEN_ACC8 = BEN_DEEPA_ACC;
        String BEN_ACC9 = BEN_SHANKY_ACC;
        String BEN_ACC10 = BEN_AARYA_ACC;
        String NM = "Nitish singh";
        String NM_BEN = "GAURAV MISHRA";          // QT recipient name (for QT Shivalik)
        String NM_OB = "Test Recipient";           // QT recipient name (for QT Other Bank)
        String NEFT = "NEFT";
        String IMPS = "IMPS";
        String RTGS = "RTGS";
        // TB module: beneficiaries are pre-added, search by name
        String BEN_S = "shivam";                   // TB Shivalik beneficiary search term
        String BEN_S_FULL = "MS Shivam Kapil";    // TB Shivalik beneficiary full name
        String BEN_OB = "neha";                    // TB Other Bank beneficiary search term
        String BEN_OB_FULL = "NEHA";               // TB Other Bank beneficiary full name
        // E2E transaction data
        String OTP = "222222";                  // Fixed OTP for all transactions
        String HOLDER = "Nitish singh";         // Account holder name (logged-in user)
        String BANK_SH = "Shivalik Small Finance Bank";  // Shivalik bank name
        String BANK_OB_PNB = "PUNJAB NATIONAL BANK";     // Other Bank - PNB
        String BEN_OB_ACC = "";                 // OB account (auto-filled by app in TB mode)
        String IFSC_SH = "SMCB0001001";        // Shivalik IFSC code

        // ===== Expected messages =====
        String ERR_TB = "Amount is required;Transfer from is required";
        String ERR_QT = "Bank account number is required;Please re-enter your bank account number;Recipient's name is required;Amount is required;Transfer from is required";
        String ERR_QT_OB = "Bank account number is required;Please re-enter your bank account number;Recipient's name is required;Amount is required;Transfer from is required";
        String ERR_SA = "Amount is required;Transfer from is required";
        String ERR_MISMATCH = "Account numbers do not match";
        String ERR_IFSC = "Please enter valid IFSC code";
        String ERR_CANCEL = "Are you sure you want to cancel the transaction?";
        String ERR_IMPS = "Charges may apply for IMPS";
        String ERR_AMT = "Please enter valid amount";
        String ERR_AMT_MAX = "Maximum amount for Quick Transfer is Rs. 25000";
        String CONFIRM = "Confirm Transfer Details";

        // Columns: TestCaseID, Description, BankType, BenSearch, AccNo, ReAccNo, IFSC, RecipientName, Amount, Remarks, TransType, Expected
        String[][] data = {

            // =====================================================================
            // SECTION 1: EXPECTED RESULT VALIDATION (referenced by feature steps)
            // =====================================================================
            {"FT_044", "TB SH mandatory errors empty form", SH, "", "", "", "", "", "", "", "", ERR_TB},
            {"FT_052", "TB SH cancel popup", SH, "", "", "", "", "", "", "", "", ERR_CANCEL},
            {"FT_085", "TB OB NEFT mandatory errors", OB, "", "", "", "", "", "", "", NEFT, ERR_TB},
            {"FT_089", "TB OB IMPS charges warning", OB, "", "", "", "", "", "", "", IMPS, ERR_IMPS},
            {"FT_093", "TB OB IMPS mandatory errors", OB, "", "", "", "", "", "", "", IMPS, ERR_TB},
            {"FT_094", "TB OB cancel popup", OB, "", "", "", "", "", "", "", "", ERR_CANCEL},
            {"FT_150", "QT SH mandatory errors empty", SH, "", "", "", "", "", "", "", "", ERR_QT},
            {"FT_158", "QT SH mismatch account", SH, "", ACC2, ACC_MM, "", "", "", "", "", ERR_MISMATCH},
            {"FT_165", "QT SH cancel popup", SH, "", "", "", "", "", "", "", "", ERR_CANCEL},
            {"FT_204", "QT OB mandatory errors with IFSC", OB, "", "", "", "", "", "", "", "", ERR_QT_OB},
            {"FT_274", "SA mandatory errors empty", "", "", "", "", "", "", "", "", "", ERR_SA},
            {"FT_280", "SA cancel popup", "", "", "", "", "", "", "", "", "", ERR_CANCEL},

            // =====================================================================
            // SECTION 2: COMPOSITE FORM FILL (E2E complete transaction data)
            // Cols 0-11: TestCaseID, Desc, BankType, BenSearch, AccNo, ReAccNo, IFSC, RecipientName, Amount, Remarks, TransType, ExpectedResult
            // Cols 12-17: BeneficiaryName, BeneficiaryAccountNo, TransferFromAccount, AccountHolderName, BankName, OTP
            // =====================================================================
            {"FT_TB_001", "TB Shivalik E2E", SH, BEN_S, "", "", "", "",
                "5000", "Shivalik Transfer", "", "",
                BEN_S_FULL, "", ACC1, HOLDER, BANK_SH, OTP},
            {"FT_TB_OB_NEFT_001", "TB OB NEFT E2E", OB, BEN_OB, "", "", "", "",
                "10000", "NEFT Transfer", NEFT, "",
                BEN_OB_FULL, BEN_OB_ACC, ACC1, HOLDER, BANK_OB_PNB, OTP},
            {"FT_TB_OB_IMPS_001", "TB OB IMPS E2E", OB, BEN_OB, "", "", "", "",
                "5000", "", IMPS, "",
                BEN_OB_FULL, BEN_OB_ACC, ACC1, HOLDER, BANK_OB_PNB, OTP},
            {"FT_TB_OB_RTGS_001", "TB OB RTGS E2E", OB, BEN_OB, "", "", "", "",
                "200000", "RTGS Transfer", RTGS, "",
                BEN_OB_FULL, BEN_OB_ACC, ACC1, HOLDER, BANK_OB_PNB, OTP},
            {"FT_QT_SB_001", "QT Shivalik E2E", SH, "", BEN_NAITIK, BEN_NAITIK, "", NM_BEN,
                "100", "Quick Transfer", "", "",
                NM_BEN, BEN_NAITIK, ACC1, HOLDER, BANK_SH, OTP},
            {"FT_QT_OB_001", "QT Other Bank E2E", OB, "", BEN_ACC2, BEN_ACC2, IFSC, NM_BEN,
                "500", "QT Other Bank", "", "",
                NM_BEN, BEN_ACC2, ACC1, HOLDER, BANK_OB_PNB, OTP},
            {"FT_SA_001", "SA E2E", "", "", "", "", "", "",
                "5000", "Self Transfer", "", "",
                "Sachin Kumar", "", ACC5, HOLDER, BANK_SH, OTP},

            // =====================================================================
            // SECTION 3: TB SHIVALIK - Extra variations (positive + negative)
            // =====================================================================
            {"FT_TB_002", "TB SH with ACC1 high balance", SH, BEN_S, "", "", "", "", "10000", "High balance", "", ""},
            {"FT_TB_003", "TB SH small amount 100", SH, BEN_S, "", "", "", "", "100", "Small Transfer", "", ""},
            {"FT_TB_004", "TB SH large amount 50000", SH, BEN_S, "", "", "", "", "50000", "Large Transfer", "", ""},
            {"FT_TB_005", "TB SH zero amount (negative)", SH, BEN_S, "", "", "", "", "0", "Zero amt", "", ERR_AMT},
            {"FT_TB_006", "TB SH negative amount (negative)", SH, BEN_S, "", "", "", "", "-500", "Neg amt", "", ERR_AMT},
            {"FT_TB_007", "TB SH decimal amount 500.50", SH, BEN_S, "", "", "", "", "500.50", "Decimal", "", ""},
            {"FT_TB_008", "TB SH empty beneficiary (negative)", SH, "", "", "", "", "", "1000", "No benef", "", ERR_TB},
            {"FT_TB_009", "TB SH amount letters (negative)", SH, BEN_S, "", "", "", "", "abc500", "Letters", "", ERR_AMT},

            // =====================================================================
            // SECTION 4: TB OTHER BANK - Extra NEFT/IMPS/RTGS variations
            // =====================================================================
            {"FT_TB_OB_NEFT_002", "TB OB NEFT small amount 500", OB, BEN_OB, "", "", "", "", "500", "NEFT Small", NEFT, ""},
            {"FT_TB_OB_NEFT_003", "TB OB NEFT zero amount (negative)", OB, BEN_OB, "", "", "", "", "0", "Zero NEFT", NEFT, ERR_AMT},
            {"FT_TB_OB_NEFT_004", "TB OB NEFT negative amount (negative)", OB, BEN_OB, "", "", "", "", "-1000", "Neg NEFT", NEFT, ERR_AMT},
            {"FT_TB_OB_IMPS_002", "TB OB IMPS small amount 100", OB, BEN_OB, "", "", "", "", "100", "", IMPS, ""},
            {"FT_TB_OB_IMPS_003", "TB OB IMPS max amount 25000", OB, BEN_OB, "", "", "", "", "25000", "", IMPS, ""},
            {"FT_TB_OB_RTGS_002", "TB OB RTGS large 500000", OB, BEN_OB, "", "", "", "", "500000", "RTGS Large", RTGS, ""},
            {"FT_TB_OB_RTGS_003", "TB OB RTGS below min (negative)", OB, BEN_OB, "", "", "", "", "100000", "Below min", RTGS, ERR_AMT},

            // =====================================================================
            // SECTION 5: QT SHIVALIK - Extra variations with different accounts
            // =====================================================================
            {"FT_QT_SB_002", "QT SH with BEN_ACC2", SH, "", BEN_ACC2, BEN_ACC2, "", NM_BEN, "500", "QT BEN2", "", ""},
            {"FT_QT_SB_003", "QT SH with BEN_ACC3", SH, "", BEN_ACC3, BEN_ACC3, "", NM_BEN, "200", "QT BEN3", "", ""},
            {"FT_QT_SB_004", "QT SH with BEN_ACC4", SH, "", BEN_ACC4, BEN_ACC4, "", NM_BEN, "300", "QT BEN4", "", ""},
            {"FT_QT_SB_005", "QT SH with BEN_ACC5", SH, "", BEN_ACC5, BEN_ACC5, "", NM_BEN, "100", "QT BEN5", "", ""},
            {"FT_QT_SB_006", "QT SH max amount 25000", SH, "", BEN_NAITIK, BEN_NAITIK, "", NM_BEN, "25000", "Max QT", "", ""},
            {"FT_QT_SB_007", "QT SH above max 30000 (negative)", SH, "", BEN_NAITIK, BEN_NAITIK, "", NM_BEN, "30000", "Above max", "", ERR_AMT_MAX},
            {"FT_QT_SB_008", "QT SH zero amount (negative)", SH, "", BEN_NAITIK, BEN_NAITIK, "", NM_BEN, "0", "Zero amt", "", ERR_AMT},
            {"FT_QT_SB_009", "QT SH mismatch by 1 digit (negative)", SH, "", BEN_NAITIK, "100310016598", "", NM_BEN, "100", "Mismatch", "", ERR_MISMATCH},
            {"FT_QT_SB_010", "QT SH special chars recipient (negative)", SH, "", BEN_NAITIK, BEN_NAITIK, "", "Test@123", "500", "Special", "", ""},
            {"FT_QT_SB_011", "QT SH empty remarks (optional)", SH, "", BEN_NAITIK, BEN_NAITIK, "", NM_BEN, "500", "", "", ""},

            // =====================================================================
            // SECTION 6: QT OTHER BANK - Extra variations with IFSC edge cases
            // =====================================================================
            {"FT_QT_OB_002", "QT OB with BEN_ACC2", OB, "", BEN_ACC2, BEN_ACC2, IFSC, NM_BEN, "1000", "QT OB BEN2", "", ""},
            {"FT_QT_OB_003", "QT OB with BEN_ACC4", OB, "", BEN_ACC4, BEN_ACC4, IFSC, NM_BEN, "300", "QT OB BEN4", "", ""},
            {"FT_QT_OB_004", "QT OB IFSC too short (negative)", OB, "", BEN_ACC3, BEN_ACC3, IFSC_SHORT, NM_BEN, "500", "Short IFSC", "", ERR_IFSC},
            {"FT_QT_OB_005", "QT OB IFSC special chars (negative)", OB, "", BEN_ACC3, BEN_ACC3, "PUNB@20650", NM_BEN, "500", "Special IFSC", "", ERR_IFSC},
            {"FT_QT_OB_006", "QT OB account mismatch (negative)", OB, "", BEN_ACC3, ACC_MM, IFSC, NM_BEN, "500", "Mismatch", "", ERR_MISMATCH},
            {"FT_QT_OB_007", "QT OB zero amount (negative)", OB, "", BEN_ACC3, BEN_ACC3, IFSC, NM_BEN, "0", "Zero amt", "", ERR_AMT},
            {"FT_QT_OB_008", "QT OB above max 30000 (negative)", OB, "", BEN_ACC3, BEN_ACC3, IFSC, NM_BEN, "30000", "Above max", "", ERR_AMT_MAX},
            {"FT_QT_OB_009", "QT OB BEN_ACC6 (negative)", OB, "", BEN_ACC6, BEN_ACC6, IFSC, NM_BEN, "100", "QT OB BEN6", "", ERR_AMT},

            // =====================================================================
            // SECTION 7: SELF ACCOUNT - Extra variations
            // =====================================================================
            {"FT_SA_002", "SA small amount 100", "", "", "", "", "", "", "100", "SA Small", "", ""},
            {"FT_SA_003", "SA large amount 50000", "", "", "", "", "", "", "50000", "SA Large", "", ""},
            {"FT_SA_004", "SA zero amount (negative)", "", "", "", "", "", "", "0", "Zero amt", "", ERR_AMT},
            {"FT_SA_005", "SA negative amount (negative)", "", "", "", "", "", "", "-500", "Neg amt", "", ERR_AMT},
            {"FT_SA_006", "SA decimal amount 1000.50", "", "", "", "", "", "", "1000.50", "Decimal", "", ""},
            {"FT_SA_007", "SA letters in amount (negative)", "", "", "", "", "", "", "abc500", "Letter amt", "", ERR_AMT},
            {"FT_SA_008", "SA empty remarks (optional)", "", "", "", "", "", "", "5000", "", "", ""},
            {"FT_SA_009", "SA special chars remarks", "", "", "", "", "", "", "5000", "Test@#$%&!", "", ""},

            // =====================================================================
            // SECTION 8: SINGLE-FIELD VALIDATION SCENARIOS (for feature file parameterized steps)
            // =====================================================================
            // TB Shivalik - field validations
            {"FT_042", "TB amount numeric 5000", SH, "", "", "", "", "", "5000", "", "", ""},
            {"FT_043", "TB amount special chars", SH, "", "", "", "", "", "abc@#500", "", "", ""},
            {"FT_045", "TB beneficiary search", SH, BEN_S, "", "", "", "", "", "", "", ""},
            {"FT_050", "TB remarks input", SH, "", "", "", "", "", "", "Test Remarks", "", ""},
            {"FT_051", "TB remarks max 35 chars", SH, "", "", "", "", "", "", "This is a very long remark exceeding max allowed", "", ""},

            // QT - field validations (account, recipient, amount, IFSC)
            {"FT_151", "QT 2-digit account error", SH, "", "12", "", "", "", "", "", "", ""},
            {"FT_152", "QT 3-digit account no error", SH, "", "123", "", "", "", "", "", "", ""},
            {"FT_153", "QT account max 12 chars", SH, "", "1234567890123", "", "", "", "", "", "", ""},
            {"FT_154", "QT account numeric only", SH, "", "abc12345xyz", "", "", "", "", "", "", ""},
            {"FT_155", "QT account masked test", SH, "", ACC2, "", "", "", "", "", "", ""},
            {"FT_157", "QT re-enter account test", SH, "", ACC2, ACC2, "", "", "", "", "", ""},
            {"FT_159", "QT re-enter numeric only", SH, "", "", "abc99887766", "", "", "", "", "", ""},
            {"FT_160", "QT recipient alpha only", SH, "", "", "", "", "Ankur Singh", "", "", "", ""},
            {"FT_162", "QT amount min 100", SH, "", "", "", "", "", "100", "", "", ""},
            {"FT_164", "QT remarks input", SH, "", "", "", "", "", "", "Test Quick Transfer", "", ""},
            {"FT_171", "QT amount max 25000 error", SH, "", "", "", "", "", "30000", "", "", ""},
            {"FT_202", "QT OB IFSC too short", OB, "", "", "", "ABC", "", "", "", "", ""},
            {"FT_205", "QT OB IFSC max 11 chars", OB, "", "", "", "SBIN00012345678", "", "", "", "", ""},

            // SA - field validations
            {"FT_272", "SA amount numeric 5000", "", "", "", "", "", "", "5000", "", "", ""},
            {"FT_278", "SA remarks input", "", "", "", "", "", "", "", "Self transfer test", "", ""},
            {"FT_279", "SA remarks max 120 chars", "", "", "", "", "", "", "", "This is a very long remark that should be truncated because it exceeds the maximum allowed character limit of one hundred and twenty characters total", "", ""},
        };

        for (int i = 0; i < data.length; i++) {
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < data[i].length; j++) {
                row.createCell(j).setCellValue(data[i][j]);
            }
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        System.out.println("Created sheet: " + sheetName + " (" + data.length + " rows, row-based format)");
    }

    // ==================== UTILITY METHODS ====================

    private static void createKeyValueSheet(Workbook workbook, CellStyle headerStyle,
                                             String sheetName, String[][] data) {
        if (workbook.getSheet(sheetName) != null) {
            System.out.println("Sheet '" + sheetName + "' already exists - skipping");
            return;
        }

        Sheet sheet = workbook.createSheet(sheetName);
        Row header = sheet.createRow(0);
        createCell(header, 0, "DataKey", headerStyle);
        createCell(header, 1, "Value", headerStyle);

        for (int i = 0; i < data.length; i++) {
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(data[i][0]);
            row.createCell(1).setCellValue(data[i][1]);
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        System.out.println("Created sheet: " + sheetName + " (" + data.length + " entries)");
    }

    private static void createCell(Row row, int column, String value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }
}
