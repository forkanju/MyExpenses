package ngo.friendship.mhealth.dc.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class InterviewDetails(
    val interviewId: Long = 0L,
    val beneficiaryId: Long = 0L,
    val beneficiaryName: String = "",
    val beneficiaryCode: String = "",
    val location: String = "",
    val status: String = "",
    val startTime: String = "",
    val questionnaireId: Long? = null,
    val questionnaireName: String = "",
    val stCaption: String? = null,
    val printCaption: String? = null,
    val userName: String? = null,
    val isNotification: Boolean = false,
    val priority: Int = 0,
    val fcmInfo: String? = null,
    val waitingFor: String? = null,
    val stName: String? = null,
    val description: String? = null,
    val details: List<InterviewAnswer> = emptyList()
)