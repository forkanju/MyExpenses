package ngo.friendship.mhealth.dc.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SetupData(
    val medicineBrandTypes: List<MedicineBrandType> = emptyList(),
    val investigations: List<Investigation> = emptyList(),
    val diagnoses: List<Diagnosis> = emptyList(),
    val referralCenters: List<ReferralCenter> = emptyList()
)
@Serializable
data class MedicineBrandType(
    val medicineId: Long = 0L,
    val type: String = ""
)
@Serializable
data class Investigation(
    val invResultUnit: String = "",
    val investigationName: String = "",
    val orgId: Int = 0,
    val investigationId: Long = 0L,
    val state: Boolean = false,
    val investigationCode: String = ""
)
@Serializable
data class Diagnosis(
    val diagId: Long = 0L,
    val diagName: String = ""
)
@Serializable
data class ReferralCenter(
    val refCenterCode: String = "",
    val refCenterId: Long = 0L,
    val refCenterName: String = "",
    val locationId: Long = 0L
)