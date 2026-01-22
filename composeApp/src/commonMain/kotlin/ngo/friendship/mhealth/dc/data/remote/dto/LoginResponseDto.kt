package ngo.friendship.mhealth.dc.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    @SerialName("responseCode") val responseCode: String? = null,
    @SerialName("errorDesc") val errorDesc: String? = null,
    @SerialName("data") val data: UserDto? = null
)

@Serializable
data class UserDto(
    // Put actual fields from API response
    @SerialName("userId") val userId: Int? = null,
    @SerialName("userName") val userName: String? = null,
    @SerialName("token") val token: String? = null
)