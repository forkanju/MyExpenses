package ngo.friendship.mhealth.dc.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PrescriptionTemplateResDto(
    @SerialName("responseType") val responseType: String? = null,
    @SerialName("data") val data: PrescriptionTemplateData? = null,
    @SerialName("errorDesc") val errorDesc: String? = null,
    @SerialName("responseTime") val responseTime: String? = null,
    @SerialName("dataLength") val dataLength: Int? = null,
    @SerialName("execTime") val execTime: Int? = null,
    @SerialName("errorCode") val errorCode: String? = null,
    @SerialName("responseName") val responseName: String? = null,
    @SerialName("responseCode") val responseCode: String? = null
)

@Serializable
data class PrescriptionTemplateData(
    @SerialName("prescription_template") val prescriptionTemplates: List<PrescriptionTemplateDto>? = null
)

@Serializable
data class PrescriptionTemplateDto(
    @SerialName("CREATE_DATE") val createDate: String? = null,
    @SerialName("PRESC_LABEL") val prescLabel: String? = null,
    @SerialName("MESSAGE_TO_FCM") val messageToFcm: String? = null,
    @SerialName("medicine_list") val medicineList: List<PrescriptionMedicineDto>? = null,
    @SerialName("LAST_UPDATED_ON") val lastUpdatedOn: String? = null,
    @SerialName("DOCTOR_FINDINGS") val doctorFindings: String? = null,
    @SerialName("PRESC_ID") val prescId: String? = null,
    @SerialName("DOCTOR_ADVICE") val doctorAdvice: String? = null,
    @SerialName("IS_GLOBAL_PRESCRIPTION") val isGlobalPress: String? = null
)

@Serializable
data class PrescriptionMedicineDto(
    @SerialName("ORG_ID") val orgId: String? = null,
    @SerialName("DURATION_DAY") val durationDay: String? = null,
    @SerialName("MEDICINE_ID") val medicineId: String? = null,
    @SerialName("QTY") val qty: String? = null,
    @SerialName("DAILY_DOSE") val dailyDose: String? = null,
    @SerialName("PRESC_ID") val prescId: String? = null,
    @SerialName("TAKING_RULE") val takingRule: String? = null,
    @SerialName("GENERIC_NAME") val genericName: String? = null,
    @SerialName("BRAND_NAME") val brandName: String? = null,
)
