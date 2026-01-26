package ngo.friendship.mhealth.dc.data.remote

import io.ktor.client.HttpClient
import io.ktor.http.Parameters
import kotlinx.serialization.json.Json
import ngo.friendship.mhealth.dc.data.remote.dto.LoginRequestDto
import ngo.friendship.mhealth.dc.data.remote.dto.LoginResponseDto
import ngo.friendship.mhealth.dc.domain.network.processFormRequest

class ApiService(
    private val client: HttpClient
) {
    suspend fun login(request: LoginRequestDto): LoginResponseDto {
        val jsonString = Json.encodeToString(request)
        return client.processFormRequest(
            url = "mHealthEnt_gateway/api/usergate",
            formParameters = Parameters.build {
                append("data", jsonString)
            }
        )
    }

//    suspend fun login(request: LoginRequestDto): LoginResponseDto {
//        return client.postAsFormData<LoginResponseDto, LoginRequestDto>(
//            url = "mHealthEnt_gateway/api/usergate",
//            body = request
//        )
//    }
}