package ngo.friendship.mhealth.dc.domain.repository

import ngo.friendship.mhealth.dc.domain.model.User
import ngo.friendship.mhealth.dc.presentation.state.RequestState


interface AuthRepository {
    suspend fun login(userCode: String, password: String): RequestState<User>
}