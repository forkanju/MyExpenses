package ngo.friendship.mhealth.dc.domain.mapper


import ngo.friendship.mhealth.dc.data.remote.dto.QuestionAnswerDataResDto
import ngo.friendship.mhealth.dc.domain.model.QuestionAnswerJson
import ngo.friendship.mhealth.dc.domain.model.QuestionAnswerTemplate
import ngo.friendship.mhealth.dc.domain.model.QuestionAnswerTemplateDefault

fun QuestionAnswerDataResDto.QuestionAnswerJsonItemDto.toDomain(): QuestionAnswerTemplate {
    return QuestionAnswerTemplate(
        orgId = orgId ?: 0,
        textId = textId ?: 0L,
        purpose = purpose.orEmpty(),
        langCode = langCode.orEmpty(),
        caption = caption.orEmpty(),
        langId = langId ?: 0,
        entrySrc = entrySrc.orEmpty(),
        name = name.orEmpty()
    )
}

fun QuestionAnswerDataResDto.QuestionAnswerJson2ItemDto.toDomain(): QuestionAnswerTemplateDefault {
    return QuestionAnswerTemplateDefault(
        orgId = orgId ?: 0,
        textId = textId ?: 0L,
        purpose = purpose.orEmpty(),
        langCode = langCode.orEmpty(),
        caption = caption.orEmpty(),
        langId = langId ?: 0,
        entrySrc = entrySrc.orEmpty(),
        name = name.orEmpty()
    )
}

fun QuestionAnswerDataResDto.toDomain(): QuestionAnswerJson {
    return QuestionAnswerJson(
        questionAnswerJson = data?.questionAnswerJson?.map { it.toDomain() }.orEmpty(),
        questionAnswerJson2 = data?.questionAnswerJson2?.map { it.toDomain() }.orEmpty()
    )
}