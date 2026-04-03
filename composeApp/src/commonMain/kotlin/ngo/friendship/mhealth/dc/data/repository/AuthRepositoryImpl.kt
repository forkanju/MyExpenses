package ngo.friendship.mhealth.dc.data.repository

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
        val request = LoginRequestDto(userName = userName, password = password)
        val response = api.login(request)
        val data = response.data

        if (data != null) {
            val user = data.toDomain(password)
            settings.token = data.token
            settings.user = user
            return user
        } else {
            error("No user data found")
        }
    }
}