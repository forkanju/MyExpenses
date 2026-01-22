package ngo.friendship.mhealth.dc.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import ngo.friendship.mhealth.dc.data.remote.dto.LoginRequestDto
import ngo.friendship.mhealth.dc.data.remote.dto.LoginResponseDto

class ApiService(
    private val client: HttpClient
) {
    suspend fun login(request: LoginRequestDto): LoginResponseDto {
        return client.post("usergate") {
            setBody(request)
        }.body()
    }
}