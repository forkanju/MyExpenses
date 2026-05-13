package ngo.friendship.mhealth.dc.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import ngo.friendship.mhealth.dc.data.local.AppDatabase
import ngo.friendship.mhealth.dc.data.local.LocalSettings
import ngo.friendship.mhealth.dc.data.remote.ApiService
import ngo.friendship.mhealth.dc.data.remote.dto.BeneficiaryProfileReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.FcmCodeParam
import ngo.friendship.mhealth.dc.data.remote.dto.FcmProfileReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.InterviewDetailsReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.InterviewListReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.MedicineListReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.QuestionAnswerJsonReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.SaveDoctorFeedbackReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.UpdateInterviewStatusReqDto
import ngo.friendship.mhealth.dc.domain.mapper.toDomain
import ngo.friendship.mhealth.dc.domain.mapper.toDomainFcmProfile
import ngo.friendship.mhealth.dc.domain.model.BeneficiaryProfile
import ngo.friendship.mhealth.dc.domain.model.FcmProfile
import ngo.friendship.mhealth.dc.domain.model.Interview
import ngo.friendship.mhealth.dc.domain.model.InterviewDetails
import ngo.friendship.mhealth.dc.domain.model.Medicine
import ngo.friendship.mhealth.dc.domain.model.QuestionAnswerJson
import ngo.friendship.mhealth.dc.domain.model.SaveDoctorFeedbackResult
import ngo.friendship.mhealth.dc.domain.repository.CaseRepository
import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.model.DoctorFeedbackFormState
import ngo.friendship.mhealth.dc.utils.currentTimestamp
import ngo.friendship.mhealth.dc.utils.log
import ngo.friendship.mhealth.dc.utils.md5
import ngo.friendship.mhealth.dc.utils.toDateTimeServerSlash

class CaseRepositoryImpl(
    private val api: ApiService,
    private val localSettings: LocalSettings,
    private val appDatabase: AppDatabase
) : CaseRepository {

    private val _interviews = MutableStateFlow<List<Interview>>(emptyList())
    private val medicineDao = appDatabase.medicineDao()

    override fun observeCases(): Flow<List<Interview>> = _interviews.asStateFlow()

    override suspend fun getInterviewList(appVersion: Int, type: String): List<Interview> {
        println("DEBUG: Repository getInterviewList started for type=$type")
        val response = api.getInterviewList(
            request = InterviewListReqDto.build(
                userName = localSettings.user.userName,
                password = localSettings.user.password,
                requestTime = currentTimestamp.toDateTimeServerSlash(),
                type = type
            ),
            appVersion = appVersion
        )

        val result = response.data
            ?.interviewList
            ?.map { it.toDomain() }
            ?: emptyList()

        _interviews.update { current ->
            val updated = current.toMutableList()
            result.forEach { newItem ->
                val index = updated.indexOfFirst { it.interviewId == newItem.interviewId }
                if (index != -1) {
                    updated[index] = newItem
                } else {
                    updated.add(newItem)
                }
            }
            updated
        }

        println("DEBUG: Repository returning ${result.size} domain interviews")
        return result
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
        val cached = medicineDao.getMedicinesByType(type)
        if (cached.isNotEmpty()) {
            return cached
        }

        val response = api.getMedicineList(
            request = MedicineListReqDto.build(
                userName = localSettings.user.userName,
                password = localSettings.user.password,
                requestTime = currentTimestamp.toDateTimeServerSlash(),
                type = type
            )
        )
        val medicines = response.data?.medicineList?.map { it.toDomain() }.orEmpty()
        if (medicines.isNotEmpty()) {
            medicineDao.deleteMedicinesByType(type)
            medicineDao.insertMedicines(medicines)
        }
        return medicines
    }

    override suspend fun getAllMedicines(): List<Medicine> {
        return medicineDao.getAllMedicines()
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
        val success = response.responseCode == "01"
        if (success) {
            _interviews.update { current ->
                current.map {
                    if (it.interviewId == interviewId) it.copy(status = status) else it
                }
            }
        }
        return success
    }

    override suspend fun getFcmProfile(fcmCode: String): FcmProfile? {
        val sanitizedFcmCode = fcmCode.substringBefore("-").substringBefore("[").trim()
        val userCode = localSettings.user.userName.md5()
        val password = localSettings.user.password.md5()
        "Fetching FCM Profile. Code: $fcmCode, Sanitized: $sanitizedFcmCode, User: $userCode".log("FCM_DEBUG")
        val response = api.getFcmProfile(
            request = FcmProfileReqDto(
                orgCode = "FR",
                userCode = userCode,
                password = password,
                orgId = 101,
                imei = "IMEI_FREE",
                demo = false,
                requestType = "DOCTOR_CENTER",
                requestName = "FCM_PROFILE",
                moduleName = "mHealth-FCM",
                requestTime = currentTimestamp.toDateTimeServerSlash(),
                requestAction = "",
                dataLength = 2,
                lang = "bn",
                param1 = FcmCodeParam(sanitizedFcmCode)
            )
        )
        "FCM Response: code=${response.responseCode}, name=${response.responseName}, error=${response.errorDesc}".log(
            "FCM_DEBUG"
        )
        "FCM Data: ${response.data?.fcmProfile}".log("FCM_DEBUG")
        return response.data?.fcmProfile?.toDomainFcmProfile()
    }

    override suspend fun getBeneficiaryProfile(benefCode: String): BeneficiaryProfile? {
        val sanitizedBenCode = benefCode.substringBefore("[").replace("-", "").trim()
        val response = api.getBeneficiaryProfile(
            request = BeneficiaryProfileReqDto.build(
                userName = localSettings.user.userName,
                password = localSettings.user.password,
                requestTime = currentTimestamp.toDateTimeServerSlash(),
                benefCode = sanitizedBenCode
            )
        )
        return response.toDomain()
    }

    override suspend fun sendSms(msisdn: String, message: String): JsonObject {
        return api.sendSms(
            msisdn,
            message
        )
    }

    override suspend fun markAsOpened(interviewId: String) {
        val idLong = interviewId.toLongOrNull() ?: return
        val currentCase = _interviews.value.find { it.interviewId == idLong }
        if (currentCase?.status == "New" || currentCase?.status == "Older") {
            updateInterviewStatus(interviewId = idLong, status = "Open")
        }
    }


}