package ngo.friendship.mhealth.dc.data.repository

import ngo.friendship.mhealth.dc.data.remote.ApiService
import ngo.friendship.mhealth.dc.data.remote.dto.InterviewListReqDto
import ngo.friendship.mhealth.dc.domain.mapper.toDomain
import ngo.friendship.mhealth.dc.domain.model.Interview
import ngo.friendship.mhealth.dc.domain.repository.InterviewListRepository
import ngo.friendship.mhealth.dc.utils.currentTimestamp
import ngo.friendship.mhealth.dc.utils.toDateTimeServerSlash

class InterviewListRepositoryImpl(
    private val api: ApiService
) : InterviewListRepository {

    override suspend fun getInterviewList(
        userName: String, password: String, appVersion: Int
    ): List<Interview> {
        val response = api.getInterviewList(
            request = InterviewListReqDto.build(
                userName = userName,       // demo pattern
                password = password,
                appVersion = appVersion,
                requestTime = currentTimestamp.toDateTimeServerSlash()
            ),
            appVersion = appVersion
        )

        return response.data
            ?.interviewList
            ?.map { it.toDomain() }
            ?: emptyList()
    }
}