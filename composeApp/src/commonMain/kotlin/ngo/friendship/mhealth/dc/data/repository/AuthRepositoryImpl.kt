package ngo.friendship.mhealth.dc.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import ngo.friendship.mhealth.dc.data.local.LocalSettings
import ngo.friendship.mhealth.dc.data.remote.ApiService
import ngo.friendship.mhealth.dc.data.remote.dto.LoginRequestDto
import ngo.friendship.mhealth.dc.domain.mapper.toDomain
import ngo.friendship.mhealth.dc.domain.model.User
import ngo.friendship.mhealth.dc.domain.repository.AuthRepository


class AuthRepositoryImpl(
    private val api: ApiService,
    private val settings: LocalSettings
) : AuthRepository {

    override suspend fun login(userName: String, password: String): User {
        return withContext(Dispatchers.IO) {
            val request = LoginRequestDto(userCode = userName, password = password)
            val response = api.login(request)

            val code = response.responseCode
            val data = response.data

            if (code == "01" && data != null) {
                val user = data.toDomain()
                settings.token = data.token
                settings.user = user
                user
            } else {
                error(response.errorDesc ?: "Unauthorized")
            }
        }
    }
}