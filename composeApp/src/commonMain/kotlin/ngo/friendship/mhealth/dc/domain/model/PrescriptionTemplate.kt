package ngo.friendship.mhealth.dc.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PrescriptionTemplate(
    val id: String,
    val title: String,
    val updatedDate: String,
    val createdDate: String,
    val color: Long // Hex color code
)
