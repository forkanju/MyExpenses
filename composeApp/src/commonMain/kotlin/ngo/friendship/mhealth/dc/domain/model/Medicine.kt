package ngo.friendship.mhealth.dc.domain.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity
data class Medicine(
    @PrimaryKey
    val medicineId: Long = -1,
    val genericName: String = "",
    val brandName: String = "",
    val type: String = "",
    val boxSize: Int = 0,
    val unitType: String = ""
)
