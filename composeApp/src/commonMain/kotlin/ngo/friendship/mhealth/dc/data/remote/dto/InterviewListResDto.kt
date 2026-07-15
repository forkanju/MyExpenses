package ngo.friendship.mhealth.dc.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InterviewListResDto(
    @SerialName("responseType") val responseType: String? = null,
    @SerialName("data") val data: Data? = null,
    @SerialName("errorDesc") val errorDesc: String? = null,
    @SerialName("errorCode") val errorCode: String? = null,
    @SerialName("responseCode") val responseCode: String? = null,
    @SerialName("responseName") val responseName: String? = null,
    @SerialName("param1") val param1: Map<String, String> = emptyMap()
) {
    @Serializable
    data class Data(
        @SerialName("interview_list") val interviewList: List<Item> = emptyList()
    )

    @Serializable
    data class Item(
        @SerialName("INTERVIEW_ID") val interviewId: Long? = null,
        @SerialName("BENEF_ID") val benefId: Long? = null,
        @SerialName("BENEF_NAME") val benefName: String? = null,
        @SerialName("BENEF_CODE") val benefCode: String? = null,
        @SerialName("LOCATION") val location: String? = null,
        @SerialName("STATUS") val status: String? = null,
        @SerialName("START_TIME") val startTime: String? = null,//interview time
        @SerialName("QUESTIONNAIRE_ID") val questionnaireId: Long? = null,
        @SerialName("QUESTIONNAIRE_NAME") val questionnaireName: String? = null,
        @SerialName("stCaption") val stCaption: String? = null,
        @SerialName("printCaption") val printCaption: String? = null,
        @SerialName("actionName") val actionName: String? = null,
        @SerialName("controllerName") val controllerName: String? = null,
        @SerialName("nextFeatureId") val nextFeatureId: Int? = null,
        @SerialName("featureId") val featureId: Int? = null,
        @SerialName("nextActionRef") val nextActionRef: Int? = null,
        @SerialName("USER_NAME") val userName: String? = null,
        @SerialName("IS_NOTIFICATION") val isNotification: Int? = null,
        @SerialName("PRIORITY") val priority: Int? = null,
        @SerialName("components") val components: String? = null,
        @SerialName("compoParams") val compoParams: String? = null,
        @SerialName("fcmInfo") val fcmInfo: String? = null,
        @SerialName("WAITING_FOR") val waitingFor: String? = null,
        @SerialName("stName") val stName: String? = null,
        @SerialName("description") val description: String? = null,
        @SerialName("GENDER") val gender: String? = null,
        @SerialName("DOB") val dob: String? = null,
        @SerialName("CREATE_DATE") val createDate: String? = null)//upload time
}
