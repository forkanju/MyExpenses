package ngo.friendship.mhealth.dc.domain.model

import kotlinx.serialization.Serializable
@Serializable
data class Interview(
    val interviewId: Long = 0L,
    val beneficiaryId: Long = 0L,
    val beneficiaryName: String = "",
    val beneficiaryCode: String = "",
    val location: String = "",
    val status: String = "",
    val startTime: String = "00:00",
    val questionnaireId: Long? = null,
    val questionnaireName: String = "",
    val stCaption: String? = null,
    val printCaption: String? = null,
    val actionName: String? = null,
    val controllerName: String? = null,
    val nextFeatureId: Int? = null,
    val featureId: Int? = null,
    val nextActionRef: Int? = null,
    val userName: String? = null,
    val isNotification: Boolean = false,
    val priority: Int = 0,
    val components: String? = null,
    val compoParams: String? = null,
    val fcmInfo: String? = null,
    val waitingFor: String? = null,
    val stName: String? = null,
    val description: String? = null
)
