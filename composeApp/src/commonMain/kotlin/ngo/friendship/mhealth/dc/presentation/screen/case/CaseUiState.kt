package ngo.friendship.mhealth.dc.presentation.screen.case

import ngo.friendship.mhealth.dc.domain.model.InterviewDetails
import ngo.friendship.mhealth.dc.domain.model.Medicine
import ngo.friendship.mhealth.dc.domain.model.QuestionAnswerJson
import ngo.friendship.mhealth.dc.presentation.screens.case.case_detail.model.CustomMessageState
import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.model.DoctorFeedbackFormState
import ngo.friendship.mhealth.dc.presentation.screens.case.CaseDetailsMode
import ngo.friendship.mhealth.dc.presentation.screens.case.case_detail.model.MedicineComposerState
import ngo.friendship.mhealth.dc.presentation.screens.profile.fcm.FcmProfileUiState
import ngo.friendship.mhealth.dc.presentation.screens.case.case_list.components.CaseTab

data class CaseUiState(
    val interviewDetails: InterviewDetails = InterviewDetails(),
    val fcmProfileState: FcmProfileUiState = FcmProfileUiState(),
    val formState: DoctorFeedbackFormState = DoctorFeedbackFormState(),
    val medicineList: List<Medicine> = emptyList(),
    val medicineBrandTypeList: List<String> = emptyList(),
    val medicineComposerState: MedicineComposerState = MedicineComposerState(),
    val questionAnswerData: QuestionAnswerJson = QuestionAnswerJson(),
    val mode: CaseDetailsMode = CaseDetailsMode.NORMAL,
    val selectedTab: CaseTab = CaseTab.Pending,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDatePickerVisible: Boolean = false,
    val selectedSummaryTab: Int = 0,
    val isSendMessageDialogVisible: Boolean = false,
    val customMessageState: CustomMessageState = CustomMessageState(),
    val isPrescriptionWithSmsChecked: Boolean = false,
    val patientName: String = "",
    val selectedGender: String = "Male",
    val age: String = "",
    val officeId: String = "",
    val sector: String = "ISM",
    val interviewNote: String = ""
)