package ngo.friendship.mhealth.dc.data.repository

import ngo.friendship.mhealth.dc.data.local.LocalSettings
import ngo.friendship.mhealth.dc.data.remote.ApiService
import ngo.friendship.mhealth.dc.data.remote.dto.LoginRequestDto
import ngo.friendship.mhealth.dc.domain.mapper.toDomain
import ngo.friendship.mhealth.dc.domain.model.User
import ngo.friendship.mhealth.dc.domain.repository.LoginRepository


class LoginRepositoryImpl(
    private val api: ApiService,
    private val settings: LocalSettings
) : LoginRepository {

    override suspend fun login(userName: String, password: String): User {
        val request = LoginRequestDto(userName = userName, password = password)
        val response = api.login(request)
        
        if (response.responseCode == "01" && response.errorCode != "01" && response.data != null) {
            val data = response.data
            val user = data.toDomain(password)
            settings.token = data.token
            settings.user = user
            return user
        } else {
            val errorMessage = response.errorDesc ?: "Login failed"
            println("DEBUG: Login failed. Code: ${response.responseCode}, Desc: $errorMessage")
            error(errorMessage)
        }
    }
}
