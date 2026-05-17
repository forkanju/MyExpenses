package ngo.friendship.mhealth.dc.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ngo.friendship.mhealth.dc.utils.md5

@Serializable
data class DoctorFeedbackReqDto(
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
    val param1: DoctorFeedbackRequestParam1
) {
    companion object {
        fun build(
            userName: String,
            password: String,
            requestTime: String,
            interviewId: Long
        ): DoctorFeedbackReqDto {
            return DoctorFeedbackReqDto(
                orgCode = "FR",
                userCode = userName.md5(),
                pw = password.md5(),
                orgId = 101,
                imei = "IMEI_FREE",
                demo = false,
                requestType = "DOCTOR_CENTER",
                requestName = "GET_PATIENT_INTERVIEW_DOCTOR_FEEDBACK",
                moduleName = "mHealth-FCM",
                requestTime = requestTime,
                requestAction = "",
                dataLength = 2,
                data = emptyMap(),
                lang = "bn",
                param1 = DoctorFeedbackRequestParam1(
                    interviewId = interviewId
                )
            )
        }
    }
}

@Serializable
data class DoctorFeedbackRequestParam1(
    @SerialName("INTERVIEW_ID")
    val interviewId: Long
)
