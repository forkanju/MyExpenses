package ngo.friendship.mhealth.dc.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ngo.friendship.mhealth.dc.utils.md5

@Serializable
data class UpdateInterviewStatusReqDto(
    @SerialName("ORG_CODE") val orgCode: String = "FR",
    @SerialName("ORG_ID") val orgId: Int = 101,
    @SerialName("userCode") val userCode: String,
    @SerialName("pw") val password: String,
    @SerialName("imei") val imei: String? = "IMEI_FREE",
    @SerialName("DEMO") val demo: Boolean = false,

    @SerialName("requestType") val requestType: String = "DOCTOR_CENTER",
    @SerialName("requestName") val requestName: String = "UPDATE_INTERVIEW_STATUS",
    @SerialName("module_name") val moduleName: String = "mHealth-FCM",
    @SerialName("requestTime") val requestTime: String,

    @SerialName("requestAction") val requestAction: String = "",
    @SerialName("dataLength") val dataLength: Int = 2,
    @SerialName("data") val data: Map<String, String> = emptyMap(),
    @SerialName("lang") val lang: String = "bn",

    @SerialName("param1") val param1: Param1
) {
    @Serializable
    data class Param1(
        @SerialName("INTERVIEW_ID")
        val interviewId: Long,
        @SerialName("status")
        val status: String
    )

    companion object {
        fun build(
            userName: String,
            password: String,
            requestTime: String,
            interviewId: Long,
            status: String
        ) = UpdateInterviewStatusReqDto(
            userCode = userName.md5(),
            password = password.md5(),
            requestTime = requestTime,
            param1 = Param1(
                interviewId = interviewId,
                status = status
            )
        )
    }
}