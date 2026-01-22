package ngo.friendship.mhealth.dc.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ngo.friendship.mhealth.dc.domain.model.User
import ngo.friendship.mhealth.dc.domain.repository.AuthRepository
import ngo.friendship.mhealth.dc.presentation.state.RequestState

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<RequestState<User>>(RequestState.Idle)
    val loginState: StateFlow<RequestState<User>> = _loginState

    fun onLoginClick(userCode: String, pass: String) {
        viewModelScope.launch {
            _loginState.value = RequestState.Loading
            _loginState.value = repository.login(userCode, pass)
        }
    }
}