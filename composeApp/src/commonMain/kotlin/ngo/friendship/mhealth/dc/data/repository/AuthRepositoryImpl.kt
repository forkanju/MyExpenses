package ngo.friendship.mhealth.dc.data.repository

import ngo.friendship.mhealth.dc.data.remote.ApiService
import ngo.friendship.mhealth.dc.data.remote.LoginRequestDefaults
import ngo.friendship.mhealth.dc.domain.mapper.toDomain
import ngo.friendship.mhealth.dc.domain.model.User
import ngo.friendship.mhealth.dc.domain.repository.AuthRepository
import ngo.friendship.mhealth.dc.presentation.state.RequestState


class AuthRepositoryImpl(
    private val api: ApiService,
    private val defaults: LoginRequestDefaults
) : AuthRepository {

    override suspend fun login(userCode: String, password: String): RequestState<User> {
        return try {
            val request = defaults.build(userCode, password)
            val response = api.login(request)

            val code = response.responseCode
            val data = response.data

            if (code == "01" && data != null) {
                RequestState.Success(data.toDomain())
            } else {
                RequestState.Error(response.errorDesc ?: "Unauthorized")
            }
        } catch (e: Exception) {
            RequestState.Error(e.message ?: "Network Error")
        }
    }
}