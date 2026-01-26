package ngo.friendship.mhealth.dc.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse(
    @SerialName("errorCode") var errorCode: String? = null,
    @SerialName("errorDesc") val errorDesc: String? = null, //INVALID_LOGIN_ID_OR_PASSWORD
    @SerialName("responseCode") val responseCode: String? = null, // 01
    @SerialName("message") val message: String? = null // Full authentication is required to access this resource
)
