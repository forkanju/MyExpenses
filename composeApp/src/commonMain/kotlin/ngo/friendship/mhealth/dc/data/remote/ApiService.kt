package ngo.friendship.mhealth.dc.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import ngo.friendship.mhealth.dc.data.remote.dto.InterviewDetailsReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.InterviewDetailsResDto
import ngo.friendship.mhealth.dc.data.remote.dto.InterviewListReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.InterviewListResDto
import ngo.friendship.mhealth.dc.data.remote.dto.LoginRequestDto
import ngo.friendship.mhealth.dc.data.remote.dto.LoginResponseDto
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
        request: InterviewDetailsReqDto,
        appVersion: Int
    ): InterviewDetailsResDto {
        return client.processFormDataRequest<InterviewDetailsReqDto, InterviewDetailsResDto>(
            url = "mHealthEnt_gateway/api/usergate"){
            header("app_version", appVersion)
        }
    }
}