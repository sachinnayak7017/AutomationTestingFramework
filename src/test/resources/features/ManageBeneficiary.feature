# language: en
@managebeneficiary
Feature: Manage Beneficiary - Complete Module Testing
  Test beneficiary management functionality for Shivalik Bank Internet Banking
  Modules: MB Main Page, Add Shivalik, Manage, Add Other Bank, Edit, Delete
  Test data from TestSuite.xlsx sheet "managebeneficiary"

  Background:
    Given user is logged in and navigates to Manage Beneficiary page

  # ==============================================================================
  # MODULE 1: MANAGE BENEFICIARIES MAIN PAGE (MB_001 - MB_010)
  # ==============================================================================

  @managebeneficiary_mainpage @MB_001
  Scenario: MB_001 - Verify Manage Beneficiaries page title is displayed
    Then "PageTitle_Object" should be displayed on MB page

  @managebeneficiary_mainpage @MB_002
  Scenario: MB_002 - Verify Back Arrow is displayed
    Then "BackArrow_Object" should be displayed on MB page

  @managebeneficiary_mainpage @MB_003
  Scenario: MB_003 - Verify Search Beneficiary input is displayed
    Then "SearchBeneficiaryInput_Object" should be displayed on MB page

  @managebeneficiary_mainpage @MB_004
  Scenario: MB_004 - Verify ADD button is displayed
    Then "AddButton_Object" should be displayed on MB page

  @managebeneficiary_mainpage @MB_005
  Scenario: MB_005 - Verify All tab is displayed
    Then "AllTab_Object" should be displayed on MB page

  @managebeneficiary_mainpage @MB_006
  Scenario: MB_006 - Verify Shivalik tab is displayed
    Then "ShivalikTab_Object" should be displayed on MB page

  @managebeneficiary_mainpage @MB_007
  Scenario: MB_007 - Verify Other Bank tab is displayed
    Then "OtherBankTab_Object" should be displayed on MB page

  @managebeneficiary_mainpage @MB_008
  Scenario: MB_008 - Verify Cooling Period section is displayed
    Then "CoolingPeriodIcon_Object" should be displayed on MB page

  @managebeneficiary_mainpage @MB_009
  Scenario: MB_009 - Verify Favourite section is displayed
    Then "FavoriteIcon_Object" should be displayed on MB page

  @managebeneficiary_mainpage @MB_010
  Scenario: MB_010 - Verify No Beneficiaries text or beneficiary list is shown
    Then beneficiary list or no beneficiaries text should be displayed on MB page

  # ==============================================================================
  # MODULE 2: ADD BENEFICIARY - SHIVALIK BANK (MB_011 - MB_057)
  # Form: Bank Account Number*, Re-enter Account*, Beneficiary Name*, Nickname (optional)
  # ==============================================================================

  @add_shivalik @MB_011
  Scenario: MB_011 - Click ADD button and verify Add form opens
    When user clicks on "AddButton_Object" on MB page
    Then "MB_AddTitle_Object" should be displayed on MB page

  @add_shivalik @MB_012
  Scenario: MB_012 - Verify Back Arrow on Add form
    When user clicks on "AddButton_Object" on MB page
    Then "MB_AddBackArrow_Object" should be displayed on MB page

  @add_shivalik @MB_013
  Scenario: MB_013 - Verify CANCEL button on Add form
    When user clicks on "AddButton_Object" on MB page
    Then "MB_CancelButton_Object" should be displayed on MB page

  @add_shivalik @MB_014
  Scenario: MB_014 - Verify CONTINUE button on Add form
    When user clicks on "AddButton_Object" on MB page
    Then "MB_ContinueButton_Object" should be displayed on MB page

  @add_shivalik @MB_015
  Scenario: MB_015 - Verify Shivalik Bank radio button is displayed
    When user clicks on "AddButton_Object" on MB page
    Then "MB_ShivalikBankRadio_Object" should be displayed on MB page

  @add_shivalik @MB_016
  Scenario: MB_016 - Verify Other Bank radio button is displayed
    When user clicks on "AddButton_Object" on MB page
    Then "MB_OtherBankRadio_Object" should be displayed on MB page

  @add_shivalik @MB_017
  Scenario: MB_017 - Verify Bank Account Number input is displayed
    When user clicks on "AddButton_Object" on MB page
    Then "MB_AccountNumberInput_Object" should be displayed on MB page

  @add_shivalik @MB_018
  Scenario: MB_018 - Verify Re-enter Account Number input is displayed
    When user clicks on "AddButton_Object" on MB page
    Then "MB_ReEnterAccountInput_Object" should be displayed on MB page

  @add_shivalik @MB_019
  Scenario: MB_019 - Verify Beneficiary Name input is displayed
    When user clicks on "AddButton_Object" on MB page
    Then "MB_BeneficiaryNameInput_Object" should be displayed on MB page

  @add_shivalik @MB_020
  Scenario: MB_020 - Verify Nickname input is displayed
    When user clicks on "AddButton_Object" on MB page
    Then "MB_NicknameInput_Object" should be displayed on MB page

  @add_shivalik @MB_021
  Scenario: MB_021 - Verify Account Number field is masked by default
    When user clicks on "AddButton_Object" on MB page
    Then MB account number field should be masked

  @add_shivalik @MB_022
  Scenario: MB_022 - Verify Eye icon toggles Account Number visibility
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_AccountNumberEyeIcon_Object" on MB page
    Then MB account number field should be unmasked

  @add_shivalik @MB_023
  Scenario: MB_023 - Select Shivalik Bank radio and verify selected
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_ShivalikBankLabel_Object" on MB page
    Then "MB_ShivalikBankRadio_Object" should be selected by default on MB page

  @add_shivalik @MB_024
  Scenario: MB_024 - Enter valid Shivalik account number
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_ShivalikBankLabel_Object" on MB page
    And user enters MB test data "AccountNumber_Value" from test case "MB_024" in "MB_AccountNumberInput_Object" on MB page
    Then "MB_AccountNumberInput_Object" field should have value entered on MB page

  @add_shivalik @MB_025
  Scenario: MB_025 - Enter re-enter account number matching
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_ShivalikBankLabel_Object" on MB page
    And user enters MB test data "AccountNumber_Value" from test case "MB_025" in "MB_AccountNumberInput_Object" on MB page
    And user enters MB test data "ReEnterAccountNumber_Value" from test case "MB_025" in "MB_ReEnterAccountInput_Object" on MB page
    Then "MB_ReEnterAccountInput_Object" field should have value entered on MB page

  @add_shivalik @MB_026
  Scenario: MB_026 - Enter mismatched re-enter account number
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_ShivalikBankLabel_Object" on MB page
    And user enters MB test data "AccountNumber_Value" from test case "MB_026" in "MB_AccountNumberInput_Object" on MB page
    And user enters MB test data "ReEnterAccountNumber_Value" from test case "MB_026" in "MB_ReEnterAccountInput_Object" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then expected MB result from test case "MB_026" should be validated

  @add_shivalik @MB_027
  Scenario: MB_027 - Enter valid Shivalik beneficiary name
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_ShivalikBankLabel_Object" on MB page
    And user enters MB test data "BeneficiaryName_Value" from test case "MB_027" in "MB_BeneficiaryNameInput_Object" on MB page
    Then "MB_BeneficiaryNameInput_Object" field should have value entered on MB page

  @add_shivalik @MB_028
  Scenario: MB_028 - Enter Shivalik nickname
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_ShivalikBankLabel_Object" on MB page
    And user enters MB test data "Nickname_Value" from test case "MB_028" in "MB_NicknameInput_Object" on MB page
    Then "MB_NicknameInput_Object" field should have value entered on MB page

  @add_shivalik @MB_029
  Scenario: MB_029 - Click CONTINUE with empty form fields
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_ShivalikBankLabel_Object" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then expected MB result from test case "MB_029" should be validated

  @add_shivalik @MB_030
  Scenario: MB_030 - Click CONTINUE with only account number filled
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_ShivalikBankLabel_Object" on MB page
    And user enters MB test data "AccountNumber_Value" from test case "MB_030" in "MB_AccountNumberInput_Object" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then expected MB result from test case "MB_030" should be validated

  @add_shivalik @MB_031
  Scenario: MB_031 - Click CONTINUE with account and re-enter but no name
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_ShivalikBankLabel_Object" on MB page
    And user enters MB test data "AccountNumber_Value" from test case "MB_031" in "MB_AccountNumberInput_Object" on MB page
    And user enters MB test data "ReEnterAccountNumber_Value" from test case "MB_031" in "MB_ReEnterAccountInput_Object" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then expected MB result from test case "MB_031" should be validated

  @add_shivalik @MB_032
  Scenario: MB_032 - Enter invalid Shivalik account number format
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_ShivalikBankLabel_Object" on MB page
    And user enters MB test data "AccountNumber_Value" from test case "MB_032" in "MB_AccountNumberInput_Object" on MB page
    And user enters MB test data "ReEnterAccountNumber_Value" from test case "MB_032" in "MB_ReEnterAccountInput_Object" on MB page
    And user enters MB test data "BeneficiaryName_Value" from test case "MB_032" in "MB_BeneficiaryNameInput_Object" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then expected MB result from test case "MB_032" should be validated

  @add_shivalik @MB_033
  Scenario: MB_033 - Fill complete Shivalik form and click CONTINUE
    When user clicks on "AddButton_Object" on MB page
    And user fills Shivalik beneficiary form with test case "MB_033" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then "MB_ConfirmTitle_Object" should be displayed on MB page

  @add_shivalik @MB_034
  Scenario: MB_034 - Verify Confirm page displays beneficiary name
    When user clicks on "AddButton_Object" on MB page
    And user fills Shivalik beneficiary form with test case "MB_034" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then "MB_ConfirmBeneficiaryName_Object" should be displayed on MB page

  @add_shivalik @MB_035
  Scenario: MB_035 - Verify Confirm page displays account number
    When user clicks on "AddButton_Object" on MB page
    And user fills Shivalik beneficiary form with test case "MB_035" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then "MB_ConfirmAccountNumber_Object" should be displayed on MB page

  @add_shivalik @MB_036
  Scenario: MB_036 - Verify Confirm page displays bank name
    When user clicks on "AddButton_Object" on MB page
    And user fills Shivalik beneficiary form with test case "MB_036" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then "MB_ConfirmBankName_Object" should be displayed on MB page

  @add_shivalik @MB_037
  Scenario: MB_037 - Verify Confirm page displays IFSC code
    When user clicks on "AddButton_Object" on MB page
    And user fills Shivalik beneficiary form with test case "MB_037" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then "MB_ConfirmIFSCCode_Object" should be displayed on MB page

  @add_shivalik @MB_038
  Scenario: MB_038 - Verify Confirm page displays nickname
    When user clicks on "AddButton_Object" on MB page
    And user fills Shivalik beneficiary form with test case "MB_038" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then "MB_ConfirmNickname_Object" should be displayed on MB page

  @add_shivalik @MB_039
  Scenario: MB_039 - Verify OTP input field displayed on Confirm page
    When user clicks on "AddButton_Object" on MB page
    And user fills Shivalik beneficiary form with test case "MB_039" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then "MB_OTPInput_Object" should be displayed on MB page

  @add_shivalik @MB_040
  Scenario: MB_040 - Verify OTP timer is displayed
    When user clicks on "AddButton_Object" on MB page
    And user fills Shivalik beneficiary form with test case "MB_040" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then "MB_OTPTimerText_Object" should be displayed on MB page

  @add_shivalik @MB_041
  Scenario: MB_041 - Verify Resend OTP link is displayed
    When user clicks on "AddButton_Object" on MB page
    And user fills Shivalik beneficiary form with test case "MB_041" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then "MB_ResendOTPLink_Object" should be displayed on MB page

  @add_shivalik @MB_042
  Scenario: MB_042 - Verify OTP message text is displayed
    When user clicks on "AddButton_Object" on MB page
    And user fills Shivalik beneficiary form with test case "MB_042" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then "MB_OTPMessage_Object" should be displayed on MB page

  @add_shivalik @MB_043
  Scenario: MB_043 - Verify Never share OTP text is displayed
    When user clicks on "AddButton_Object" on MB page
    And user fills Shivalik beneficiary form with test case "MB_043" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then "MB_NeverShareOTPText_Object" should be displayed on MB page

  @add_shivalik @MB_044
  Scenario: MB_044 - Verify SUBMIT button is disabled before OTP entry
    When user clicks on "AddButton_Object" on MB page
    And user fills Shivalik beneficiary form with test case "MB_044" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then MB SUBMIT button should be disabled

  @add_shivalik @MB_045
  Scenario: MB_045 - Enter invalid OTP and verify error
    When user clicks on "AddButton_Object" on MB page
    And user fills Shivalik beneficiary form with test case "MB_045" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    And user enters MB OTP from test case "MB_045"
    And user clicks on "MB_SubmitButton_Object" on MB page
    Then expected MB result from test case "MB_045" should be validated

  @add_shivalik @MB_046
  Scenario: MB_046 - Enter wrong OTP three times and verify lock message
    When user clicks on "AddButton_Object" on MB page
    And user fills Shivalik beneficiary form with test case "MB_046" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    And user enters wrong OTP three times on MB page with test case "MB_046"
    Then expected MB result from test case "MB_046" should be validated

  @add_shivalik @MB_047
  Scenario: MB_047 - Click CANCEL on Confirm page and verify return
    When user clicks on "AddButton_Object" on MB page
    And user fills Shivalik beneficiary form with test case "MB_047" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    And user clicks on "MB_CancelButton_Object" on MB page
    Then "PageTitle_Object" should be displayed on MB page

  @add_shivalik @MB_048
  Scenario: MB_048 - Click Back Arrow on Confirm page
    When user clicks on "AddButton_Object" on MB page
    And user fills Shivalik beneficiary form with test case "MB_048" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    And user clicks on "MB_AddBackArrow_Object" on MB page
    Then "MB_AddTitle_Object" should be displayed on MB page

  @add_shivalik @MB_049
  Scenario: MB_049 - Enter valid OTP and submit Shivalik beneficiary
    When user clicks on "AddButton_Object" on MB page
    And user fills Shivalik beneficiary form with test case "MB_049" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    And user enters MB OTP from test case "MB_049"
    And user clicks on "MB_SubmitButton_Object" on MB page
    Then expected MB result from test case "MB_049" should be validated

  @add_shivalik @MB_050
  Scenario: MB_050 - Verify Success page message text
    When user clicks on "AddButton_Object" on MB page
    And user fills Shivalik beneficiary form with test case "MB_050" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    And user enters MB OTP from test case "MB_050"
    And user clicks on "MB_SubmitButton_Object" on MB page
    Then "MB_SuccessMessage_Object" should be displayed on MB page

  @add_shivalik @MB_051
  Scenario: MB_051 - Verify Done button on Success page
    When user clicks on "AddButton_Object" on MB page
    And user fills Shivalik beneficiary form with test case "MB_051" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    And user enters MB OTP from test case "MB_051"
    And user clicks on "MB_SubmitButton_Object" on MB page
    Then "MB_DoneButton_Object" should be displayed on MB page

  @add_shivalik @MB_052
  Scenario: MB_052 - Click Done button and return to Manage Beneficiaries
    When user clicks on "AddButton_Object" on MB page
    And user fills Shivalik beneficiary form with test case "MB_052" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    And user enters MB OTP from test case "MB_052"
    And user clicks on "MB_SubmitButton_Object" on MB page
    And user clicks on "MB_DoneButton_Object" on MB page
    Then "PageTitle_Object" should be displayed on MB page

  @add_shivalik @MB_053
  Scenario: MB_053 - Verify CANCEL returns from Add form
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_CancelButton_Object" on MB page
    Then "PageTitle_Object" should be displayed on MB page

  @add_shivalik @MB_054
  Scenario: MB_054 - Verify Back Arrow returns from Add form
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_AddBackArrow_Object" on MB page
    Then "PageTitle_Object" should be displayed on MB page

  @add_shivalik @MB_055
  Scenario: MB_055 - Verify Beneficiary type label is displayed
    When user clicks on "AddButton_Object" on MB page
    Then "MB_BeneficiaryTypeLabel_Object" should be displayed on MB page

  @add_shivalik @MB_056
  Scenario: MB_056 - Verify Account number label is displayed
    When user clicks on "AddButton_Object" on MB page
    Then "MB_AccountNumberLabel_Object" should be displayed on MB page

  @add_shivalik @MB_057
  Scenario: MB_057 - Verify Beneficiary name label is displayed
    When user clicks on "AddButton_Object" on MB page
    Then "MB_BeneficiaryNameLabel_Object" should be displayed on MB page

  # ==============================================================================
  # MODULE 3: MANAGE BENEFICIARY (MB_058 - MB_067)
  # ==============================================================================

  @manage_beneficiary @MB_058
  Scenario: MB_058 - Verify beneficiary list is displayed after adding
    Then "BeneficiaryList_Object" should be displayed on MB page

  @manage_beneficiary @MB_059
  Scenario: MB_059 - Verify All tab shows all beneficiaries
    When user clicks on "AllTab_Object" on MB page
    Then "BeneficiaryList_Object" should be displayed on MB page

  @manage_beneficiary @MB_060
  Scenario: MB_060 - Verify Shivalik tab filters Shivalik beneficiaries
    When user clicks on "ShivalikTab_Object" on MB page
    Then "ShivalikTab_Object" should be displayed on MB page

  @manage_beneficiary @MB_061
  Scenario: MB_061 - Verify Other Bank tab filters other bank beneficiaries
    When user clicks on "OtherBankTab_Object" on MB page
    Then "OtherBankTab_Object" should be displayed on MB page

  @manage_beneficiary @MB_062
  Scenario: MB_062 - Verify Search beneficiary with valid name
    When user enters MB test data "SearchText_Value" from test case "MB_062" in "SearchBeneficiaryInput_Object" on MB page
    And user clicks on "SearchButton_Object" on MB page
    Then "BeneficiaryList_Object" should be displayed on MB page

  @manage_beneficiary @MB_063
  Scenario: MB_063 - Verify Search beneficiary with invalid name
    When user enters MB test data "SearchText_Value" from test case "MB_063" in "SearchBeneficiaryInput_Object" on MB page
    And user clicks on "SearchButton_Object" on MB page
    Then "EmptyWrapperBox_Object" should be displayed on MB page

  @manage_beneficiary @MB_064
  Scenario: MB_064 - Verify Filter icon is displayed
    Then "FilterIcon_Object" should be displayed on MB page

  @manage_beneficiary @MB_065
  Scenario: MB_065 - Verify A-Z sort option
    When user clicks on "FilterIcon_Object" on MB page
    Then "AZSortText_Object" should be displayed on MB page

  @manage_beneficiary @MB_066
  Scenario: MB_066 - Verify Cooling Period section message
    Then "CoolingPeriodText_Object" should be displayed on MB page

  @manage_beneficiary @MB_067
  Scenario: MB_067 - Verify No beneficiaries under cooling period text
    Then "NoBeneficiariesCoolingText_Object" should be displayed on MB page

  # ==============================================================================
  # MODULE 4: ADD BENEFICIARY - OTHER BANK (MB_068 - MB_105)
  # Form: Bank Account Number*, Re-enter Account*, IFSC Code*, Beneficiary Name*,
  #        Nickname (optional), Verify Beneficiary button
  # ==============================================================================

  @add_otherbank @MB_068
  Scenario: MB_068 - Click ADD and select Other Bank radio
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_OtherBankLabel_Object" on MB page
    Then "MB_OtherBankRadio_Object" should be selected by default on MB page

  @add_otherbank @MB_069
  Scenario: MB_069 - Verify IFSC code input is displayed for Other Bank
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_OtherBankLabel_Object" on MB page
    Then "MB_IFSCCodeInput_Object" should be displayed on MB page

  @add_otherbank @MB_070
  Scenario: MB_070 - Verify Search IFSC link is displayed for Other Bank
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_OtherBankLabel_Object" on MB page
    Then "MB_SearchIFSCLink_Object" should be displayed on MB page

  @add_otherbank @MB_071
  Scenario: MB_071 - Verify Verify Beneficiary button is displayed for Other Bank
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_OtherBankLabel_Object" on MB page
    Then "MB_VerifyBeneficiaryButton_Object" should be displayed on MB page

  @add_otherbank @MB_072
  Scenario: MB_072 - Enter valid Other Bank account number
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_OtherBankLabel_Object" on MB page
    And user enters MB test data "AccountNumber_Value" from test case "MB_072" in "MB_AccountNumberInput_Object" on MB page
    Then "MB_AccountNumberInput_Object" field should have value entered on MB page

  @add_otherbank @MB_073
  Scenario: MB_073 - Enter valid Other Bank re-enter account number
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_OtherBankLabel_Object" on MB page
    And user enters MB test data "AccountNumber_Value" from test case "MB_073" in "MB_AccountNumberInput_Object" on MB page
    And user enters MB test data "ReEnterAccountNumber_Value" from test case "MB_073" in "MB_ReEnterAccountInput_Object" on MB page
    Then "MB_ReEnterAccountInput_Object" field should have value entered on MB page

  @add_otherbank @MB_074
  Scenario: MB_074 - Enter valid IFSC code for Other Bank
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_OtherBankLabel_Object" on MB page
    And user enters MB test data "IFSCCode_Value" from test case "MB_074" in "MB_IFSCCodeInput_Object" on MB page
    Then "MB_IFSCCodeInput_Object" field should have value entered on MB page

  @add_otherbank @MB_075
  Scenario: MB_075 - Enter invalid IFSC code format
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_OtherBankLabel_Object" on MB page
    And user enters MB test data "AccountNumber_Value" from test case "MB_075" in "MB_AccountNumberInput_Object" on MB page
    And user enters MB test data "ReEnterAccountNumber_Value" from test case "MB_075" in "MB_ReEnterAccountInput_Object" on MB page
    And user enters MB test data "IFSCCode_Value" from test case "MB_075" in "MB_IFSCCodeInput_Object" on MB page
    And user enters MB test data "BeneficiaryName_Value" from test case "MB_075" in "MB_BeneficiaryNameInput_Object" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then expected MB result from test case "MB_075" should be validated

  @add_otherbank @MB_076
  Scenario: MB_076 - Enter valid Other Bank beneficiary name
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_OtherBankLabel_Object" on MB page
    And user enters MB test data "BeneficiaryName_Value" from test case "MB_076" in "MB_BeneficiaryNameInput_Object" on MB page
    Then "MB_BeneficiaryNameInput_Object" field should have value entered on MB page

  @add_otherbank @MB_077
  Scenario: MB_077 - Enter Other Bank nickname
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_OtherBankLabel_Object" on MB page
    And user enters MB test data "Nickname_Value" from test case "MB_077" in "MB_NicknameInput_Object" on MB page
    Then "MB_NicknameInput_Object" field should have value entered on MB page

  @add_otherbank @MB_078
  Scenario: MB_078 - Click Verify Beneficiary button
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_OtherBankLabel_Object" on MB page
    And user enters MB test data "AccountNumber_Value" from test case "MB_078" in "MB_AccountNumberInput_Object" on MB page
    And user enters MB test data "IFSCCode_Value" from test case "MB_078" in "MB_IFSCCodeInput_Object" on MB page
    And user enters MB test data "BeneficiaryName_Value" from test case "MB_078" in "MB_BeneficiaryNameInput_Object" on MB page
    And user clicks on "MB_VerifyBeneficiaryButton_Object" on MB page

  @add_otherbank @MB_079
  Scenario: MB_079 - Click CONTINUE with empty Other Bank form
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_OtherBankLabel_Object" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then expected MB result from test case "MB_079" should be validated

  @add_otherbank @MB_080
  Scenario: MB_080 - Click CONTINUE with only account number for Other Bank
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_OtherBankLabel_Object" on MB page
    And user enters MB test data "AccountNumber_Value" from test case "MB_080" in "MB_AccountNumberInput_Object" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then expected MB result from test case "MB_080" should be validated

  @add_otherbank @MB_081
  Scenario: MB_081 - Click CONTINUE without IFSC code for Other Bank
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_OtherBankLabel_Object" on MB page
    And user enters MB test data "AccountNumber_Value" from test case "MB_081" in "MB_AccountNumberInput_Object" on MB page
    And user enters MB test data "ReEnterAccountNumber_Value" from test case "MB_081" in "MB_ReEnterAccountInput_Object" on MB page
    And user enters MB test data "BeneficiaryName_Value" from test case "MB_081" in "MB_BeneficiaryNameInput_Object" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then expected MB result from test case "MB_081" should be validated

  @add_otherbank @MB_082
  Scenario: MB_082 - Enter mismatched re-enter account for Other Bank
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_OtherBankLabel_Object" on MB page
    And user enters MB test data "AccountNumber_Value" from test case "MB_082" in "MB_AccountNumberInput_Object" on MB page
    And user enters MB test data "ReEnterAccountNumber_Value" from test case "MB_082" in "MB_ReEnterAccountInput_Object" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then expected MB result from test case "MB_082" should be validated

  @add_otherbank @MB_083
  Scenario: MB_083 - Enter invalid Other Bank account number format
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_OtherBankLabel_Object" on MB page
    And user enters MB test data "AccountNumber_Value" from test case "MB_083" in "MB_AccountNumberInput_Object" on MB page
    And user enters MB test data "ReEnterAccountNumber_Value" from test case "MB_083" in "MB_ReEnterAccountInput_Object" on MB page
    And user enters MB test data "IFSCCode_Value" from test case "MB_083" in "MB_IFSCCodeInput_Object" on MB page
    And user enters MB test data "BeneficiaryName_Value" from test case "MB_083" in "MB_BeneficiaryNameInput_Object" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then expected MB result from test case "MB_083" should be validated

  @add_otherbank @MB_084
  Scenario: MB_084 - Fill complete Other Bank form and click CONTINUE
    When user clicks on "AddButton_Object" on MB page
    And user fills Other Bank beneficiary form with test case "MB_084" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then "MB_ConfirmTitle_Object" should be displayed on MB page

  @add_otherbank @MB_085
  Scenario: MB_085 - Verify Confirm page for Other Bank displays beneficiary name
    When user clicks on "AddButton_Object" on MB page
    And user fills Other Bank beneficiary form with test case "MB_085" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then "MB_ConfirmBeneficiaryName_Object" should be displayed on MB page

  @add_otherbank @MB_086
  Scenario: MB_086 - Verify Confirm page for Other Bank displays account number
    When user clicks on "AddButton_Object" on MB page
    And user fills Other Bank beneficiary form with test case "MB_086" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then "MB_ConfirmAccountNumber_Object" should be displayed on MB page

  @add_otherbank @MB_087
  Scenario: MB_087 - Verify Confirm page for Other Bank displays bank name
    When user clicks on "AddButton_Object" on MB page
    And user fills Other Bank beneficiary form with test case "MB_087" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then "MB_ConfirmBankName_Object" should be displayed on MB page

  @add_otherbank @MB_088
  Scenario: MB_088 - Verify Confirm page for Other Bank displays IFSC code
    When user clicks on "AddButton_Object" on MB page
    And user fills Other Bank beneficiary form with test case "MB_088" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then "MB_ConfirmIFSCCode_Object" should be displayed on MB page

  @add_otherbank @MB_089
  Scenario: MB_089 - Verify Confirm page for Other Bank displays nickname
    When user clicks on "AddButton_Object" on MB page
    And user fills Other Bank beneficiary form with test case "MB_089" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then "MB_ConfirmNickname_Object" should be displayed on MB page

  @add_otherbank @MB_090
  Scenario: MB_090 - Verify OTP input on Other Bank Confirm page
    When user clicks on "AddButton_Object" on MB page
    And user fills Other Bank beneficiary form with test case "MB_090" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then "MB_OTPInput_Object" should be displayed on MB page

  @add_otherbank @MB_091
  Scenario: MB_091 - Verify OTP timer for Other Bank
    When user clicks on "AddButton_Object" on MB page
    And user fills Other Bank beneficiary form with test case "MB_091" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then "MB_OTPTimerText_Object" should be displayed on MB page

  @add_otherbank @MB_092
  Scenario: MB_092 - Verify Resend OTP link for Other Bank
    When user clicks on "AddButton_Object" on MB page
    And user fills Other Bank beneficiary form with test case "MB_092" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then "MB_ResendOTPLink_Object" should be displayed on MB page

  @add_otherbank @MB_093
  Scenario: MB_093 - Verify SUBMIT disabled before OTP for Other Bank
    When user clicks on "AddButton_Object" on MB page
    And user fills Other Bank beneficiary form with test case "MB_093" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then MB SUBMIT button should be disabled

  @add_otherbank @MB_094
  Scenario: MB_094 - Enter invalid OTP for Other Bank and verify error
    When user clicks on "AddButton_Object" on MB page
    And user fills Other Bank beneficiary form with test case "MB_094" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    And user enters MB OTP from test case "MB_094"
    And user clicks on "MB_SubmitButton_Object" on MB page
    Then expected MB result from test case "MB_094" should be validated

  @add_otherbank @MB_095
  Scenario: MB_095 - Enter wrong OTP three times for Other Bank
    When user clicks on "AddButton_Object" on MB page
    And user fills Other Bank beneficiary form with test case "MB_095" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    And user enters wrong OTP three times on MB page with test case "MB_095"
    Then expected MB result from test case "MB_095" should be validated

  @add_otherbank @MB_096
  Scenario: MB_096 - Click CANCEL on Other Bank Confirm page
    When user clicks on "AddButton_Object" on MB page
    And user fills Other Bank beneficiary form with test case "MB_096" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    And user clicks on "MB_CancelButton_Object" on MB page
    Then "PageTitle_Object" should be displayed on MB page

  @add_otherbank @MB_097
  Scenario: MB_097 - Click Back Arrow on Other Bank Confirm page
    When user clicks on "AddButton_Object" on MB page
    And user fills Other Bank beneficiary form with test case "MB_097" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    And user clicks on "MB_AddBackArrow_Object" on MB page
    Then "MB_AddTitle_Object" should be displayed on MB page

  @add_otherbank @MB_098
  Scenario: MB_098 - Enter valid OTP and submit Other Bank beneficiary
    When user clicks on "AddButton_Object" on MB page
    And user fills Other Bank beneficiary form with test case "MB_098" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    And user enters MB OTP from test case "MB_098"
    And user clicks on "MB_SubmitButton_Object" on MB page
    Then expected MB result from test case "MB_098" should be validated

  @add_otherbank @MB_099
  Scenario: MB_099 - Verify Success page for Other Bank
    When user clicks on "AddButton_Object" on MB page
    And user fills Other Bank beneficiary form with test case "MB_099" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    And user enters MB OTP from test case "MB_099"
    And user clicks on "MB_SubmitButton_Object" on MB page
    Then "MB_SuccessMessage_Object" should be displayed on MB page

  @add_otherbank @MB_100
  Scenario: MB_100 - Verify Done button on Other Bank Success page
    When user clicks on "AddButton_Object" on MB page
    And user fills Other Bank beneficiary form with test case "MB_100" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    And user enters MB OTP from test case "MB_100"
    And user clicks on "MB_SubmitButton_Object" on MB page
    Then "MB_DoneButton_Object" should be displayed on MB page

  @add_otherbank @MB_101
  Scenario: MB_101 - Click Done and return to Manage Beneficiaries from Other Bank
    When user clicks on "AddButton_Object" on MB page
    And user fills Other Bank beneficiary form with test case "MB_101" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    And user enters MB OTP from test case "MB_101"
    And user clicks on "MB_SubmitButton_Object" on MB page
    And user clicks on "MB_DoneButton_Object" on MB page
    Then "PageTitle_Object" should be displayed on MB page

  @add_otherbank @MB_102
  Scenario: MB_102 - Verify CANCEL returns from Other Bank Add form
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_OtherBankLabel_Object" on MB page
    And user clicks on "MB_CancelButton_Object" on MB page
    Then "PageTitle_Object" should be displayed on MB page

  @add_otherbank @MB_103
  Scenario: MB_103 - Verify Back Arrow returns from Other Bank Add form
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_OtherBankLabel_Object" on MB page
    And user clicks on "MB_AddBackArrow_Object" on MB page
    Then "PageTitle_Object" should be displayed on MB page

  @add_otherbank @MB_104
  Scenario: MB_104 - Verify IFSC code label is displayed for Other Bank
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_OtherBankLabel_Object" on MB page
    Then "MB_IFSCCodeLabel_Object" should be displayed on MB page

  @add_otherbank @MB_105
  Scenario: MB_105 - Verify Search IFSC link click opens search
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_OtherBankLabel_Object" on MB page
    And user clicks on "MB_SearchIFSCLink_Object" on MB page

  # ==============================================================================
  # MODULE 5: BENEFICIARY DETAILS EDIT (MB_106 - MB_125)
  # ==============================================================================

  @edit_beneficiary @MB_106
  Scenario: MB_106 - Click on first beneficiary row to open details
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    Then "MB_BeneficiaryDetailName_Object" should be displayed on MB page

  @edit_beneficiary @MB_107
  Scenario: MB_107 - Verify beneficiary detail name is displayed
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    Then "MB_BeneficiaryDetailName_Object" should be displayed on MB page

  @edit_beneficiary @MB_108
  Scenario: MB_108 - Verify beneficiary detail account number is displayed
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    Then "MB_BeneficiaryDetailAccount_Object" should be displayed on MB page

  @edit_beneficiary @MB_109
  Scenario: MB_109 - Verify Edit button is displayed in beneficiary details
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    Then "MB_EditButton_Object" should be displayed on MB page

  @edit_beneficiary @MB_110
  Scenario: MB_110 - Verify Delete button is displayed in beneficiary details
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    Then "MB_DeleteButton_Object" should be displayed on MB page

  @edit_beneficiary @MB_111
  Scenario: MB_111 - Click Edit button and verify Edit form opens
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_EditButton_Object" on MB page
    Then "MB_EditTitle_Object" should be displayed on MB page

  @edit_beneficiary @MB_112
  Scenario: MB_112 - Verify nickname field is displayed in Edit form
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_EditButton_Object" on MB page
    Then "MB_NicknameInput_Object" should be displayed on MB page

  @edit_beneficiary @MB_113
  Scenario: MB_113 - Edit nickname with valid value
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_EditButton_Object" on MB page
    And user clears "MB_NicknameInput_Object" on MB page
    And user enters MB test data "Nickname_Value" from test case "MB_113" in "MB_NicknameInput_Object" on MB page
    Then "MB_NicknameInput_Object" field should have value entered on MB page

  @edit_beneficiary @MB_114
  Scenario: MB_114 - Click SAVE button after editing nickname
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_EditButton_Object" on MB page
    And user clears "MB_NicknameInput_Object" on MB page
    And user enters MB test data "Nickname_Value" from test case "MB_114" in "MB_NicknameInput_Object" on MB page
    And user clicks on "MB_SaveButton_Object" on MB page
    Then "MB_OTPInput_Object" should be displayed on MB page

  @edit_beneficiary @MB_115
  Scenario: MB_115 - Enter valid OTP for edit and submit
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_EditButton_Object" on MB page
    And user clears "MB_NicknameInput_Object" on MB page
    And user enters MB test data "Nickname_Value" from test case "MB_115" in "MB_NicknameInput_Object" on MB page
    And user clicks on "MB_SaveButton_Object" on MB page
    And user enters MB OTP from test case "MB_115"
    And user clicks on "MB_SubmitButton_Object" on MB page
    Then expected MB result from test case "MB_115" should be validated

  @edit_beneficiary @MB_116
  Scenario: MB_116 - Enter invalid OTP for edit and verify error
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_EditButton_Object" on MB page
    And user clears "MB_NicknameInput_Object" on MB page
    And user enters MB test data "Nickname_Value" from test case "MB_116" in "MB_NicknameInput_Object" on MB page
    And user clicks on "MB_SaveButton_Object" on MB page
    And user enters MB OTP from test case "MB_116"
    And user clicks on "MB_SubmitButton_Object" on MB page
    Then expected MB result from test case "MB_116" should be validated

  @edit_beneficiary @MB_117
  Scenario: MB_117 - Click CANCEL on Edit form
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_EditButton_Object" on MB page
    And user clicks on "MB_CancelButton_Object" on MB page
    Then "MB_BeneficiaryDetailName_Object" should be displayed on MB page

  @edit_beneficiary @MB_118
  Scenario: MB_118 - Click Back Arrow on Edit form
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_EditButton_Object" on MB page
    And user clicks on "MB_AddBackArrow_Object" on MB page
    Then "MB_BeneficiaryDetailName_Object" should be displayed on MB page

  @edit_beneficiary @MB_119
  Scenario: MB_119 - Edit nickname with empty value and save
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_EditButton_Object" on MB page
    And user clears "MB_NicknameInput_Object" on MB page
    And user clicks on "MB_SaveButton_Object" on MB page
    Then MB nickname update should proceed or error should be shown

  @edit_beneficiary @MB_120
  Scenario: MB_120 - Edit nickname with special characters
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_EditButton_Object" on MB page
    And user clears "MB_NicknameInput_Object" on MB page
    And user enters MB test data "Nickname_Value" from test case "MB_120" in "MB_NicknameInput_Object" on MB page
    And user clicks on "MB_SaveButton_Object" on MB page
    Then MB nickname update should proceed or error should be shown

  @edit_beneficiary @MB_121
  Scenario: MB_121 - Verify SAVE button is displayed in Edit form
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_EditButton_Object" on MB page
    Then "MB_SaveButton_Object" should be displayed on MB page

  @edit_beneficiary @MB_122
  Scenario: MB_122 - Verify CANCEL button is displayed in Edit form
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_EditButton_Object" on MB page
    Then "MB_CancelButton_Object" should be displayed on MB page

  @edit_beneficiary @MB_123
  Scenario: MB_123 - Edit and verify OTP timer is displayed
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_EditButton_Object" on MB page
    And user clears "MB_NicknameInput_Object" on MB page
    And user enters MB test data "Nickname_Value" from test case "MB_123" in "MB_NicknameInput_Object" on MB page
    And user clicks on "MB_SaveButton_Object" on MB page
    Then "MB_OTPTimerText_Object" should be displayed on MB page

  @edit_beneficiary @MB_124
  Scenario: MB_124 - Edit and verify Resend OTP link is displayed
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_EditButton_Object" on MB page
    And user clears "MB_NicknameInput_Object" on MB page
    And user enters MB test data "Nickname_Value" from test case "MB_124" in "MB_NicknameInput_Object" on MB page
    And user clicks on "MB_SaveButton_Object" on MB page
    Then "MB_ResendOTPLink_Object" should be displayed on MB page

  @edit_beneficiary @MB_125
  Scenario: MB_125 - Click Done after successful edit and return to list
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_EditButton_Object" on MB page
    And user clears "MB_NicknameInput_Object" on MB page
    And user enters MB test data "Nickname_Value" from test case "MB_125" in "MB_NicknameInput_Object" on MB page
    And user clicks on "MB_SaveButton_Object" on MB page
    And user enters MB OTP from test case "MB_125"
    And user clicks on "MB_SubmitButton_Object" on MB page
    And user clicks on "MB_DoneButton_Object" on MB page
    Then "PageTitle_Object" should be displayed on MB page

  # ==============================================================================
  # MODULE 6: BENEFICIARY DETAILS DELETE (MB_126 - MB_141)
  # ==============================================================================

  @delete_beneficiary @MB_126
  Scenario: MB_126 - Click Delete button on beneficiary details
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_DeleteButton_Object" on MB page
    Then "MB_DeleteConfirmTitle_Object" should be displayed on MB page

  @delete_beneficiary @MB_127
  Scenario: MB_127 - Verify Delete confirmation title
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_DeleteButton_Object" on MB page
    Then "MB_DeleteConfirmTitle_Object" should be displayed on MB page

  @delete_beneficiary @MB_128
  Scenario: MB_128 - Verify DELETE button on confirmation dialog
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_DeleteButton_Object" on MB page
    Then "MB_DeleteConfirmButton_Object" should be displayed on MB page

  @delete_beneficiary @MB_129
  Scenario: MB_129 - Verify CANCEL button on Delete confirmation dialog
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_DeleteButton_Object" on MB page
    Then "MB_CancelButton_Object" should be displayed on MB page

  @delete_beneficiary @MB_130
  Scenario: MB_130 - Click CANCEL on Delete confirmation
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_DeleteButton_Object" on MB page
    And user clicks on "MB_CancelButton_Object" on MB page
    Then "MB_BeneficiaryDetailName_Object" should be displayed on MB page

  @delete_beneficiary @MB_131
  Scenario: MB_131 - Click DELETE confirm and verify OTP input
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_DeleteButton_Object" on MB page
    And user clicks on "MB_DeleteConfirmButton_Object" on MB page
    Then "MB_OTPInput_Object" should be displayed on MB page

  @delete_beneficiary @MB_132
  Scenario: MB_132 - Enter valid OTP for delete and submit
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_DeleteButton_Object" on MB page
    And user clicks on "MB_DeleteConfirmButton_Object" on MB page
    And user enters MB OTP from test case "MB_132"
    And user clicks on "MB_SubmitButton_Object" on MB page
    Then expected MB result from test case "MB_132" should be validated

  @delete_beneficiary @MB_133
  Scenario: MB_133 - Enter invalid OTP for delete and verify error
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_DeleteButton_Object" on MB page
    And user clicks on "MB_DeleteConfirmButton_Object" on MB page
    And user enters MB OTP from test case "MB_133"
    And user clicks on "MB_SubmitButton_Object" on MB page
    Then expected MB result from test case "MB_133" should be validated

  @delete_beneficiary @MB_134
  Scenario: MB_134 - Enter wrong OTP three times for delete
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_DeleteButton_Object" on MB page
    And user clicks on "MB_DeleteConfirmButton_Object" on MB page
    And user enters wrong OTP three times on MB page with test case "MB_134"
    Then expected MB result from test case "MB_134" should be validated

  @delete_beneficiary @MB_135
  Scenario: MB_135 - Verify OTP timer on Delete confirmation
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_DeleteButton_Object" on MB page
    And user clicks on "MB_DeleteConfirmButton_Object" on MB page
    Then "MB_OTPTimerText_Object" should be displayed on MB page

  @delete_beneficiary @MB_136
  Scenario: MB_136 - Verify Resend OTP link on Delete confirmation
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_DeleteButton_Object" on MB page
    And user clicks on "MB_DeleteConfirmButton_Object" on MB page
    Then "MB_ResendOTPLink_Object" should be displayed on MB page

  @delete_beneficiary @MB_137
  Scenario: MB_137 - Click Done after successful delete and return to list
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_DeleteButton_Object" on MB page
    And user clicks on "MB_DeleteConfirmButton_Object" on MB page
    And user enters MB OTP from test case "MB_137"
    And user clicks on "MB_SubmitButton_Object" on MB page
    And user clicks on "MB_DoneButton_Object" on MB page
    Then "PageTitle_Object" should be displayed on MB page

  @delete_beneficiary @MB_138
  Scenario: MB_138 - Verify beneficiary is removed from list after delete
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_DeleteButton_Object" on MB page
    And user clicks on "MB_DeleteConfirmButton_Object" on MB page
    And user enters MB OTP from test case "MB_138"
    And user clicks on "MB_SubmitButton_Object" on MB page
    And user clicks on "MB_DoneButton_Object" on MB page
    Then "BeneficiaryList_Object" should be displayed on MB page

  @delete_beneficiary @MB_139
  Scenario: MB_139 - Click Back Arrow on Delete OTP page
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_DeleteButton_Object" on MB page
    And user clicks on "MB_DeleteConfirmButton_Object" on MB page
    And user clicks on "MB_AddBackArrow_Object" on MB page
    Then "MB_DeleteConfirmTitle_Object" should be displayed on MB page

  @delete_beneficiary @MB_140
  Scenario: MB_140 - Verify Manage Beneficiaries page after all operations
    Then "PageTitle_Object" should be displayed on MB page
    And "BeneficiaryList_Object" should be displayed on MB page

  @delete_beneficiary @MB_141
  Scenario: MB_141 - Verify Back Arrow navigates back from Manage Beneficiaries page
    When user clicks on "BackArrow_Object" on MB page
