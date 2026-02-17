package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * ManageBeneficiariesPage - Page Object for Manage Beneficiaries Module
 * Covers: Listing page, Add form, Confirm/OTP, Success, Edit, Delete flows
 */
public class ManageBeneficiariesPage extends BasePage {

    public ManageBeneficiariesPage() {
        super();
    }

    private String key(String objectName) {
        return objectName;
    }

    // ========== Wait / Load Methods ==========

    public void waitForManageBeneficiaryPageLoad() {
        try {
            By locator = orManager.getLocator(key("PageTitle_Object"));
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(15))
                    .until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(locator));
            logger.info("Manage Beneficiary page loaded successfully");
        } catch (Exception e) {
            logger.warn("Manage Beneficiary page load wait timed out");
        }
    }

    public void waitForAddFormLoad() {
        try {
            waitForElementVisible(key("MB_AddTitle_Object"), 15);
        } catch (Exception e) {
            logger.warn("Add beneficiary form load wait timed out");
        }
    }

    public void waitForConfirmPageLoad() {
        try {
            waitForElementVisible(key("MB_ConfirmTitle_Object"), 15);
        } catch (Exception e) {
            logger.warn("Confirm page load wait timed out");
        }
    }

    public void waitForSuccessPageLoad() {
        try {
            waitForElementVisible(key("MB_SuccessTitle_Object"), 15);
        } catch (Exception e) {
            logger.warn("Success page load wait timed out");
        }
    }

    public void waitForOTPInput() {
        try {
            waitForElementVisible(key("MB_OTPInput_Object"), 15);
        } catch (Exception e) {
            logger.warn("OTP input wait timed out");
        }
    }

    public void waitForEditPageLoad() {
        try {
            waitForElementVisible(key("MB_EditTitle_Object"), 15);
        } catch (Exception e) {
            logger.warn("Edit page load wait timed out");
        }
    }

    public void waitForDeletePageLoad() {
        try {
            waitForElementVisible(key("MB_DeleteConfirmTitle_Object"), 15);
        } catch (Exception e) {
            logger.warn("Delete page load wait timed out");
        }
    }

    public void waitForBeneficiaryDetailLoad() {
        try {
            waitForElementVisible(key("MB_BeneficiaryDetailTitle_Object"), 15);
        } catch (Exception e) {
            logger.warn("Beneficiary detail page load wait timed out");
        }
    }

    // ========== Navigation Actions ==========

    public void clickBackArrow() {
        try {
            click(key("MB_AddBackArrow_Object"));
        } catch (Exception e) {
            jsClick(key("BackArrow_Object"));
        }
    }

    public void clickAddBackArrow() {
        click(key("MB_AddBackArrow_Object"));
    }

    public void clickHomeNav() {
        click(key("HomeNavLink_Object"));
    }

    public void clickBillsNav() {
        click(key("BillsNavLink_Object"));
    }

    public void clickOffersNav() {
        click(key("OffersNavLink_Object"));
    }

    public void clickProfileButton() {
        click(key("ProfileButton_Object"));
    }

    public void clickNotificationIcon() {
        click(key("NotificationIcon_Object"));
    }

    public void navigateBack() {
        driver.navigate().back();
    }

    // ========== Tab Actions ==========

    public void clickAllTab() {
        click(key("AllTab_Object"));
    }

    public void clickShivalikTab() {
        click(key("ShivalikTab_Object"));
    }

    public void clickOtherBankTab() {
        click(key("OtherBankTab_Object"));
    }

    // ========== Search Actions ==========

    public void enterSearchBeneficiary(String searchText) {
        type(key("SearchBeneficiaryInput_Object"), searchText);
    }

    public void clearSearchBeneficiary() {
        clear(key("SearchBeneficiaryInput_Object"));
    }

    public void clickSearchButton() {
        click(key("SearchButton_Object"));
    }

    // ========== Listing Page Actions ==========

    public void clickAddButton() {
        click(key("AddButton_Object"));
    }

    public void clickFilterIcon() {
        click(key("FilterIcon_Object"));
    }

    public void clickFirstBeneficiaryRow() {
        click(key("MB_BeneficiaryRow_Object"));
    }

    // ========== Add Beneficiary Form Actions ==========

    public void clickShivalikBankRadio() {
        jsClick(key("MB_ShivalikBankLabel_Object"));
    }

    public void clickOtherBankRadio() {
        jsClick(key("MB_OtherBankLabel_Object"));
    }

    public void enterAccountNumber(String accountNumber) {
        type(key("MB_AccountNumberInput_Object"), accountNumber);
    }

    public void clearAccountNumber() {
        clear(key("MB_AccountNumberInput_Object"));
    }

    public void clickAccountNumberEyeIcon() {
        click(key("MB_AccountNumberEyeIcon_Object"));
    }

    public void enterReEnterAccountNumber(String accountNumber) {
        type(key("MB_ReEnterAccountInput_Object"), accountNumber);
    }

    public void clearReEnterAccountNumber() {
        clear(key("MB_ReEnterAccountInput_Object"));
    }

    public void enterIFSCCode(String ifscCode) {
        type(key("MB_IFSCCodeInput_Object"), ifscCode);
    }

    public void clearIFSCCode() {
        clear(key("MB_IFSCCodeInput_Object"));
    }

    public void clickSearchIFSCLink() {
        click(key("MB_SearchIFSCLink_Object"));
    }

    public void enterBeneficiaryName(String name) {
        type(key("MB_BeneficiaryNameInput_Object"), name);
    }

    public void clearBeneficiaryName() {
        clear(key("MB_BeneficiaryNameInput_Object"));
    }

    public void clickVerifyBeneficiaryButton() {
        click(key("MB_VerifyBeneficiaryButton_Object"));
    }

    public void enterNickname(String nickname) {
        type(key("MB_NicknameInput_Object"), nickname);
    }

    public void clearNickname() {
        clear(key("MB_NicknameInput_Object"));
    }

    public void clickCancelButton() {
        click(key("MB_CancelButton_Object"));
    }

    public void clickContinueButton() {
        click(key("MB_ContinueButton_Object"));
    }

    // ========== Confirm Page Actions ==========

    public void clickSubmitButton() {
        click(key("MB_SubmitButton_Object"));
    }

    public void enterOTP(String otp) {
        type(key("MB_OTPInput_Object"), otp);
    }

    public void clearOTP() {
        clear(key("MB_OTPInput_Object"));
    }

    public void clickResendOTPLink() {
        click(key("MB_ResendOTPLink_Object"));
    }

    // ========== Success Page Actions ==========

    public void clickDoneButton() {
        click(key("MB_DoneButton_Object"));
    }

    // ========== Edit/Delete Actions ==========

    public void clickMoreSquareIcon() {
        click(key("MB_MoreSquareIcon_Object"));
    }

    public void clickEditButton() {
        click(key("MB_EditButton_Object"));
    }

    public void clickDeleteButton() {
        click(key("MB_DeleteButton_Object"));
    }

    public void clickSaveButton() {
        click(key("MB_SaveButton_Object"));
    }

    public void clickDeleteConfirmButton() {
        click(key("MB_DeleteConfirmButton_Object"));
    }

    public void enterEditNickname(String nickname) {
        type(key("MB_EditNicknameInput_Object"), nickname);
    }

    public void clearEditNickname() {
        clear(key("MB_EditNicknameInput_Object"));
    }

    public String getEditNicknameValue() {
        return getAttribute(key("MB_EditNicknameInput_Object"), "value");
    }

    public void clickVerifyBeneficiaryDetailButton() {
        click(key("MB_VerifyBeneficiaryDetailButton_Object"));
    }

    public void clickSendMoneyButton() {
        click(key("MB_SendMoneyButton_Object"));
    }

    // ========== Get Text Methods ==========

    public String getPageTitleText() {
        return getText(key("PageTitle_Object"));
    }

    public String getAddFormTitle() {
        return getText(key("MB_AddTitle_Object"));
    }

    public String getConfirmTitle() {
        return getText(key("MB_ConfirmTitle_Object"));
    }

    public String getConfirmBeneficiaryName() {
        return getText(key("MB_ConfirmBeneficiaryName_Object"));
    }

    public String getConfirmAccountNumber() {
        return getText(key("MB_ConfirmAccountNumber_Object"));
    }

    public String getConfirmBankName() {
        return getText(key("MB_ConfirmBankName_Object"));
    }

    public String getConfirmIFSCCode() {
        return getText(key("MB_ConfirmIFSCCode_Object"));
    }

    public String getConfirmNickname() {
        return getText(key("MB_ConfirmNickname_Object"));
    }

    public String getSuccessTitle() {
        return getText(key("MB_SuccessTitle_Object"));
    }

    public String getSuccessMessage() {
        return getText(key("MB_SuccessMessage_Object"));
    }

    public String getCoolingPeriodMessage() {
        return getText(key("CoolingPeriodMessage_Object"));
    }

    public String getErrorMessage() {
        try {
            return getText(key("MB_ErrorMessage_Object"));
        } catch (Exception e) {
            return "";
        }
    }

    public String getToastMessage() {
        try {
            return getText(key("MB_ToastMessage_Object"));
        } catch (Exception e) {
            return "";
        }
    }

    public String getOTPTimerText() {
        return getText(key("MB_OTPTimerText_Object"));
    }

    public String getOTPMessage() {
        return getText(key("MB_OTPMessage_Object"));
    }

    public String getNeverShareOTPText() {
        return getText(key("MB_NeverShareOTPText_Object"));
    }

    public String getAZSortText() {
        return getText(key("AZSortText_Object"));
    }

    public String getBeneficiaryDetailName() {
        return getText(key("MB_BeneficiaryDetailName_Object"));
    }

    public String getBeneficiaryDetailAccount() {
        return getText(key("MB_BeneficiaryDetailAccount_Object"));
    }

    public String getEditTitle() {
        return getText(key("MB_EditTitle_Object"));
    }

    public String getDeleteConfirmTitle() {
        return getText(key("MB_DeleteConfirmTitle_Object"));
    }

    public String getDeleteSuccessMessage() {
        return getText(key("MB_DeleteSuccessMessage_Object"));
    }

    public String getEditSuccessMessage() {
        return getText(key("MB_EditSuccessMessage_Object"));
    }

    public String getAccountNumberInputValue() {
        return getAttribute(key("MB_AccountNumberInput_Object"), "value");
    }

    public String getReEnterAccountInputValue() {
        return getAttribute(key("MB_ReEnterAccountInput_Object"), "value");
    }

    public String getIFSCCodeInputValue() {
        return getAttribute(key("MB_IFSCCodeInput_Object"), "value");
    }

    public String getBeneficiaryNameInputValue() {
        return getAttribute(key("MB_BeneficiaryNameInput_Object"), "value");
    }

    public String getNicknameInputValue() {
        return getAttribute(key("MB_NicknameInput_Object"), "value");
    }

    // ========== Verification - Listing Page ==========

    public boolean isBackArrowDisplayed() {
        return isDisplayed(key("BackArrow_Object"));
    }

    public boolean isPageTitleDisplayed() {
        try {
            By locator = orManager.getLocator(key("PageTitle_Object"));
            org.openqa.selenium.WebElement el = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                    .until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(locator));
            return el != null;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSearchInputDisplayed() {
        return isDisplayed(key("SearchBeneficiaryInput_Object"));
    }

    public boolean isSearchButtonDisplayed() {
        return isDisplayed(key("SearchButton_Object"));
    }

    public boolean isAddButtonDisplayed() {
        return isDisplayed(key("AddButton_Object"));
    }

    public boolean isAllTabDisplayed() {
        return isDisplayed(key("AllTab_Object"));
    }

    public boolean isShivalikTabDisplayed() {
        return isDisplayed(key("ShivalikTab_Object"));
    }

    public boolean isOtherBankTabDisplayed() {
        return isDisplayed(key("OtherBankTab_Object"));
    }

    public boolean isCoolingPeriodIconDisplayed() {
        return isDisplayed(key("CoolingPeriodIcon_Object"));
    }

    public boolean isCoolingPeriodTextDisplayed() {
        return isDisplayed(key("CoolingPeriodText_Object"));
    }

    public boolean isNoBeneficiariesCoolingTextDisplayed() {
        return isDisplayed(key("NoBeneficiariesCoolingText_Object"));
    }

    public boolean isFavoriteIconDisplayed() {
        return isDisplayed(key("FavoriteIcon_Object"));
    }

    public boolean isFavoriteTextDisplayed() {
        return isDisplayed(key("FavoriteText_Object"));
    }

    public boolean isNoFavoriteBeneficiariesTextDisplayed() {
        return isDisplayed(key("NoFavoriteBeneficiariesText_Object"));
    }

    public boolean isNoBeneficiariesTextDisplayed() {
        return isDisplayed(key("NoBeneficiariesText_Object"));
    }

    public boolean isBeneficiaryListDisplayed() {
        return isDisplayed(key("BeneficiaryList_Object"));
    }

    public boolean isFilterIconDisplayed() {
        return isDisplayed(key("FilterIcon_Object"));
    }

    public boolean isAZSortTextDisplayed() {
        return isDisplayed(key("AZSortText_Object"));
    }

    public boolean isNickNameColumnHeaderDisplayed() {
        return isDisplayed(key("NickNameColumnHeader_Object"));
    }

    public boolean isCustomerNameColumnHeaderDisplayed() {
        return isDisplayed(key("CustomerNameColumnHeader_Object"));
    }

    public boolean isIsFavoriteColumnHeaderDisplayed() {
        return isDisplayed(key("IsFavoriteColumnHeader_Object"));
    }

    public boolean isEmptyWrapperBoxDisplayed() {
        return isDisplayed(key("EmptyWrapperBox_Object"));
    }

    public boolean isLogoDisplayed() {
        return isDisplayed(key("ShivalikLogo_Object"));
    }

    public boolean isHomeNavDisplayed() {
        return isDisplayed(key("HomeNavLink_Object"));
    }

    public boolean isBillsNavDisplayed() {
        return isDisplayed(key("BillsNavLink_Object"));
    }

    public boolean isOffersNavDisplayed() {
        return isDisplayed(key("OffersNavLink_Object"));
    }

    public boolean isProfileButtonDisplayed() {
        return isDisplayed(key("ProfileButton_Object"));
    }

    public boolean isNotificationIconDisplayed() {
        return isDisplayed(key("NotificationIcon_Object"));
    }

    // ========== Verification - Add Form ==========

    public boolean isAddFormTitleDisplayed() {
        return isDisplayed(key("MB_AddTitle_Object"));
    }

    public boolean isAddBackArrowDisplayed() {
        return isDisplayed(key("MB_AddBackArrow_Object"));
    }

    public boolean isCancelButtonDisplayed() {
        return isDisplayed(key("MB_CancelButton_Object"));
    }

    public boolean isContinueButtonDisplayed() {
        return isDisplayed(key("MB_ContinueButton_Object"));
    }

    public boolean isContinueButtonEnabled() {
        return isEnabled(key("MB_ContinueButton_Object"));
    }

    public boolean isShivalikBankRadioDisplayed() {
        return isDisplayed(key("MB_ShivalikBankLabel_Object"));
    }

    public boolean isOtherBankRadioDisplayed() {
        return isDisplayed(key("MB_OtherBankLabel_Object"));
    }

    public boolean isAccountNumberInputDisplayed() {
        return isDisplayed(key("MB_AccountNumberInput_Object"));
    }

    public boolean isReEnterAccountInputDisplayed() {
        return isDisplayed(key("MB_ReEnterAccountInput_Object"));
    }

    public boolean isIFSCCodeInputDisplayed() {
        return isDisplayed(key("MB_IFSCCodeInput_Object"));
    }

    public boolean isBeneficiaryNameInputDisplayed() {
        return isDisplayed(key("MB_BeneficiaryNameInput_Object"));
    }

    public boolean isNicknameInputDisplayed() {
        return isDisplayed(key("MB_NicknameInput_Object"));
    }

    public boolean isVerifyBeneficiaryButtonDisplayed() {
        return isDisplayed(key("MB_VerifyBeneficiaryButton_Object"));
    }

    public boolean isSearchIFSCLinkDisplayed() {
        return isDisplayed(key("MB_SearchIFSCLink_Object"));
    }

    public boolean isAccountNumberEyeIconDisplayed() {
        return isDisplayed(key("MB_AccountNumberEyeIcon_Object"));
    }

    public boolean isAccountNumberLabelDisplayed() {
        return isDisplayed(key("MB_AccountNumberLabel_Object"));
    }

    public boolean isReEnterAccountLabelDisplayed() {
        return isDisplayed(key("MB_ReEnterAccountLabel_Object"));
    }

    public boolean isBeneficiaryNameLabelDisplayed() {
        return isDisplayed(key("MB_BeneficiaryNameLabel_Object"));
    }

    public boolean isNicknameLabelDisplayed() {
        return isDisplayed(key("MB_NicknameLabel_Object"));
    }

    public boolean isIFSCCodeLabelDisplayed() {
        return isDisplayed(key("MB_IFSCCodeLabel_Object"));
    }

    public boolean isBeneficiaryTypeLabelDisplayed() {
        return isDisplayed(key("MB_BeneficiaryTypeLabel_Object"));
    }

    // ========== Verification - Confirm / OTP Page ==========

    public boolean isConfirmTitleDisplayed() {
        return isDisplayed(key("MB_ConfirmTitle_Object"));
    }

    public boolean isSubmitButtonDisplayed() {
        return isDisplayed(key("MB_SubmitButton_Object"));
    }

    public boolean isSubmitButtonEnabled() {
        try {
            WebElement btn = findElement(key("MB_SubmitButton_Object"));
            String disabled = btn.getAttribute("disabled");
            String classAttr = btn.getAttribute("class");
            if (disabled != null && !disabled.isEmpty()) return false;
            if (classAttr != null && classAttr.contains("cursor-not-allowed")) return false;
            return btn.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isOTPInputDisplayed() {
        return isDisplayed(key("MB_OTPInput_Object"));
    }

    public boolean isOTPTimerDisplayed() {
        return isDisplayed(key("MB_OTPTimerText_Object"));
    }

    public boolean isResendOTPLinkDisplayed() {
        return isDisplayed(key("MB_ResendOTPLink_Object"));
    }

    public boolean isOTPMessageDisplayed() {
        return isDisplayed(key("MB_OTPMessage_Object"));
    }

    public boolean isNeverShareOTPTextDisplayed() {
        return isDisplayed(key("MB_NeverShareOTPText_Object"));
    }

    public boolean isConfirmBeneficiaryNameDisplayed() {
        return isDisplayed(key("MB_ConfirmBeneficiaryName_Object"));
    }

    public boolean isConfirmAccountNumberDisplayed() {
        return isDisplayed(key("MB_ConfirmAccountNumber_Object"));
    }

    public boolean isConfirmBankNameDisplayed() {
        return isDisplayed(key("MB_ConfirmBankName_Object"));
    }

    public boolean isConfirmIFSCCodeDisplayed() {
        return isDisplayed(key("MB_ConfirmIFSCCode_Object"));
    }

    public boolean isConfirmNicknameDisplayed() {
        return isDisplayed(key("MB_ConfirmNickname_Object"));
    }

    // ========== Verification - Success Page ==========

    public boolean isSuccessTitleDisplayed() {
        return isDisplayed(key("MB_SuccessTitle_Object"));
    }

    public boolean isSuccessMessageDisplayed() {
        return isDisplayed(key("MB_SuccessMessage_Object"));
    }

    public boolean isDoneButtonDisplayed() {
        return isDisplayed(key("MB_DoneButton_Object"));
    }

    // ========== Verification - Error / Toast ==========

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(key("MB_ErrorMessage_Object"));
    }

    public boolean isToastMessageDisplayed() {
        return isDisplayed(key("MB_ToastMessage_Object"));
    }

    // ========== Verification - Edit / Delete ==========

    public boolean isEditButtonDisplayed() {
        return isDisplayed(key("MB_EditButton_Object"));
    }

    public boolean isDeleteButtonDisplayed() {
        return isDisplayed(key("MB_DeleteButton_Object"));
    }

    public boolean isEditTitleDisplayed() {
        return isDisplayed(key("MB_EditTitle_Object"));
    }

    public boolean isSaveButtonDisplayed() {
        return isDisplayed(key("MB_SaveButton_Object"));
    }

    public boolean isDeleteConfirmTitleDisplayed() {
        return isDisplayed(key("MB_DeleteConfirmTitle_Object"));
    }

    public boolean isDeleteConfirmButtonDisplayed() {
        return isDisplayed(key("MB_DeleteConfirmButton_Object"));
    }

    public boolean isDeleteSuccessMessageDisplayed() {
        return isDisplayed(key("MB_DeleteSuccessMessage_Object"));
    }

    public boolean isEditSuccessMessageDisplayed() {
        return isDisplayed(key("MB_EditSuccessMessage_Object"));
    }

    public boolean isBeneficiaryRowDisplayed() {
        return isDisplayed(key("MB_BeneficiaryRow_Object"));
    }

    public boolean isBeneficiaryDetailNameDisplayed() {
        return isDisplayed(key("MB_BeneficiaryDetailName_Object"));
    }

    public boolean isBeneficiaryDetailAccountDisplayed() {
        return isDisplayed(key("MB_BeneficiaryDetailAccount_Object"));
    }

    public boolean isMoreSquareIconDisplayed() {
        return isDisplayed(key("MB_MoreSquareIcon_Object"));
    }

    public boolean isBeneficiaryDetailTitleDisplayed() {
        return isDisplayed(key("MB_BeneficiaryDetailTitle_Object"));
    }

    public boolean isEditNicknameInputDisplayed() {
        return isDisplayed(key("MB_EditNicknameInput_Object"));
    }

    public boolean isVerifyBeneficiaryDetailButtonDisplayed() {
        return isDisplayed(key("MB_VerifyBeneficiaryDetailButton_Object"));
    }

    public boolean isSendMoneyButtonDisplayed() {
        return isDisplayed(key("MB_SendMoneyButton_Object"));
    }

    // ========== Scroll Methods ==========

    public void scrollToAddForm() {
        scrollToElement(key("MB_AccountNumberInput_Object"));
    }

    public void scrollToNickname() {
        scrollToElement(key("MB_NicknameInput_Object"));
    }

    public void scrollToOTPSection() {
        scrollToElement(key("MB_OTPInput_Object"));
    }

    // ========== Composite / Helper Methods ==========

    public void fillShivalikBeneficiaryForm(String accountNo, String reAccountNo, String beneficiaryName, String nickname) {
        // Shivalik Bank is selected by default - no radio click needed
        enterAccountNumber(accountNo);
        enterReEnterAccountNumber(reAccountNo);
        enterBeneficiaryName(beneficiaryName);
        if (nickname != null && !nickname.isEmpty()) {
            enterNickname(nickname);
        }
    }

    public void fillOtherBankBeneficiaryForm(String accountNo, String reAccountNo, String ifsc, String beneficiaryName, String nickname) {
        clickOtherBankRadio();
        enterAccountNumber(accountNo);
        enterReEnterAccountNumber(reAccountNo);
        enterIFSCCode(ifsc);
        enterBeneficiaryName(beneficiaryName);
        if (nickname != null && !nickname.isEmpty()) {
            enterNickname(nickname);
        }
    }

    public void clearAllFormFields() {
        try { clearAccountNumber(); } catch (Exception ignored) {}
        try { clearReEnterAccountNumber(); } catch (Exception ignored) {}
        try { clearIFSCCode(); } catch (Exception ignored) {}
        try { clearBeneficiaryName(); } catch (Exception ignored) {}
        try { clearNickname(); } catch (Exception ignored) {}
    }

    public boolean isAccountNumberMasked() {
        try {
            String type = getAttribute(key("MB_AccountNumberInput_Object"), "type");
            return "password".equalsIgnoreCase(type);
        } catch (Exception e) {
            return false;
        }
    }

    public String getAccountNumberInputType() {
        return getAttribute(key("MB_AccountNumberInput_Object"), "type");
    }

    public int getBeneficiaryListCount() {
        try {
            List<WebElement> items = driver.findElements(By.xpath("//ul[contains(@class,'transactionList')]//li"));
            return items.size();
        } catch (Exception e) {
            return 0;
        }
    }

    // ========== Fast Navigation State Checks (short timeout) ==========

    public boolean isPageTitleDisplayedFast() {
        return isDisplayedWithin(key("PageTitle_Object"), 3);
    }

    public boolean isDoneButtonDisplayedFast() {
        return isDisplayedWithin(key("MB_DoneButton_Object"), 2);
    }

    public boolean isAddBackArrowDisplayedFast() {
        return isDisplayedWithin(key("MB_AddBackArrow_Object"), 2);
    }

    public boolean isCancelButtonDisplayedFast() {
        return isDisplayedWithin(key("MB_CancelButton_Object"), 2);
    }

    public boolean isBackArrowDisplayedFast() {
        return isDisplayedWithin(key("BackArrow_Object"), 2);
    }

    // ========== CAUTION Popup Methods ==========

    public boolean isCautionPopupDisplayed() {
        return isDisplayedWithin(key("MB_CautionPopupTitle_Object"), 10);
    }

    public void clickCautionConfirmButton() {
        jsClick(key("MB_CautionPopupConfirmButton_Object"));
    }

    public void clickCautionCancelButton() {
        click(key("MB_CautionPopupCancelButton_Object"));
    }

    public void dismissMuiBackdrop() {
        try {
            WebElement backdrop = driver.findElement(By.xpath("//div[contains(@class,'MuiBackdrop-root')]"));
            backdrop.click();
        } catch (Exception e) {
            logger.debug("No MUI backdrop to dismiss");
        }
    }

    /**
     * Handle CAUTION popup if displayed - click Confirm to leave the form.
     * @return true if popup was handled
     */
    public boolean handleCautionPopupIfPresent() {
        try {
            if (isCautionPopupDisplayed()) {
                logger.info("CAUTION popup detected - clicking Confirm to leave form");
                clickCautionConfirmButton();
                return true;
            }
        } catch (Exception e) {
            logger.debug("No CAUTION popup present");
        }
        return false;
    }
}
