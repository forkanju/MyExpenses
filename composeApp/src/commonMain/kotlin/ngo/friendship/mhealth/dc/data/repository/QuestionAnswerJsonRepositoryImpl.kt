package ngo.friendship.mhealth.dc.data.repository

import ngo.friendship.mhealth.dc.data.remote.ApiService
import ngo.friendship.mhealth.dc.data.remote.dto.QuestionAnswerJsonReqDto
import ngo.friendship.mhealth.dc.domain.mapper.toDomain
import ngo.friendship.mhealth.dc.domain.model.QuestionAnswerJson
import ngo.friendship.mhealth.dc.domain.repository.QuestionAnswerJsonRepository
import ngo.friendship.mhealth.dc.utils.currentTimestamp
import ngo.friendship.mhealth.dc.utils.toDateTimeServerSlash

class QuestionAnswerJsonRepositoryImpl(

    private val api: ApiService
) : QuestionAnswerJsonRepository {

    override suspend fun getQuestionAnswerData(
        userName: String,
        password: String
    ): QuestionAnswerJson {
        val response = api.getQuestionAnswerData(
            request = QuestionAnswerJsonReqDto.build(
                userName = userName,
                password = password,
                requestTime = currentTimestamp.toDateTimeServerSlash()
            )
        )
        return response.toDomain()
    }
}