package ngo.friendship.mhealth.dc.domain.mapper

import ngo.friendship.mhealth.dc.data.remote.dto.InterviewDetailsResDto
import ngo.friendship.mhealth.dc.domain.model.InterviewAnswer
import ngo.friendship.mhealth.dc.domain.model.InterviewDetails
import ngo.friendship.mhealth.dc.domain.model.SysPrescription
import ngo.friendship.mhealth.dc.utils.calculateAge

fun InterviewDetailsResDto.InterviewDetails.toDomain(): InterviewDetails = InterviewDetails(
    interviewId = interviewId ?: -1,
    beneficiaryId = benefId ?: -1,
    beneficiaryName = benefName.orEmpty(),
    beneficiaryGender = benefGender,
    beneficiaryDob = benefDob,
    beneficiaryAge = benefDob?.calculateAge() ?: "",
    beneficiaryCode = benefCode.orEmpty(),
    location = location.orEmpty(),
    status = status.orEmpty(),
    startTime = startTime.orEmpty(),
    questionnaireId = questionnaireId,
    questionnaireName = questionnaireName.orEmpty(),
    stCaption = stCaption,
    printCaption = printCaption,
    userName = userName,
    isNotification = (isNotification ?: 0) == 1,
    priority = priority ?: 0,
    fcmInfo = fcmInfo,
    waitingFor = waitingFor,
    stName = stName,
    description = description,
    createDate = createDate,
    fcmMobile = smsNumbers.firstOrNull()?.fcmMobile,
    beneficiaryMobile = smsNumbers.firstOrNull()?.benefMobile,
    details = detailsList.map { it.toDomain() },
    sysPrescriptionList = sysPrescriptionList.map { it.toDomain() }
)

fun InterviewDetailsResDto.DetailItem.toDomain(): InterviewAnswer = InterviewAnswer(
    questionId = questionId ?: -1,
    questionName = questionName.orEmpty(),
    answer = answer.orEmpty()
)

fun InterviewDetailsResDto.SysPrescriptionItem.toDomain(): SysPrescription = SysPrescription(
    prescription = prescription.orEmpty()
)