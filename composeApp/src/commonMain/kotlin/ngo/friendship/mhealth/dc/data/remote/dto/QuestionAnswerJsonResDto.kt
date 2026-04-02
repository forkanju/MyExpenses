package ngo.friendship.mhealth.dc.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionAnswerDataResDto(
    @SerialName("responseType")
    val responseType: String = "",
    @SerialName("data")
    val data: DataDto? = null,
    @SerialName("errorDesc")
    val errorDesc: String = "",
    @SerialName("responseTime")
    val responseTime: String = "",
    @SerialName("dataLength")
    val dataLength: Int = 0,
    @SerialName("execTime")
    val execTime: Int = 0,
    @SerialName("errorCode")
    val errorCode: String = "",
    @SerialName("responseName")
    val responseName: String = "",
    @SerialName("param1")
    val param1: Map<String, String> = emptyMap(),
    @SerialName("responseCode")
    val responseCode: String = ""
) {
    @Serializable
    data class DataDto(
        @SerialName("question_answer_json")
        val questionAnswerJson: List<QuestionAnswerJsonItemDto> = emptyList(),
        @SerialName("question_answer_json2")
        val questionAnswerJson2: List<QuestionAnswerJson2ItemDto> = emptyList()
    )

    @Serializable
    data class QuestionAnswerJsonItemDto(
        @SerialName("ORG_ID")
        val orgId: Int? = null,
        @SerialName("TEXT_ID")
        val textId: Long? = null,
        @SerialName("PURPOSE")
        val purpose: String? = null,
        @SerialName("LANG_CODE")
        val langCode: String? = null,
        @SerialName("CAPTION")
        val caption: String? = null,
        @SerialName("LANG_ID")
        val langId: Int? = null,
        @SerialName("ENTRY_SRC")
        val entrySrc: String? = null,
        @SerialName("NAME")
        val name: String? = null
    )

    @Serializable
    data class QuestionAnswerJson2ItemDto(
        @SerialName("ORG_ID")
        val orgId: Int? = null,
        @SerialName("TEXT_ID")
        val textId: Long? = null,
        @SerialName("PURPOSE")
        val purpose: String? = null,
        @SerialName("LANG_CODE")
        val langCode: String? = null,
        @SerialName("CAPTION")
        val caption: String? = null,
        @SerialName("LANG_ID")
        val langId: Int? = null,
        @SerialName("ENTRY_SRC")
        val entrySrc: String? = null,
        @SerialName("NAME")
        val name: String? = null
    )
}