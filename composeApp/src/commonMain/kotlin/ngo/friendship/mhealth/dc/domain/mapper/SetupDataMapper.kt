package ngo.friendship.mhealth.dc.domain.mapper


import ngo.friendship.mhealth.dc.data.remote.dto.SetupDataResDto
import ngo.friendship.mhealth.dc.domain.model.Diagnosis
import ngo.friendship.mhealth.dc.domain.model.Investigation
import ngo.friendship.mhealth.dc.domain.model.MedicineBrandType
import ngo.friendship.mhealth.dc.domain.model.ReferralCenter
import ngo.friendship.mhealth.dc.domain.model.SetupData

fun SetupDataResDto.Data.toDomain(): SetupData {
    return SetupData(
        medicineBrandTypes = medicineBrandTypeList.orEmpty().map { it.toDomain() },
        investigations = investigationList.orEmpty().map { it.toDomain() },
        diagnoses = diagnosisList.orEmpty().map { it.toDomain() },
        referralCenters = referralCenterList.orEmpty().map { it.toDomain() }
    )
}

fun SetupDataResDto.MedicineBrandTypeDto.toDomain(): MedicineBrandType {
    return MedicineBrandType(
        medicineId = medicineId ?: 0L,
        type = type.orEmpty()
    )
}

fun SetupDataResDto.InvestigationDto.toDomain(): Investigation {
    return Investigation(
        invResultUnit = invResultUnit.orEmpty(),
        investigationName = investigationName.orEmpty(),
        orgId = orgId ?: 0,
        investigationId = investigationId ?: 0L,
        state = state ?: false,
        investigationCode = investigationCode.orEmpty()
    )
}

fun SetupDataResDto.DiagnosisDto.toDomain(): Diagnosis {
    return Diagnosis(
        diagId = diagId ?: 0L,
        diagName = diagName.orEmpty()
    )
}

fun SetupDataResDto.ReferralCenterDto.toDomain(): ReferralCenter {
    return ReferralCenter(
        refCenterCode = refCenterCode.orEmpty(),
        refCenterId = refCenterId ?: 0L,
        refCenterName = refCenterName.orEmpty(),
        locationId = locationId ?: 0L
    )
}