package ngo.friendship.mhealth.dc.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FcmProfileResDto(
    @SerialName("responseType")
    val responseType: String? = null,
    @SerialName("data")
    val data: Data? = null,
    @SerialName("errorDesc")
    val errorDesc: String? = null,
    @SerialName("responseTime")
    val responseTime: String? = null,
    @SerialName("dataLength")
    val dataLength: Int? = null,
    @SerialName("execTime")
    val execTime: Int? = null,
    @SerialName("errorCode")
    val errorCode: String? = null,
    @SerialName("responseName")
    val responseName: String? = null,
    @SerialName("responseCode")
    val responseCode: String? = null
) {
    @Serializable
    data class Data(
        @SerialName("fcmProfile")
        val fcmProfile: FcmProfileDto? = null
    )

    @Serializable
    data class FcmProfileDto(
        @SerialName("ORG_ID")
        val orgId: Int? = null,
        @SerialName("EMAIL_ADDRS")
        val emailAddrs: String? = null,
        @SerialName("USER_LOGIN_ID")
        val userLoginId: String? = null,
        @SerialName("OTHER_DETAILS")
        val otherDetails: String? = null,
        @SerialName("ORG_NAME")
        val orgName: String? = null,
        @SerialName("USER_ID")
        val userId: Int? = null,
        @SerialName("USER_NAME")
        val userName: String? = null,
        @SerialName("HEADER_SMALL_LOGO_PATH")
        val headerSmallLogoPath: String? = null,
        @SerialName("GROUP_ID")
        val groupId: Int? = null,
        @SerialName("TITLE_LOGO_PATH_MOBILE")
        val titleLogoPathMobile: String? = null,
        @SerialName("ASSIGNED_MOBILE_IMEI")
        val assignedMobileImei: String? = null,
        @SerialName("MOBILE_NO")
        val mobileNo: String? = null,
        @SerialName("ORG_DESC")
        val orgDesc: String? = null,
        @SerialName("LOGIN_IMAGE_PATH_MOBILE")
        val loginImagePathMobile: String? = null,
        @SerialName("APP_TITLE_MOBILE")
        val appTitleMobile: String? = null,
        @SerialName("TARGET_HH")
        val targetHh: Int? = null,
        @SerialName("ORG_CODE")
        val orgCode: String? = null,
        @SerialName("ORG_COUNTRY")
        val orgCountry: String? = null,
        @SerialName("LOCATION_NAME")
        val locationName: String? = null,
        @SerialName("ORG_ADDRESS")
        val orgAddress: String? = null
    )
}
