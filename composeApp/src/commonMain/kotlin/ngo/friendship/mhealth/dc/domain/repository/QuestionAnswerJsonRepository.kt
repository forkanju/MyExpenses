package ngo.friendship.mhealth.dc.domain.repository

import ngo.friendship.mhealth.dc.domain.model.GateQuestionAnswerData

interface QuestionAnswerJsonRepository {
    suspend fun getQuestionAnswerData(
        userName: String,
        password: String
    ): GateQuestionAnswerData
}