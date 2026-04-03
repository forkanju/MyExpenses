package ngo.friendship.mhealth.dc.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ngo.friendship.mhealth.dc.data.local.AppDatabase
import ngo.friendship.mhealth.dc.data.local.LocalSettings
import ngo.friendship.mhealth.dc.domain.model.SetupData
import ngo.friendship.mhealth.dc.domain.repository.MainRepository
import ngo.friendship.mhealth.dc.data.remote.ApiService
import ngo.friendship.mhealth.dc.data.remote.dto.SetupDataReqDto
import ngo.friendship.mhealth.dc.domain.mapper.toDomain

class MainRepositoryImpl(
    private val api: ApiService,
    private val localSettings: LocalSettings,
    appDatabase: AppDatabase
) : MainRepository {
    private val setupDataDao = appDatabase.setupDataDao()

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

    suspend fun getCachedSetupData(): SetupData {
        return SetupData(
            medicineBrandTypes = setupDataDao.getMedicineBrandTypes(),
            investigations = setupDataDao.getInvestigations(),
            diagnoses = setupDataDao.getDiagnoses(),
            referralCenters = setupDataDao.getReferralCenters()
        )
    }

}