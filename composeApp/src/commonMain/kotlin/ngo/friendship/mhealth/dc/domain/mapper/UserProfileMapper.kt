package ngo.friendship.mhealth.dc.domain.mapper

import ngo.friendship.mhealth.dc.data.remote.dto.FcmProfile
import ngo.friendship.mhealth.dc.domain.model.UserProfile

fun FcmProfile.toDomain(): UserProfile {
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
