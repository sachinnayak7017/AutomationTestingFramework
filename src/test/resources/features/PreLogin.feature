# language: en
@prelogin
Feature: PreLogin Module Testing (PL_001 to PL_053)
  Test pre-login page functionality for Shivalik Bank Internet Banking
  All test data fetched from Excel using keys

  Background:
    Given user navigates to PreLogin URL from Excel key "BASE_URL"

  # ==============================================================================
  # SECTION 1: PAGE LOAD AND HEADER LOGO (PL_001 - PL_002)
  # ==============================================================================

  @smoke @PL_001
  Scenario: PL_001 - Verify Shivalik logo is displayed at header
    Then Shivalik logo should be displayed at header

  @smoke @PL_002
  Scenario: PL_002 - Verify Pre-Login page opens successfully
    Then Pre-Login page should be displayed

  # ==============================================================================
  # SECTION 2: HEADER NAVIGATION - CONTACT US (PL_003 - PL_004)
  # ==============================================================================

  @smoke @PL_003
  Scenario: PL_003 - Verify Contact Us button is displayed and clickable at header
    Then Contact Us button should be displayed at header
    And Contact Us button should be clickable

  @regression @PL_004
  Scenario: PL_004 - Verify redirection to Contact Us page
    When user clicks on Contact Us button
    Then user should be redirected to Contact Us page

  # ==============================================================================
  # SECTION 3: HEADER NAVIGATION - APPLY NOW (PL_005 - PL_006)
  # ==============================================================================

  @smoke @PL_005
  Scenario: PL_005 - Verify Apply Now button is displayed and clickable at header
    Then Apply Now button should be displayed at header
    And Apply Now button should be clickable

  @regression @PL_006
  Scenario: PL_006 - Verify redirection to Apply for loan page
    When user clicks on Apply Now button
    Then user should be redirected to Apply for loan page

  # ==============================================================================
  # SECTION 4: HEADER NAVIGATION - BRANCH LOCATOR (PL_007 - PL_008)
  # ==============================================================================

  @smoke @PL_007
  Scenario: PL_007 - Verify Branch Locator button is displayed and clickable at header
    Then Branch Locator button should be displayed at header
    And Branch Locator button should be clickable

  @regression @PL_008
  Scenario: PL_008 - Verify redirection to Branch Locator page
    When user clicks on Branch Locator button
    Then user should be redirected to Branch Locator page

  # ==============================================================================
  # SECTION 5: HEADER NAVIGATION - LANGUAGE (PL_009 - PL_011)
  # ==============================================================================

  @smoke @PL_009
  Scenario: PL_009 - Verify Language button is displayed and clickable at header
    Then Language button should be displayed at header
    And Language button should be clickable

  @regression @PL_010
  Scenario: PL_010 - Verify Go to back home button displays after clicking Language
    When user clicks on Language button
    Then Go to back home button should be displayed

  @regression @PL_011
  Scenario: PL_011 - Verify Go to home button redirects to home page
    When user clicks on Language button
    And user clicks on Go to home button
    Then user should be redirected to home page

  # ==============================================================================
  # SECTION 6: HEADER NAVIGATION - REGISTER (PL_012 - PL_013)
  # ==============================================================================

  @smoke @PL_012
  Scenario: PL_012 - Verify Register button is displayed and clickable at header
    Then Register button should be displayed at header
    And Register button should be clickable

  @regression @PL_013
  Scenario: PL_013 - Verify redirection to Register page
    When user clicks on Register button
    Then user should be redirected to Register Form

  # ==============================================================================
  # SECTION 7: QUICK SERVICES - RECHARGE & BILLS (PL_014 - PL_016)
  # ==============================================================================

  @smoke @PL_014
  Scenario: PL_014 - Verify Recharge and Bills icon and text are displayed
    Then Recharge and Bills icon should be displayed
    And Recharge and Bills text should be displayed

  @regression @PL_015
  Scenario: PL_015 - Verify Recharge and Bills click redirects user
    When user clicks on Recharge and Bills service
    Then user should be redirected to  Register Form

  @regression @PL_016
  Scenario: PL_016 - Verify new page opens for Recharge and Bills service
    When user clicks on Recharge and Bills service
    Then new page should open for the service

  # ==============================================================================
  # SECTION 8: QUICK SERVICES - OPEN FIXED DEPOSIT (PL_017 - PL_019)
  # ==============================================================================

  @smoke @PL_017
  Scenario: PL_017 - Verify Open Fixed Deposit icon and text are displayed
    Then Open Fixed Deposit icon should be displayed
    And Open Fixed Deposit text should be displayed

  @regression @PL_018
  Scenario: PL_018 - Verify Open Fixed Deposit click redirects user
    When user clicks on Open Fixed Deposit service
    Then user should be redirected to Register Form

  @regression @PL_019
  Scenario: PL_019 - Verify new page opens for Open Fixed Deposit service
    When user clicks on Open Fixed Deposit service
    Then new page should open for the service

  # ==============================================================================
  # SECTION 9: QUICK SERVICES - FINANCIAL CALCULATOR (PL_020 - PL_025)
  # ==============================================================================

  @smoke @PL_020
  Scenario: PL_020 - Verify Financial Calculator icon and text are displayed
    Then Financial Calculator icon should be displayed
    And Financial Calculator text should be displayed

  @regression @PL_021
  Scenario: PL_021 - Verify Financial Calculator click redirects user
    When user clicks on Financial Calculator service
    Then user should be redirected to Financial Calculator page

  @regression @PL_022
  Scenario: PL_022 - Verify Loan and Deposit options are displayed on Financial Calculator
    When user clicks on Financial Calculator service
    Then Loan option should be displayed
    And Deposit option should be displayed

  @regression @PL_023
  Scenario: PL_023 - Verify user can select Deposit option
    When user clicks on Financial Calculator service
    And user selects Deposit option
    Then Deposit option should be selected

  @regression @PL_024
  Scenario: PL_024 - Verify Fixed and Recurring Deposit buttons on Deposit selection
    When user clicks on Financial Calculator service
    And user selects Deposit option
    Then Fixed Deposit button should be displayed
    And Recurring Deposit button should be displayed

  @regression @PL_025
  Scenario: PL_025 - Verify new page opens for Financial Calculator service
    When user clicks on Financial Calculator service
    Then new page should open for the service

  # ==============================================================================
  # SECTION 10: QUICK SERVICES - DEPOSIT RATES (PL_026 - PL_028)
  # ==============================================================================

  @smoke @PL_026
  Scenario: PL_026 - Verify Deposit Rates icon and text are displayed
    Then Deposit Rates icon should be displayed
    And Deposit Rates text should be displayed

  @regression @PL_027
  Scenario: PL_027 - Verify Deposit Rates click redirects user
    When user clicks on Deposit Rates service
    Then user should be redirected to new page

  @regression @PL_028
  Scenario: PL_028 - Verify new page opens for Deposit Rates service
    When user clicks on Deposit Rates service
    Then new page should open for the service

  # ==============================================================================
  # SECTION 11: QUICK SERVICES - OFFERS (PL_029 - PL_031)
  # ==============================================================================

  @smoke @PL_029
  Scenario: PL_029 - Verify Offers icon and text are displayed
    Then Offers icon should be displayed
    And Offers text should be displayed

  @regression @PL_030
  Scenario: PL_030 - Verify Offers click redirects user
    When user clicks on Offers service
    Then user should be redirected to new page

  @regression @PL_031
  Scenario: PL_031 - Verify new page opens for Offers service
    When user clicks on Offers service
    Then new page should open for the service

  # ==============================================================================
  # SECTION 12: QUICK SERVICES - FAQ (PL_032 - PL_034)
  # ==============================================================================

  @smoke @PL_032
  Scenario: PL_032 - Verify FAQ icon and text are displayed
    Then FAQ icon should be displayed
    And FAQ text should be displayed

  @regression @PL_033
  Scenario: PL_033 - Verify FAQ click redirects user
    When user clicks on FAQ service
    Then user should be redirected to FAQ page

  @regression @PL_034
  Scenario: PL_034 - Verify new page opens for FAQ service
    When user clicks on FAQ service
    Then new page should open for the service

  # ==============================================================================
  # SECTION 13: LOGIN FORM - USER ID (PL_035 - PL_038)
  # ==============================================================================

  @smoke @PL_035
  Scenario: PL_035 - Verify user can enter User ID in the field
    When user enters User ID from Excel key "USER_ID_SAMPLE"
    Then User ID should be entered in the field

  @regression @PL_036
  Scenario: PL_036 - Verify User ID field accepts various formats
    When user enters User ID from Excel key "USER_ID_VALID"
    Then User ID should be accepted
    When user clears User ID field
    And user enters User ID from Excel key "USER_ID_ALL_CHARS"
    Then User ID should be accepted

  @smoke @PL_037
  Scenario: PL_037 - Verify Remember User ID checkbox functionality
    When user clicks on Remember User ID checkbox
    Then Remember User ID checkbox should be checked

  @regression @PL_038
  Scenario: PL_038 - Verify Forgot User ID link works
    When user clicks on Forgot User ID link
    Then Forgot password form should be displayed

  # ==============================================================================
  # SECTION 14: LOGIN FORM - PASSWORD (PL_039 - PL_043)
  # ==============================================================================

  @smoke @PL_039
  Scenario: PL_039 - Verify user can enter Password in the field
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks Proceed button
    And user enters Password from Excel key "PASSWORD_SAMPLE"
    Then Password should be entered in the field

  @regression @PL_040
  Scenario: PL_040 - Verify Password field is masked by default
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks Proceed button
    And user enters Password from Excel key "PASSWORD_SAMPLE"
    Then Password field should be masked

  @regression @PL_041
  Scenario: PL_041 - Verify eye button unmasks the Password
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks Proceed button
    And user enters Password from Excel key "PASSWORD_SAMPLE"
    And user clicks on eye button
    Then Password should be displays password value in passwrod field 

  @regression @PL_042
  Scenario: PL_042 - Verify onscreen keyboard displays on keyboard icon click
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks Proceed button
    And user clicks on keyboard icon
    Then onscreen keyboard should be displayed

  @regression @PL_043
  Scenario: PL_043 - Verify Forgot Password link works
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks Proceed button
    Then user Forgot Password page should be displayed not click
    
  # ==============================================================================
  # SECTION 15: LOGIN FORM - CHECKBOXES (PL_044, PL_048)
  # ==============================================================================

  @regression @PL_044
  Scenario: PL_044 - Verify Access image and Message checkbox functionality
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks Proceed button
    Then Access image and Message checkbox should be unchecked

  @smoke @PL_048
  Scenario: PL_048 - Verify Terms and Conditions checkbox functionality
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks Proceed button
    And user clicks on Access image and Message checkbox
    Then Terms and Conditions checkbox should be checked

  # ==============================================================================
  # SECTION 16: LOGIN FORM - CAPTCHA (PL_045)
  # Note: Only testing field entry, not actual captcha validation
  # ==============================================================================

  @smoke @PL_045
  Scenario: PL_045 - Verify user can enter Captcha in the field
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks Proceed button
    Then Captcha should already  in the field

  # ==============================================================================
  # SECTION 17: LOGIN BUTTON (PL_050 - PL_052)
  # ==============================================================================

  @smoke @PL_050
  Scenario: PL_050 - Verify Login button is displayed and clickable
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks Proceed button
    And user clicks on Access image and Message checkbox
    Then Login button should be displayed
    And Login button should be clickable

  @regression @PL_051
  Scenario: PL_051 - Verify error message for blank fields
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks Proceed button
    And user clicks on Access image and Message checkbox
    And user clicks on Login button
    Then error message should be displayed for blank fields

  @regression @PL_052
  Scenario: PL_052 - Verify error message for unchecked checkboxes
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks Proceed button
    And user enters Password from Excel key "PASSWORD_VALID"
    Then user should not be displays  Login button 
    

  # ==============================================================================
  # SECTION 18: CAPTCHA-DEPENDENT TESTS (PL_046, PL_047, PL_049, PL_053)
  # These require real captcha - marked as @manual for skip during automation
  # ==============================================================================

  @manual @skip @PL_046
  Scenario: PL_046 - [MANUAL] Verify error for incorrect captcha
    # Requires real captcha to test invalid captcha scenario
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks Proceed button
    And user enters Password from Excel key "PASSWORD_VALID"
    And user enters Captcha from Excel key "CAPTCHA_INVALID"
    And user clicks on Access image and Message checkbox
    And user clicks on Terms and Conditions checkbox
    And user clicks on Login button
    Then error message should be displayed for invalid captcha

  @manual @skip @PL_047
  Scenario: PL_047 - [MANUAL] Verify error for invalid credentials
    # Requires real captcha to test invalid password scenario
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks Proceed button
    And user enters Password from Excel key "PASSWORD_WRONG"
    And user enters Captcha from Excel key "CAPTCHA_VALID"
    And user clicks on Access image and Message checkbox
    And user clicks on Terms and Conditions checkbox
    And user clicks on Login button
    Then error message should be displayed for invalid credentials

  @manual @skip @PL_049
  Scenario: PL_049 - [MANUAL] Verify login blocked when T&C unchecked
    # Requires real captcha entry
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks Proceed button
    And user enters Password from Excel key "PASSWORD_VALID"
    And user enters Captcha from Excel key "CAPTCHA_VALID"
    And user clicks on Access image and Message checkbox
    And user clicks on Login button
    Then error message should be displayed for unchecked T&C

  @regression @PL_053
  Scenario: PL_053 - Verify successful login with correct credentials
    When user enters User ID from Excel key "USER_ID_VALID"
    And user clicks Proceed button
    And user enters Password from Excel key "PASSWORD_VALID"
    And user Captcha fields should be displays with predifind captcha
    And user clicks on Access image and Message checkbox
    And user clicks on Login button
    Then user should login successfully
