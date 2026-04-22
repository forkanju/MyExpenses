package ngo.friendship.mhealth.dc.domain.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "user_profile")
@Serializable
data class UserProfile(
    @PrimaryKey val userId: Int = 0,
    val userName: String = "",
    val loginId: String = "",
    val email: String = "",
    val mobileNo: String = "",
    val location: String = "",
    val orgName: String = "",
    val profileImage: String = ""
)
