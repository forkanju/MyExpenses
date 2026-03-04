package ngo.friendship.mhealth.dc.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class InterviewAnswer(
    val questionId: Long = 0L,
    val questionName: String = "",
    val answer: String = ""
)