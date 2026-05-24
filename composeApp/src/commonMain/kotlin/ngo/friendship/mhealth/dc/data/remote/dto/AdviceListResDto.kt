package ngo.friendship.mhealth.dc.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdviceListResDto(
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
        @SerialName("advice_list") val adviceList: List<AdviceItemDto> = emptyList()
    )

    @Serializable
    data class AdviceItemDto(
        @SerialName("ADVICE_NAME") val adviceName: String? = null,
        @SerialName("ADVICE_ID") val adviceId: Int? = null,
        @SerialName("STATE") val state: Boolean? = null,
        @SerialName("ADVICE_DETAILS") val adviceDetails: String? = null,
        @SerialName("VERSION_NO") val versionNo: Int? = null
    )
}
