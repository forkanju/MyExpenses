package ngo.friendship.mhealth.dc.domain.repository

import kotlinx.coroutines.flow.Flow
import ngo.friendship.mhealth.dc.domain.model.PrescriptionTemplate

interface MoreRepository {
    fun getPrescriptionTemplates(): Flow<List<PrescriptionTemplate>>
    suspend fun deleteTemplate(id: String)
}
