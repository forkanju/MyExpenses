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
import ngo.friendship.mhealth.dc.domain.repository.CaseRepository
import ngo.friendship.mhealth.dc.domain.repository.MainRepository
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.BottomNavItems
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.navigation.components.replaceWith
import ngo.friendship.mhealth.dc.presentation.navigation.navConfiguration
import ngo.friendship.mhealth.dc.presentation.screens.case.case_list.components.CaseTab

class MainViewModel(
    val settings: LocalSettings,
    private val caseRepository: CaseRepository,
    repository: MainRepository,
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
    val interviewListState = MutableStateFlow<List<Interview>>(emptyList())

    var selectedCaseTab by mutableStateOf(CaseTab.Pending)
        private set

    var caseTabCounts by mutableStateOf(
        mapOf(
            CaseTab.Pending to 0,
            CaseTab.Opened to 0,
            CaseTab.Older to 0,
            CaseTab.Answered to 0
        )
    )
        private set


    var isCaseCountsLoaded by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            isUnauthorizedFlow.collect { isUnauthorized ->
                if (isUnauthorized) {
                    showError("Session Expired. Please login again.")
                    backStack.replaceWith(Screens.Auth)
                }
            }
        }
    }


    fun selectBottomTab(tab: BottomNavItems) {
        selectedBottomTab = tab
    }

    fun selectCaseTab(tab: CaseTab, appVersion: Int = 3069) {
        if (selectedCaseTab == tab) return

        selectedCaseTab = tab
        loadInterviewList(
            appVersion = appVersion,
            tab = tab
        )
    }

    fun loadInterviewList(
        appVersion: Int = 3069,
        tab: CaseTab = selectedCaseTab
    ) {
        launch(loading = Loading.Secondary) {
            val list = caseRepository.getInterviewList(
                appVersion = appVersion,
                type = tab.apiParam
            )

            interviewListState.value = list

            caseTabCounts = caseTabCounts.toMutableMap().apply {
                this[tab] = list.size
            }
        }
    }

    fun initializeCases(appVersion: Int = 3069) {
        if (isCaseCountsLoaded && interviewListState.value.isNotEmpty()) return

        launch(loading = Loading.Secondary) {
            val counts = mutableMapOf<CaseTab, Int>()

            val selectedList = caseRepository.getInterviewList(
                appVersion = appVersion,
                type = selectedCaseTab.apiParam
            )
            interviewListState.value = selectedList
            counts[selectedCaseTab] = selectedList.size

            CaseTab.entries
                .filter { it != selectedCaseTab }
                .forEach { tab ->
                    val list = caseRepository.getInterviewList(
                        appVersion = appVersion,
                        type = tab.apiParam
                    )
                    counts[tab] = list.size
                }
            caseTabCounts = counts
            isCaseCountsLoaded = true
        }
    }


    fun logout() {
        settings.clear()
        backStack.replaceWith(Screens.Auth)
    }


}