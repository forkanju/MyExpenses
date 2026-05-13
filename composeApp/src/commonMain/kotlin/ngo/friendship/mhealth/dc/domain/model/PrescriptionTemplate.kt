package ngo.friendship.mhealth.dc.domain.model

import kotlinx.serialization.Serializable

import ngo.friendship.mhealth.dc.data.remote.dto.PrescriptionMedicineDto

@Serializable
data class PrescriptionTemplate(
    val id: String,
    val prescriptionId: Long? = null,
    val title: String,
    val updatedDate: String,
    val createdDate: String,
    val color: Long, // Hex color code
    val doctorAdvice: String? = null,
    val commentsForFcm: String? = null,
    val doctorNotes: String? = null,
    val medicineList: List<PrescriptionMedicineDto>? = null,
    val isGlobal: Boolean = false
)
