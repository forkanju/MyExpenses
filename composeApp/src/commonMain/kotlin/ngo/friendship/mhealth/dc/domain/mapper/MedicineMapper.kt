package ngo.friendship.mhealth.dc.domain.mapper

import ngo.friendship.mhealth.dc.data.remote.dto.MedicineListResDto
import ngo.friendship.mhealth.dc.domain.model.Medicine

fun MedicineListResDto.MedicineDto.toDomain(): Medicine = Medicine(
    medicineId = medicineId ?: -1,
    genericName = genericName.orEmpty(),
    brandName = brandName.orEmpty(),
    type = type.orEmpty(),
    boxSize = boxSize ?: 0,
    unitType = unitType.orEmpty()
)
