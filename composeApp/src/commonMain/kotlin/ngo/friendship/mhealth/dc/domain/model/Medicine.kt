package ngo.friendship.mhealth.dc.domain.model

data class Medicine(
    val medicineId: Long = -1,
    val genericName: String = "",
    val brandName: String = "",
    val type: String = "",
    val boxSize: Int = 0,
    val unitType: String = ""
)
