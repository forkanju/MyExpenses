package ngo.friendship.mhealth.dc.domain.repository

import kotlinx.coroutines.flow.Flow
import ngo.friendship.mhealth.dc.domain.model.PrescriptionTemplate
import ngo.friendship.mhealth.dc.domain.model.SetupData
import ngo.friendship.mhealth.dc.domain.model.UserProfile
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.AdviceItemData

interface MainRepository {
    fun getSetupData(forceRefresh: Boolean = false): Flow<SetupData>
    fun getDoctorProfile(): Flow<UserProfile?>
    suspend fun getAdviceList(): List<AdviceItemData>
    suspend fun getPrescriptionTemplates(): List<PrescriptionTemplate>
    suspend fun getPrescriptionTemplateDtos(): List<ngo.friendship.mhealth.dc.data.remote.dto.PrescriptionTemplateDto>
    suspend fun saveAdvice(title: String, content: String): Pair<Boolean, String?>
    suspend fun saveDiagnosis(title: String): Pair<Boolean, String?>
    suspend fun saveInvestigation(title: String): Pair<Boolean, String?>
    suspend fun saveMedicine(
        genericName: String,
        brandName: String,
        type: String,
        strength: String
    ): Pair<Boolean, String?>
    suspend fun changePassword(old: String, new: String): Pair<Boolean, String?>
    suspend fun getDoctorFeedback(interviewId: Long): ngo.friendship.mhealth.dc.data.remote.dto.DoctorFeedbackResDto
    suspend fun clearAllData()
}
