package ngo.friendship.mhealth.dc.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ngo.friendship.mhealth.dc.data.local.LocalSettings
import ngo.friendship.mhealth.dc.di.isUnauthorizedFlow
import ngo.friendship.mhealth.dc.domain.model.Interview
import ngo.friendship.mhealth.dc.domain.model.SetupData
import ngo.friendship.mhealth.dc.domain.repository.PrescriptionFormRepository
import ngo.friendship.mhealth.dc.domain.repository.MainRepository
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.BottomNavItems
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.navigation.components.replaceWith
import ngo.friendship.mhealth.dc.presentation.navigation.navConfiguration

class MainViewModel(
    val settings: LocalSettings,
    private val repository: MainRepository,
    private val prescriptionFormRepository: PrescriptionFormRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val isUserLoggedIn
        get() = settings.isUserLoggedIn

    override var backStack by savedStateHandle.saved(
        configuration = navConfiguration
    ) {
        NavBackStack(if (isUserLoggedIn) Screens.Main else Screens.Auth)
    }

    var selectedBottomTab by mutableStateOf(BottomNavItems.Home)
        private set

    val setupDataState = repository.getSetupData().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = SetupData()
    )

    val interviewListState: StateFlow<List<Interview>>
        field = MutableStateFlow(emptyList())

    init {
        viewModelScope.launch {
            loadInterviewList(
//                userName = "sharif.dr",
//                password = "1234",
                appVersion = 3069
            )
            isUnauthorizedFlow.collect { isUnauthorized ->
                if (isUnauthorized) {
                    showError("Session Expired. Please login again.")
                    backStack.replaceWith(Screens.Auth)
                }
            }
        }
    }

    fun selectBottomTab(tab: BottomNavItems){
        selectedBottomTab = tab
    }

    fun loadInterviewList(appVersion: Int) {
        launch {
            interviewListState.value =
                prescriptionFormRepository.getInterviewList(
                    appVersion = appVersion
                )
        }
    }



    fun logout() {
        settings.clear()
        backStack.replaceWith(Screens.Auth)
    }
}