package ngo.friendship.mhealth.dc.presentation.screens.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ngo.friendship.mhealth.dc.domain.model.User
import ngo.friendship.mhealth.dc.domain.repository.AuthRepository
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.navigation.components.replaceWith

class AuthViewModel(private val repository: AuthRepository) : BaseViewModel() {

    val loginState: StateFlow<User>
        field = MutableStateFlow(User())

    fun login(userName: String, password: String) {
        launch {
            loginState.value = repository.login(userName, password)
            backStack.replaceWith(Screens.Main)
        }
    }
}