package ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.model

import ngo.friendship.mhealth.dc.data.remote.dto.PrescriptionItem
import ngo.friendship.mhealth.dc.data.remote.dto.QuestionAnswerItem
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
    val questionAnswers: List<QuestionAnswerTemplate> = emptyList(),
    val questionAnswers2: List<QuestionAnswerTemplateDefault> = emptyList(),
    val prescriptions: List<PrescriptionItem> = emptyList()
)