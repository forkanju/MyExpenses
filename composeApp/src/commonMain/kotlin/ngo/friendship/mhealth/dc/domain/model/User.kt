package ngo.friendship.mhealth.dc.domain.model

data class User(
    val userId: Int = 0,
    val userName: String = "",
    val userKey: String = "",
    val profileImage: String = "",
    val mobileNo: String = ""
)
