package ngo.friendship.mhealth.dc.domain.repository

import ngo.friendship.mhealth.dc.domain.model.Medicine

interface MedicineListRepository {
    suspend fun getMedicineList(
        userName: String,
        password: String,
        type: String
    ): List<Medicine>
}