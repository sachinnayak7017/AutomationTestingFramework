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
            {"USER_ID_VALID", "REPLACE_WITH_VALID_USER_ID"},
            {"USER_ID_INVALID", "invalidUser123"},
            {"PASSWORD_VALID", "REPLACE_WITH_VALID_PASSWORD"},
            {"PASSWORD_INVALID", "wrongPassword"},
            {"USER_ID_EMPTY", ""},
            {"PASSWORD_EMPTY", ""}
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

        // ===== Real data from AccountInfo sheet =====
        String SH = "Shivalik Bank";
        String OB = "Other Bank";
        // Valid accounts from AccountInfo
        String ACC1 = "100110513763";      // Account number1 (saving, more balance)
        String ACC2 = "100010510088";      // Account number2 (saving, 100)
        String ACC3 = "100012250035";      // Account number3 (current, 200)
        String ACC4 = "100012250034";      // Account number4 (current, 500)
        String ACC5 = "100012510000";      // Account number5 (saving, 0 balance)
        String ACC6 = "100012250036";      // Account number6 (current, 0 balance)
        // Invalid / negative test data
        String ACC_MM = "9999999999";       // Mismatch account
        String ACC_INV = "ABC123";          // Invalid format (non-numeric)
        String ACC_SHORT = "12345";         // Too short account
        String ACC_LONG = "12345678901234567890"; // Too long account
        String IFSC = "PUNB0206500";        // Real IFSC from AccountInfo
        String IFSC_INV = "INVALID1234";    // Invalid IFSC
        String IFSC_SHORT = "PUN";          // Too short IFSC
        // Names from AccountInfo
        String NM_S = "Nitish singh";       // Real user name
        String NK_S = "nitish";             // Real nickname
        String NM_O = "Test Beneficiary Other";
        String NK_O = "TestOther";
        String NK_E = "EditedNickname";
        String NK_SP = "Test@#$%";          // Special chars nickname
        String NK_LONG = "ThisIsAVeryLongNicknameForTestingPurposesOnly"; // Long nickname
        // OTP values
        String OTP_V = "123456";            // Valid OTP (replace with actual)
        String OTP_I = "000000";            // Invalid OTP
        String OTP_W = "111111";            // Wrong OTP
        String OTP_SHORT = "123";           // Too short OTP
        // Search values
        String SEARCH_VALID = "Nitish";     // Real beneficiary name for search
        String SEARCH_INVALID = "ZZZZNONEXISTENT"; // Non-existent beneficiary

        // ===== Expected messages (extracted from app screenshots) =====
        String ERR_EMPTY = "Bank account number is required;Please re-enter your bank account number;Beneficiary name is required";
        String ERR_REENTER_NAME = "Please re-enter your bank account number;Beneficiary name is required";
        String ERR_NAME_ONLY = "Beneficiary name is required";
        String ERR_MISMATCH = "Account numbers do not match";
        String ERR_FORMAT = "Bank Account number should be 12 digits";
        String ERR_IFSC = "Please enter valid IFSC code";
        String ERR_OTP = "Invalid OTP. Please try again.";
        String ERR_OTP_MAX = "Maximum OTP attempts exceeded.";
        String SUC_ADD = "Beneficiary added successfully!";
        String SUC_COOL = "It will take 1 Hour for the beneficiary to get activated";
        String SUC_EDIT = "Beneficiary updated successfully!";
        String SUC_DEL = "Beneficiary deleted successfully!";
        String MSG_OTP = "Please enter One Time Password (OTP) to confirm the beneficiary";
        String MSG_OTP_SHARE = "Never share your OTP to anyone, Bank employees will never ask for your OTP.";
        String MSG_COOL_NONE = "No beneficiaries under cooling period";
        String CONFIRM_TITLE = "Confirm beneficiary details";

        // Columns: TestCaseID, Description, BankType, Account, ReEnter, IFSC, BenName, Nick, OTP, Search, Expected
        String[][] data = {

            // =====================================================================
            // SECTION 1: NO BENEFICIARY ADDED - UI Verification (MB_001 - MB_010)
            // =====================================================================
            {"MB_001", "Verify page title displayed", "", "", "", "", "", "", "", "", "Manage Beneficiaries"},
            {"MB_002", "Verify Back Arrow displayed", "", "", "", "", "", "", "", "", ""},
            {"MB_003", "Verify Search input displayed", "", "", "", "", "", "", "", "", ""},
            {"MB_004", "Verify ADD button displayed", "", "", "", "", "", "", "", "", ""},
            {"MB_005", "Verify All tab displayed", "", "", "", "", "", "", "", "", ""},
            {"MB_006", "Verify Shivalik tab displayed", "", "", "", "", "", "", "", "", ""},
            {"MB_007", "Verify Other Bank tab displayed", "", "", "", "", "", "", "", "", ""},
            {"MB_008", "Verify Cooling Period section", "", "", "", "", "", "", "", "", ""},
            {"MB_009", "Verify Favourite section", "", "", "", "", "", "", "", "", ""},
            {"MB_010", "Verify No Beneficiaries or list shown", "", "", "", "", "", "", "", "", ""},

            // =====================================================================
            // SECTION 2: ADD SHIVALIK BANK (MB_011 - MB_057)
            // =====================================================================
            // --- Add form UI elements (MB_011 - MB_023) ---
            {"MB_011", "Click ADD - form opens", SH, "", "", "", "", "", "", "", ""},
            {"MB_012", "Verify Back Arrow on add form", SH, "", "", "", "", "", "", "", ""},
            {"MB_013", "Verify CANCEL button on add form", SH, "", "", "", "", "", "", "", ""},
            {"MB_014", "Verify CONTINUE button on add form", SH, "", "", "", "", "", "", "", ""},
            {"MB_015", "Verify Shivalik Bank radio displayed", SH, "", "", "", "", "", "", "", ""},
            {"MB_016", "Verify Other Bank radio displayed", SH, "", "", "", "", "", "", "", ""},
            {"MB_017", "Verify Account Number input", SH, "", "", "", "", "", "", "", ""},
            {"MB_018", "Verify Re-enter Account input", SH, "", "", "", "", "", "", "", ""},
            {"MB_019", "Verify Beneficiary Name input", SH, "", "", "", "", "", "", "", ""},
            {"MB_020", "Verify Nickname input", SH, "", "", "", "", "", "", "", ""},
            {"MB_021", "Verify Account masked by default", SH, "", "", "", "", "", "", "", ""},
            {"MB_022", "Eye icon toggles visibility", SH, "", "", "", "", "", "", "", ""},
            {"MB_023", "Select Shivalik radio - selected", SH, "", "", "", "", "", "", "", ""},

            // --- Positive: field entry (MB_024 - MB_028) ---
            {"MB_024", "Enter valid Shivalik account", SH, ACC2, "", "", "", "", "", "", ""},
            {"MB_025", "Enter matching re-enter", SH, ACC2, ACC2, "", "", "", "", "", ""},
            {"MB_026", "Mismatched re-enter account (negative)", SH, ACC2, ACC_MM, "", "", "", "", "", ERR_MISMATCH},
            {"MB_027", "Enter valid beneficiary name", SH, "", "", "", NM_S, "", "", "", ""},
            {"MB_028", "Enter valid nickname", SH, "", "", "", "", NK_S, "", "", ""},

            // --- Negative: validation errors (MB_029 - MB_032) ---
            {"MB_029", "Empty form - all fields empty (negative)", SH, "", "", "", "", "", "", "", ERR_EMPTY},
            {"MB_030", "Only account filled - no reenter/name (negative)", SH, ACC2, "", "", "", "", "", "", ERR_REENTER_NAME},
            {"MB_031", "Account+reenter but no name (negative)", SH, ACC2, ACC2, "", "", "", "", "", ERR_NAME_ONLY},
            {"MB_032", "Invalid account format ABC123 (negative)", SH, ACC_INV, ACC_INV, "", NM_S, "", "", "", ERR_FORMAT},

            // --- Positive: complete form -> confirm page (MB_033 - MB_038) ---
            {"MB_033", "Complete Shivalik form -> confirm page", SH, ACC2, ACC2, "", NM_S, NK_S, "", "", CONFIRM_TITLE},
            {"MB_034", "Confirm page - beneficiary name shown", SH, ACC2, ACC2, "", NM_S, NK_S, "", "", NM_S},
            {"MB_035", "Confirm page - account number shown", SH, ACC2, ACC2, "", NM_S, NK_S, "", "", ACC2},
            {"MB_036", "Confirm page - bank name shown", SH, ACC2, ACC2, "", NM_S, NK_S, "", "", "Shivalik"},
            {"MB_037", "Confirm page - IFSC code shown", SH, ACC2, ACC2, "", NM_S, NK_S, "", "", ""},
            {"MB_038", "Confirm page - nickname shown", SH, ACC2, ACC2, "", NM_S, NK_S, "", "", NK_S},

            // --- OTP page elements (MB_039 - MB_044) ---
            {"MB_039", "OTP input field displayed", SH, ACC2, ACC2, "", NM_S, NK_S, "", "", ""},
            {"MB_040", "OTP timer displayed", SH, ACC2, ACC2, "", NM_S, NK_S, "", "", ""},
            {"MB_041", "Resend OTP link displayed", SH, ACC2, ACC2, "", NM_S, NK_S, "", "", ""},
            {"MB_042", "OTP message text displayed", SH, ACC2, ACC2, "", NM_S, NK_S, "", "", MSG_OTP},
            {"MB_043", "Never share OTP text displayed", SH, ACC2, ACC2, "", NM_S, NK_S, "", "", MSG_OTP_SHARE},
            {"MB_044", "SUBMIT button disabled before OTP", SH, ACC2, ACC2, "", NM_S, NK_S, "", "", ""},

            // --- Negative: OTP errors (MB_045 - MB_046) ---
            {"MB_045", "Invalid OTP 000000 (negative)", SH, ACC2, ACC2, "", NM_S, NK_S, OTP_I, "", ERR_OTP},
            {"MB_046", "Wrong OTP 3 times - locked (negative)", SH, ACC2, ACC2, "", NM_S, NK_S, OTP_W, "", ERR_OTP_MAX},

            // --- Navigation: cancel/back (MB_047 - MB_048) ---
            {"MB_047", "Cancel on confirm page -> return", SH, ACC2, ACC2, "", NM_S, NK_S, "", "", ""},
            {"MB_048", "Back arrow on confirm page", SH, ACC2, ACC2, "", NM_S, NK_S, "", "", ""},

            // --- Positive: successful add (MB_049 - MB_052) ---
            {"MB_049", "Valid OTP - beneficiary added success", SH, ACC2, ACC2, "", NM_S, NK_S, OTP_V, "", SUC_ADD},
            {"MB_050", "Success page - cooling period message", SH, ACC2, ACC2, "", NM_S, NK_S, OTP_V, "", SUC_COOL},
            {"MB_051", "Done button on success page", SH, ACC2, ACC2, "", NM_S, NK_S, OTP_V, "", ""},
            {"MB_052", "Done button - return to list", SH, ACC2, ACC2, "", NM_S, NK_S, OTP_V, "", ""},

            // --- Navigation: cancel/back from add form (MB_053 - MB_054) ---
            {"MB_053", "CANCEL returns from add form", SH, "", "", "", "", "", "", "", ""},
            {"MB_054", "Back Arrow returns from add form", SH, "", "", "", "", "", "", "", ""},

            // --- Labels verification (MB_055 - MB_057) ---
            {"MB_055", "Beneficiary type label displayed", SH, "", "", "", "", "", "", "", ""},
            {"MB_056", "Account number label displayed", SH, "", "", "", "", "", "", "", ""},
            {"MB_057", "Beneficiary name label displayed", SH, "", "", "", "", "", "", "", ""},

            // =====================================================================
            // SECTION 3: MANAGE BENEFICIARY (MB_058 - MB_067)
            // =====================================================================
            {"MB_058", "Beneficiary list displayed", "", "", "", "", "", "", "", "", ""},
            {"MB_059", "All tab - shows all beneficiaries", "", "", "", "", "", "", "", "", ""},
            {"MB_060", "Shivalik tab - filter Shivalik", "", "", "", "", "", "", "", "", ""},
            {"MB_061", "Other Bank tab - filter Other Bank", "", "", "", "", "", "", "", "", ""},
            {"MB_062", "Search valid beneficiary name (positive)", "", "", "", "", "", "", "", SEARCH_VALID, ""},
            {"MB_063", "Search invalid name - no results (negative)", "", "", "", "", "", "", "", SEARCH_INVALID, ""},
            {"MB_064", "Filter icon displayed", "", "", "", "", "", "", "", "", ""},
            {"MB_065", "A-Z sort option displayed", "", "", "", "", "", "", "", "", ""},
            {"MB_066", "Cooling Period section message", "", "", "", "", "", "", "", "", MSG_COOL_NONE},
            {"MB_067", "No cooling period beneficiaries", "", "", "", "", "", "", "", "", MSG_COOL_NONE},

            // =====================================================================
            // SECTION 4: ADD OTHER BANK (MB_068 - MB_105)
            // =====================================================================
            // --- Other Bank form UI elements (MB_068 - MB_071) ---
            {"MB_068", "Select Other Bank radio", OB, "", "", "", "", "", "", "", ""},
            {"MB_069", "IFSC code input displayed", OB, "", "", "", "", "", "", "", ""},
            {"MB_070", "Search IFSC link displayed", OB, "", "", "", "", "", "", "", ""},
            {"MB_071", "Verify Beneficiary button displayed", OB, "", "", "", "", "", "", "", ""},

            // --- Positive: field entry (MB_072 - MB_078) ---
            {"MB_072", "Enter valid Other Bank account", OB, ACC3, "", "", "", "", "", "", ""},
            {"MB_073", "Enter matching Other Bank re-enter", OB, ACC3, ACC3, "", "", "", "", "", ""},
            {"MB_074", "Enter valid IFSC code PUNB0206500", OB, "", "", IFSC, "", "", "", "", ""},
            {"MB_075", "Invalid IFSC code INVALID1234 (negative)", OB, ACC3, ACC3, IFSC_INV, NM_O, "", "", "", ERR_IFSC},
            {"MB_076", "Enter valid Other Bank name", OB, "", "", "", NM_O, "", "", "", ""},
            {"MB_077", "Enter Other Bank nickname", OB, "", "", "", "", NK_O, "", "", ""},
            {"MB_078", "Click Verify Beneficiary button", OB, ACC3, "", IFSC, NM_O, "", "", "", ""},

            // --- Negative: validation errors (MB_079 - MB_083) ---
            {"MB_079", "Empty Other Bank form (negative)", OB, "", "", "", "", "", "", "", ERR_EMPTY},
            {"MB_080", "Only account filled Other Bank (negative)", OB, ACC3, "", "", "", "", "", "", ERR_REENTER_NAME},
            {"MB_081", "No IFSC code Other Bank (negative)", OB, ACC3, ACC3, "", NM_O, "", "", "", ERR_IFSC},
            {"MB_082", "Mismatched re-enter Other Bank (negative)", OB, ACC3, ACC_MM, "", "", "", "", "", ERR_MISMATCH},
            {"MB_083", "Invalid account format Other Bank (negative)", OB, ACC_INV, ACC_INV, IFSC, NM_O, "", "", "", ERR_FORMAT},

            // --- Positive: complete form -> confirm page (MB_084 - MB_089) ---
            {"MB_084", "Complete Other Bank form -> confirm page", OB, ACC3, ACC3, IFSC, NM_O, NK_O, "", "", CONFIRM_TITLE},
            {"MB_085", "Confirm OB - beneficiary name shown", OB, ACC3, ACC3, IFSC, NM_O, NK_O, "", "", NM_O},
            {"MB_086", "Confirm OB - account number shown", OB, ACC3, ACC3, IFSC, NM_O, NK_O, "", "", ACC3},
            {"MB_087", "Confirm OB - bank name shown", OB, ACC3, ACC3, IFSC, NM_O, NK_O, "", "", ""},
            {"MB_088", "Confirm OB - IFSC code shown", OB, ACC3, ACC3, IFSC, NM_O, NK_O, "", "", IFSC},
            {"MB_089", "Confirm OB - nickname shown", OB, ACC3, ACC3, IFSC, NM_O, NK_O, "", "", NK_O},

            // --- OTP page elements OB (MB_090 - MB_093) ---
            {"MB_090", "OTP input field OB", OB, ACC3, ACC3, IFSC, NM_O, NK_O, "", "", ""},
            {"MB_091", "OTP timer OB", OB, ACC3, ACC3, IFSC, NM_O, NK_O, "", "", ""},
            {"MB_092", "Resend OTP link OB", OB, ACC3, ACC3, IFSC, NM_O, NK_O, "", "", ""},
            {"MB_093", "SUBMIT disabled before OTP OB", OB, ACC3, ACC3, IFSC, NM_O, NK_O, "", "", ""},

            // --- Negative: OTP errors OB (MB_094 - MB_095) ---
            {"MB_094", "Invalid OTP OB 000000 (negative)", OB, ACC3, ACC3, IFSC, NM_O, NK_O, OTP_I, "", ERR_OTP},
            {"MB_095", "Wrong OTP 3x OB - locked (negative)", OB, ACC3, ACC3, IFSC, NM_O, NK_O, OTP_W, "", ERR_OTP_MAX},

            // --- Navigation OB (MB_096 - MB_097) ---
            {"MB_096", "Cancel confirm page OB", OB, ACC3, ACC3, IFSC, NM_O, NK_O, "", "", ""},
            {"MB_097", "Back arrow confirm page OB", OB, ACC3, ACC3, IFSC, NM_O, NK_O, "", "", ""},

            // --- Positive: successful add OB (MB_098 - MB_101) ---
            {"MB_098", "Valid OTP OB - added success", OB, ACC3, ACC3, IFSC, NM_O, NK_O, OTP_V, "", SUC_ADD},
            {"MB_099", "Success OB - cooling message", OB, ACC3, ACC3, IFSC, NM_O, NK_O, OTP_V, "", SUC_COOL},
            {"MB_100", "Done button OB success page", OB, ACC3, ACC3, IFSC, NM_O, NK_O, OTP_V, "", ""},
            {"MB_101", "Done button OB - return to list", OB, ACC3, ACC3, IFSC, NM_O, NK_O, OTP_V, "", ""},

            // --- Navigation OB add form (MB_102 - MB_103) ---
            {"MB_102", "CANCEL returns from OB add form", OB, "", "", "", "", "", "", "", ""},
            {"MB_103", "Back Arrow returns from OB add form", OB, "", "", "", "", "", "", "", ""},

            // --- Labels OB (MB_104 - MB_105) ---
            {"MB_104", "IFSC code label displayed OB", OB, "", "", "", "", "", "", "", ""},
            {"MB_105", "Search IFSC link click opens search", OB, "", "", "", "", "", "", "", ""},

            // =====================================================================
            // SECTION 5: BENEFICIARY EDIT (MB_106 - MB_125)
            // =====================================================================
            // --- Detail view UI (MB_106 - MB_112) ---
            {"MB_106", "Click first beneficiary - detail view", "", "", "", "", "", "", "", "", ""},
            {"MB_107", "Detail - beneficiary name displayed", "", "", "", "", "", "", "", "", ""},
            {"MB_108", "Detail - account number displayed", "", "", "", "", "", "", "", "", ""},
            {"MB_109", "Detail - Edit button displayed", "", "", "", "", "", "", "", "", ""},
            {"MB_110", "Detail - Delete button displayed", "", "", "", "", "", "", "", "", ""},
            {"MB_111", "Click Edit - edit form opens", "", "", "", "", "", "", "", "", ""},
            {"MB_112", "Edit form - nickname input displayed", "", "", "", "", "", "", "", "", ""},

            // --- Positive: edit nickname (MB_113 - MB_115) ---
            {"MB_113", "Edit nickname with valid value", "", "", "", "", "", NK_E, "", "", ""},
            {"MB_114", "Save after edit - OTP input shown", "", "", "", "", "", NK_E, "", "", ""},
            {"MB_115", "Valid OTP edit - success message", "", "", "", "", "", NK_E, OTP_V, "", SUC_EDIT},

            // --- Negative: edit OTP error (MB_116) ---
            {"MB_116", "Invalid OTP edit 000000 (negative)", "", "", "", "", "", NK_E, OTP_I, "", ERR_OTP},

            // --- Navigation: cancel/back from edit (MB_117 - MB_118) ---
            {"MB_117", "CANCEL on edit form - return detail", "", "", "", "", "", "", "", "", ""},
            {"MB_118", "Back Arrow on edit form - return detail", "", "", "", "", "", "", "", "", ""},

            // --- Negative: empty/special nickname (MB_119 - MB_120) ---
            {"MB_119", "Empty nickname save (negative)", "", "", "", "", "", "", "", "", ""},
            {"MB_120", "Special chars nickname Test@#$% (negative)", "", "", "", "", "", NK_SP, "", "", ""},

            // --- Edit form UI (MB_121 - MB_122) ---
            {"MB_121", "SAVE button displayed in edit form", "", "", "", "", "", "", "", "", ""},
            {"MB_122", "CANCEL button displayed in edit form", "", "", "", "", "", "", "", "", ""},

            // --- Edit OTP page elements (MB_123 - MB_124) ---
            {"MB_123", "Edit OTP timer displayed", "", "", "", "", "", NK_E, "", "", ""},
            {"MB_124", "Edit Resend OTP link displayed", "", "", "", "", "", NK_E, "", "", ""},

            // --- Positive: edit done return (MB_125) ---
            {"MB_125", "Edit Done - return to list success", "", "", "", "", "", NK_E, OTP_V, "", SUC_EDIT},

            // =====================================================================
            // SECTION 6: BENEFICIARY DELETE (MB_126 - MB_141)
            // =====================================================================
            // --- Delete dialog UI (MB_126 - MB_130) ---
            {"MB_126", "Click Delete - confirmation dialog", "", "", "", "", "", "", "", "", ""},
            {"MB_127", "Delete confirmation title displayed", "", "", "", "", "", "", "", "", ""},
            {"MB_128", "DELETE confirm button displayed", "", "", "", "", "", "", "", "", ""},
            {"MB_129", "CANCEL button on delete dialog", "", "", "", "", "", "", "", "", ""},
            {"MB_130", "CANCEL delete - return to detail view", "", "", "", "", "", "", "", "", ""},

            // --- Positive: delete with OTP (MB_131 - MB_132) ---
            {"MB_131", "DELETE confirm - OTP input shown", "", "", "", "", "", "", "", "", ""},
            {"MB_132", "Valid OTP delete - success message", "", "", "", "", "", "", OTP_V, "", SUC_DEL},

            // --- Negative: delete OTP errors (MB_133 - MB_134) ---
            {"MB_133", "Invalid OTP delete 000000 (negative)", "", "", "", "", "", "", OTP_I, "", ERR_OTP},
            {"MB_134", "Wrong OTP 3x delete - locked (negative)", "", "", "", "", "", "", OTP_W, "", ERR_OTP_MAX},

            // --- Delete OTP page elements (MB_135 - MB_136) ---
            {"MB_135", "Delete OTP timer displayed", "", "", "", "", "", "", "", "", ""},
            {"MB_136", "Delete Resend OTP link displayed", "", "", "", "", "", "", "", "", ""},

            // --- Positive: delete done/verify (MB_137 - MB_138) ---
            {"MB_137", "Delete Done - return to list", "", "", "", "", "", "", OTP_V, "", SUC_DEL},
            {"MB_138", "Deleted beneficiary removed from list", "", "", "", "", "", "", OTP_V, "", ""},

            // --- Navigation & final (MB_139 - MB_141) ---
            {"MB_139", "Back Arrow on delete OTP page", "", "", "", "", "", "", "", "", ""},
            {"MB_140", "Verify page after all operations", "", "", "", "", "", "", "", "", ""},
            {"MB_141", "Back Arrow navigates back from page", "", "", "", "", "", "", "", "", ""},

            // =====================================================================
            // EXTRA SCENARIOS: ADDITIONAL NEGATIVE & POSITIVE TESTS (MB_142 - MB_200)
            // =====================================================================

            // --- Shivalik: Different valid accounts from AccountInfo ---
            {"MB_142", "Add Shivalik with Account1 (saving, high balance)", SH, ACC1, ACC1, "", NM_S, NK_S, OTP_V, "", SUC_ADD},
            {"MB_143", "Add Shivalik with Account3 (current, 200)", SH, ACC3, ACC3, "", NM_S, "nick_cur200", OTP_V, "", SUC_ADD},
            {"MB_144", "Add Shivalik with Account4 (current, 500)", SH, ACC4, ACC4, "", NM_S, "nick_cur500", OTP_V, "", SUC_ADD},
            {"MB_145", "Add Shivalik with Account5 (saving, 0 balance)", SH, ACC5, ACC5, "", NM_S, "nick_zero", OTP_V, "", SUC_ADD},
            {"MB_146", "Add Shivalik with Account6 (current, 0 balance)", SH, ACC6, ACC6, "", NM_S, "nick_cur0", OTP_V, "", SUC_ADD},

            // --- Shivalik: Account number edge cases (negative) ---
            {"MB_147", "Short account number 12345 (negative)", SH, ACC_SHORT, ACC_SHORT, "", NM_S, NK_S, "", "", ERR_FORMAT},
            {"MB_148", "Long account number 20 digits (negative)", SH, ACC_LONG, ACC_LONG, "", NM_S, NK_S, "", "", ERR_FORMAT},
            {"MB_149", "Account with spaces (negative)", SH, "1000 1051 0088", "1000 1051 0088", "", NM_S, NK_S, "", "", ERR_FORMAT},
            {"MB_150", "Account with special chars (negative)", SH, "10001051@088", "10001051@088", "", NM_S, NK_S, "", "", ERR_FORMAT},
            {"MB_151", "Account all zeros (negative)", SH, "000000000000", "000000000000", "", NM_S, NK_S, "", "", ""},
            {"MB_152", "Re-enter empty but account filled (negative)", SH, ACC2, "", "", NM_S, NK_S, "", "", ERR_REENTER_NAME},

            // --- Shivalik: Beneficiary name edge cases ---
            {"MB_153", "Name with numbers (negative)", SH, ACC2, ACC2, "", "Test123", NK_S, "", "", ""},
            {"MB_154", "Name with special chars (negative)", SH, ACC2, ACC2, "", "Test@#$!", NK_S, "", "", ""},
            {"MB_155", "Very long name 50+ chars", SH, ACC2, ACC2, "", "This Is A Very Long Beneficiary Name For Testing Only", NK_S, "", "", ""},
            {"MB_156", "Single character name", SH, ACC2, ACC2, "", "A", NK_S, "", "", ""},
            {"MB_157", "Name with only spaces (negative)", SH, ACC2, ACC2, "", "   ", NK_S, "", "", ""},

            // --- Shivalik: Nickname edge cases ---
            {"MB_158", "Very long nickname 45 chars (negative)", SH, ACC2, ACC2, "", NM_S, NK_LONG, "", "", ""},
            {"MB_159", "Nickname with spaces", SH, ACC2, ACC2, "", NM_S, "nick name", "", "", ""},
            {"MB_160", "Nickname only numbers", SH, ACC2, ACC2, "", NM_S, "12345", "", "", ""},
            {"MB_161", "Nickname single character", SH, ACC2, ACC2, "", NM_S, "N", "", "", ""},
            {"MB_162", "Nickname empty (beneficiary added without nick)", SH, ACC2, ACC2, "", NM_S, "", OTP_V, "", ""},

            // --- Shivalik: OTP edge cases ---
            {"MB_163", "OTP with letters (negative)", SH, ACC2, ACC2, "", NM_S, NK_S, "ABCDEF", "", ERR_OTP},
            {"MB_164", "OTP too short 3 digits (negative)", SH, ACC2, ACC2, "", NM_S, NK_S, OTP_SHORT, "", ERR_OTP},
            {"MB_165", "OTP with spaces (negative)", SH, ACC2, ACC2, "", NM_S, NK_S, "12 34 56", "", ERR_OTP},
            {"MB_166", "OTP all same digits (negative)", SH, ACC2, ACC2, "", NM_S, NK_S, "999999", "", ERR_OTP},

            // --- Other Bank: Different valid accounts from AccountInfo ---
            {"MB_167", "Add OB with Account1 (saving, high balance)", OB, ACC1, ACC1, IFSC, NM_O, NK_O, OTP_V, "", SUC_ADD},
            {"MB_168", "Add OB with Account4 (current, 500)", OB, ACC4, ACC4, IFSC, NM_O, "ob_cur500", OTP_V, "", SUC_ADD},
            {"MB_169", "Add OB with Account5 (saving, 0 balance)", OB, ACC5, ACC5, IFSC, NM_O, "ob_zero", OTP_V, "", SUC_ADD},
            {"MB_170", "Add OB with Account6 (current, 0 balance)", OB, ACC6, ACC6, IFSC, NM_O, "ob_cur0", OTP_V, "", SUC_ADD},

            // --- Other Bank: IFSC code edge cases (negative) ---
            {"MB_171", "IFSC too short PUN (negative)", OB, ACC3, ACC3, IFSC_SHORT, NM_O, NK_O, "", "", ERR_IFSC},
            {"MB_172", "IFSC with special chars (negative)", OB, ACC3, ACC3, "PUNB@20650", NM_O, NK_O, "", "", ERR_IFSC},
            {"MB_173", "IFSC all numbers (negative)", OB, ACC3, ACC3, "12345678901", NM_O, NK_O, "", "", ERR_IFSC},
            {"MB_174", "IFSC empty (negative)", OB, ACC3, ACC3, "", NM_O, NK_O, "", "", ERR_IFSC},
            {"MB_175", "IFSC with spaces (negative)", OB, ACC3, ACC3, "PUNB 020650", NM_O, NK_O, "", "", ERR_IFSC},
            {"MB_176", "IFSC lowercase (negative)", OB, ACC3, ACC3, "punb0206500", NM_O, NK_O, "", "", ERR_IFSC},

            // --- Other Bank: Account edge cases (negative) ---
            {"MB_177", "OB short account (negative)", OB, ACC_SHORT, ACC_SHORT, IFSC, NM_O, NK_O, "", "", ERR_FORMAT},
            {"MB_178", "OB long account (negative)", OB, ACC_LONG, ACC_LONG, IFSC, NM_O, NK_O, "", "", ERR_FORMAT},
            {"MB_179", "OB account with letters (negative)", OB, "ABCDEFGHIJKL", "ABCDEFGHIJKL", IFSC, NM_O, NK_O, "", "", ERR_FORMAT},
            {"MB_180", "OB mismatched by 1 digit (negative)", OB, ACC3, "100012250036", "", NM_O, NK_O, "", "", ERR_MISMATCH},

            // --- Other Bank: Name edge cases ---
            {"MB_181", "OB name with special chars (negative)", OB, ACC3, ACC3, IFSC, "Test@Bank#", NK_O, "", "", ""},
            {"MB_182", "OB very long name", OB, ACC3, ACC3, IFSC, "Very Long Other Bank Beneficiary Name For Testing", NK_O, "", "", ""},
            {"MB_183", "OB name with numbers", OB, ACC3, ACC3, IFSC, "Beneficiary123", NK_O, "", "", ""},

            // --- Other Bank: OTP edge cases (negative) ---
            {"MB_184", "OB OTP with letters (negative)", OB, ACC3, ACC3, IFSC, NM_O, NK_O, "ABCDEF", "", ERR_OTP},
            {"MB_185", "OB OTP too short (negative)", OB, ACC3, ACC3, IFSC, NM_O, NK_O, OTP_SHORT, "", ERR_OTP},
            {"MB_186", "OB OTP expired after timer (negative)", OB, ACC3, ACC3, IFSC, NM_O, NK_O, "222222", "", ERR_OTP},

            // --- Search edge cases ---
            {"MB_187", "Search with special chars (negative)", "", "", "", "", "", "", "", "@#$%^", ""},
            {"MB_188", "Search with single char", "", "", "", "", "", "", "", "N", ""},
            {"MB_189", "Search with partial name", "", "", "", "", "", "", "", "Nit", ""},
            {"MB_190", "Search with full account number", "", "", "", "", "", "", "", ACC2, ""},
            {"MB_191", "Search with empty string", "", "", "", "", "", "", "", "", ""},
            {"MB_192", "Search with very long string (negative)", "", "", "", "", "", "", "", "ThisIsAVeryLongSearchStringThatShouldNotMatchAnything", ""},

            // --- Edit: Different nickname variations ---
            {"MB_193", "Edit nickname to numbers only", "", "", "", "", "", "99999", OTP_V, "", SUC_EDIT},
            {"MB_194", "Edit nickname with unicode chars", "", "", "", "", "", "TestUnicode", OTP_V, "", SUC_EDIT},
            {"MB_195", "Edit nickname very long (negative)", "", "", "", "", "", NK_LONG, "", "", ""},
            {"MB_196", "Edit nickname same as old (no change)", "", "", "", "", "", NK_S, OTP_V, "", SUC_EDIT},

            // --- Delete: Multiple beneficiary operations ---
            {"MB_197", "Delete after fresh add (positive flow)", "", "", "", "", "", "", OTP_V, "", SUC_DEL},
            {"MB_198", "Delete with expired OTP (negative)", "", "", "", "", "", "", "333333", "", ERR_OTP},
            {"MB_199", "Delete cancel then re-delete", "", "", "", "", "", "", OTP_V, "", SUC_DEL},
            {"MB_200", "Final page state after delete all", "", "", "", "", "", "", "", "", ""},
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
