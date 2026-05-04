package ngo.friendship.mhealth.dc.domain.mapper

import ngo.friendship.mhealth.dc.data.remote.dto.DoctorProfile
import ngo.friendship.mhealth.dc.data.remote.dto.FcmProfileResDto
import ngo.friendship.mhealth.dc.domain.model.FcmProfile
import ngo.friendship.mhealth.dc.domain.model.UserProfile

fun DoctorProfile.toDomain(): UserProfile {
    return UserProfile(
        userId = userId ?: 0,
        userName = userName ?: "",
        loginId = userLoginId ?: "",
        email = email ?: "",
        mobileNo = mobileNo ?: "",
        location = locationName ?: "",
        orgName = orgName ?: "",
        profileImage = profileImage ?: ""
    )
}

fun FcmProfileResDto.FcmProfileDto.toDomainFcmProfile(): FcmProfile {
    return FcmProfile(
        userId = userId ?: 0,
        userName = userName ?: "",
        loginId = userLoginId ?: "",
        email = emailAddrs ?: "",
        mobileNo = mobileNo ?: "",
        location = locationName ?: "",
        orgName = orgName ?: "",
        orgId = orgId ?: 0,
        otherDetails = otherDetails ?: "",
        headerSmallLogoPath = headerSmallLogoPath ?: "",
        groupId = groupId ?: 0,
        titleLogoPathMobile = titleLogoPathMobile ?: "",
        assignedMobileImei = assignedMobileImei ?: "",
        orgDesc = orgDesc ?: "",
        loginImagePathMobile = loginImagePathMobile ?: "",
        appTitleMobile = appTitleMobile ?: "",
        targetHh = targetHh ?: 0,
        orgCode = orgCode ?: "",
        orgCountry = orgCountry ?: "",
        orgAddress = orgAddress ?: ""
    )
}
