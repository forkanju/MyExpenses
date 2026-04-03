package ngo.friendship.mhealth.dc.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userId: Int = 0,
    val userName: String = "",
    val password: String = "",
    val userKey: String = "",
    val profileImage: String = "",
    val mobileNo: String = ""
)
