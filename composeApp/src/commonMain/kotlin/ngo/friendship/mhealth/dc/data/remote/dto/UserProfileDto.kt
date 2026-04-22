package ngo.friendship.mhealth.dc.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ngo.friendship.mhealth.dc.utils.currentTimestamp
import ngo.friendship.mhealth.dc.utils.md5
import ngo.friendship.mhealth.dc.utils.toDateTimeServerSlash

@Serializable
data class UserProfileReqDto(
    @SerialName("ORG_CODE") val orgCode: String = "FR",
    @SerialName("userCode") val userCode: String,
    @SerialName("pw") val password: String,
    @SerialName("ORG_ID") val orgId: Int = 101,
    @SerialName("imei") val imei: String? = "null",
    @SerialName("DEMO") val demo: Boolean = false,
    @SerialName("requestType") val requestType: String = "USER_GATE",
    @SerialName("requestName") val requestName: String = "FCM_PROFILE",
    @SerialName("module_name") val moduleName: String = "mHealth-FCM",
    @SerialName("requestTime") val requestTime: String,
    @SerialName("requestAction") val requestAction: String = "",
    @SerialName("dataLength") val dataLength: Int = 2,
    @SerialName("data") val data: EmptyDataDto = EmptyDataDto(),
    @SerialName("lang") val lang: String = "bn",
    @SerialName("param1") val param1: EmptyParamDto = EmptyParamDto()
) {
    companion object {
        fun build(userName: String, password: String): UserProfileReqDto {
            return UserProfileReqDto(
                userCode = userName.md5(),
                password = password.md5(),
                requestTime = currentTimestamp.toDateTimeServerSlash()
            )
        }
    }
}

@Serializable
data class UserProfileResDto(
    @SerialName("data") val data: UserProfileData? = null,
    @SerialName("responseCode") val responseCode: String? = null,
    @SerialName("errorDesc") val errorDesc: String? = null,
    @SerialName("errorCode") val errorCode: String? = null,
    @SerialName("responseName") val responseName: String? = null,
    @SerialName("responseTime") val responseTime: String? = null,
    @SerialName("responseType") val responseType: String? = null
)

@Serializable
data class UserProfileData(
    @SerialName("fcmProfile") val fcmProfile: FcmProfile? = null
)

@Serializable
data class FcmProfile(
    @SerialName("USER_ID") val userId: Int? = null,
    @SerialName("USER_NAME") val userName: String? = null,
    @SerialName("USER_LOGIN_ID") val userLoginId: String? = null,
    @SerialName("EMAIL_ADDRS") val email: String? = null,
    @SerialName("MOBILE_NO") val mobileNo: String? = null,
    @SerialName("LOCATION_NAME") val locationName: String? = null,
    @SerialName("ORG_NAME") val orgName: String? = null,
    @SerialName("ORG_DESC") val orgDesc: String? = null,
    @SerialName("ORG_CODE") val orgCode: String? = null,
    @SerialName("ORG_ID") val orgId: Int? = null,
    @SerialName("PROFILE_IMAGE") val profileImage: String? = null,
    @SerialName("APP_TITLE_MOBILE") val appTitleMobile: String? = null,
    @SerialName("ASSIGNED_MOBILE_IMEI") val assignedMobileImei: String? = null,
    @SerialName("ORG_COUNTRY") val orgCountry: String? = null,
    @SerialName("ORG_ADDRESS") val orgAddress: String? = null,
    @SerialName("USER_KEY") val userKey: String? = null
)
