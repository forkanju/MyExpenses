package ngo.friendship.mhealth.dc.domain.mapper

import ngo.friendship.mhealth.dc.data.remote.dto.SaveDoctorFeedbackResDto
import ngo.friendship.mhealth.dc.domain.model.SaveDoctorFeedbackResult

fun SaveDoctorFeedbackResDto.toDomain(): SaveDoctorFeedbackResult {
    return SaveDoctorFeedbackResult(
        isSuccess = responseCode == "01",
        message = if (responseCode == "01") {
            "Saved successfully"
        } else {
            errorDesc.ifEmpty { "Save failed" }
        },
        responseTime = responseTime
    )
}