package ngo.friendship.mhealth.dc.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ngo.friendship.mhealth.dc.utils.md5

@Serializable
data class PrescriptionTemplateReqDto(
    @SerialName("ORG_CODE") val orgCode: String = "FR",
    @SerialName("userCode") val userCode: String,
    @SerialName("pw") val password: String,
    @SerialName("ORG_ID") val orgId: Int = 101,
    @SerialName("imei") val imei: String = "IMEI_FREE",
    @SerialName("DEMO") val demo: Boolean = false,
    @SerialName("requestType") val requestType: String = "DOCTOR_CENTER",
    @SerialName("requestName") val requestName: String = "PRESCRIPTION_TEMPLATE",
    @SerialName("module_name") val moduleName: String = "mHealth-FCM",
    @SerialName("requestTime") val requestTime: String,
    @SerialName("requestAction") val requestAction: String = "",
    @SerialName("dataLength") val dataLength: Int = 2,
    @SerialName("data") val data: EmptyData = EmptyData(),
    @SerialName("lang") val lang: String = "bn",
    @SerialName("param1") val param1: EmptyData = EmptyData()
) {
    @Serializable
    class EmptyData

    companion object {
        fun build(userName: String, password: String, requestTime: String): PrescriptionTemplateReqDto {
            return PrescriptionTemplateReqDto(
                userCode = userName.md5(),
                password = password.md5(),
                requestTime = requestTime
            )
        }
    }
}
