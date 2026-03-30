package ngo.friendship.mhealth.dc.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MedicineListResDto(
    @SerialName("responseType") val responseType: String? = null,
    @SerialName("data") val data: Data? = null,
    @SerialName("errorDesc") val errorDesc: String? = null,
    @SerialName("responseCode") val responseCode: String? = null,
    @SerialName("responseName") val responseName: String? = null,
    @SerialName("param1") val param1: Map<String, String> = emptyMap()
) {
    @Serializable
    data class Data(
        @SerialName("medicine_list") val medicineList: List<MedicineDto> = emptyList()
    )

    @Serializable
    data class MedicineDto(
        @SerialName("BOX_SIZE") val boxSize: Int? = null,
        @SerialName("UNIT_TYPE") val unitType: String? = null,
        @SerialName("GENERIC_NAME") val genericName: String? = null,
        @SerialName("BRAND_NAME") val brandName: String? = null,
        @SerialName("MEDICINE_ID") val medicineId: Long? = null,
        @SerialName("TYPE") val type: String? = null
    )
}