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
    @SerialName("requestType") val requestType: String,
    @SerialName("requestName") val requestName: String,
    @SerialName("module_name") val moduleName: String,
    @SerialName("requestTime") val requestTime: String,
    @SerialName("lang") val lang: String
)