package ngo.friendship.mhealth.dc.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InterviewDetailsResDto(
    @SerialName("responseType") val responseType: String? = null,
    @SerialName("data") val data: Data? = null,
    @SerialName("errorDesc") val errorDesc: String? = null,
    @SerialName("responseCode") val responseCode: String? = null,
    @SerialName("responseName") val responseName: String? = null,
    @SerialName("param1") val param1: Map<String, String> = emptyMap()
) {
    @Serializable
    data class Data(
        @SerialName("interview_details") val interviewDetails: InterviewDetails? = null
    )

    @Serializable
    data class InterviewDetails(
        @SerialName("INTERVIEW_ID") val interviewId: Long? = null,
        @SerialName("BENEF_ID") val benefId: Long? = null,
        @SerialName("BENEF_NAME") val benefName: String? = null,
        @SerialName("BENEF_CODE") val benefCode: String? = null,
        @SerialName("LOCATION") val location: String? = null,
        @SerialName("STATUS") val status: String? = null,
        @SerialName("START_TIME") val startTime: String? = null,
        @SerialName("QUESTIONNAIRE_ID") val questionnaireId: Long? = null,
        @SerialName("QUESTIONNAIRE_NAME") val questionnaireName: String? = null,
        @SerialName("stCaption") val stCaption: String? = null,
        @SerialName("printCaption") val printCaption: String? = null,
        @SerialName("USER_NAME") val userName: String? = null,
        @SerialName("IS_NOTIFICATION") val isNotification: Int? = null,
        @SerialName("PRIORITY") val priority: Int? = null,
        @SerialName("fcmInfo") val fcmInfo: String? = null,
        @SerialName("WAITING_FOR") val waitingFor: String? = null,
        @SerialName("stName") val stName: String? = null,
        @SerialName("description") val description: String? = null,

        @SerialName("details_list") val detailsList: List<DetailItem> = emptyList()
    )

    @Serializable
    data class DetailItem(
        @SerialName("ANSWER") val answer: String? = null,
        @SerialName("Q_NAME") val questionName: String? = null,
        @SerialName("Q_ID") val questionId: Long? = null
    )
}