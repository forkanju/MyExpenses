package ngo.friendship.mhealth.dc.presentation.screen.login

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import ngo.friendship.mhealth.dc.domain.usecase.LoginUseCase
import ngo.friendship.mhealth.dc.notification.AppNotifierManager
import ngo.friendship.mhealth.dc.notification.FcmTopics
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.navigation.components.replaceWith

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val notifierManager: AppNotifierManager
) : BaseViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _effect = Channel<LoginEffect>()
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.Login -> login(intent.username, intent.password)
        }
    }

    private fun login(userName: String, password: String) {
        launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val user = loginUseCase(userName, password)
                _state.value = _state.value.copy(user = user, isLoading = false)
                onLoginSuccess(doctorId = userName)
                backStack.replaceWith(Screens.Main)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.message)
                showError(e.message ?: "Login failed")
            }
        }
    }

    private fun onLoginSuccess(doctorId: String) {
        launch(loading = Loading.Gone) {
            notifierManager.subscribeToTopic(FcmTopics.CASE_LIST_UPDATES)
            notifierManager.subscribeToTopic(FcmTopics.doctorCaseList(doctorId))

            val token = notifierManager.getDeviceToken()
            println("LoginViewModel: FCM token = $token")
            println("LoginViewModel: doctor topic = ${FcmTopics.doctorCaseList(doctorId)}")
        }
    }
}
