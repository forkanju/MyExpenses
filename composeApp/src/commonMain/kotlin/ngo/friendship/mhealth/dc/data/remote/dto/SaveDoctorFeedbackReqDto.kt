package ngo.friendship.mhealth.dc.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ngo.friendship.mhealth.dc.domain.model.QuestionAnswerTemplate
import ngo.friendship.mhealth.dc.domain.model.QuestionAnswerTemplateDefault
import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.model.DoctorFeedbackFormState
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
                requestName = when {
                    formState.requestName == "DELETE_PRESCRIPTION_TEMPLATE" -> "DELETE_PRESCRIPTION_TEMPLATE"
                    formState.prescriptionId != null -> "UPDATE_PRESCRIPTION_TEMPLATE"
                    formState.isPresTempSave == 1 -> "SAVE_PRESCRIPTION_TEMPLATE"
                    else -> "SAVE_DOCTOR_FEEDBACK"
                },
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

    @SerialName("advice_message_for_fcm") //comments
    val commentsForFCM: String,

    @SerialName("doctor_advice")
    val doctorAdvice: String,

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
    val prescription: List<PrescriptionItemDto>,

    @SerialName("is_pres_temp_save")
    val isPresTempSave: Int,

    @SerialName("prescription_name")
    val prescriptionName: String,

    @SerialName("is_global_prescription")
    val isGlobalPrescription: Int,

    @SerialName("prescription_id")
    val prescriptionId: Long? = null,

    @SerialName("call_back_on_phn")
    val isCalledBack: Long = 0L,

    @SerialName("is_fcm_checked")
    val isFcmChecked: Int = 1,

    @SerialName("is_beneficiary_checked")
    val isBeneficiaryChecked: Int = 1
)

@Serializable
data class IdNameItem(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String
)

@Serializable
data class QuestionAnswerItem(
    @SerialName("question_id")
    val questionId: String,
    @SerialName("answer")
    val answer: String
)

@Serializable
data class PrescriptionItemDto(
    @SerialName("MED_ID")
    val medId: String,
    @SerialName("GEN_NAME")
    val genName: String,
    @SerialName("MED_TYPE")
    val medType: String,
    @SerialName("MED_NAME")
    val medName: String,
    @SerialName("MED_QTY")
    val medQty: String,
    @SerialName("SALE_QTY")
    val saleQty: String,
    @SerialName("MED_DURATION")
    val medDuration: String,
    @SerialName("MTR")
    val mtr: String,
    @SerialName("MTR_LBL")
    val mtrLbl: String,
    @SerialName("MTR_SF")
    val mtrSf: String,
    @SerialName("AFM")
    val afm: String,
    @SerialName("AFM_SF")
    val afmSf: String,
    @SerialName("SF")
    val sf: String,
    @SerialName("SMS_SF")
    val smsSf: String
)

fun DoctorFeedbackFormState.toDto(): DoctorFeedbackObject {
    return DoctorFeedbackObject(
        interviewId = interviewId,
        doctorFindings = doctorNotes,
        commentsForFCM = commentsForFcm,
        refCenterId = selectedReferralCenter?.refCenterId.toString(),
        investigationResult = investigationResult,
        nextFollowUpDate = nextFollowUpDate,
        investigation = selectedInvestigations.map {
            IdNameItem(
                id = it.investigationId.toString(),
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
        prescription = prescriptions,
        isPresTempSave = isPresTempSave,
        prescriptionName = prescriptionName,
        isGlobalPrescription = isGlobalPrescription,
        doctorAdvice = doctorAdvice,
        prescriptionId = prescriptionId,
        isCalledBack = if (isCalledBack) 1L else 0L,
        isFcmChecked = if (isFcmChecked) 1 else 0,
        isBeneficiaryChecked = if (isBeneficiaryChecked) 1 else 0
    )
}