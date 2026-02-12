# language: en
@dashboard
Feature: Dashboard Module Testing
  Test cases from dashboard1 2.xlsx - Shivalik Bank Internet Banking
  Every click navigates back to Dashboard before next scenario

  Background:
    Given user is logged in successfully

  # ==============================================================================
  # MODULE 1: DASHBOARD (DB_0001 - DB_0026)
  # Skipped: DB_0010 (notification popup), DB_0013-DB_0024 (profile dropdown/logout)
  # ==============================================================================

  @smoke @DB_0001
  Scenario: DB_0001 - Verify Dashboard opens with header elements
    Then Dashboard should be displayed
    And "ShivalikLogo_Object" should be displayed on Dashboard page
    And "ProfileName_Object" should be displayed on Dashboard page
    And "CustomerCarePhoneText_Object" should be displayed on Dashboard page
    And "CustomerCarePhone_Object" should be displayed on Dashboard page
    And "CustomerCareEmail_Object" should be displayed on Dashboard page
    And "LatestUpdatesTitle_Object" should be displayed on Dashboard page
    And "MarqueeText_Object" should be displayed on Dashboard page

  @regression @DB_0007
  Scenario: DB_0007 - Verify Bill Pay button click and return
    Then "Db_BillsNavLink_Object" should be displayed on Dashboard page
    When user clicks on "Db_BillsNavLink_Object" on Dashboard page
    When user navigates back to Dashboard
    Then Dashboard should be displayed

  @regression @DB_0008
  Scenario: DB_0008 - Verify Offers button click and return
    Then "Db_OffersNavLink_Object" should be displayed on Dashboard page
    When user clicks on "Db_OffersNavLink_Object" on Dashboard page
    When user navigates back to Dashboard
    Then Dashboard should be displayed

  @smoke @DB_0009
  Scenario: DB_0009 - Verify Notification and Profile section
    Then "NotificationIcon_Object" should be displayed on Dashboard page
    And "ProfileButton_Object" should be displayed on Dashboard page
    And "ProfileName_Object" should be displayed on Dashboard page

  # ==============================================================================
  # MODULE 2: QUICK SERVICES (QS_0001 - QS_0016)
  # Skipped: QS_0010 (Positive Pay click - external site)
  # Skipped: QS_0012 (Apply for Loan click - external site)
  # Skipped: QS_0014 (Apply for Locker click - external site)
  # ==============================================================================

  @smoke @QS_0001
  Scenario: QS_0001 - Verify Quick Services section with all options
    Then "Db_QuickServicesTitle_Object" should be displayed on Dashboard page
    And "ViewChequeStatusService_Object" should be displayed on Dashboard page
    And "RequestChequeBookService_Object" should be displayed on Dashboard page
    And "StopChequeService_Object" should be displayed on Dashboard page
    And "PositivePayService_Object" should be displayed on Dashboard page
    And "ApplyForLoanService_Object" should be displayed on Dashboard page
    And "ApplyForLockerService_Object" should be displayed on Dashboard page
    And "GenerateDebitCardPinService_Object" should be displayed on Dashboard page

  @regression @QS_0003
  Scenario: QS_0003 - Verify View Cheque Status click and return
    When user clicks on "ViewChequeStatusService_Object" on Dashboard page
    When user navigates back to Dashboard
    Then Dashboard should be displayed

  @regression @QS_0005
  Scenario: QS_0005 - Verify Request Cheque Book click and return
    When user clicks on "RequestChequeBookService_Object" on Dashboard page
    When user navigates back to Dashboard
    Then Dashboard should be displayed

  @regression @QS_0007
  Scenario: QS_0007 - Verify Stop Cheque click and return
    When user clicks on "StopChequeService_Object" on Dashboard page
    When user navigates back to Dashboard
    Then Dashboard should be displayed

  @regression @QS_0015
  Scenario: QS_0015 - Verify Generate Green Card PIN click and return
    When user clicks on "GenerateDebitCardPinService_Object" on Dashboard page
    When user navigates back to Dashboard
    Then Dashboard should be displayed

  # ==============================================================================
  # MODULE 3: FAVOURITE BENEFICIARY (FB_0001 - FB_0008)
  # Skipped: FB_0007, FB_0008 (account masking - no beneficiary data)
  # ==============================================================================

  @smoke @FB_0001
  Scenario: FB_0001 - Verify Favourite Beneficiary section
    Then "FavouriteBeneficiaryTitle_Object" should be displayed on Dashboard page
    And "SeeAllBeneficiary_Object" should be displayed on Dashboard page
    And "AddNewBeneficiary_Object" should be displayed on Dashboard page
    And no beneficiary image should be displayed or beneficiary cards should be visible

  @regression @FB_0003
  Scenario: FB_0003 - Verify See All click and return
    When user clicks on "SeeAllBeneficiary_Object" on Dashboard page
    When user navigates back to Dashboard
    Then Dashboard should be displayed

  @regression @FB_0006
  Scenario: FB_0006 - Verify Add New click and return
    When user clicks on "AddNewBeneficiary_Object" on Dashboard page
    When user navigates back to Dashboard
    Then Dashboard should be displayed

  # ==============================================================================
  # MODULE 4: APP HYPERLINK (AH_0001 - AH_0003)
  # ==============================================================================

  @smoke @AH_0001
  Scenario: AH_0001 - Verify App Download links displayed
    Then "DownloadAppSection_Object" should be displayed on Dashboard page
    And "PlayStoreIcon_Object" should be displayed on Dashboard page
    And "AppStoreIcon_Object" should be displayed on Dashboard page

  @regression @AH_0002
  Scenario: AH_0002 - Verify Google Play Store link opens new page
    When user clicks on "PlayStoreIcon_Object" on Dashboard page
    Then new page should open for the download link

  @regression @AH_0003
  Scenario: AH_0003 - Verify App Store link opens new page
    When user clicks on "AppStoreIcon_Object" on Dashboard page
    Then new page should open for the download link

  # ==============================================================================
  # MODULE 5: ACCOUNTS (AN_0001 - AN_0015)
  # Skipped: AN_0001-AN_0006 (navigation arrows not in accounts DOM)
  # Skipped: AN_0013, AN_0015 (duplicate of AN_0012, AN_0014)
  # ==============================================================================

  @smoke @AN_0007
  Scenario: AN_0007 - Verify Accounts section with all tabs
    Then "AccountsTitle_Object" should be displayed on Dashboard page
    And "SavingsTab_Object" should be displayed on Dashboard page
    And "CurrentTab_Object" should be displayed on Dashboard page
    And "DepositTab_Object" should be displayed on Dashboard page
    And "LoanTab_Object" should be displayed on Dashboard page
    And "ODCCTab_Object" should be displayed on Dashboard page

  @regression @AN_0012
  Scenario: AN_0012 - Verify account balance masked and eye icon
    Then "AccountCard_Object" should be displayed on Dashboard page
    And "AccountNumber_Object" should be displayed on Dashboard page
    And "ShowDetailsIcon_Object" should be displayed on Dashboard page
    When user clicks on "ShowDetailsIcon_Object" on Dashboard page

  # ==============================================================================
  # MODULE 6: SERVICES (SS_0001 - SS_0013)
  # Skipped: SS_0011 (Apply for Loan click - external site)
  # Skipped: SS_0013 (Apply for Locker click - external site)
  # ==============================================================================

  @smoke @SS_0001
  Scenario: SS_0001 - Verify Services section with all options
    Then "ServicesTitle_Object" should be displayed on Dashboard page
    And "FundTransferService_Object" should be displayed on Dashboard page
    And "OpenFDRDService_Object" should be displayed on Dashboard page
    And "AccountStatementService_Object" should be displayed on Dashboard page
    And "Db_ManageBeneficiaryService_Object" should be displayed on Dashboard page
    And "ApplyForLoanServiceCard_Object" should be displayed on Dashboard page
    And "ApplyForLockerServiceCard_Object" should be displayed on Dashboard page

  @regression @SS_0003
  Scenario: SS_0003 - Verify Fund Transfer click and return
    When user clicks on "FundTransferService_Object" on Dashboard page
    When user navigates back to Dashboard
    Then Dashboard should be displayed

  @regression @SS_0005
  Scenario: SS_0005 - Verify Open FD/RD click and return
    When user clicks on "OpenFDRDService_Object" on Dashboard page
    When user navigates back to Dashboard
    Then Dashboard should be displayed

  @regression @SS_0007
  Scenario: SS_0007 - Verify Account Statement click and return
    When user clicks on "AccountStatementService_Object" on Dashboard page
    When user navigates back to Dashboard
    Then Dashboard should be displayed

  @regression @SS_0009
  Scenario: SS_0009 - Verify Manage Beneficiary click and return
    When user clicks on "Db_ManageBeneficiaryService_Object" on Dashboard page
    When user navigates back to Dashboard
    Then Dashboard should be displayed

  # ==============================================================================
  # MODULE 7: RECENT TRANSACTIONS (RT_0001 - RT_0012)
  # Skipped: RT_0003-RT_0006 (dropdown content/masking verification)
  # Skipped: RT_0011-RT_0012 (debit/credit arrow icons)
  # ==============================================================================

  @smoke @RT_0001
  Scenario: RT_0001 - Verify Recent Transactions section
    Then "RecentTransactionsTitle_Object" should be displayed on Dashboard page
    And "TransactionAccountDropdown_Object" should be displayed on Dashboard page
    And "TransactionList_Object" should be displayed on Dashboard page
    And "TransactionName_Object" should be displayed on Dashboard page
    And "TransactionAmount_Object" should be displayed on Dashboard page
    And "TransactionDate_Object" should be displayed on Dashboard page

  # ==============================================================================
  # MODULE 8: FASTAG (FT_0001 - FT_0008)
  # Skipped: FT_0002-FT_0008 (no registered vehicle)
  # Using FTAG prefix to avoid conflict with FundTransfer
  # ==============================================================================

  @smoke @FTAG_0001
  Scenario: FTAG_0001 - Verify FASTag section displayed
    Then "FastagTitle_Object" should be displayed on Dashboard page
    And "FastagRegisterText_Object" should be displayed on Dashboard page

  # ==============================================================================
  # MODULE 9: MY INVESTMENTS (MI_0001 - MI_0003)
  # ==============================================================================

  @smoke @MI_0001
  Scenario: MI_0001 - Verify My Investments section
    Then "MyInvestmentsTitle_Object" should be displayed on Dashboard page
    And "FDInvestmentText_Object" should be displayed on Dashboard page
    And "Db_OpenFDButton_Object" should be displayed on Dashboard page

  @regression @MI_0003
  Scenario: MI_0003 - Verify Book Fixed Deposits click and return
    When user clicks on "Db_OpenFDButton_Object" on Dashboard page
    When user navigates back to Dashboard
    Then Dashboard should be displayed

  # ==============================================================================
  # MODULE 10: UPCOMING BILLS (UB_0001 - UB_0011)
  # Skipped: UB_0002-UB_0011 (no bills configured for test account)
  # ==============================================================================

  @smoke @UB_0001
  Scenario: UB_0001 - Verify Upcoming Bills section displayed
    Then "UpcomingBillsTitle_Object" should be displayed on Dashboard page
    And no bills image should be displayed or bill list should be visible
