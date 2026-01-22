package ngo.friendship.mhealth.dc.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorDto(
    @SerialName("errorCode")
    var errorCode: String? = null,
    @SerialName("errorMsg")
    var errorDesc: String? = null,
    @SerialName("message")
    val message: String? = null // Full authentication is required to access this resource
)
