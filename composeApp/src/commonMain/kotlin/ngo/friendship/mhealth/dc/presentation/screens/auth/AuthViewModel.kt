package ngo.friendship.mhealth.dc.presentation.screens.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ngo.friendship.mhealth.dc.domain.model.User
import ngo.friendship.mhealth.dc.domain.repository.AuthRepository
import ngo.friendship.mhealth.dc.fcm.EventTopicSubscriber
import ngo.friendship.mhealth.dc.fcm.FcmTopics
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.navigation.components.replaceWith

class AuthViewModel(
    private val repository: AuthRepository,
    private val eventTopicSubscriber: EventTopicSubscriber
) : BaseViewModel() {

    val loginState: StateFlow<User>
        field = MutableStateFlow(User())

    fun login(userName: String, password: String) {
        launch {
            loginState.value = repository.login(userName, password)
            backStack.replaceWith(Screens.Main)
        }
    }


    fun onLoginSuccess(doctorId: String) {
        launch {
            eventTopicSubscriber.subscribeToTopic(FcmTopics.CASE_LIST_UPDATES)
            eventTopicSubscriber.subscribeToTopic(
                FcmTopics.doctorCaseList(doctorId)
            )
        }
    }
}