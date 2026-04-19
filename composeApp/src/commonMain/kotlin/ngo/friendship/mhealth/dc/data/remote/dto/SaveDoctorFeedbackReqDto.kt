package ngo.friendship.mhealth.dc.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ngo.friendship.mhealth.dc.domain.model.QuestionAnswerTemplate
import ngo.friendship.mhealth.dc.domain.model.QuestionAnswerTemplateDefault
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.model.DoctorFeedbackFormState
import ngo.friendship.mhealth.dc.utils.md5

@Serializable
data class SaveDoctorFeedbackReqDto(
    @SerialName("ORG_CODE")
    val orgCode: String,

    @SerialName("userCode")
    val userCode: String,

    @SerialName("pw")
    val pw: String,

    @SerialName("ORG_ID")
    val orgId: Int,

    @SerialName("imei")
    val imei: String,

    @SerialName("DEMO")
    val demo: Boolean,

    @SerialName("requestType")
    val requestType: String,

    @SerialName("requestName")
    val requestName: String,

    @SerialName("module_name")
    val moduleName: String,

    @SerialName("requestTime")
    val requestTime: String,

    @SerialName("requestAction")
    val requestAction: String,

    @SerialName("dataLength")
    val dataLength: Int,

    @SerialName("data")
    val data: Map<String, String>,

    @SerialName("lang")
    val lang: String,

    @SerialName("param1")
    val param1: DoctorFeedbackParam1
) {
    companion object {
        fun build(
            userName: String,
            password: String,
            requestTime: String,
            formState: DoctorFeedbackFormState
        ): SaveDoctorFeedbackReqDto {
            return SaveDoctorFeedbackReqDto(
                orgCode = "FR",
                userCode = userName.md5(),
                pw = password.md5(),
                orgId = 101,
                imei = "IMEI_FREE",
                demo = false,
                requestType = "DOCTOR_CENTER",
                requestName = "SAVE_DOCTOR_FEEDBACK",
                moduleName = "mHealth-FCM",
                requestTime = requestTime,
                requestAction = "",
                dataLength = 2,
                data = emptyMap(),
                lang = "bn",
                param1 = DoctorFeedbackParam1(
                    doctorFeedbackObject = formState.toDto()
                )
            )
        }
    }
}


@Serializable
data class DoctorFeedbackParam1(
    @SerialName("DOCTOR_FEEDBACK_OBJECT")
    val doctorFeedbackObject: DoctorFeedbackObject
)

@Serializable
data class DoctorFeedbackObject(
    @SerialName("INTERVIEW_ID")
    val interviewId: Long?,

    @SerialName("doctorFindings")
    val doctorFindings: String = "static findings",//static for temporary

    @SerialName("advice_message_for_fcm")
    val adviceMessageForFcm: String,

    @SerialName("REF_CENTER_ID")
    val refCenterId: String?,

    @SerialName("investigation_result")
    val investigationResult: String,

    @SerialName("next_follow_up_date")
    val nextFollowUpDate: String? = null,

    @SerialName("investigation")
    val investigation: List<IdNameItem>,

    @SerialName("diagnosisdx")
    val diagnosisdx: List<IdNameItem>,

    @SerialName("QUESTION_ANSWER_JSON")
    val questionAnswerJson: List<QuestionAnswerTemplate>,

    @SerialName("QUESTION_ANSWER_JSON2")
    val questionAnswerJson2: List<QuestionAnswerTemplateDefault>,

    @SerialName("prescription")
    val prescription: List<PrescriptionItem>
)

@Serializable
data class IdNameItem(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String
)

@Serializable
data class QuestionAnswerItem(
    @SerialName("question_id")
    val questionId: Long,
    @SerialName("answer")
    val answer: String
)

@Serializable
data class PrescriptionItem(
    @SerialName("medicine_name")
    val medicineName: String,
    @SerialName("dose")
    val dose: String,
    @SerialName("duration")
    val duration: String
)

fun DoctorFeedbackFormState.toDto(): DoctorFeedbackObject {
    return DoctorFeedbackObject(
        interviewId = interviewId,
        doctorFindings = doctorNotes,
        adviceMessageForFcm = listOfNotNull(
            doctorAdvice.takeIf { it.isNotBlank() },
            commentsForFcm.takeIf { it.isNotBlank() }
        ).joinToString("\n"),
        refCenterId = selectedReferralCenter?.refCenterId.toString(),
        investigationResult = investigationResult,
        nextFollowUpDate = nextFollowUpDate,
        investigation = selectedInvestigations.map {
            IdNameItem(
                id = it.investigationId,
                name = it.investigationName
            )
        },
        diagnosisdx = selectedDiagnoses.map {
            IdNameItem(
                id = it.diagId,
                name = it.diagName
            )
        },
        questionAnswerJson = questionAnswers,
        questionAnswerJson2 = questionAnswers2,
        prescription = prescriptions
    )
}