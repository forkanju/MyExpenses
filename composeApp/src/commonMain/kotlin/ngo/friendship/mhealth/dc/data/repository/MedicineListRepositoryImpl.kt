package ngo.friendship.mhealth.dc.data.repository

import ngo.friendship.mhealth.dc.data.remote.ApiService
import ngo.friendship.mhealth.dc.data.remote.dto.MedicineListReqDto
import ngo.friendship.mhealth.dc.domain.mapper.toDomain
import ngo.friendship.mhealth.dc.domain.model.Medicine
import ngo.friendship.mhealth.dc.domain.repository.MedicineListRepository
import ngo.friendship.mhealth.dc.utils.currentTimestamp
import ngo.friendship.mhealth.dc.utils.toDateTimeServerSlash

class MedicineListRepositoryImpl(
    private val api: ApiService
) : MedicineListRepository {
    override suspend fun getMedicineList(
        userName: String,
        password: String,
        type: String
    ): List<Medicine> {
        val response = api.getMedicineList(
            request = MedicineListReqDto.build(
                userName = userName,
                password = password,
                requestTime = currentTimestamp.toDateTimeServerSlash(),
                type = type
            )
        )
        return response.data?.medicineList?.map { it.toDomain() }.orEmpty()
    }
}