# language: en
@fundtransfer
Feature: Fund Transfer - Complete Module Testing
  Test all Fund Transfer functionality for Shivalik Bank Internet Banking
  Modules: FT Main Page, Transfer to Beneficiary, Quick Transfer, Self Account
  All test data from TestSuite.xlsx sheet "fundtransfer"

  Background:
    Given user is logged in successfully
    And user navigates to Fund Transfer from Dashboard

  # ==============================================================================
  # MODULE 1: FUND TRANSFER MAIN PAGE
  # Consolidated: FT_003+004+005 merged, FT_009+010+011 merged, FT_012-019 merged
  # Removed: FT_006/007/008 (clickable = tested by FT_020/021/022), FT_024 (tested by FT_003)
  # ==============================================================================

  @fundtransfer_mainpage @FT_001
  Scenario: FT_001 - Verify Fund Transfer page opens successfully
    Then "FT_PageTitle_Object" should be displayed on FT page

  @fundtransfer_mainpage @FT_002
  Scenario: FT_002 - Verify Back arrow is displayed on Fund Transfer page
    Then "FT_BackArrow_Object" should be displayed on FT page

  @fundtransfer_mainpage @FT_003
  Scenario: FT_003 - Verify all transfer cards displayed with details
    Then "TransferToBeneficiaryCard_Object" should be displayed on FT page
    And "TransferToBeneficiaryTitle_Object" should be displayed on FT page
    And "TransferToBeneficiaryDesc_Object" should be displayed on FT page
    And "QuickTransferCard_Object" should be displayed on FT page
    And "QuickTransferTitle_Object" should be displayed on FT page
    And "QuickTransferDesc_Object" should be displayed on FT page
    And "SelfAccountCard_Object" should be displayed on FT page
    And "SelfAccountTitle_Object" should be displayed on FT page
    And "SelfAccountDesc_Object" should be displayed on FT page

  @fundtransfer_mainpage @FT_009
  Scenario: FT_009 - Verify Recent Transactions and Services sections
    Then "FT_RecentTransactionsTitle_Object" should be displayed on FT page
    And "FT_TransactionAccountDropdown_Object" should be displayed on FT page
    And "FT_TransactionList_Object" should be displayed on FT page
    And "FT_ServicesTitle_Object" should be displayed on FT page

  @fundtransfer_mainpage @FT_012
  Scenario: FT_012 - Verify all service links displayed
    Then "ScheduleTransactionsService_Object" should be displayed on FT page
    And "AccountStatementService_Object" should be displayed on FT page
    And "FavoriteTransactionsService_Object" should be displayed on FT page
    And "MiniStatementService_Object" should be displayed on FT page
    And "EmailStatementService_Object" should be displayed on FT page
    And "ManageBeneficiaryService_Object" should be displayed on FT page
    And "RaiseDisputeService_Object" should be displayed on FT page
    And "ModifyTransactionLimitService_Object" should be displayed on FT page

  @fundtransfer_mainpage @FT_020
  Scenario: FT_020 - Verify clicking Transfer to Beneficiary card opens TB page
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    Then "TransferToBeneficiaryPageTitle_Object" should be displayed on TB page
    When user clicks on "GoBackButton_Object" on TB page
    And user clicks on Confirm button on cancel popup on TB page

  @fundtransfer_mainpage @FT_021
  Scenario: FT_021 - Verify clicking Quick Transfer card opens QT method selection
    When user clicks on "QuickTransferCard_Object" on FT page
    Then "QuickTransferPageTitle_Object" should be displayed on QT page
    When user clicks on "GoBackButton_Object" on QT page

  @fundtransfer_mainpage @FT_022
  Scenario: FT_022 - Verify clicking Self Account card opens SA sidebar
    When user clicks on "SelfAccountCard_Object" on FT page
    Then "SA_SidebarTitle_Object" should be displayed on FT page
    When user selects account from sidebar for test case "FT_SA_001"
    Then "SelfAccountPageTitle_Object" should be displayed on SA page
    When user clicks on "GoBackButton_Object" on SA page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @fundtransfer_mainpage @FT_023
  Scenario: FT_023 - Verify Back arrow navigates back from Fund Transfer page
    When user clicks on "GoBackButton_Object" on FT page

  # ==============================================================================
  # MODULE 2: TRANSFER TO BENEFICIARY - SHIVALIK BANK (FT_030 - FT_055)
  # Consolidated: FT_030+031+032+033 merged, FT_035-041 merged, FT_047+048 merged
  # Removed: FT_029 (same as FT_020)
  # All hardcoded values replaced with Excel data references
  # ==============================================================================

  @transfer_to_beneficiary @FT_030
  Scenario: FT_030 - Verify Transfer Type label and radio buttons on TB page
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    Then "TransferTypeLabel_Object" should be displayed on TB page
    And "ShivalikBankRadio_Object" should be selected by default on TB page
    And "OtherBankLabel_Object" should be displayed on TB page
    And only one radio should be selected between "ShivalikBankRadio_Object" and "OtherBankRadio_Object" on TB page

  @transfer_to_beneficiary @FT_034
  Scenario: FT_034 - Verify switching to Other Bank radio deselects Shivalik
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    And user clicks on "OtherBankLabel_Object" on TB page
    Then only one radio should be selected between "ShivalikBankRadio_Object" and "OtherBankRadio_Object" on TB page
    When user clicks on "GoBackButton_Object" on TB page
    And user clicks on Confirm button on cancel popup on TB page

  @transfer_to_beneficiary @FT_035
  Scenario: FT_035 - Verify all TB Shivalik form fields and buttons displayed
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    Then "SelectBeneficiaryInput_Object" should be displayed on TB page
    And "EnterAmountInput_Object" should be displayed on TB page
    And "RupeeSymbol_Object" should be displayed on TB page
    And "TransferFromDropdown_Object" should be displayed on TB page
    And "PaymentDateInput_Object" should be displayed on TB page
    And "RemarksInput_Object" should be displayed on TB page
    And "FT_CancelButton_Object" should be displayed on TB page
    And "FT_ContinueButton_Object" should be displayed on TB page

  @transfer_to_beneficiary @FT_042
  Scenario: FT_042 - Verify Amount field accepts numeric values
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    And user enters "Amount_Value" from test case "FT_042" in "EnterAmountInput_Object" on TB page
    Then "EnterAmountInput_Object" field should have value entered on TB page
    And "EnterAmountInput_Object" field should contain only numeric values on TB page
    When user clicks on "GoBackButton_Object" on TB page
    And user clicks on Confirm button on cancel popup on TB page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @transfer_to_beneficiary @FT_043
  Scenario: FT_043 - Verify Amount field rejects special characters
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    And user enters "Amount_Value" from test case "FT_043" in "EnterAmountInput_Object" on TB page
    Then "EnterAmountInput_Object" field should contain only numeric values on TB page
    When user clicks on "GoBackButton_Object" on TB page
    And user clicks on Confirm button on cancel popup on TB page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @transfer_to_beneficiary @FT_044
  Scenario: FT_044 - Verify mandatory field errors when CONTINUE clicked without filling
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    And user clicks on "FT_ContinueButton_Object" on TB page
    Then expected result from test case "FT_044" should be validated on TB page

  @transfer_to_beneficiary @FT_045
  Scenario: FT_045 - Verify Select Beneficiary accepts search input
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    And user enters "BeneficiarySearch_Value" from test case "FT_045" in "SelectBeneficiaryInput_Object" on TB page
    Then "SelectBeneficiaryInput_Object" field should have value entered on TB page
    When user clicks on "GoBackButton_Object" on TB page
    And user clicks on Confirm button on cancel popup on TB page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @transfer_to_beneficiary @FT_046
  Scenario: FT_046 - Verify beneficiary dropdown shows options
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    And user enters "BeneficiarySearch_Value" from test case "FT_045" in "SelectBeneficiaryInput_Object" on TB page
    Then "BeneficiaryDropdownItem_Object" should be displayed on TB page
    When user selects first beneficiary from dropdown on TB page
    And user clicks on "GoBackButton_Object" on TB page
    And user clicks on Confirm button on cancel popup on TB page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @transfer_to_beneficiary @FT_047
  Scenario: FT_047 - Verify Transfer From dropdown selection and Available Balance
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    And user selects first option from "TransferFromDropdown_Object" on TB page
    Then "TransferFromDropdown_Object" should have option selected on TB page
    When user enters "Amount_Value" from test case "FT_042" in "EnterAmountInput_Object" on TB page
    And user clicks on "GoBackButton_Object" on TB page
    And user clicks on Confirm button on cancel popup on TB page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @transfer_to_beneficiary @FT_049
  Scenario: FT_049 - Verify Payment Date is auto-populated with today
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    Then "PaymentDateInput_Object" should be auto-populated on TB page

  @transfer_to_beneficiary @FT_050
  Scenario: FT_050 - Verify Remarks field accepts input
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    And user enters "BeneficiarySearch_Value" from test case "FT_045" in "SelectBeneficiaryInput_Object" on TB page
    When user selects first beneficiary from dropdown on TB page
    And user enters "Amount_Value" from test case "FT_042" in "EnterAmountInput_Object" on TB page
    And user enters "Remarks_Value" from test case "FT_050" in "RemarksInput_Object" on TB page
    Then "RemarksInput_Object" field should have value entered on TB page
    When user clicks on "GoBackButton_Object" on TB page
    And user clicks on Confirm button on cancel popup on TB page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @transfer_to_beneficiary @FT_051
  Scenario: FT_051 - Verify Remarks field max 35 characters
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    And user enters "BeneficiarySearch_Value" from test case "FT_045" in "SelectBeneficiaryInput_Object" on TB page
    When user selects first beneficiary from dropdown on TB page
    And user enters "Amount_Value" from test case "FT_042" in "EnterAmountInput_Object" on TB page
    And user enters "Remarks_Value" from test case "FT_051" in "RemarksInput_Object" on TB page
    Then "RemarksInput_Object" field value should have maximum 35 characters on TB page
    When user clicks on "GoBackButton_Object" on TB page
    And user clicks on Confirm button on cancel popup on TB page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @transfer_to_beneficiary @FT_052
  Scenario: FT_052 - Verify CAUTION popup displayed when navigating back
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    And user enters "BeneficiarySearch_Value" from test case "FT_045" in "SelectBeneficiaryInput_Object" on TB page
    When user selects first beneficiary from dropdown on TB page
    And user enters "Amount_Value" from test case "FT_042" in "EnterAmountInput_Object" on TB page
    When user clicks on "GoBackButton_Object" on TB page
    Then cancel popup should be displayed on TB page
    Then expected result from test case "FT_052" should be validated on TB page
    And user clicks on Confirm button on cancel popup on TB page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @transfer_to_beneficiary @FT_053
  Scenario: FT_053 - Verify CAUTION popup "Cancel button" keeps user on TB page
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    And user enters "BeneficiarySearch_Value" from test case "FT_045" in "SelectBeneficiaryInput_Object" on TB page
    When user selects first beneficiary from dropdown on TB page
    And user enters "Amount_Value" from test case "FT_042" in "EnterAmountInput_Object" on TB page
    When user clicks on "GoBackButton_Object" on TB page
    When user clicks on Cancel button on cancel popup on TB page
    Then "TransferToBeneficiaryPageTitle_Object" should be displayed on TB page

  @transfer_to_beneficiary @FT_054
  Scenario: FT_054 - Verify CAUTION popup Confirm button redirects to FT page
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    And user enters "BeneficiarySearch_Value" from test case "FT_045" in "SelectBeneficiaryInput_Object" on TB page
    When user selects first beneficiary from dropdown on TB page
    And user enters "Amount_Value" from test case "FT_042" in "EnterAmountInput_Object" on TB page
    When user clicks on "GoBackButton_Object" on TB page
    And user clicks on Confirm button on cancel popup on TB page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @transfer_to_beneficiary @FT_055 
  Scenario: FT_055 - E2E TB Shivalik: form fill, confirm, OTP, success validation
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    And user fills Transfer to Beneficiary form with test case "FT_TB_001" on TB page
    And user clicks on "FT_ContinueButton_Object" on TB page
    Then confirm page should display correct details for test case "FT_TB_001"
    When user enters OTP from test case "FT_TB_001" on OTP page
    And user clicks on "FT_SubmitButton_Object" on OTP page
    Then success page should display correct details for test case "FT_TB_001"
    When user clicks on "FT_DoneButton_Object" on SUCCESS page
    Then "FT_PageTitle_Object" should be displayed on FT page

  # ==============================================================================
  # MODULE 2B: TRANSFER TO BENEFICIARY - OTHER BANK (FT_082 - FT_097)
  # Consolidated: FT_082+083 merged, FT_087+088 merged
  # Removed: FT_091 (same as FT_086), FT_093 (same errors as FT_044/085)
  # ==============================================================================

  @transfer_to_beneficiary @FT_082
  Scenario: FT_082 - Verify Other Bank shows NEFT/IMPS/RTGS with NEFT selected by default
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    And user clicks on "OtherBankLabel_Object" on TB page
    Then "NEFTLabel_Object" should be displayed on TB page
    And "IMPSLabel_Object" should be displayed on TB page
    And "RTGSLabel_Object" should be displayed on TB page
    And "NEFTRadio_Object" should be selected by default on TB page

  @transfer_to_beneficiary @FT_084
  Scenario: FT_084 - Verify switching between NEFT/IMPS/RTGS
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    And user clicks on "OtherBankLabel_Object" on TB page
    And user enters "BeneficiarySearch_Value" from test case "FT_TB_OB_NEFT_001" in "SelectBeneficiaryInput_Object" on TB page
    When user selects first beneficiary from dropdown on TB page
    And user clicks on "IMPSLabel_Object" on TB page
    And user clicks on "RTGSLabel_Object" on TB page
    And user clicks on "NEFTLabel_Object" on TB page
    When user enters "Amount_Value" from test case "FT_042" in "EnterAmountInput_Object" on TB page
    And user clicks on "GoBackButton_Object" on TB page
    And user clicks on Confirm button on cancel popup on TB page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @transfer_to_beneficiary @FT_085
  Scenario: FT_085 - Verify Other Bank NEFT mandatory field errors
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    And user clicks on "OtherBankLabel_Object" on TB page
    And user clicks on "FT_ContinueButton_Object" on TB page
    Then expected result from test case "FT_085" should be validated on TB page
    When user clicks on "GoBackButton_Object" on TB page
    And user clicks on Confirm button on cancel popup on TB page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @transfer_to_beneficiary @FT_086
  Scenario: FT_086 - Verify NEFT mode shows Payment Date and Remarks fields
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    And user clicks on "OtherBankLabel_Object" on TB page
    Then "PaymentDateInput_Object" should be displayed on TB page
    And "RemarksInput_Object" should be displayed on TB page

  @transfer_to_beneficiary @FT_087
  Scenario: FT_087 - Verify IMPS mode hides Payment Date and Remarks fields
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    And user clicks on "OtherBankLabel_Object" on TB page
    And user clicks on "IMPSLabel_Object" on TB page
    Then "PaymentDateInput_Object" should not be displayed on TB page
    And "RemarksInput_Object" should not be displayed on TB page

  @transfer_to_beneficiary @FT_089
  Scenario: FT_089 - Verify IMPS mode shows charges warning
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    And user clicks on "OtherBankLabel_Object" on TB page
    And user clicks on "IMPSLabel_Object" on TB page
    Then expected result from test case "FT_089" should be validated on TB page

  @transfer_to_beneficiary @FT_090
  Scenario: FT_090 - Verify IMPS mode shows Select beneficiary, Amount and Transfer from
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    And user clicks on "OtherBankLabel_Object" on TB page
    And user clicks on "IMPSLabel_Object" on TB page
    Then "SelectBeneficiaryInput_Object" should be displayed on TB page
    And "EnterAmountInput_Object" should be displayed on TB page
    And "TransferFromDropdown_Object" should be displayed on TB page

  @transfer_to_beneficiary @FT_092
  Scenario: FT_092 - Verify switching from IMPS to NEFT restores Payment Date and Remarks
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    And user clicks on "OtherBankLabel_Object" on TB page
    And user clicks on "IMPSLabel_Object" on TB page
    Then "PaymentDateInput_Object" should not be displayed on TB page
    And "RemarksInput_Object" should not be displayed on TB page
    When user clicks on "NEFTLabel_Object" on TB page
    Then "PaymentDateInput_Object" should be displayed on TB page
    And "RemarksInput_Object" should be displayed on TB page

  @transfer_to_beneficiary @FT_094
  Scenario: FT_094 - Verify Other Bank CAUTION popup on back navigation
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    And user clicks on "OtherBankLabel_Object" on TB page
    And user enters "BeneficiarySearch_Value" from test case "FT_TB_OB_NEFT_001" in "SelectBeneficiaryInput_Object" on TB page
    When user selects first beneficiary from dropdown on TB page
    And user enters "Amount_Value" from test case "FT_042" in "EnterAmountInput_Object" on TB page
    When user clicks on "GoBackButton_Object" on TB page
    Then cancel popup should be displayed on TB page
    Then expected result from test case "FT_094" should be validated on TB page
    And user clicks on Confirm button on cancel popup on TB page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @transfer_to_beneficiary @FT_095
  Scenario: FT_095 - E2E Other Bank TB NEFT: form fill, confirm, OTP, success validation
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    And user fills Transfer to Beneficiary form with test case "FT_TB_OB_NEFT_001" on TB page
    And user clicks on "FT_ContinueButton_Object" on TB page
    Then confirm page should display correct details for test case "FT_TB_OB_NEFT_001"
    When user enters OTP from test case "FT_TB_OB_NEFT_001" on OTP page
    And user clicks on "FT_SubmitButton_Object" on OTP page
    Then success page should display correct details for test case "FT_TB_OB_NEFT_001"
    When user clicks on "FT_DoneButton_Object" on SUCCESS page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @transfer_to_beneficiary @FT_096
  Scenario: FT_096 - E2E Other Bank TB IMPS: form fill, confirm, OTP, success validation
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    And user fills Transfer to Beneficiary form with test case "FT_TB_OB_IMPS_001" on TB page
    And user clicks on "FT_ContinueButton_Object" on TB page
    Then confirm page should display correct details for test case "FT_TB_OB_IMPS_001"
    When user enters OTP from test case "FT_TB_OB_IMPS_001" on OTP page
    And user clicks on "FT_SubmitButton_Object" on OTP page
    Then success page should display correct details for test case "FT_TB_OB_IMPS_001"
    When user clicks on "FT_DoneButton_Object" on SUCCESS page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @transfer_to_beneficiary @FT_097
  Scenario: FT_097 - E2E Other Bank TB RTGS: form fill, confirm, OTP, success validation
    When user clicks on "TransferToBeneficiaryCard_Object" on FT page
    And user fills Transfer to Beneficiary form with test case "FT_TB_OB_RTGS_001" on TB page
    And user clicks on "FT_ContinueButton_Object" on TB page
    Then confirm page should display correct details for test case "FT_TB_OB_RTGS_001"
    When user enters OTP from test case "FT_TB_OB_RTGS_001" on OTP page
    And user clicks on "FT_SubmitButton_Object" on OTP page
    Then success page should display correct details for test case "FT_TB_OB_RTGS_001"
    When user clicks on "FT_DoneButton_Object" on SUCCESS page
    Then "FT_PageTitle_Object" should be displayed on FT page

  # ==============================================================================
  # MODULE 3: QUICK TRANSFER (FT_143 - FT_205)
  # Consolidated: FT_143+144+145 merged, FT_148+149 merged, FT_168+170 merged
  # Removed: FT_161 (same as FT_043), FT_201 (tested in FT_148)
  # All hardcoded values replaced with Excel data references
  # ==============================================================================

  @quick_transfer @FT_143
  Scenario: FT_143 - Verify Quick Transfer page opens with method selection cards
    When user clicks on "QuickTransferCard_Object" on FT page
    Then "QuickTransferPageTitle_Object" should be displayed on QT page
    And "ByMobileNumberCard_Object" should be displayed on QT page
    And "ByMobileNumberTitle_Object" should be displayed on QT page
    And "ByMobileNumberDesc_Object" should be displayed on QT page
    And "ByAccountDetailsCard_Object" should be displayed on QT page
    And "ByAccountDetailsTitle_Object" should be displayed on QT page
    And "ByAccountDetailsDesc_Object" should be displayed on QT page

  @quick_transfer @FT_146
  Scenario: FT_146 - Verify By Account Details page opens with Shivalik Bank selected
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    Then "QT_ByAccountDetailsPageTitle_Object" should be displayed on QT page
    And "ShivalikBankRadio_Object" should be selected by default on QT page

  @quick_transfer @FT_147
  Scenario: FT_147 - Verify all Shivalik Bank form fields displayed
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    Then "QT_AccountNumberInput_Object" should be displayed on QT page
    And "QT_ReEnterAccountInput_Object" should be displayed on QT page
    And "QT_RecipientNameInput_Object" should be displayed on QT page
    And "EnterAmountInput_Object" should be displayed on QT page
    And "TransferFromDropdown_Object" should be displayed on QT page
    And "RemarksInput_Object" should be displayed on QT page
    When user clicks on "GoBackButton_Object" on QT page
    And user clicks on Confirm button on cancel popup on QT page

  @quick_transfer @FT_148
  Scenario: FT_148 - Verify IFSC not shown for Shivalik, shown for Other Bank
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    Then "QT_IFSCCodeInput_Object" should not be displayed on QT page
    When user clicks on "OtherBankLabel_Object" on QT page
    Then "QT_IFSCCodeInput_Object" should be displayed on QT page
    And "QT_SearchIFSCLink_Object" should be displayed on QT page
    When user clicks on "GoBackButton_Object" on QT page
    And user clicks on Confirm button on cancel popup on QT page

  @quick_transfer @FT_150
  Scenario: FT_150 - Verify mandatory field errors when CONTINUE clicked without filling
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    And user clicks on "FT_ContinueButton_Object" on QT page
    Then expected result from test case "FT_150" should be validated on QT page

  @quick_transfer @FT_151
  Scenario: FT_151 - Verify 2-digit account number shows validation error
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    And user enters "BankAccountNumber_Value" from test case "FT_151" in "QT_AccountNumberInput_Object" on QT page
    And user clicks on "QT_ReEnterAccountInput_Object" on QT page
    Then field error should be displayed for "QT_AccountNumberInput_Object" on QT page

  @quick_transfer @FT_152
  Scenario: FT_152 - Verify 3-digit account number does not show error
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    And user enters "BankAccountNumber_Value" from test case "FT_152" in "QT_AccountNumberInput_Object" on QT page
    And user clicks on "QT_ReEnterAccountInput_Object" on QT page
    Then field error should not be displayed for "QT_AccountNumberInput_Object" on QT page
    When user clicks on "GoBackButton_Object" on QT page
    And user clicks on Confirm button on cancel popup on QT page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @quick_transfer @FT_153
  Scenario: FT_153 - Verify account number accepts max 12 digits for Shivalik
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    And user enters "BankAccountNumber_Value" from test case "FT_153" in "QT_AccountNumberInput_Object" on QT page
    Then "QT_AccountNumberInput_Object" field value should have maximum 12 characters on QT page

  @quick_transfer @FT_154
  Scenario: FT_154 - Verify account number accepts only numeric values
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    And user enters "BankAccountNumber_Value" from test case "FT_154" in "QT_AccountNumberInput_Object" on QT page
    Then "QT_AccountNumberInput_Object" field should contain only numeric values on QT page
    When user clicks on "GoBackButton_Object" on QT page
    And user clicks on Confirm button on cancel popup on QT page

  @quick_transfer @FT_155
  Scenario: FT_155 - Verify account number is masked by default
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    And user enters "BankAccountNumber_Value" from test case "FT_155" in "QT_AccountNumberInput_Object" on QT page
    Then "QT_AccountNumberInput_Object" field should be masked on QT page
    When user clicks on "GoBackButton_Object" on QT page
    And user clicks on Confirm button on cancel popup on QT page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @quick_transfer @FT_156 @skip
  Scenario: FT_156 - Verify account number unmasked after eye icon click
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    And user enters "BankAccountNumber_Value" from test case "FT_155" in "QT_AccountNumberInput_Object" on QT page
    And user clicks on "QT_AccountNumberEyeIcon_Object" on QT page
    Then "QT_AccountNumberInput_Object" field should not be masked on QT page

  @quick_transfer @FT_157
  Scenario: FT_157 - Verify Re-enter account number field accepts value
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    And user enters "BankAccountNumber_Value" from test case "FT_157" in "QT_AccountNumberInput_Object" on QT page
    And user enters "ReEnterAccountNumber_Value" from test case "FT_157" in "QT_ReEnterAccountInput_Object" on QT page
    Then "QT_ReEnterAccountInput_Object" field should have value entered on QT page

  @quick_transfer @FT_158
  Scenario: FT_158 - Verify mismatch error when account numbers do not match
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    And user enters "BankAccountNumber_Value" from test case "FT_158" in "QT_AccountNumberInput_Object" on QT page
    And user enters "ReEnterAccountNumber_Value" from test case "FT_158" in "QT_ReEnterAccountInput_Object" on QT page
    And user clicks on "FT_ContinueButton_Object" on QT page
    Then expected result from test case "FT_158" should be validated on QT page

  @quick_transfer @FT_159
  Scenario: FT_159 - Verify Re-enter account accepts only numeric values
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    And user enters "ReEnterAccountNumber_Value" from test case "FT_159" in "QT_ReEnterAccountInput_Object" on QT page
    Then "QT_ReEnterAccountInput_Object" field should contain only numeric values on QT page

  @quick_transfer @FT_160
  Scenario: FT_160 - Verify Recipient Name accepts only alphabet values
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    And user enters "RecipientName_Value" from test case "FT_160" in "QT_RecipientNameInput_Object" on QT page
    Then "QT_RecipientNameInput_Object" field should contain only alphabet values on QT page

  @quick_transfer @FT_162
  Scenario: FT_162 - Verify Amount minimum 100 rupees
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    And user enters "Amount_Value" from test case "FT_162" in "EnterAmountInput_Object" on QT page
    Then "EnterAmountInput_Object" field value should be minimum 100 on QT page

  @quick_transfer @FT_163
  Scenario: FT_163 - Verify Transfer From dropdown allows account selection
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    And user selects first option from "TransferFromDropdown_Object" on QT page
    Then "TransferFromDropdown_Object" should have option selected on QT page

  @quick_transfer @FT_164
  Scenario: FT_164 - Verify Remarks field accepts input
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    And user enters "Remarks_Value" from test case "FT_164" in "RemarksInput_Object" on QT page
    Then "RemarksInput_Object" field should have value entered on QT page

  @quick_transfer @FT_165
  Scenario: FT_165 - Verify Cancel popup "Cancel button" keeps user on QT page
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    And user enters "BankAccountNumber_Value" from test case "FT_155" in "QT_AccountNumberInput_Object" on QT page
    And user clicks on "GoBackButton_Object" on QT page
    Then cancel popup should be displayed on QT page
    Then expected result from test case "FT_165" should be validated on QT page
    When user clicks on Cancel button on cancel popup on QT page
    Then "QT_ByAccountDetailsPageTitle_Object" should be displayed on QT page

  @quick_transfer @FT_166
  Scenario: FT_166 - Verify Cancel popup Confirm button redirects to FT page
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    And user enters "BankAccountNumber_Value" from test case "FT_155" in "QT_AccountNumberInput_Object" on QT page
    And user clicks on "GoBackButton_Object" on QT page
    And user clicks on Confirm button on cancel popup on QT page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @quick_transfer @FT_167
  Scenario: FT_167 - E2E Quick Transfer Shivalik: form fill, confirm, OTP, success validation
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    And user fills Quick Transfer form with test case "FT_QT_SB_001" on QT page
    And user clicks on "FT_ContinueButton_Object" on QT page
    Then confirm page should display correct details for test case "FT_QT_SB_001"
    When user enters OTP from test case "FT_QT_SB_001" on OTP page
    And user clicks on "FT_SubmitButton_Object" on OTP page
    Then success page should display correct details for test case "FT_QT_SB_001"
    When user clicks on "FT_DoneButton_Object" on SUCCESS page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @quick_transfer @FT_168
  Scenario: FT_168 - Verify QT does not show Payment Date or NEFT/IMPS/RTGS
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    Then "PaymentDateInput_Object" should not be displayed on QT page
    And "NEFTLabel_Object" should not be displayed on QT page
    And "IMPSLabel_Object" should not be displayed on QT page
    And "RTGSLabel_Object" should not be displayed on QT page

  @quick_transfer @FT_169
  Scenario: FT_169 - Verify QT Payment Date field NOT displayed for Other Bank
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    And user clicks on "OtherBankLabel_Object" on QT page
    Then "PaymentDateInput_Object" should not be displayed on QT page
    When user clicks on "GoBackButton_Object" on QT page
    And user clicks on Confirm button on cancel popup on QT page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @quick_transfer @FT_171
  Scenario: FT_171 - Verify QT Amount max limit 25000
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    And user enters "Amount_Value" from test case "FT_171" in "EnterAmountInput_Object" on QT page
    And user clicks on "QT_RecipientNameInput_Object" on QT page
    Then field error should be displayed for "EnterAmountInput_Object" on QT page

  # ---- Quick Transfer - Other Bank ----

  @quick_transfer @FT_200
  Scenario: FT_200 - Verify Other Bank form fields displayed with IFSC
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    And user clicks on "OtherBankLabel_Object" on QT page
    Then "QT_AccountNumberInput_Object" should be displayed on QT page
    And "QT_ReEnterAccountInput_Object" should be displayed on QT page
    And "QT_IFSCCodeInput_Object" should be displayed on QT page
    And "QT_RecipientNameInput_Object" should be displayed on QT page
    And "EnterAmountInput_Object" should be displayed on QT page
    And "TransferFromDropdown_Object" should be displayed on QT page
    And "RemarksInput_Object" should be displayed on QT page

  @quick_transfer @FT_202
  Scenario: FT_202 - Verify Invalid IFSC code shows error
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    And user clicks on "OtherBankLabel_Object" on QT page
    And user enters "IFSCCode_Value" from test case "FT_202" in "QT_IFSCCodeInput_Object" on QT page
    And user clicks on "QT_RecipientNameInput_Object" on QT page
    Then field error should be displayed for "QT_IFSCCodeInput_Object" on QT page
    When user clicks on "GoBackButton_Object" on QT page
    And user clicks on Confirm button on cancel popup on QT page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @quick_transfer @FT_203
  Scenario: FT_203 - E2E Quick Transfer Other Bank: form fill, confirm, OTP, success validation
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    And user fills Quick Transfer form with test case "FT_QT_OB_001" on QT page
    And user clicks on "FT_ContinueButton_Object" on QT page
    Then confirm page should display correct details for test case "FT_QT_OB_001"
    When user enters OTP from test case "FT_QT_OB_001" on OTP page
    And user clicks on "FT_SubmitButton_Object" on OTP page
    Then success page should display correct details for test case "FT_QT_OB_001"
    When user clicks on "FT_DoneButton_Object" on SUCCESS page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @quick_transfer @FT_204
  Scenario: FT_204 - Verify Other Bank mandatory field errors with IFSC
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    And user clicks on "OtherBankLabel_Object" on QT page
    And user clicks on "FT_ContinueButton_Object" on QT page
    Then expected result from test case "FT_204" should be validated on QT page
    When user clicks on "GoBackButton_Object" on QT page
    And user clicks on Confirm button on cancel popup on QT page

  @quick_transfer @FT_205
  Scenario: FT_205 - Verify IFSC max 11 characters
    When user clicks on "QuickTransferCard_Object" on FT page
    And user clicks on "ByAccountDetailsCard_Object" on QT page
    And user clicks on "OtherBankLabel_Object" on QT page
    And user enters "IFSCCode_Value" from test case "FT_205" in "QT_IFSCCodeInput_Object" on QT page
    Then "QT_IFSCCodeInput_Object" field value should have maximum 11 characters on QT page

  # ==============================================================================
  # MODULE 4: SELF ACCOUNT TRANSFER (FT_264 - FT_284)
  # Consolidated: FT_266-270 merged, FT_275+276 merged
  # Removed: FT_273 (same as FT_043)
  # All SA scenarios now include sidebar handling
  # ==============================================================================

  @self_account @FT_264
  Scenario: FT_264 - Verify Self Account page opens successfully via sidebar
    When user clicks on "SelfAccountCard_Object" on FT page
    Then "SA_SidebarTitle_Object" should be displayed on FT page
    When user selects account from sidebar for test case "FT_SA_001"
    Then "SelfAccountPageTitle_Object" should be displayed on SA page
    When user clicks on "GoBackButton_Object" on SA page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @self_account @FT_265
  Scenario: FT_265 - Verify Transfer To field is displayed and auto-populated
    When user clicks on "SelfAccountCard_Object" on FT page
    Then "SA_SidebarTitle_Object" should be displayed on FT page
    When user selects account from sidebar for test case "FT_SA_001"
    Then "SelfAccountPageTitle_Object" should be displayed on SA page
    Then "TransferToInput_Object" should be displayed on SA page
    And "TransferToInput_Object" should be auto-populated on SA page
    When user clicks on "GoBackButton_Object" on SA page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @self_account @FT_266
  Scenario: FT_266 - Verify all SA form fields displayed
    When user clicks on "SelfAccountCard_Object" on FT page
    Then "SA_SidebarTitle_Object" should be displayed on FT page
    When user selects account from sidebar for test case "FT_SA_001"
    Then "ChangeAccountButton_Object" should be displayed on SA page
    And "SelfTransferAmountInput_Object" should be displayed on SA page
    And "TransferFromDropdown_Object" should be displayed on SA page
    And "PaymentDateInput_Object" should be displayed on SA page
    And "RemarksInput_Object" should be displayed on SA page

  @self_account @FT_271
  Scenario: FT_271 - Verify CANCEL and CONTINUE buttons on SA page
    When user clicks on "SelfAccountCard_Object" on FT page
    Then "SA_SidebarTitle_Object" should be displayed on FT page
    When user selects account from sidebar for test case "FT_SA_001"
    Then "FT_CancelButton_Object" should be displayed on SA page
    And "FT_ContinueButton_Object" should be displayed on SA page

  @self_account @FT_272
  Scenario: FT_272 - Verify Amount field accepts numeric values on SA
    When user clicks on "SelfAccountCard_Object" on FT page
    Then "SA_SidebarTitle_Object" should be displayed on FT page
    When user selects account from sidebar for test case "FT_SA_001"
    And user enters "Amount_Value" from test case "FT_272" in "SelfTransferAmountInput_Object" on SA page
    Then "SelfTransferAmountInput_Object" field should have value entered on SA page
    And "SelfTransferAmountInput_Object" field should contain only numeric values on SA page
    When user clicks on "GoBackButton_Object" on SA page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @self_account @FT_274
  Scenario: FT_274 - Verify mandatory field errors on SA when CONTINUE clicked empty
    When user clicks on "SelfAccountCard_Object" on FT page
    Then "SA_SidebarTitle_Object" should be displayed on FT page
    When user selects account from sidebar for test case "FT_SA_001"
    And user clicks on "FT_ContinueButton_Object" on SA page
    Then expected result from test case "FT_274" should be validated on SA page
    When user clicks on "GoBackButton_Object" on SA page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @self_account @FT_275
  Scenario: FT_275 - Verify Transfer From dropdown selection and Available Balance on SA
    When user clicks on "SelfAccountCard_Object" on FT page
    Then "SA_SidebarTitle_Object" should be displayed on FT page
    When user selects account from sidebar for test case "FT_SA_001"
    And user selects first option from "TransferFromDropdown_Object" on SA page
    Then "TransferFromDropdown_Object" should have option selected on SA page
    When user clicks on "GoBackButton_Object" on SA page

  @self_account @FT_277
  Scenario: FT_277 - Verify Payment Date auto-populated on SA
    When user clicks on "SelfAccountCard_Object" on FT page
    Then "SA_SidebarTitle_Object" should be displayed on FT page
    When user selects account from sidebar for test case "FT_SA_001"
    Then "PaymentDateInput_Object" should be auto-populated on SA page

  @self_account @FT_278
  Scenario: FT_278 - Verify Remarks field accepts input on SA
    When user clicks on "SelfAccountCard_Object" on FT page
    Then "SA_SidebarTitle_Object" should be displayed on FT page
    When user selects account from sidebar for test case "FT_SA_001"
    And user enters "Remarks_Value" from test case "FT_278" in "RemarksInput_Object" on SA page
    Then "RemarksInput_Object" field should have value entered on SA page
    When user clicks on "GoBackButton_Object" on SA page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @self_account @FT_279
  Scenario: FT_279 - Verify Remarks max 120 characters on SA
    When user clicks on "SelfAccountCard_Object" on FT page
    Then "SA_SidebarTitle_Object" should be displayed on FT page
    When user selects account from sidebar for test case "FT_SA_001"
    And user enters "Remarks_Value" from test case "FT_279" in "RemarksInput_Object" on SA page
    Then "RemarksInput_Object" field value should have maximum 120 characters on SA page

  @self_account @FT_280
  Scenario: FT_280 - Verify Cancel button on SA redirects to FT page without popup
    When user clicks on "SelfAccountCard_Object" on FT page
    Then "SA_SidebarTitle_Object" should be displayed on FT page
    When user selects account from sidebar for test case "FT_SA_001"
    And user clicks on "FT_CancelButton_Object" on SA page
    Then "FT_PageTitle_Object" should be displayed on FT page

  @self_account @FT_284
  Scenario: FT_284 - E2E Self Account: sidebar, form fill, confirm, OTP, success validation
    When user clicks on "SelfAccountCard_Object" on FT page
    Then "SA_SidebarTitle_Object" should be displayed on FT page
    When user selects account from sidebar for test case "FT_SA_001"
    And user fills Self Account form with test case "FT_SA_001" on SA page
    And user clicks on "FT_ContinueButton_Object" on SA page
    Then confirm page should display correct details for test case "FT_SA_001"
    When user enters OTP from test case "FT_SA_001" on OTP page
    And user clicks on "FT_SubmitButton_Object" on OTP page
    Then success page should display correct details for test case "FT_SA_001"
    When user clicks on "FT_DoneButton_Object" on SUCCESS page
    Then "FT_PageTitle_Object" should be displayed on FT page

