package ngo.friendship.mhealth.dc.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ngo.friendship.mhealth.dc.utils.currentTimestamp
import ngo.friendship.mhealth.dc.utils.md5
import ngo.friendship.mhealth.dc.utils.toDateTimeServerSlash

@Serializable
data class ChangePasswordReqDto(
    @SerialName("ORG_CODE") val orgCode: String = "FR",
    @SerialName("userCode") val userCode: String,
    @SerialName("pw") val password: String,
    @SerialName("ORG_ID") val orgId: Int = 101,
    @SerialName("imei") val imei: String? = "IMEI_FREE",
    @SerialName("DEMO") val demo: Boolean = false,
    @SerialName("requestType") val requestType: String = "DOCTOR_CENTER",
    @SerialName("requestName") val requestName: String = "PW_CHANGE",
    @SerialName("module_name") val moduleName: String = "mHealth-FCM",
    @SerialName("requestTime") val requestTime: String,
    @SerialName("requestAction") val requestAction: String = "",
    @SerialName("dataLength") val dataLength: Int = 2,
    @SerialName("data") val data: EmptyDataDto = EmptyDataDto(),
    @SerialName("lang") val lang: String = "bn",
    @SerialName("param1") val param1: ChangePasswordParamDto
) {
    companion object {
        fun build(
            userName: String,
            oldPassword: String,
            newPassword: String
        ): ChangePasswordReqDto {
            val newPasswordMd5 = newPassword.md5()
            return ChangePasswordReqDto(
                userCode = userName.md5(),
                password = oldPassword.md5(),
                requestTime = currentTimestamp.toDateTimeServerSlash(),
                param1 = ChangePasswordParamDto(
                    passMd5 = newPasswordMd5,
                    passEncrypted = newPasswordMd5
                )
            )
        }
    }
}

@Serializable
data class ChangePasswordParamDto(
    @SerialName("PASS_MD5") val passMd5: String,
    @SerialName("PASS_ENCRYPTED") val passEncrypted: String
)

@Serializable
data class ChangePasswordResDto(
    @SerialName("responseCode") val responseCode: String? = null,
    @SerialName("errorDesc") val errorDesc: String? = null,
    @SerialName("errorCode") val errorCode: String? = null,
    @SerialName("responseName") val responseName: String? = null,
    @SerialName("responseTime") val responseTime: String? = null,
    @SerialName("responseType") val responseType: String? = null,
    @SerialName("data") val data: EmptyDataDto? = null
)
