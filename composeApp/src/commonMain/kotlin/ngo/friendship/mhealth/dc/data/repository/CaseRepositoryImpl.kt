package ngo.friendship.mhealth.dc.data.repository

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import ngo.friendship.mhealth.dc.data.local.LocalSettings
import ngo.friendship.mhealth.dc.data.remote.ApiService
import ngo.friendship.mhealth.dc.data.remote.dto.InterviewDetailsReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.InterviewListReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.MedicineListReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.QuestionAnswerJsonReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.SaveDoctorFeedbackReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.UpdateInterviewStatusReqDto
import ngo.friendship.mhealth.dc.domain.mapper.toDomain
import ngo.friendship.mhealth.dc.domain.model.Interview
import ngo.friendship.mhealth.dc.domain.model.InterviewDetails
import ngo.friendship.mhealth.dc.domain.model.Medicine
import ngo.friendship.mhealth.dc.domain.model.QuestionAnswerJson
import ngo.friendship.mhealth.dc.domain.model.SaveDoctorFeedbackResult
import ngo.friendship.mhealth.dc.domain.repository.CaseRepository
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.model.DoctorFeedbackFormState
import ngo.friendship.mhealth.dc.utils.currentTimestamp
import ngo.friendship.mhealth.dc.utils.toDateTimeServerSlash

class CaseRepositoryImpl(
    private val api: ApiService,
    private val localSettings: LocalSettings
) : CaseRepository {

    override suspend fun getInterviewList(appVersion: Int, type: String): List<Interview> {
        val response = api.getInterviewList(
            request = InterviewListReqDto.build(
                userName = localSettings.user.userName,
                password = localSettings.user.password,
                requestTime = currentTimestamp.toDateTimeServerSlash(),
                type = type
            ),
            appVersion = appVersion
        )

        return response.data
            ?.interviewList
            ?.map { it.toDomain() }
            ?: emptyList()
    }

    override suspend fun getInterviewDetails(interviewId: Long): InterviewDetails {
        val response = api.getInterviewDetails(
            request = InterviewDetailsReqDto.build(
                userName = localSettings.user.userName,
                password = localSettings.user.password,
                requestTime = currentTimestamp.toDateTimeServerSlash(),
                interviewId = interviewId
            )
        )

        val details = response.data?.interviewDetails
            ?: error("Interview details not found")
        return details.toDomain()
    }

    override suspend fun saveDoctorFeedback(formState: DoctorFeedbackFormState): SaveDoctorFeedbackResult {
        val request = SaveDoctorFeedbackReqDto.build(
            userName = localSettings.user.userName,
            password = localSettings.user.password,
            requestTime = currentTimestamp.toDateTimeServerSlash(),
            formState = formState
        )

        val jsonString = Json.encodeToString(request)
        println("SAVE_API REPO JSON: $jsonString")

        val response = api.saveDoctorFeedback(request)

        println("SAVE_API FINAL RESPONSE = $response")

        return response.toDomain()
    }

    override suspend fun getMedicineList(
        type: String
    ): List<Medicine> {
        val response = api.getMedicineList(
            request = MedicineListReqDto.build(
                userName = localSettings.user.userName,
                password = localSettings.user.password,
                requestTime = currentTimestamp.toDateTimeServerSlash(),
                type = type
            )
        )
        return response.data?.medicineList?.map { it.toDomain() }.orEmpty()
    }

    override suspend fun getQuestionAnswerData(): QuestionAnswerJson {
        val response = api.getQuestionAnswerData(
            request = QuestionAnswerJsonReqDto.build(
                userName = localSettings.user.userName,
                password = localSettings.user.password,
                requestTime = currentTimestamp.toDateTimeServerSlash()
            )
        )
        return response.toDomain()
    }

    override suspend fun updateInterviewStatus(
        interviewId: Long,
        status: String
    ): Boolean {
        val response = api.updateInterviewStatus(
            request = UpdateInterviewStatusReqDto.build(
                userName = localSettings.user.userName,
                password = localSettings.user.password,
                requestTime = currentTimestamp.toDateTimeServerSlash(),
                interviewId = interviewId,
                status = status
            )
        )
        return response.responseCode == "01"
    }

    override suspend fun sendSms(msisdn: String, message: String): JsonObject {
        return api.sendSms(
            msisdn,
            message
        )
    }
}