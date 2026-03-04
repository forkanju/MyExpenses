package ngo.friendship.mhealth.dc.domain.repository

import ngo.friendship.mhealth.dc.domain.model.InterviewDetails

interface InterviewDetailsRepository {
    suspend fun getInterviewDetails(
        userName: String,
        password: String,
        interviewId: Long,
    ): InterviewDetails
}