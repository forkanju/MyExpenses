package ngo.friendship.mhealth.dc.domain.mapper

import ngo.friendship.mhealth.dc.data.remote.dto.BeneficiaryProfileResDto
import ngo.friendship.mhealth.dc.domain.model.BeneficiaryProfile
import ngo.friendship.mhealth.dc.domain.model.BeneficiaryServiceItem

fun BeneficiaryProfileResDto.toDomain(): BeneficiaryProfile? {
    return this.data?.beneficiaryProfile?.let {
        BeneficiaryProfile(
            benefName = it.benefName ?: "",
            benefCode = it.benefCode ?: "",
            benefId = it.benefId ?: 0L,
            locationName = it.locationName ?: "",
            dob = it.dob ?: "",
            gender = it.gender ?: "",
            mobileNumber = it.mobileNumber ?: "",
            guardianName = it.guardianName ?: "",
            relationGuardian = it.relationGuardian ?: "",
            maritalStatus = it.maritalStatus ?: "",
            occupation = it.occupation ?: "",
            religion = it.religion ?: "",
            serviceList = it.serviceList.map { service ->
                BeneficiaryServiceItem(
                    status = service.status ?: "",
                    referredTo = service.referedTo ?: "",
                    lastOpenedBy = service.lastOpenedBy ?: "",
                    caseName = service.caseName ?: "",
                    interviewTime = service.interviewTime ?: ""
                )
            }
        )
    }
}
