package ngo.friendship.mhealth.dc.domain.repository

import kotlinx.serialization.json.JsonObject
import ngo.friendship.mhealth.dc.domain.model.BeneficiaryProfile
import ngo.friendship.mhealth.dc.domain.model.FcmProfile
import ngo.friendship.mhealth.dc.domain.model.Interview
import ngo.friendship.mhealth.dc.domain.model.InterviewDetails
import ngo.friendship.mhealth.dc.domain.model.Medicine
import ngo.friendship.mhealth.dc.domain.model.QuestionAnswerJson
import ngo.friendship.mhealth.dc.domain.model.SaveDoctorFeedbackResult
import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.model.DoctorFeedbackFormState

interface CaseRepository {

    suspend fun getInterviewList(appVersion: Int, type: String): List<Interview>

    suspend fun getInterviewDetails(interviewId: Long): InterviewDetails

    suspend fun getMedicineList(type: String): List<Medicine>

    suspend fun saveDoctorFeedback(formState: DoctorFeedbackFormState): SaveDoctorFeedbackResult

    suspend fun getQuestionAnswerData(): QuestionAnswerJson

    suspend fun updateInterviewStatus(interviewId: Long, status: String): Boolean

    suspend fun getFcmProfile(fcmCode: String): FcmProfile?

    suspend fun getBeneficiaryProfile(benefCode: String): BeneficiaryProfile?

    suspend fun sendSms(
        msisdn: String,
        message: String
    ): JsonObject

}