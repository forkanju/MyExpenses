package ngo.friendship.mhealth.dc.domain.model


data class QuestionAnswerJson(
    val questionAnswerJson: List<QuestionAnswerTemplate> = emptyList(),
    val questionAnswerJson2: List<QuestionAnswerTemplateDefault> = emptyList()
)

data class QuestionAnswerTemplate(
    val orgId: Int = 0,
    val textId: Long = 0L,
    val purpose: String = "",
    val langCode: String = "",
    val caption: String = "",
    val langId: Int = 0,
    val entrySrc: String = "",
    val name: String = ""
)

data class QuestionAnswerTemplateDefault(
    val orgId: Int = 0,
    val textId: Long = 0L,
    val purpose: String = "",
    val langCode: String = "",
    val caption: String = "",
    val langId: Int = 0,
    val entrySrc: String = "",
    val name: String = ""
)