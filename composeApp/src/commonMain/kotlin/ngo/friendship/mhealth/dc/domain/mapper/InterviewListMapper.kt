package ngo.friendship.mhealth.dc.domain.mapper

import ngo.friendship.mhealth.dc.data.remote.dto.InterviewListResDto
import ngo.friendship.mhealth.dc.domain.model.Interview

fun InterviewListResDto.Item.toDomain(): Interview = Interview(
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
    actionName = actionName,
    controllerName = controllerName,
    nextFeatureId = nextFeatureId,
    featureId = featureId,
    nextActionRef = nextActionRef,
    userName = userName,
    isNotification = isNotification == 1,
    priority = priority ?: 0,
    components = components,
    compoParams = compoParams,
    fcmInfo = fcmInfo,
    waitingFor = waitingFor,
    stName = stName,
    description = description,
    gender = gender,
    dob = dob,
    createDate = createDate
)
