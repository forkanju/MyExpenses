package ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.model

import ngo.friendship.mhealth.dc.data.remote.dto.PrescriptionItem
import ngo.friendship.mhealth.dc.domain.model.Diagnosis
import ngo.friendship.mhealth.dc.domain.model.Investigation
import ngo.friendship.mhealth.dc.domain.model.QuestionAnswerTemplate
import ngo.friendship.mhealth.dc.domain.model.QuestionAnswerTemplateDefault
import ngo.friendship.mhealth.dc.domain.model.ReferralCenter

data class DoctorFeedbackFormState(
    val interviewId: Long? = null,
    val selectedDiagnoses: List<Diagnosis> = emptyList(),
    val selectedInvestigations: List<Investigation> = emptyList(),
    val selectedReferralCenter: ReferralCenter? = null,
    val doctorAdvice: String = "",
    val commentsForFcm: String = "",
    val doctorNotes: String = "",
    val investigationResult: String = "",
    val nextFollowUpDate: String = "",
    val mobile: String = "",
    val sms: String = "",
    val questionAnswers: List<QuestionAnswerTemplate> = emptyList(),
    val questionAnswers2: List<QuestionAnswerTemplateDefault> = emptyList(),
    val prescriptions: List<PrescriptionItem> = emptyList(),
    val isPresTempSave: Int = 0,
    val prescriptionName: String = "",
    val isGlobalPrescription: Int = 0
)