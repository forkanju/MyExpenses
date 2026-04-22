package ngo.friendship.mhealth.dc.domain.mapper

import ngo.friendship.mhealth.dc.data.remote.dto.LoginResponseDto
import ngo.friendship.mhealth.dc.domain.model.User


fun LoginResponseDto.Data.toDomain(password: String): User {
    return User(
        userId = userId ?: -1,
        userName = userLoginId ?: "",
        password = password,
        userKey = userKey.orEmpty(),
        profileImage = profileImage.orEmpty(),
        mobileNo = mobileNo.orEmpty()
    )
}