package ngo.friendship.mhealth.dc.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import ngo.friendship.mhealth.dc.data.remote.dto.InterviewDetailsReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.InterviewDetailsResDto
import ngo.friendship.mhealth.dc.data.remote.dto.InterviewListReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.InterviewListResDto
import ngo.friendship.mhealth.dc.data.remote.dto.LoginRequestDto
import ngo.friendship.mhealth.dc.data.remote.dto.LoginResponseDto
import ngo.friendship.mhealth.dc.data.remote.dto.MedicineListReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.MedicineListResDto
import ngo.friendship.mhealth.dc.data.remote.dto.SetupDataReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.SetupDataResDto
import ngo.friendship.mhealth.dc.domain.network.processFormDataRequest

class ApiService(
    private val client: HttpClient
) {
    suspend fun login(request: LoginRequestDto): LoginResponseDto = client.processFormDataRequest(
        url = "mHealthEnt_gateway/api/usergate", body = request
    )

    suspend fun getInterviewList(
        request: InterviewListReqDto, appVersion: Int
    ): InterviewListResDto = client.processFormDataRequest(
        url = "mHealthEnt_gateway/api/usergate", body = request, request = {
            header("app_version", appVersion.toString())
        })

    suspend fun getInterviewDetails(
        request: InterviewDetailsReqDto
    ): InterviewDetailsResDto {
        return client.processFormDataRequest<InterviewDetailsReqDto, InterviewDetailsResDto>(
            url = "mHealthEnt_gateway/api/usergate", body = request
        )
    }

    suspend fun getSetupData(
        request: SetupDataReqDto
    ): SetupDataResDto {
        return client.processFormDataRequest<SetupDataReqDto, SetupDataResDto>(
            url = "mHealthEnt_gateway/api/usergate",
            body = request
        )
    }

    suspend fun getMedicineList(
        request: MedicineListReqDto
    ): MedicineListResDto {
        return client.processFormDataRequest<MedicineListReqDto, MedicineListResDto>(
            url = "mHealthEnt_gateway/api/usergate",
            body = request
        )
    }
}