package ngo.friendship.mhealth.dc.domain.repository

import kotlinx.coroutines.flow.Flow
import ngo.friendship.mhealth.dc.domain.model.SetupData

interface MainRepository {
    fun getSetupData(): Flow<SetupData>
}