package ngo.friendship.mhealth.dc.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ngo.friendship.mhealth.dc.utils.currentTimestamp
import ngo.friendship.mhealth.dc.utils.md5
import ngo.friendship.mhealth.dc.utils.toDateTimeServerSlash

@Serializable
data class SetupDataReqDto(
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
    val param1: EmptyParamDto = EmptyParamDto()
) {
    companion object {
        fun build(
            userName: String,
            password: String,
            requestTime: String = currentTimestamp.toDateTimeServerSlash()
        ): SetupDataReqDto {
            return SetupDataReqDto(
                orgCode = "FR",
                userCode = userName.md5(),
                password = password.md5(),
                orgId = 101,
                imei = "IMEI_FREE",
                demo = false,
                requestType = "DOCTOR_CENTER",
                requestName = "GET_SETUP_DATA",
                moduleName = "mHealth-FCM",
                requestTime = requestTime,
                requestAction = "",
                dataLength = 2,
                data = EmptyDataDto(),
                lang = "bn",
                param1 = EmptyParamDto()
            )
        }
    }
}

@Serializable
class EmptyDataDto

@Serializable
class EmptyParamDto