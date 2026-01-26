package ngo.friendship.mhealth.dc.data.remote

import io.ktor.client.HttpClient
import io.ktor.http.parameters
import ngo.friendship.mhealth.dc.data.remote.dto.LoginRequestDto
import ngo.friendship.mhealth.dc.data.remote.dto.LoginResponseDto
import ngo.friendship.mhealth.dc.domain.network.processFormRequest
import ngo.friendship.mhealth.dc.utils.toJson

class ApiService(
    private val client: HttpClient
) {
    suspend fun login(request: LoginRequestDto): LoginResponseDto {
        return client.processFormRequest(
            url = "mHealthEnt_gateway/api/usergate",
            formParameters = parameters {
                append("data", request.toJson())
            }
        )
    }
}