package ngo.friendship.mhealth.dc.presentation.screens.profile.fcm

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.domain.repository.CaseRepository
import ngo.friendship.mhealth.dc.presentation.screen.profile.fcm.FcmProfileIntent
import ngo.friendship.mhealth.dc.presentation.screen.profile.fcm.FcmProfileUiEffect
import ngo.friendship.mhealth.dc.presentation.screen.profile.fcm.FcmProfileUiState
import ngo.friendship.mhealth.dc.utils.log

class FcmProfileViewModel(
    private val repository: CaseRepository,
) : BaseViewModel() {

    private val _state = MutableStateFlow(FcmProfileUiState())
    val state = _state.asStateFlow()

    private val _effect = Channel<FcmProfileUiEffect>()
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: FcmProfileIntent) {
        when (intent) {
            is FcmProfileIntent.LoadProfile -> loadProfile(intent.fcmCode)
            is FcmProfileIntent.SetFcmProfile -> {
                _state.update { it.copy(fcmProfile = intent.fcmProfile) }
            }
            is FcmProfileIntent.SelectTab -> {
                _state.update { it.copy(selectedTab = intent.index) }
            }
            FcmProfileIntent.NavigateBack -> {
                launch { _effect.send(FcmProfileUiEffect.NavigateBack) }
            }
        }
    }

    private fun loadProfile(fcmCode: String) {
        launch {
            val profile = repository.getFcmProfile(fcmCode)
            "name: ${profile?.userName}".log("FCM_DETAIL")

            _state.update {
                it.copy(
                    fcmProfile = profile ?: it.fcmProfile,
                )
            }
        }
    }
}
