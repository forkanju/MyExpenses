package ngo.friendship.mhealth.dc.domain.usecase

import ngo.friendship.mhealth.dc.domain.model.User
import ngo.friendship.mhealth.dc.domain.repository.LoginRepository

class LoginUseCase(private val repository: LoginRepository) {
    suspend operator fun invoke(userName: String, password: String): User {
        if (userName.isBlank()) {
            throw IllegalArgumentException("Username/Email cannot be empty")
        }

        if (password.isBlank()) {
            throw IllegalArgumentException("Password cannot be empty")
        }

        if (password.length < 4) {
            throw IllegalArgumentException("Password must be at least 4 characters")
        }

        return repository.login(userName, password)
    }
}
