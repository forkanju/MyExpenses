package ngo.friendship.mhealth.dc.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveAdviceResDto(
    @SerialName("responseType") val responseType: String? = null,
    @SerialName("data") val data: Data? = null,
    @SerialName("errorDesc") val errorDesc: String? = null,
    @SerialName("errorCode") val errorCode: String? = null,
    @SerialName("responseCode") val responseCode: String? = null,
    @SerialName("responseName") val responseName: String? = null,
    @SerialName("param1") val param1: Map<String, String>? = null
) {
    @Serializable
    data class Data(
        @SerialName("advice_id") val adviceId: Int? = null
    )
}
