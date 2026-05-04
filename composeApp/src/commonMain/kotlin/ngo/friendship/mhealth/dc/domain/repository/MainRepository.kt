package ngo.friendship.mhealth.dc.domain.repository

import kotlinx.coroutines.flow.Flow
import ngo.friendship.mhealth.dc.domain.model.SetupData
import ngo.friendship.mhealth.dc.domain.model.UserProfile

interface MainRepository {
    fun getSetupData(): Flow<SetupData>
    fun getDoctorProfile(): Flow<UserProfile?>
    suspend fun saveDiagnosis(title: String): Boolean
    suspend fun saveInvestigation(title: String): Boolean
    suspend fun saveMedicine(
        genericName: String,
        brandName: String,
        type: String,
        strength: String
    ): Boolean
}
