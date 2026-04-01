package ngo.friendship.mhealth.dc.data.repository

import kotlinx.serialization.json.Json
import ngo.friendship.mhealth.dc.data.remote.ApiService
import ngo.friendship.mhealth.dc.data.remote.dto.SaveDoctorFeedbackReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.SaveDoctorFeedbackResDto
import ngo.friendship.mhealth.dc.domain.mapper.toDomain
import ngo.friendship.mhealth.dc.domain.model.SaveDoctorFeedbackResult
import ngo.friendship.mhealth.dc.domain.repository.SaveDoctorFeedbackRepository
import ngo.friendship.mhealth.dc.presentation.screens.main.case.save_feedback.DoctorFeedbackFormState
import ngo.friendship.mhealth.dc.utils.currentTimestamp
import ngo.friendship.mhealth.dc.utils.md5
import ngo.friendship.mhealth.dc.utils.toDateTimeServerSlash
class SaveDoctorFeedbackRepositoryImpl(
    private val api: ApiService
) : SaveDoctorFeedbackRepository {

    override suspend fun saveDoctorFeedback(
        userName: String,
        password: String,
        formState: DoctorFeedbackFormState
    ): SaveDoctorFeedbackResult {
        println("SAVE_API: repository entered")

        val request = SaveDoctorFeedbackReqDto.build(
            userName = userName,
            password = password,
            requestTime = currentTimestamp.toDateTimeServerSlash(),
            formState = formState
        )

        val jsonString = Json.encodeToString(request)
        println("SAVE_API REPO JSON: $jsonString" )

        val response = api.saveDoctorFeedback(request)

        println("SAVE_API FINAL RESPONSE = $response")

        return response.toDomain()
    }
}