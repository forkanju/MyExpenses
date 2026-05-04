package ngo.friendship.mhealth.dc.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ngo.friendship.mhealth.dc.data.local.AppDatabase
import ngo.friendship.mhealth.dc.data.local.LocalSettings
import ngo.friendship.mhealth.dc.data.remote.ApiService
import kotlinx.coroutines.flow.emitAll
import ngo.friendship.mhealth.dc.data.remote.dto.SaveDiagnosisReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.SaveInvestigationReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.SaveMedicineReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.SetupDataReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.DoctorProfileReqDto
import ngo.friendship.mhealth.dc.domain.mapper.toDomain
import ngo.friendship.mhealth.dc.domain.model.SetupData
import ngo.friendship.mhealth.dc.domain.model.UserProfile
import ngo.friendship.mhealth.dc.domain.repository.MainRepository
import ngo.friendship.mhealth.dc.utils.currentTimestamp
import ngo.friendship.mhealth.dc.utils.toDateTimeServerSlash

class MainRepositoryImpl(
    private val api: ApiService,
    private val localSettings: LocalSettings,
    appDatabase: AppDatabase
) : MainRepository {
    private val setupDataDao = appDatabase.setupDataDao()
    private val userProfileDao = appDatabase.userProfileDao()

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
        emit(getCachedSetupData())
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
        // 1. Emit current local data if available
        try {
            // We use a separate collect or combine if we want continuous DB updates, 
            // but for a simple refresh we can emitAll or use a more complex pattern.
            // For debugging, let's just make sure the API call actually runs.
        } catch (e: Exception) {}

        // Start collecting from DB and emit to the UI
        // We use emitAll to keep the flow alive with DB changes
        // But we need to trigger the API call as well.
        
        // Trigger API refresh in background or before emitAll
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

    suspend fun getCachedSetupData(): SetupData {
        return SetupData(
            medicineBrandTypes = setupDataDao.getMedicineBrandTypes(),
            investigations = setupDataDao.getInvestigations(),
            diagnoses = setupDataDao.getDiagnoses(),
            referralCenters = setupDataDao.getReferralCenters()
        )
    }

}