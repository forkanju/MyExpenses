package ngo.friendship.mhealth.dc.domain.mapper

import ngo.friendship.mhealth.dc.data.remote.dto.InterviewDetailsResDto
import ngo.friendship.mhealth.dc.domain.model.InterviewAnswer
import ngo.friendship.mhealth.dc.domain.model.InterviewDetails

fun InterviewDetailsResDto.InterviewDetails.toDomain(): InterviewDetails = InterviewDetails(
    interviewId = interviewId ?: -1,
    beneficiaryId = benefId ?: -1,
    beneficiaryName = benefName.orEmpty(),
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
    details = detailsList.map { it.toDomain() }
)

fun InterviewDetailsResDto.DetailItem.toDomain(): InterviewAnswer = InterviewAnswer(
    questionId = questionId ?: -1,
    questionName = questionName.orEmpty(),
    answer = answer.orEmpty()
)