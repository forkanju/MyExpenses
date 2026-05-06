package ngo.friendship.mhealth.dc.domain.repository

import ngo.friendship.mhealth.dc.domain.model.User

interface LoginRepository {
    suspend fun login(userName: String, password: String): User
}
