package ngo.friendship.mhealth.dc.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class FcmProfile(
    val userId: Int = 0,
    val userName: String = "",
    val loginId: String = "",
    val email: String = "",
    val mobileNo: String = "",
    val location: String = "",
    val orgName: String = "",
    val orgId: Int = 0,
    val otherDetails: String = "",
    val headerSmallLogoPath: String = "",
    val groupId: Int = 0,
    val titleLogoPathMobile: String = "",
    val assignedMobileImei: String = "",
    val orgDesc: String = "",
    val loginImagePathMobile: String = "",
    val appTitleMobile: String = "",
    val targetHh: Int = 0,
    val orgCode: String = "",
    val orgCountry: String = "",
    val orgAddress: String = ""
)
