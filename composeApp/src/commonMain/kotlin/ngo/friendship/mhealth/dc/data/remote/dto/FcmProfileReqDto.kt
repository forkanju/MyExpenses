package ngo.friendship.mhealth.dc.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FcmProfileReqDto(
    @SerialName("ORG_CODE")
    val orgCode: String,
    @SerialName("userCode")
    val userCode: String,
    @SerialName("pw")
    val password: String,
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
    val data: EmptyDataDto = EmptyDataDto(),
    @SerialName("lang")
    val lang: String,
    @SerialName("param1")
    val param1: FcmCodeParam
)

@Serializable
data class FcmCodeParam(
    @SerialName("FCM_CODE")
    val fcmCode: String
)
