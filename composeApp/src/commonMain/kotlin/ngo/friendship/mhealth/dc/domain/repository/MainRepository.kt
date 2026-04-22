package ngo.friendship.mhealth.dc.domain.repository

import kotlinx.coroutines.flow.Flow
import ngo.friendship.mhealth.dc.domain.model.SetupData
import ngo.friendship.mhealth.dc.domain.model.UserProfile

interface MainRepository {
    fun getSetupData(): Flow<SetupData>
    fun getUserProfile(): Flow<UserProfile?>
}
