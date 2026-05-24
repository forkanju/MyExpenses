package ngo.friendship.mhealth.dc.domain.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class SetupData(
    val medicineBrandTypes: List<MedicineBrandType> = emptyList(),
    val investigations: List<Investigation> = emptyList(),
    val diagnoses: List<Diagnosis> = emptyList(),
    val referralCenters: List<ReferralCenter> = emptyList()
)

@Serializable
@Entity
data class MedicineBrandType(
    val medicineId: Long = 0L,
    val type: String = "",
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L
)

@Serializable
@Entity
data class Investigation(
    val invResultUnit: String = "",
    val investigationName: String = "",
    val orgId: Int = 0,
    val investigationId: Long = 0L,
    val state: Boolean = false,
    val investigationCode: String = "",
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L
)

@Serializable
@Entity
data class Diagnosis(
    val diagId: String = "",
    val diagName: String = "",
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L
)

@Serializable
@Entity
data class ReferralCenter(
    val refCenterCode: String = "",
    val refCenterId: Long = 0L,
    val refCenterName: String = "",
    val locationId: Long = 0L,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L
)

