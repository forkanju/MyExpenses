package ngo.friendship.mhealth.dc.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ngo.friendship.mhealth.dc.domain.model.PrescriptionTemplate
import ngo.friendship.mhealth.dc.domain.repository.MoreRepository

class MoreRepositoryImpl : MoreRepository {
    override fun getPrescriptionTemplates(): Flow<List<PrescriptionTemplate>> {
        // Mock data based on the design
        return flowOf(
            listOf(
                PrescriptionTemplate("1", "Oral ulcer prescription by Abir", "1:16 PM, 25 Jan 25", "3:35 PM, 12 Nov 25", 0xFF60BF77),
                PrescriptionTemplate("2", "Oral ulcer prescription by Abir", "1:16 PM, 25 Jan 25", "3:35 PM, 12 Nov 25", 0xFF214695),
                PrescriptionTemplate("3", "Oral ulcer prescription by Abir", "1:16 PM, 25 Jan 25", "3:35 PM, 12 Nov 25", 0xFFF78A6C),
                PrescriptionTemplate("4", "Oral ulcer prescription by Abir", "1:16 PM, 25 Jan 25", "3:35 PM, 12 Nov 25", 0xFF60BF77),
                PrescriptionTemplate("5", "Oral ulcer prescription by Abir", "1:16 PM, 25 Jan 25", "3:35 PM, 12 Nov 25", 0xFF214695),
                PrescriptionTemplate("6", "Oral ulcer prescription by Abir", "1:16 PM, 25 Jan 25", "3:35 PM, 12 Nov 25", 0xFFF78A6C)
            )
        )
    }

    override suspend fun deleteTemplate(id: String) {
        // Implement delete logic
    }
}
