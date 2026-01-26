package ngo.friendship.mhealth.dc.data.remote

import io.ktor.client.HttpClient
import ngo.friendship.mhealth.dc.data.remote.dto.LoginRequestDto
import ngo.friendship.mhealth.dc.data.remote.dto.LoginResponseDto
import ngo.friendship.mhealth.dc.domain.network.processFormDataRequest

class ApiService(
    private val client: HttpClient
) {
    suspend fun login(request: LoginRequestDto): LoginResponseDto = client.processFormDataRequest(
        url = "mHealthEnt_gateway/api/usergate",
        body = request
    )
}