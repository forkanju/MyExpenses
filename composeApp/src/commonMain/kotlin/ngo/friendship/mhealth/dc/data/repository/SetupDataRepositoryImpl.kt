package ngo.friendship.mhealth.dc.data.repository

import ngo.friendship.mhealth.dc.data.local.LocalSettings
import ngo.friendship.mhealth.dc.domain.model.SetupData
import ngo.friendship.mhealth.dc.domain.repository.SetupDataRepository
import ngo.friendship.mhealth.dc.data.remote.ApiService
import ngo.friendship.mhealth.dc.data.remote.dto.SetupDataReqDto
import ngo.friendship.mhealth.dc.domain.mapper.toDomain

class SetupDataRepositoryImpl(
    private val apiService: ApiService,
    private val localSettings: LocalSettings
) : SetupDataRepository {

    override suspend fun getSetupData(
        userName: String,
        password: String
    ): SetupData {
        val response = apiService.getSetupData(
            request = SetupDataReqDto.build(
                userName = userName,
                password = password
            )
        )
        val setupData = response.data?.toDomain() ?: SetupData()
        localSettings.setupData = setupData
        return setupData
    }

    override fun getCachedSetupData(): SetupData {
        return localSettings.setupData
    }
}