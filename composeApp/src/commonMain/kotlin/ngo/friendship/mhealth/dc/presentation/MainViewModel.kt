package ngo.friendship.mhealth.dc.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
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
import ngo.friendship.mhealth.dc.fcm.AppNotifierManager
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.BottomNavItems
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.navigation.components.replaceWith
import ngo.friendship.mhealth.dc.presentation.navigation.navConfiguration
import ngo.friendship.mhealth.dc.presentation.screens.case.CaseDetailsMode
import ngo.friendship.mhealth.dc.presentation.screens.case.case_list.components.CaseTab

sealed interface MainUiEvent {
    data object Idle : MainUiEvent
    data object OpenCasesTab : MainUiEvent
}

class MainViewModel(
    val settings: LocalSettings,
    private val caseRepository: CaseRepository,
    private val notifierManager: AppNotifierManager,
    repository: MainRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val uiEvent: SharedFlow<MainUiEvent>
        field = MutableSharedFlow(replay = 1)

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
            notifierManager.notificationClickFlow.collect { data ->
                println("MainViewModel: Notification click received = $data")

                backStack.replaceWith(Screens.Main)
                uiEvent.emit(MainUiEvent.OpenCasesTab)
            }
        }

        viewModelScope.launch {
            isUnauthorizedFlow.collect { isUnauthorized ->
                if (isUnauthorized) {
                    showError("Session Expired. Please login again.")
                    backStack.replaceWith(Screens.Auth)
                }
            }
        }
    }

    fun clearOpenCasesEvent() {
        launch {
            uiEvent.emit(MainUiEvent.Idle)
        }
    }

    fun selectBottomTab(tab: BottomNavItems) {
        selectedBottomTab = tab
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

    fun selectCaseTab(tab: CaseTab, appVersion: Int = 3069) {
        if (selectedCaseTab == tab) return

        selectedCaseTab = tab
        loadInterviewList(appVersion = appVersion, tab = tab)
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

    private fun shouldUpdateToOpen(tab: CaseTab): Boolean {
        return when (tab) {
            CaseTab.Pending -> true
            CaseTab.Older -> true
            CaseTab.Opened -> false
            CaseTab.Answered -> false
        }
    }

    fun openCase2(interview: Interview) {
        if (!shouldUpdateToOpen(selectedCaseTab)) {
            backStack.add(Screens.PrescriptionForm(interview.interviewId))
            return
        }

        launch(loading = Loading.Secondary) {
            val isSuccess = caseRepository.updateInterviewStatus(
                interviewId = interview.interviewId,
                status = CaseTab.Opened.apiParam
            )

            if (isSuccess) {
                interviewListState.value = interviewListState.value.filterNot {
                    it.interviewId == interview.interviewId
                }

                caseTabCounts = caseTabCounts.toMutableMap().apply {
                    val currentCount = this[selectedCaseTab] ?: 0
                    this[selectedCaseTab] = (currentCount - 1).coerceAtLeast(0)

                    val openCount = this[CaseTab.Opened] ?: 0
                    this[CaseTab.Opened] = openCount + 1
                }

                backStack.add(Screens.PrescriptionForm(interview.interviewId))
            } else {
                showError("Failed to update interview status")
            }
        }
    }

    fun openCase(interview: Interview) {
        val mode = if (selectedCaseTab == CaseTab.Answered) {
            CaseDetailsMode.ANSWERED
        } else {
            CaseDetailsMode.NORMAL
        }

        if (!shouldUpdateToOpen(selectedCaseTab)) {
            backStack.add(
                Screens.PrescriptionForm(
                    interviewId = interview.interviewId,
                    mode = mode
                )
            )
            return
        }

        launch(loading = Loading.Secondary) {
            val isSuccess = caseRepository.updateInterviewStatus(
                interviewId = interview.interviewId,
                status = CaseTab.Opened.apiParam
            )

            if (isSuccess) {
                interviewListState.value = interviewListState.value.filterNot {
                    it.interviewId == interview.interviewId
                }

                caseTabCounts = caseTabCounts.toMutableMap().apply {
                    val currentCount = this[selectedCaseTab] ?: 0
                    this[selectedCaseTab] = (currentCount - 1).coerceAtLeast(0)

                    val openCount = this[CaseTab.Opened] ?: 0
                    this[CaseTab.Opened] = openCount + 1
                }

                backStack.add(
                    Screens.PrescriptionForm(
                        interviewId = interview.interviewId,
                        mode = mode
                    )
                )
            } else {
                showError("Failed to update interview status")
            }
        }
    }

    fun logout() {
        settings.clear()
        backStack.replaceWith(Screens.Auth)
    }
}