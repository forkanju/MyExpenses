package ngo.friendship.mhealth.dc.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveDiagnosisResDto(
    @SerialName("responseType")
    val responseType: String? = null,
    @SerialName("data")
    val data: SaveDiagnosisResData? = null,
    @SerialName("errorDesc")
    val errorDesc: String? = null,
    @SerialName("responseTime")
    val responseTime: String? = null,
    @SerialName("dataLength")
    val dataLength: Int? = null,
    @SerialName("execTime")
    val execTime: Int? = null,
    @SerialName("errorCode")
    val errorCode: String? = null,
    @SerialName("responseName")
    val responseName: String? = null,
    @SerialName("param1")
    val param1: Map<String, String>? = null,
    @SerialName("responseCode")
    val responseCode: String? = null
)

@Serializable
data class SaveDiagnosisResData(
    @SerialName("diag_id")
    val diagId: Int? = null
)
