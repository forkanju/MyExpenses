package ngo.friendship.mhealth.dc.domain.mapper

import ngo.friendship.mhealth.dc.data.remote.dto.LoginResponseDto
import ngo.friendship.mhealth.dc.domain.model.User


fun LoginResponseDto.Data.toDomain(): User {
    return User(
        userId = userId ?: -1,
        userName = userName.orEmpty(),
        token = token.orEmpty()
    )
}

//
//
//fun Attendance.toEntity() = AttendanceEntity(
//    beneficiaryId = beneficiaryId,
//    date = date,
//    ecdAttended = ecdAttended,
//    finLitAttended = finLitAttended,
//    gbvAttended = gbvAttended,
//    washAttended = washAttended,
//    present = present,
//    status = status,
//    message = message,
//    type = type
//)
//
//fun Beneficiary.toEntity(bomaId: Int, criteria: Criteria) = BeneficiaryEntity(
//    id = id,
//    age = age,
//    gender = gender,
//    phoneNo = phoneNo,
//    bomaId = bomaId,
//    criteria = criteria,
//    householdNumber = householdNumber,
//    beneficiaryName = name,
//    photoData = photoData
//)
//
//fun Area.Boma.toEntity() = BomaEntity(
//    id = id,
//    name = name,
//    totalBeneficiary = 0,
//    lastUpdated = currentTimestamp
//)