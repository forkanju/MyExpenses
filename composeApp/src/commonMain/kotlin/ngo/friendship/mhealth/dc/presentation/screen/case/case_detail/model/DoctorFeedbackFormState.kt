package ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.model

import kotlinx.serialization.json.JsonArray
import ngo.friendship.mhealth.dc.data.remote.dto.PrescriptionItemDto
import ngo.friendship.mhealth.dc.domain.model.Diagnosis
import ngo.friendship.mhealth.dc.domain.model.Investigation
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
    val questionAnswers: JsonArray = JsonArray(emptyList()), // এটি সার্ভারে QUESTION_ANSWER_JSON হিসেবে যাবে
    val questionAnswers2: JsonArray = JsonArray(emptyList()), // এটি সার্ভারে QUESTION_ANSWER_JSON2 হিসেবে যাবে
    val prescriptions: List<PrescriptionItemDto> = emptyList(),
    val isPresTempSave: Int = 0,
    val prescriptionName: String = "",
    val isGlobalPrescription: Int = 0,
    val prescriptionId: Long? = null,
    val requestName: String? = null,
    val isCalledBack: Boolean = false,
    val isFcmChecked: Boolean = true,
    val isBeneficiaryChecked: Boolean = true
)
