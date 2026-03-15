package ngo.friendship.mhealth.dc.domain.repository

import ngo.friendship.mhealth.dc.domain.model.SetupData

interface SetupDataRepository {
    suspend fun getSetupData(
        userName: String,
        password: String
    ): SetupData

    fun getCachedSetupData(): SetupData
}