package ngo.friendship.mhealth.dc.domain.mapper

import ngo.friendship.mhealth.dc.data.remote.dto.PrescriptionTemplateDto
import ngo.friendship.mhealth.dc.domain.model.PrescriptionTemplate

fun PrescriptionTemplateDto.toDomain(): PrescriptionTemplate {
    return PrescriptionTemplate(
        id = prescId ?: "",
        prescriptionId = prescId?.toLongOrNull(),
        title = prescLabel.orEmpty(),
        updatedDate = lastUpdatedOn.orEmpty(),
        createdDate = createDate.orEmpty(),
        color = 0xFF60BF77, // Default color
        doctorAdvice = doctorAdvice,
        commentsForFcm = messageToFcm,
        doctorNotes = doctorFindings,
        medicineList = medicineList,
        isGlobal = isGlobalPress == "1"
    )
}
