package ngo.friendship.mhealth.dc.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    @SerialName("data") val data: Data? = null,
    @SerialName("dataLength") val dataLength: Int? = null, // 1069
    @SerialName("errorCode") val errorCode: String? = null,
    @SerialName("errorDesc") val errorDesc: String? = null,
    @SerialName("execTime") val execTime: Int? = null, // 14
    @SerialName("responseCode") val responseCode: String? = null, // 01
    @SerialName("responseName") val responseName: String? = null, // LOGIN_WEB
    @SerialName("responseTime") val responseTime: String? = null, // Thu Jan 22 16:15:51 BDT 2026
    @SerialName("responseType") val responseType: String? = null // USER_GATE
) {
    @Serializable
    data class Data(
        @SerialName("APP_TITLE_MOBILE") val appTitleMobile: String? = null, // Friendship mHealth
        @SerialName("ASSIGNED_MOBILE_IMEI") val assignedMobileImei: String? = null, // IMEI_FREE
        @SerialName("IS_CLONE") val isClone: Boolean? = null, // false
        @SerialName("MOBILE_NO") val mobileNo: String? = null, // 000
        @SerialName("ORG_ADDRESS") val orgAddress: String? = null, // Dhaka
        @SerialName("ORG_CODE") val orgCode: String? = null, // FR
        @SerialName("ORG_COUNTRY") val orgCountry: String? = null, // Bangladesh
        @SerialName("ORG_DESC") val orgDesc: String? = null, // mHealth program of Friendship NGO
        @SerialName("ORG_ID") val orgId: Int? = null, // 101
        @SerialName("ORG_NAME") val orgName: String? = null, // Friendship
        @SerialName("PROFILE_IMAGE") val profileImage: String? = null,
        @SerialName("TOKEN") val token: String? = null, // eyJhbGciOiJIUzI1NiJ9...
        @SerialName("USER_ID") val userId: Int? = null, // 152
        @SerialName("USER_KEY") val userKey: String? = null, // IRxMawG9xtcFbA4VwCb0Eg==
        @SerialName("USER_LOGIN_ID") val userLoginId: String? = null, // sharif.dr
        @SerialName("USER_NAME") val userName: String? = null // sharif.dr
    )
}