package ngo.friendship.mhealth.dc.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    @SerialName("ORG_CODE") val orgCode: String,
    @SerialName("userCode") val userCode: String,
    @SerialName("pw") val password: String,
    @SerialName("ORG_ID") val orgId: Int,
    @SerialName("imei") val imei: String? = null,
    @SerialName("DEMO") val demo: Boolean = false,
    @SerialName("requestType") val requestType: String, // USER_GATE
    @SerialName("requestName") val requestName: String, // LOGIN_WEB
    @SerialName("module_name") val moduleName: String, // mHealth-FCM
    @SerialName("requestTime") val requestTime: String, // 17/08/2021 14:05:28
    @SerialName("lang") val lang: String
)