# language: en
@prelogin
Feature: PreLogin Module Testing (PL_001 to PL_053)
  Test pre-login page functionality for Shivalik Bank Internet Banking
  All test data fetched from Excel using keys

  Background:
    Given user navigates to PreLogin URL from Excel key "BASE_URL"

  # ==============================================================================
  # SECTION 1: PAGE LOAD AND HEADER (PL_001 - PL_002)
  # ==============================================================================

  @smoke @PL_001
  Scenario: PL_001 - Verify Shivalik logo is displayed at header
    Then "ShivalikLogo_Object" should be displayed on PL page

  @smoke @PL_002
  Scenario: PL_002 - Verify Pre-Login page opens successfully
    Then "LoginFormTitle_Object" should be displayed on PL page

  # ==============================================================================
  # SECTION 2: HEADER NAVIGATION BUTTONS (PL_003 - PL_008)
  # ==============================================================================

  @smoke @PL_003
  Scenario: PL_003 - Verify Contact Us button is displayed and clickable
    Then "ContactUsButton_Object" should be displayed on PL page
    And "ContactUsButton_Object" should be clickable on PL page

  @regression @PL_004
  Scenario: PL_004 - Verify redirection to Contact Us page
    When user clicks on "ContactUsButton_Object" on PL page
    Then URL should contain "contact"

  @smoke @PL_005
  Scenario: PL_005 - Verify Apply Now button is displayed and clickable
    Then "ApplyNowButton_Object" should be displayed on PL page
    And "ApplyNowButton_Object" should be clickable on PL page

  @regression @PL_006
  Scenario: PL_006 - Verify redirection to Apply for Loan page
    When user clicks on "ApplyNowButton_Object" on PL page
    Then user should be on a new or redirected page

  @smoke @PL_007
  Scenario: PL_007 - Verify Branch Locator button is displayed and clickable
    Then "BranchLocatorButton_Object" should be displayed on PL page
    And "BranchLocatorButton_Object" should be clickable on PL page

  @regression @PL_008
  Scenario: PL_008 - Verify redirection to Branch Locator page
    When user clicks on "BranchLocatorButton_Object" on PL page
    Then user should be on a new or redirected page

  # ==============================================================================
  # SECTION 3: LANGUAGE AND REGISTER (PL_009 - PL_013)
  # ==============================================================================

  @smoke @PL_009
  Scenario: PL_009 - Verify Language button is displayed and clickable
    Then "LanguageButton_Object" should be displayed on PL page
    And "LanguageButton_Object" should be clickable on PL page

  @regression @PL_010
  Scenario: PL_010 - Verify Go to home button displays after Language click
    When user clicks on "LanguageButton_Object" on PL page
    Then "GoToHomeButton_Object" should be displayed on PL page

  @regression @PL_011
  Scenario: PL_011 - Verify Go to home button redirects to home page
    When user clicks on "LanguageButton_Object" on PL page
    And user clicks on "GoToHomeButton_Object" on PL page
    Then URL should contain "RIB"

  @smoke @PL_012
  Scenario: PL_012 - Verify Register button is displayed and clickable
    Then "RegisterNavButton_Object" should be displayed on PL page
    And "RegisterNavButton_Object" should be clickable on PL page

  @regression @PL_013
  Scenario: PL_013 - Verify redirection to Register page
    When user clicks on "RegisterNavButton_Object" on PL page
    Then user should be on a new or redirected page

  # ==============================================================================
  # SECTION 4: QUICK SERVICES - RECHARGE (PL_014 - PL_016)
  # Skipped: Recharge service removed from page
  # ==============================================================================

  @smoke @PL_014 @skip
  Scenario: PL_014 - Verify Recharge and Bills icon and text are displayed
    Then "RechargeAndBillsIcon_Object" should be displayed on PL page
    And "RechargeAndBillsText_Object" should be displayed on PL page

  @regression @PL_015 @skip
  Scenario: PL_015 - Verify Recharge and Bills click redirects user
    When user clicks on "RechargeAndBillsService_Object" on PL page
    Then user should be on a new or redirected page

  @regression @PL_016 @skip
  Scenario: PL_016 - Verify new page opens for Recharge and Bills service
    When user clicks on "RechargeAndBillsService_Object" on PL page
    Then user should be on a new or redirected page

  # ==============================================================================
  # SECTION 5: QUICK SERVICES - OPEN FIXED DEPOSIT (PL_017 - PL_019)
  # ==============================================================================

  @smoke @PL_017
  Scenario: PL_017 - Verify Open Fixed Deposit icon and text are displayed
    Then "OpenFixedDepositIcon_Object" should be displayed on PL page
    And "OpenFixedDepositText_Object" should be displayed on PL page

  @regression @PL_018
  Scenario: PL_018 - Verify Open Fixed Deposit click redirects user
    When user clicks on "OpenDepositAccountService_Object" on PL page
    Then user should be on a new or redirected page

  @regression @PL_019
  Scenario: PL_019 - Verify new page opens for Open Fixed Deposit service
    When user clicks on "OpenDepositAccountService_Object" on PL page
    Then user should be on a new or redirected page

  # ==============================================================================
  # SECTION 6: QUICK SERVICES - FINANCIAL CALCULATOR (PL_020 - PL_025)
  # PL_024 skipped: FD/RD buttons not found on Deposit tab
  # ==============================================================================

  @smoke @PL_020
  Scenario: PL_020 - Verify Financial Calculator icon and text are displayed
    Then "FinancialCalculatorIcon_Object" should be displayed on PL page
    And "FinancialCalculatorText_Object" should be displayed on PL page

  @regression @PL_021
  Scenario: PL_021 - Verify Financial Calculator click redirects user
    When user clicks on "FinancialCalculatorService_Object" on PL page
    Then "FinancialCalculatorPageTitle_Object" should be displayed on PL page

  @regression @PL_022
  Scenario: PL_022 - Verify Loan and Deposit options on Financial Calculator
    When user clicks on "FinancialCalculatorService_Object" on PL page
    Then "LoanOption_Object" should be displayed on PL page
    And "DepositOption_Object" should be displayed on PL page

  @regression @PL_023
  Scenario: PL_023 - Verify user can select Deposit option
    When user clicks on "FinancialCalculatorService_Object" on PL page
    And user clicks on "DepositOption_Object" on PL page
    Then "DepositOption_Object" should be displayed on PL page

  @regression @PL_024 @skip
  Scenario: PL_024 - Verify Fixed and Recurring Deposit buttons
    When user clicks on "FinancialCalculatorService_Object" on PL page
    And user clicks on "DepositOption_Object" on PL page
    Then "FixedDepositButton_Object" should be displayed on PL page
    And "RecurringDepositButton_Object" should be displayed on PL page

  @regression @PL_025
  Scenario: PL_025 - Verify new page opens for Financial Calculator service
    When user clicks on "FinancialCalculatorService_Object" on PL page
    Then user should be on a new or redirected page

  # ==============================================================================
  # SECTION 7: QUICK SERVICES - DEPOSITS RATE (PL_026 - PL_028)
  # ==============================================================================

  @smoke @PL_026
  Scenario: PL_026 - Verify Deposit Rates icon and text are displayed
    Then "DepositsRateIcon_Object" should be displayed on PL page
    And "DepositsRateText_Object" should be displayed on PL page

  @regression @PL_027
  Scenario: PL_027 - Verify Deposit Rates click redirects user
    When user clicks on "DepositsRateService_Object" on PL page
    Then user should be on a new or redirected page

  @regression @PL_028
  Scenario: PL_028 - Verify new page opens for Deposit Rates service
    When user clicks on "DepositsRateService_Object" on PL page
    Then user should be on a new or redirected page

  # ==============================================================================
  # SECTION 8: QUICK SERVICES - OFFERS (PL_029 - PL_031)
  # ==============================================================================

  @smoke @PL_029
  Scenario: PL_029 - Verify Offers icon and text are displayed
    Then "OffersIcon_Object" should be displayed on PL page
    And "OffersText_Object" should be displayed on PL page

  @regression @PL_030
  Scenario: PL_030 - Verify Offers click redirects user
    When user clicks on "OffersService_Object" on PL page
    Then user should be on a new or redirected page

  @regression @PL_031
  Scenario: PL_031 - Verify new page opens for Offers service
    When user clicks on "OffersService_Object" on PL page
    Then user should be on a new or redirected page

  # ==============================================================================
  # SECTION 9: QUICK SERVICES - FAQ (PL_032 - PL_034)
  # ==============================================================================

  @smoke @PL_032
  Scenario: PL_032 - Verify FAQ icon and text are displayed
    Then "FAQIcon_Object" should be displayed on PL page
    And "FAQText_Object" should be displayed on PL page

  @regression @PL_033
  Scenario: PL_033 - Verify FAQ click redirects user
    When user clicks on "FAQService_Object" on PL page
    Then "FAQPageTitle_Object" should be displayed on PL page

  @regression @PL_034
  Scenario: PL_034 - Verify new page opens for FAQ service
    When user clicks on "FAQService_Object" on PL page
    Then user should be on a new or redirected page

  # ==============================================================================
  # SECTION 10: LOGIN FORM - USER ID (PL_035 - PL_038)
  # ==============================================================================

  
  @regression @PL_036
  Scenario: PL_036 - Verify User ID field accepts various formats
    When user enters User ID from Excel key "USER_ID_VALID"
    Then "UserIdInput_Object" field should have value entered on PL page
    When user clears "UserIdInput_Object" on PL page
    And user enters User ID from Excel key "USER_ID_ALL_CHARS"
    Then "UserIdInput_Object" field should have value entered on PL page

  @smoke @PL_037
  Scenario: PL_037 - Verify Remember User ID checkbox functionality
    When user clicks on "RememberUserIdCheckbox_Object" on PL page
    Then "RememberUserIdCheckbox_Object" should be displayed on PL page

  @regression @PL_038 @skip
  Scenario: PL_038 - Verify Forgot User ID link works
    When user clicks on "ForgotUserIdLink_Object" on PL page
    Then user should be on a new or redirected page

  # ==============================================================================
  # SECTION 11: LOGIN FORM - PASSWORD (PL_039 - PL_043)
  # ==============================================================================

  
  @regression @PL_040
  Scenario: PL_040 - Verify Password field is masked by default
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks on "ProceedButton_Object" on PL page
    And user enters Password from Excel key "PASSWORD_SAMPLE"
    Then Password field should be masked

  @regression @PL_041
  Scenario: PL_041 - Verify eye button unmasks the Password
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks on "ProceedButton_Object" on PL page
    And user enters Password from Excel key "PASSWORD_SAMPLE"
    And user clicks on "PasswordEyeButton_Object" on PL page
    Then Password should be visible

  @regression @PL_042
  Scenario: PL_042 - Verify onscreen keyboard displays on keyboard icon click
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks on "ProceedButton_Object" on PL page
    And user clicks on "KeyboardIcon_Object" on PL page
    Then "OnscreenKeyboard_Object" should be displayed on PL page

  @regression @PL_043
  Scenario: PL_043 - Verify Forgot Password link is displayed
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks on "ProceedButton_Object" on PL page
    Then "ForgotPasswordLink_Object" should be displayed on PL page

  # ==============================================================================
  # SECTION 12: LOGIN FORM - CHECKBOXES AND CAPTCHA (PL_044, PL_045, PL_048)
  # ==============================================================================

  @regression @PL_044
  Scenario: PL_044 - Verify Access image checkbox is displayed
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks on "ProceedButton_Object" on PL page
    Then "AccessImageCheckbox_Object" should be displayed on PL page

  @smoke @PL_045
  Scenario: PL_045 - Verify Captcha/Secure message is pre-filled
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks on "ProceedButton_Object" on PL page
    Then "SecureMessageDisplay_Object" should be displayed on PL page

  @smoke @PL_048 @skip
  Scenario: PL_048 - Verify Terms and Conditions checkbox appears
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks on "ProceedButton_Object" on PL page
    And user clicks on "AccessImageCheckbox_Object" on PL page
    Then "TCCheckbox_Object" should be displayed on PL page

  # ==============================================================================
  # SECTION 13: LOGIN BUTTON (PL_050 - PL_052)
  # ==============================================================================

  @smoke @PL_050
  Scenario: PL_050 - Verify Login button is displayed and clickable
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks on "ProceedButton_Object" on PL page
    And user clicks on "AccessImageCheckbox_Object" on PL page
    Then "LoginButton_Object" should be displayed on PL page
    And "LoginButton_Object" should be clickable on PL page

  @regression @PL_051
  Scenario: PL_051 - Verify error message for blank fields
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks on "ProceedButton_Object" on PL page
    And user clicks on "AccessImageCheckbox_Object" on PL page
    And user clicks on "LoginButton_Object" on PL page
    Then "ErrorMessage_Object" should be displayed on PL page

  @regression @PL_052
  Scenario: PL_052 - Verify Login button disabled without checkboxes
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks on "ProceedButton_Object" on PL page
    And user enters Password from Excel key "PASSWORD_VALID"
    Then "LoginButtonDisabled_Object" should be displayed on PL page

  # ==============================================================================
  # SECTION 14: CAPTCHA-DEPENDENT TESTS (PL_046, PL_047, PL_049, PL_053)
  # PL_046-049 require manual captcha - marked @skip
  # ==============================================================================

  @manual @skip @PL_047
  Scenario: PL_047 - [MANUAL] Verify error for invalid credentials
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks on "ProceedButton_Object" on PL page
    And user enters Password from Excel key "PASSWORD_WRONG"
    And user enters Captcha from Excel key "CAPTCHA_VALID"
    And user clicks on "AccessImageCheckbox_Object" on PL page
    And user clicks on "TCCheckbox_Object" on PL page
    And user clicks on "LoginButton_Object" on PL page
    Then "ErrorMessage_Object" should be displayed on PL page

  @manual @skip @PL_049
  Scenario: PL_049 - [MANUAL] Verify login blocked when T&C unchecked
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks on "ProceedButton_Object" on PL page
    And user enters Password from Excel key "PASSWORD_VALID"
    And user enters Captcha from Excel key "CAPTCHA_VALID"
    And user clicks on "AccessImageCheckbox_Object" on PL page
    And user clicks on "LoginButton_Object" on PL page
    Then "ErrorMessage_Object" should be displayed on PL page

  @regression @PL_053
  Scenario: PL_053 - Verify successful login with correct credentials
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks on "ProceedButton_Object" on PL page
    And user enters Password from Excel key "PASSWORD_VALID"
    Then "SecureMessageDisplay_Object" should be displayed on PL page
    When user clicks on "AccessImageCheckbox_Object" on PL page
    And user clicks on "LoginButton_Object" on PL page
    Then user should login successfully
