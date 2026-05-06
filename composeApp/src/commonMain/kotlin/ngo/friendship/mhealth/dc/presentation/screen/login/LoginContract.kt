package ngo.friendship.mhealth.dc.presentation.screen.login

import ngo.friendship.mhealth.dc.domain.model.User

data class LoginState(
    val user: User = User(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface LoginIntent {
    data class Login(val username: String, val password: String) : LoginIntent
}

sealed interface LoginEffect {
    data object NavigateToMain : LoginEffect
    data class ShowError(val message: String) : LoginEffect
}
