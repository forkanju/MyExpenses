package ngo.friendship.mhealth.dc.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SetupDataResDto(
    @SerialName("responseType")
    val responseType: String? = null,
    @SerialName("data")
    val data: Data? = null,
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
    val param1: Param1? = null,
    @SerialName("responseCode")
    val responseCode: String? = null
) {
    @Serializable
    data class Data(
        @SerialName("medicine_brand_type_list")
        val medicineBrandTypeList: List<MedicineBrandTypeDto>? = null,
        @SerialName("investigation_list")
        val investigationList: List<InvestigationDto>? = null,
        @SerialName("diagnosis_list")
        val diagnosisList: List<DiagnosisDto>? = null,
        @SerialName("referral_center_list")
        val referralCenterList: List<ReferralCenterDto>? = null
    )

    @Serializable
    data class MedicineBrandTypeDto(
        @SerialName("MEDICINE_ID")
        val medicineId: Long? = null,
        @SerialName("TYPE")
        val type: String? = null
    )

    @Serializable
    data class InvestigationDto(
        @SerialName("inv_result_unit")
        val invResultUnit: String? = null,
        @SerialName("investigation_name")
        val investigationName: String? = null,
        @SerialName("org_id")
        val orgId: Int? = null,
        @SerialName("investigation_id")
        val investigationId: Long? = null,
        @SerialName("state")
        val state: Boolean? = null,
        @SerialName("investigation_code")
        val investigationCode: String? = null
    )

    @Serializable
    data class DiagnosisDto(
        @SerialName("DIAG_ID")
        val diagId: Long? = null,
        @SerialName("DIAG_NAME")
        val diagName: String? = null
    )

    @Serializable
    data class ReferralCenterDto(
        @SerialName("REF_CENTER_CODE")
        val refCenterCode: String? = null,
        @SerialName("REF_CENTER_ID")
        val refCenterId: Long? = null,
        @SerialName("REF_CENTER_NAME")
        val refCenterName: String? = null,
        @SerialName("LOCATION_ID")
        val locationId: Long? = null
    )

    @Serializable
    class Param1
}