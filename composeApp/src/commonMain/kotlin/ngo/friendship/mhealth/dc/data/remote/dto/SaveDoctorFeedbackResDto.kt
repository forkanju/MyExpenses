package ngo.friendship.mhealth.dc.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveDoctorFeedbackResDto(
    @SerialName("responseType")
    val responseType: String = "",
    @SerialName("data")
    val data: Map<String, String> = emptyMap(),
    @SerialName("errorDesc")
    val errorDesc: String = "",
    @SerialName("responseTime")
    val responseTime: String = "",
    @SerialName("dataLength")
    val dataLength: Int = 0,
    @SerialName("execTime")
    val execTime: Int = 0,
    @SerialName("errorCode")
    val errorCode: String = "",
    @SerialName("responseName")
    val responseName: String = "",
    @SerialName("param1")
    val param1: Map<String, String> = emptyMap(),
    @SerialName("responseCode")
    val responseCode: String = ""
)