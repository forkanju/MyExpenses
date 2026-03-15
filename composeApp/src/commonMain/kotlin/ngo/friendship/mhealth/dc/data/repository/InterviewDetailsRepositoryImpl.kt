package ngo.friendship.mhealth.dc.data.repository

import ngo.friendship.mhealth.dc.data.remote.ApiService
import ngo.friendship.mhealth.dc.data.remote.dto.InterviewDetailsReqDto
import ngo.friendship.mhealth.dc.domain.mapper.toDomain
import ngo.friendship.mhealth.dc.domain.model.InterviewDetails
import ngo.friendship.mhealth.dc.domain.repository.InterviewDetailsRepository
import ngo.friendship.mhealth.dc.utils.currentTimestamp
import ngo.friendship.mhealth.dc.utils.toDateTimeServerSlash

class InterviewDetailsRepositoryImpl(
    private val api: ApiService
) : InterviewDetailsRepository {
    override suspend fun getInterviewDetails(
        userName: String,
        password: String,
        interviewId: Long
    ): InterviewDetails {
        val response = api.getInterviewDetails(
            request = InterviewDetailsReqDto.build(
                userName = userName,
                password = password,
                requestTime = currentTimestamp.toDateTimeServerSlash(),
                interviewId = interviewId
            )
        )

        val details = response.data?.interviewDetails
            ?: return InterviewDetails()
        return details.toDomain()

    }
}