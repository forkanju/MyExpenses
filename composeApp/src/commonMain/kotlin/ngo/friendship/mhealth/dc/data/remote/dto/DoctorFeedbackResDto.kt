package ngo.friendship.mhealth.dc.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DoctorFeedbackResDto(
    @SerialName("responseType")
    val responseType: String? = null,
    @SerialName("data")
    val data: DoctorFeedbackData? = null,
    @SerialName("errorDesc")
    val errorDesc: String? = null,
    @SerialName("responseTime")
    val responseTime: String? = null,
    @SerialName("dataLength")
    val dataLength: Int? = null,
    @SerialName("execTime")
    val execTime: Int? = null,
    @SerialName("errorCode")
    val errorCode: String? = null,
    @SerialName("responseName")
    val responseName: String? = null,
    @SerialName("responseCode")
    val responseCode: String? = null
)

@Serializable
data class DoctorFeedbackData(
    @SerialName("patient_interview_feedback")
    val patientInterviewFeedback: List<PatientInterviewFeedbackDto>? = null
)

@Serializable
data class PatientInterviewFeedbackDto(
    @SerialName("DIAG_ID")
    val diagId: String? = null,
    @SerialName("ORG_ID")
    val orgId: Int? = null,
    @SerialName("DOC_FOLLOWUP_ID")
    val docFollowupId: Long? = null,
    @SerialName("REF_CENTER_NAME")
    val refCenterName: String? = null,
    @SerialName("MESSAGE_TO_FCM")
    val messageToFcm: String? = null,
    @SerialName("UPDATE_BY")
    val updateBy: Int? = null,
    @SerialName("USER_ID")
    val userId: Long? = null,
    @SerialName("FEEDBACK_CENTER_ID")
    val feedbackCenterId: Long? = null,
    @SerialName("RECORD_DATE")
    val recordDate: String? = null,
    @SerialName("DOCTOR_FINDINGS")
    val doctorFindings: String? = null,
    @SerialName("NOTIFICATION_STATUS")
    val notificationStatus: Int? = null,
    @SerialName("FEEDBACK_DATE")
    val feedbackDate: String? = null,
    @SerialName("NEXT_FOLLOWUP_DATE")
    val nextFollowupDate: String? = null,
    @SerialName("FOLLOWUP_ADJUST")
    val followupAdjust: Boolean? = null,
    @SerialName("PRESCRIBED_MEDICINE")
    val prescribedMedicine: String? = null,
    @SerialName("REF_CENTER_ID")
    val refCenterId: Long? = null,
    @SerialName("INTERVIEW_ID")
    val interviewId: Long? = null,
    @SerialName("GENDER")
    val gender: String? = null,
    @SerialName("TRANS_REF")
    val transRef: Long? = null,
    @SerialName("diag_name")
    val diagDesc: String? = null,
    @SerialName("UPDATE_ON")
    val updateOn: Long? = null,
    @SerialName("INVES_ADVICE")
    val investigationAdvice: String? = null,
    @SerialName("INVES_RESULT")
    val investigationResult: String? = null
)
