# language: en
@managebeneficiary
Feature: Manage Beneficiary - Complete Module Testing
  Test beneficiary management for Shivalik Bank Internet Banking
  Modules: Main Page, Add Shivalik, Manage List, Add Other Bank, Search IFSC, Edit, Delete
  Test data from TestSuite.xlsx sheet "managebeneficiary"

  Background:
    Given user is logged in and navigates to Manage Beneficiary page

  # ==============================================================================
  # MODULE 1: MANAGE BENEFICIARIES MAIN PAGE
  # ==============================================================================

  @managebeneficiary_mainpage @MB_001
  Scenario: MB_001 - Verify Manage Beneficiaries main page elements
    Then "PageTitle_Object" should be displayed on MB page
    And "CoolingPeriodIcon_Object" should be displayed on MB page
    And "FavoriteIcon_Object" should be displayed on MB page
    And beneficiary list or no beneficiaries text should be displayed on MB page
    And "AddButton_Object" should be displayed on MB page
    And "SearchBeneficiaryInput_Object" should be displayed on MB page

  # ==============================================================================
  # MODULE 2: ADD BENEFICIARY - SHIVALIK BANK
  # Shivalik Bank is selected by default - no radio click needed
  # Form: Bank Account Number*, Re-enter Account*, Beneficiary Name*, Nickname
  # ==============================================================================

  @add_shivalik @MB_011
  Scenario: MB_011 - Click ADD and verify add form with all elements
    When user clicks on "AddButton_Object" on MB page
    Then "MB_AddTitle_Object" should be displayed on MB page
    And "MB_ShivalikBankRadio_Object" should be selected by default on MB page
    And "MB_AccountNumberInput_Object" should be displayed on MB page
    And "MB_ReEnterAccountInput_Object" should be displayed on MB page
    And "MB_BeneficiaryNameInput_Object" should be displayed on MB page
    And "MB_NicknameInput_Object" should be displayed on MB page
    And "MB_ContinueButton_Object" should be displayed on MB page
    And "MB_AddBackArrow_Object" should be displayed on MB page
    And "MB_BeneficiaryTypeLabel_Object" should be displayed on MB page

  @add_shivalik @MB_012
  Scenario: MB_012 - Click CONTINUE with empty Shivalik form and validate errors
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then expected MB result from test case "MB_012" should be validated

  @add_shivalik @MB_013
  Scenario: MB_013 - Enter mismatched re-enter account and validate error
    When user clicks on "AddButton_Object" on MB page
    And user enters MB test data "AccountNumber_Value" from test case "MB_013" in "MB_AccountNumberInput_Object" on MB page
    And user enters MB test data "ReEnterAccountNumber_Value" from test case "MB_013" in "MB_ReEnterAccountInput_Object" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then expected MB result from test case "MB_013" should be validated

  @add_shivalik @MB_014
  Scenario: MB_014 - Enter invalid account format and validate error
    When user clicks on "AddButton_Object" on MB page
    And user enters MB test data "AccountNumber_Value" from test case "MB_014" in "MB_AccountNumberInput_Object" on MB page
    And user enters MB test data "ReEnterAccountNumber_Value" from test case "MB_014" in "MB_ReEnterAccountInput_Object" on MB page
    And user enters MB test data "BeneficiaryName_Value" from test case "MB_014" in "MB_BeneficiaryNameInput_Object" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then expected MB result from test case "MB_014" should be validated

  @add_shivalik @MB_015
  Scenario: MB_015 - Complete Shivalik form, verify confirm page high-level validation and OTP section
    When user clicks on "AddButton_Object" on MB page
    And user fills Shivalik beneficiary form with test case "MB_015" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then "MB_ConfirmTitle_Object" should be displayed on MB page
    And MB confirm page should display correct details for test case "MB_015"
    And "MB_OTPInput_Object" should be displayed on MB page
    And "MB_OTPTimerText_Object" should be displayed on MB page
    And "MB_ResendOTPLink_Object" should be displayed on MB page
    And "MB_OTPMessage_Object" should be displayed on MB page
    And "MB_NeverShareOTPText_Object" should be displayed on MB page
    And MB SUBMIT button should be disabled

  @add_shivalik @MB_016
  Scenario: MB_016 - E2E Add Shivalik: fill form → confirm validate → OTP → success → Done → main
    When user clicks on "AddButton_Object" on MB page
    And user fills Shivalik beneficiary form with test case "MB_016" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then "MB_ConfirmTitle_Object" should be displayed on MB page
    And MB confirm page should display correct details for test case "MB_016"
    When user enters MB OTP from test case "MB_016"
    And user clicks on "MB_SubmitButton_Object" on MB page
    Then expected MB result from test case "MB_016" should be validated
    And "MB_DoneButton_Object" should be displayed on MB page
    When user clicks on "MB_DoneButton_Object" on MB page
    Then "PageTitle_Object" should be displayed on MB page

  @add_shivalik @MB_017
  Scenario: MB_017 - Back arrow on add form returns to main page
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_AddBackArrow_Object" on MB page
    Then "PageTitle_Object" should be displayed on MB page

  # ==============================================================================
  # MODULE 3: MANAGE BENEFICIARY LIST
  # ==============================================================================

  @manage_beneficiary @MB_030
  Scenario: MB_030 - Search beneficiary, verify tabs and filter with A-Z sort
    When user enters MB test data "SearchText_Value" from test case "MB_030" in "SearchBeneficiaryInput_Object" on MB page
    And user clicks on "SearchButton_Object" on MB page
    Then "BeneficiaryList_Object" should be displayed on MB page
    When user clears "SearchBeneficiaryInput_Object" on MB page
    And user enters MB test data "SearchText_Value" from test case "MB_031" in "SearchBeneficiaryInput_Object" on MB page
    And user clicks on "SearchButton_Object" on MB page
    Then "EmptyWrapperBox_Object" should be displayed on MB page
    When user clears "SearchBeneficiaryInput_Object" on MB page
    And user clicks on "AllTab_Object" on MB page
    Then "BeneficiaryList_Object" should be displayed on MB page
    When user clicks on "ShivalikTab_Object" on MB page
    Then "ShivalikTab_Object" should be displayed on MB page
    When user clicks on "OtherBankTab_Object" on MB page
    Then "OtherBankTab_Object" should be displayed on MB page
    When user clicks on "FilterIcon_Object" on MB page
    Then "AZSortText_Object" should be displayed on MB page

  @manage_beneficiary @MB_031
  Scenario: MB_031 - Verify Cooling Period and Favourite sections
    Then "CoolingPeriodText_Object" should be displayed on MB page
    And "NoBeneficiariesCoolingText_Object" should be displayed on MB page

  # ==============================================================================
  # MODULE 4: ADD BENEFICIARY - OTHER BANK
  # Form: Bank Account Number*, Re-enter Account*, IFSC Code*, Beneficiary Name*, Nickname
  # ==============================================================================

  @add_otherbank @MB_040
  Scenario: MB_040 - Click ADD, select Other Bank and verify form elements
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_OtherBankLabel_Object" on MB page
    Then "MB_OtherBankRadio_Object" should be selected by default on MB page
    And "MB_IFSCCodeInput_Object" should be displayed on MB page
    And "MB_IFSCCodeLabel_Object" should be displayed on MB page
    And "MB_SearchIFSCLink_Object" should be displayed on MB page
    And "MB_AccountNumberInput_Object" should be displayed on MB page
    And "MB_BeneficiaryNameInput_Object" should be displayed on MB page
    And "MB_NicknameInput_Object" should be displayed on MB page

  @add_otherbank @MB_041
  Scenario: MB_041 - Click CONTINUE with empty Other Bank form and validate errors
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_OtherBankLabel_Object" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then expected MB result from test case "MB_041" should be validated

  @add_otherbank @MB_042
  Scenario: MB_042 - Enter invalid IFSC code and validate error
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_OtherBankLabel_Object" on MB page
    And user enters MB test data "AccountNumber_Value" from test case "MB_042" in "MB_AccountNumberInput_Object" on MB page
    And user enters MB test data "ReEnterAccountNumber_Value" from test case "MB_042" in "MB_ReEnterAccountInput_Object" on MB page
    And user enters MB test data "IFSCCode_Value" from test case "MB_042" in "MB_IFSCCodeInput_Object" on MB page
    And user enters MB test data "BeneficiaryName_Value" from test case "MB_042" in "MB_BeneficiaryNameInput_Object" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then expected MB result from test case "MB_042" should be validated

  @add_otherbank @MB_043
  Scenario: MB_043 - Complete Other Bank form, verify confirm page high-level validation and OTP section
    When user clicks on "AddButton_Object" on MB page
    And user fills Other Bank beneficiary form with test case "MB_043" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then "MB_ConfirmTitle_Object" should be displayed on MB page
    And MB confirm page should display correct details for test case "MB_043"
    And "MB_OTPInput_Object" should be displayed on MB page
    And "MB_OTPTimerText_Object" should be displayed on MB page
    And MB SUBMIT button should be disabled

  @add_otherbank @MB_044
  Scenario: MB_044 - E2E Add Other Bank: fill form → confirm validate → OTP → success → Done → main
    When user clicks on "AddButton_Object" on MB page
    And user fills Other Bank beneficiary form with test case "MB_044" on MB page
    And user clicks on "MB_ContinueButton_Object" on MB page
    Then "MB_ConfirmTitle_Object" should be displayed on MB page
    And MB confirm page should display correct details for test case "MB_044"
    When user enters MB OTP from test case "MB_044"
    And user clicks on "MB_SubmitButton_Object" on MB page
    Then expected MB result from test case "MB_044" should be validated
    And "MB_DoneButton_Object" should be displayed on MB page
    When user clicks on "MB_DoneButton_Object" on MB page
    Then "PageTitle_Object" should be displayed on MB page

  @add_otherbank @MB_045
  Scenario: MB_045 - Open Search IFSC sidebar, verify elements and close
    When user clicks on "AddButton_Object" on MB page
    And user clicks on "MB_OtherBankLabel_Object" on MB page
    And user clicks on "MB_SearchIFSCLink_Object" on MB page
    Then "MB_SearchIFSCSidebarTitle_Object" should be displayed on MB page
    And "MB_SearchIFSCSelectBank_Object" should be displayed on MB page
    And "MB_SearchIFSCSelectState_Object" should be displayed on MB page
    And "MB_SearchIFSCSelectCity_Object" should be displayed on MB page
    And "MB_SearchIFSCSelectBranch_Object" should be displayed on MB page
    And "MB_SearchIFSCCloseIcon_Object" should be displayed on MB page
    When user clicks on "MB_SearchIFSCCloseIcon_Object" on MB page
    Then "MB_AddTitle_Object" should be displayed on MB page

  # ==============================================================================
  # MODULE 5: EDIT BENEFICIARY
  # Flow: Main → click beneficiary → Details → 3-dot → Edit → nickname → SAVE → OTP → success → Done
  # ==============================================================================

  @edit_beneficiary @MB_060
  Scenario: MB_060 - Click beneficiary, verify details page, 3-dot menu and Edit form
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    Then "MB_BeneficiaryDetailTitle_Object" should be displayed on MB page
    And "MB_BeneficiaryDetailName_Object" should be displayed on MB page
    And "MB_MoreSquareIcon_Object" should be displayed on MB page
    And "MB_VerifyBeneficiaryDetailButton_Object" should be displayed on MB page
    And "MB_SendMoneyButton_Object" should be displayed on MB page
    And MB beneficiary details should match test case "MB_060"
    When user clicks on "MB_MoreSquareIcon_Object" on MB page
    And user clicks on "MB_EditButton_Object" on MB page
    Then "MB_EditTitle_Object" should be displayed on MB page
    And "MB_EditNicknameInput_Object" should be displayed on MB page
    And "MB_SaveButton_Object" should be displayed on MB page

  @edit_beneficiary @MB_061
  Scenario: MB_061 - E2E Edit: beneficiary → 3-dot → Edit → nickname → SAVE → OTP → success → Done → main
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_MoreSquareIcon_Object" on MB page
    And user clicks on "MB_EditButton_Object" on MB page
    And user clears "MB_EditNicknameInput_Object" on MB page
    And user enters MB test data "Nickname_Value" from test case "MB_061" in "MB_EditNicknameInput_Object" on MB page
    And user clicks on "MB_SaveButton_Object" on MB page
    Then "MB_OTPInput_Object" should be displayed on MB page
    When user enters MB OTP from test case "MB_061"
    And user clicks on "MB_SubmitButton_Object" on MB page
    Then expected MB result from test case "MB_061" should be validated
    And "MB_DoneButton_Object" should be displayed on MB page
    When user clicks on "MB_DoneButton_Object" on MB page
    Then "PageTitle_Object" should be displayed on MB page

  @edit_beneficiary @MB_062
  Scenario: MB_062 - Edit nickname with empty value
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_MoreSquareIcon_Object" on MB page
    And user clicks on "MB_EditButton_Object" on MB page
    And user clears "MB_EditNicknameInput_Object" on MB page
    And user clicks on "MB_SaveButton_Object" on MB page
    Then MB nickname update should proceed or error should be shown

  # ==============================================================================
  # MODULE 6: DELETE BENEFICIARY
  # Flow: Main → click beneficiary → Details → 3-dot → Delete → Delete page → DELETE → OTP → success → Done
  # ==============================================================================

  @delete_beneficiary @MB_080
  Scenario: MB_080 - Click beneficiary, 3-dot, Delete and verify delete page elements
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_MoreSquareIcon_Object" on MB page
    And user clicks on "MB_DeleteButton_Object" on MB page
    Then "MB_DeleteConfirmTitle_Object" should be displayed on MB page
    And "MB_DeleteConfirmButton_Object" should be displayed on MB page

  @delete_beneficiary @MB_081
  Scenario: MB_081 - E2E Delete: beneficiary → 3-dot → Delete → DELETE → OTP → success → Done → main
    When user clicks on "MB_BeneficiaryRow_Object" on MB page
    And user clicks on "MB_MoreSquareIcon_Object" on MB page
    And user clicks on "MB_DeleteButton_Object" on MB page
    And user clicks on "MB_DeleteConfirmButton_Object" on MB page
    Then "MB_OTPInput_Object" should be displayed on MB page
    When user enters MB OTP from test case "MB_081"
    And user clicks on "MB_SubmitButton_Object" on MB page
    Then expected MB result from test case "MB_081" should be validated
    And "MB_DoneButton_Object" should be displayed on MB page
    When user clicks on "MB_DoneButton_Object" on MB page
    Then "PageTitle_Object" should be displayed on MB page

  @delete_beneficiary @MB_082
  Scenario: MB_082 - Verify page after all operations
    Then "PageTitle_Object" should be displayed on MB page
    And beneficiary list or no beneficiaries text should be displayed on MB page
