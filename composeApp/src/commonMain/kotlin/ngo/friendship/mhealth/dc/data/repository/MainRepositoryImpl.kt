package ngo.friendship.mhealth.dc.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ngo.friendship.mhealth.dc.data.local.AppDatabase
import ngo.friendship.mhealth.dc.data.local.LocalSettings
import ngo.friendship.mhealth.dc.data.remote.ApiService
import ngo.friendship.mhealth.dc.data.remote.dto.AdviceListReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.ChangePasswordReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.DoctorFeedbackReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.DoctorProfileReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.PrescriptionTemplateDto
import ngo.friendship.mhealth.dc.data.remote.dto.PrescriptionTemplateReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.SaveAdviceReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.SaveDiagnosisReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.SaveInvestigationReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.SaveMedicineReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.SetupDataReqDto
import ngo.friendship.mhealth.dc.domain.mapper.toDomain
import ngo.friendship.mhealth.dc.domain.model.PrescriptionTemplate
import ngo.friendship.mhealth.dc.domain.model.SetupData
import ngo.friendship.mhealth.dc.domain.model.UserProfile
import ngo.friendship.mhealth.dc.domain.repository.MainRepository
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.AdviceItemData
import ngo.friendship.mhealth.dc.utils.currentTimestamp
import ngo.friendship.mhealth.dc.utils.toDateTimeServerSlash

class MainRepositoryImpl(
    private val api: ApiService,
    private val localSettings: LocalSettings,
    private val appDatabase: AppDatabase
) : MainRepository {
    private val setupDataDao = appDatabase.setupDataDao()
    private val userProfileDao = appDatabase.userProfileDao()

    override suspend fun getAdviceList(): List<AdviceItemData> {
        return try {
            val response = api.getAdviceList(
                request = AdviceListReqDto.build(
                    userName = localSettings.user.userName,
                    password = localSettings.user.password,
                    requestTime = currentTimestamp.toDateTimeServerSlash()
                )
            )
            response.data?.adviceList?.map {
                AdviceItemData(
                    title = it.adviceName ?: "",
                    subtitle = "Updated: ${currentTimestamp.toDateTimeServerSlash()}", // Subtitle not in API, using current time as placeholder
                    details = listOf(it.adviceDetails ?: "")
                )
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getPrescriptionTemplates(): List<PrescriptionTemplate> {
        return getPrescriptionTemplateDtos().map { it.toDomain() }
    }

    override suspend fun getPrescriptionTemplateDtos(): List<PrescriptionTemplateDto> {
        return try {
            val response = api.getPrescriptionTemplates(
                request = PrescriptionTemplateReqDto.build(
                    userName = localSettings.user.userName,
                    password = localSettings.user.password,
                    requestTime = currentTimestamp.toDateTimeServerSlash()
                )
            )
            println("PRESCRIPTION_TEMPLATES RESPONSE: $response")
            response.data?.prescriptionTemplates ?: emptyList()
        } catch (e: Exception) {
            println("ERROR fetching templates: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun saveAdvice(title: String, content: String): Boolean {
        return try {
            val response = api.saveAdvice(
                request = SaveAdviceReqDto.build(
                    userName = localSettings.user.userName,
                    password = localSettings.user.password,
                    requestTime = currentTimestamp.toDateTimeServerSlash(),
                    adviceName = title,
                    adviceDetails = content
                )
            )
            response.responseCode == "01"
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun saveDiagnosis(title: String): Boolean {
        return try {
            val response = api.saveDiagnosis(
                request = SaveDiagnosisReqDto.build(
                    userName = localSettings.user.userName,
                    password = localSettings.user.password,
                    requestTime = currentTimestamp.toDateTimeServerSlash(),
                    diagName = title
                )
            )
            response.responseCode == "01"
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun saveInvestigation(title: String): Boolean {
        return try {
            val response = api.saveInvestigation(
                request = SaveInvestigationReqDto.build(
                    userName = localSettings.user.userName,
                    password = localSettings.user.password,
                    requestTime = currentTimestamp.toDateTimeServerSlash(),
                    investigationName = title
                )
            )
            response.responseCode == "01"
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun saveMedicine(
        genericName: String,
        brandName: String,
        type: String,
        strength: String
    ): Boolean {
        return try {
            val response = api.saveMedicine(
                request = SaveMedicineReqDto.build(
                    userName = localSettings.user.userName,
                    password = localSettings.user.password,
                    requestTime = currentTimestamp.toDateTimeServerSlash(),
                    genericName = genericName,
                    brandName = brandName,
                    type = type,
                    strength = strength
                )
            )
            response.responseCode == "01"
        } catch (e: Exception) {
            false
        }
    }

    override fun getSetupData(): Flow<SetupData> = flow {
        val cached = getCachedSetupData()

        // Check if cached data is not empty (checking investigations as a proxy for all setup data)
        if (cached.investigations.isNotEmpty() ||
            cached.diagnoses.isNotEmpty() ||
            cached.medicineBrandTypes.isNotEmpty() ||
            cached.referralCenters.isNotEmpty()) {
            emit(cached)
            return@flow
        }

        // If cache is empty, fetch from API
        val response = api.getSetupData(
            request = SetupDataReqDto.build(
                userName = localSettings.user.userName,
                password = localSettings.user.password
            )
        )
        response.data?.toDomain()?.also {
            setupDataDao.deleteAllData()
            setupDataDao.insertAll(it)
            emit(it)
        }
    }.flowOn(Dispatchers.IO)

    override fun getDoctorProfile(): Flow<UserProfile?> = flow<UserProfile?> {
        // ... (existing logic)
        try {
            val response = api.getDoctorProfile(
                DoctorProfileReqDto.build(
                    userName = localSettings.user.userName,
                    password = localSettings.user.password
                )
            )
            println("MainRepositoryImpl: API UserProfile Response: $response")
            response.data?.doctorProfile?.toDomain()?.also {
                println("MainRepositoryImpl: Saving UserProfile to DB: $it")
                userProfileDao.deleteProfile()
                userProfileDao.insertUserProfile(it)
            }
        } catch (e: Exception) {
            println("MainRepositoryImpl: API Error: ${e.message}")
        }

        emitAll(userProfileDao.getUserProfile())
    }.flowOn(Dispatchers.IO)

    override suspend fun changePassword(old: String, new: String): Pair<Boolean, String?> {
        return try {
            val response = api.changePassword(
                ChangePasswordReqDto.build(
                    userName = localSettings.user.userName,
                    oldPassword = old,
                    newPassword = new
                )
            )
            val isSuccess = response.responseCode == "01"
            isSuccess to (if (isSuccess) "Success" else response.errorDesc)
        } catch (e: Exception) {
            false to e.message
        }
    }

    override suspend fun getDoctorFeedback(interviewId: Long): ngo.friendship.mhealth.dc.data.remote.dto.DoctorFeedbackResDto {
        return api.getDoctorFeedback(
            request = DoctorFeedbackReqDto.build(
                userName = localSettings.user.userName,
                password = localSettings.user.password,
                requestTime = currentTimestamp.toDateTimeServerSlash(),
                interviewId = interviewId
            )
        )
    }

    override suspend fun clearAllData() {
        appDatabase.clearAllTables()
    }

    suspend fun getCachedSetupData(): SetupData {
        return SetupData(
            medicineBrandTypes = setupDataDao.getMedicineBrandTypes(),
            investigations = setupDataDao.getInvestigations(),
            diagnoses = setupDataDao.getDiagnoses(),
            referralCenters = setupDataDao.getReferralCenters()
        )
    }

}