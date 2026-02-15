package ngo.friendship.mhealth.dc.domain.repository

import ngo.friendship.mhealth.dc.domain.model.Interview

interface InterviewListRepository {
    suspend fun getInterviewList(
        userName: String,
        password: String,
        appVersion: Int
    ): List<Interview>
}