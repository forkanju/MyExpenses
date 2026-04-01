package ngo.friendship.mhealth.dc.domain.model

data class SaveDoctorFeedbackResult(
    val isSuccess: Boolean = false,
    val message: String = "",
    val responseTime: String = ""
)
