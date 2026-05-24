package ngo.friendship.mhealth.dc.domain.mapper

import ngo.friendship.mhealth.dc.data.remote.dto.SaveDoctorFeedbackResDto
import ngo.friendship.mhealth.dc.domain.model.SaveDoctorFeedbackResult

fun SaveDoctorFeedbackResDto.toDomain(): SaveDoctorFeedbackResult {
    val isSuccess = responseCode == "01" && errorCode != "01"
    return SaveDoctorFeedbackResult(
        isSuccess = isSuccess,
        message = if (isSuccess) {
            "Saved successfully"
        } else {
            errorDesc.ifEmpty { "Save failed" }
        },
        responseTime = responseTime
    )
}
