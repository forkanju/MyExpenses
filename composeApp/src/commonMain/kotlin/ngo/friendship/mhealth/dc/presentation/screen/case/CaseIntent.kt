package ngo.friendship.mhealth.dc.presentation.screen.case

import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.model.DoctorFeedbackFormState
import ngo.friendship.mhealth.dc.data.remote.dto.PrescriptionItemDto
import ngo.friendship.mhealth.dc.domain.model.Diagnosis
import ngo.friendship.mhealth.dc.domain.model.Investigation
import ngo.friendship.mhealth.dc.domain.model.ReferralCenter
import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.model.CustomMessageState
import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.model.MedicineComposerState
import ngo.friendship.mhealth.dc.presentation.screen.case.case_list.components.CaseTab
import ngo.friendship.mhealth.dc.data.remote.dto.PrescriptionTemplateDto

import ngo.friendship.mhealth.dc.domain.model.PrescriptionTemplate

sealed interface CaseIntent {
    data class LoadFromTemplate(val template: PrescriptionTemplate) : CaseIntent
    data class LoadInterviewDetails(val interviewId: Long) : CaseIntent
    data class LoadQuestionAnswerData(val interviewId: Long) : CaseIntent
    data class LoadDoctorFeedback(val interviewId: Long) : CaseIntent
    data class LoadMedicineList(val type: String = "Tab") : CaseIntent
    data object SaveDoctorFeedback : CaseIntent
    data class SetMode(val mode: CaseDetailsMode) : CaseIntent
    data class SetSelectedTab(val tab: CaseTab) : CaseIntent
    data class UpdateFormState(val state: DoctorFeedbackFormState) : CaseIntent
    data class UpdateMedicineComposerState(val state: MedicineComposerState) : CaseIntent
    
    // Finer grained intents for MVI if preferred, but starting with these to match existing ViewModel
    data class AddDiagnosis(val diagnosis: Diagnosis) : CaseIntent
    data class RemoveDiagnosis(val diagnosis: Diagnosis) : CaseIntent
    data class AddPrescription(val item: PrescriptionItemDto) : CaseIntent
    data class RemovePrescription(val index: Int) : CaseIntent
    data class UpdateDoctorAdvice(val advice: String) : CaseIntent
    data class AddInvestigation(val investigation: Investigation) : CaseIntent
    data class RemoveInvestigation(val investigation: Investigation) : CaseIntent
    data class UpdateInvestigationResult(val result: String) : CaseIntent
    data class UpdateCommentsForFcm(val comments: String) : CaseIntent
    data class UpdateReferralCenter(val center: ReferralCenter?) : CaseIntent
    data class UpdateDoctorNotes(val notes: String) : CaseIntent
    data class UpdateFollowUpDate(val date: String) : CaseIntent
    data object ToggleDatePicker : CaseIntent
    data class SetSummaryTab(val index: Int) : CaseIntent
    data object ToggleSendMessageDialog : CaseIntent
    data class UpdateCustomMessage(val messageState: CustomMessageState) : CaseIntent
    data class TogglePrescriptionSms(val checked: Boolean) : CaseIntent
    data class ToggleCalledBack(val checked: Boolean) : CaseIntent
    
    // Intents for Local Case
    data class UpdatePatientName(val name: String) : CaseIntent
    data class UpdateGender(val gender: String) : CaseIntent
    data class UpdateAge(val age: String) : CaseIntent
    data class UpdateOfficeId(val id: String) : CaseIntent
    data class UpdateSector(val sector: String) : CaseIntent
    data class UpdateInterviewNote(val note: String) : CaseIntent

    data class OpenCaseFromTab(
        val interviewId: Long,
        val sourceTab: CaseTab
    ) : CaseIntent

    data object ToggleSaveTemplateDialog : CaseIntent
    data class UpdateTemplateName(val name: String) : CaseIntent
    data object ToggleGlobalTemplate : CaseIntent
    data object SaveAsTemplate : CaseIntent
    data class SelectPrescriptionTemplate(val template: PrescriptionTemplateDto) : CaseIntent
}