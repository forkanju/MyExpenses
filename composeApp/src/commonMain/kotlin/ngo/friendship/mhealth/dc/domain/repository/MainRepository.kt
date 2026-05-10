package ngo.friendship.mhealth.dc.domain.repository

import kotlinx.coroutines.flow.Flow
import ngo.friendship.mhealth.dc.domain.model.PrescriptionTemplate
import ngo.friendship.mhealth.dc.domain.model.SetupData
import ngo.friendship.mhealth.dc.domain.model.UserProfile
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.AdviceItemData

interface MainRepository {
    fun getSetupData(): Flow<SetupData>
    fun getDoctorProfile(): Flow<UserProfile?>
    suspend fun getAdviceList(): List<AdviceItemData>
    suspend fun getPrescriptionTemplates(): List<PrescriptionTemplate>
    suspend fun saveAdvice(title: String, content: String): Boolean
    suspend fun saveDiagnosis(title: String): Boolean
    suspend fun saveInvestigation(title: String): Boolean
    suspend fun saveMedicine(
        genericName: String,
        brandName: String,
        type: String,
        strength: String
    ): Boolean
    suspend fun changePassword(old: String, new: String): Pair<Boolean, String?>
    suspend fun clearAllData()
}
