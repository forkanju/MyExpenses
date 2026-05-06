package ngo.friendship.mhealth.dc.domain.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import kotlinx.serialization.Serializable

import ngo.friendship.mhealth.dc.domain.network.NetworkConfig

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
) {
    /**
     * Returns the image source for Coil. 
     * Handles:
     * 1. Absolute URLs (starting with http)
     * 2. Base64 strings (starting with iVBO for PNG or similar)
     * 3. Relative paths (appends base URL)
     */
    fun getProfileImageSource(): Any {
        if (profileImage.isEmpty()) return ""

        // Check if it's likely a Base64 string (PNG header starts with iVBOR)
        if (profileImage.length > 100 && (profileImage.startsWith("iVBO") || profileImage.startsWith(
                "/9j/"
            ))
        ) {
            return "data:image/png;base64,$profileImage"
        }

        if (profileImage.startsWith("http")) return profileImage

        return "${NetworkConfig.getBaseUrl()}/$profileImage"
    }
}

