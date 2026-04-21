package ngo.friendship.mhealth.dc.presentation.screens.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ngo.friendship.mhealth.dc.domain.model.User
import ngo.friendship.mhealth.dc.domain.repository.AuthRepository
import ngo.friendship.mhealth.dc.fcm.AppNotifierManager
import ngo.friendship.mhealth.dc.fcm.FcmTopics
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.navigation.components.replaceWith

class AuthViewModel(
    private val repository: AuthRepository,
    private val notifierManager: AppNotifierManager
) : BaseViewModel() {

    val loginState: StateFlow<User>
        field = MutableStateFlow(User())

    fun login(userName: String, password: String) {
        launch {
            loginState.value = repository.login(userName, password)
            onLoginSuccess(doctorId = userName)
            backStack.replaceWith(Screens.Main)
        }
    }

    fun onLoginSuccess(doctorId: String) {
        launch(loading = Loading.Gone) {
            notifierManager.subscribeToTopic(FcmTopics.CASE_LIST_UPDATES)
            notifierManager.subscribeToTopic(FcmTopics.doctorCaseList(doctorId))

            val token = notifierManager.getDeviceToken()
            println("AuthViewModel: FCM token = $token")
            println("AuthViewModel: doctor topic = ${FcmTopics.doctorCaseList(doctorId)}")
        }
    }
}