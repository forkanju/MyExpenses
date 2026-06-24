package ngo.friendship.mhealth.dc.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ngo.friendship.mhealth.dc.data.local.LocalSettings
import ngo.friendship.mhealth.dc.di.isUnauthorizedFlow
import ngo.friendship.mhealth.dc.domain.model.NetworkStatus
import ngo.friendship.mhealth.dc.domain.model.Interview
import ngo.friendship.mhealth.dc.domain.model.SetupData
import ngo.friendship.mhealth.dc.domain.repository.CaseRepository
import ngo.friendship.mhealth.dc.domain.repository.MainRepository
import ngo.friendship.mhealth.dc.notification.AppNotifierManager
import ngo.friendship.mhealth.dc.notification.FcmTopics
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.BottomNavItems
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.navigation.components.replaceWith
import ngo.friendship.mhealth.dc.presentation.navigation.navConfiguration
import ngo.friendship.mhealth.dc.presentation.screen.case.CaseDetailsMode
import ngo.friendship.mhealth.dc.presentation.screen.case.case_list.components.CaseTab
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.AdviceItemData

sealed interface MainUiEvent {
    data object Idle : MainUiEvent
    data class OpenCasesTab(val tab: CaseTab = CaseTab.Pending) : MainUiEvent
    data object OpenMoreTab : MainUiEvent
}

@OptIn(FlowPreview::class)
class MainViewModel(
    val settings: LocalSettings,
    private val caseRepository: CaseRepository,
    private val notifierManager: AppNotifierManager,
    private val mainRepository: MainRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val uiEvent: SharedFlow<MainUiEvent>
        field = MutableSharedFlow(replay = 1)

    val isUserLoggedIn
        get() = settings.isUserLoggedIn

    override var backStack by savedStateHandle.saved<NavBackStack<NavKey>>(
        configuration = navConfiguration
    ) {
        NavBackStack(if (isUserLoggedIn) Screens.Main else Screens.Auth)
    }

    var selectedBottomTab by mutableStateOf(BottomNavItems.Cases)
        private set

    val setupDataState = mainRepository.getSetupData(forceRefresh = false).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = SetupData()
    )

    val userProfileState = mainRepository.getDoctorProfile().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = null
    )

    val interviewListState: StateFlow<List<Interview>>
        field = MutableStateFlow(emptyList())

    val adviceListState: StateFlow<List<AdviceItemData>>
        field = MutableStateFlow(emptyList())

    val networkStatus: StateFlow<NetworkStatus>
        field = MutableStateFlow(NetworkStatus.ONLINE)

    var selectedCaseTab by mutableStateOf(CaseTab.Pending)
        private set

    val caseTabCounts: StateFlow<Map<CaseTab, Int>> = caseRepository.observeCaseCounts()
        .map { counts ->
            CaseTab.entries.associateWith { tab -> counts[tab.apiParam] ?: 0 }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = CaseTab.entries.associateWith { 0 }
        )

    var isCaseCountsLoaded by mutableStateOf(false)
        private set

    init {
        // Observe backstack to trigger refresh when user lands on Main screen (e.g. after login)
        viewModelScope.launch(mainContext) {
            snapshotFlow { backStack }.collect {
                if (isUserLoggedIn && !isCaseCountsLoaded) {
                    println("DEBUG: MainViewModel triggered refreshAllCounts via BackStack observer")
                    refreshAllCounts()
                }
            }
        }

        viewModelScope.launch(mainContext) {
            notifierManager.notificationClickFlow.collect { data ->
                println("MainViewModel: Notification click received = $data")
                backStack.replaceWith(Screens.Main)
                uiEvent.emit(MainUiEvent.OpenCasesTab(CaseTab.Pending))
            }
        }

        viewModelScope.launch(mainContext) {
            notifierManager.notificationReceivedFlow
                .debounce(3000) // Debounce to avoid multiple rapid notifications causing multiple refreshes
                .collect {
                    refreshAllCounts()
                }
        }

        viewModelScope.launch(mainContext) {
            isUnauthorizedFlow.collect { isUnauthorized ->
                if (isUnauthorized) {
                    showError("Session Expired. Please login again.")
                    backStack.replaceWith(Screens.Auth)
                }
            }
        }
    }

    fun refreshServerStatus() {
        viewModelScope.launch {
            val status = mainRepository.checkServerStatus()
            (networkStatus as MutableStateFlow).value = status
        }
    }

    fun clearEvents() {
        launch {
            uiEvent.emit(MainUiEvent.Idle)
        }
    }

    fun openMoreTab() {
        backStack.replaceWith(Screens.Main)
        launch {
            uiEvent.emit(MainUiEvent.OpenMoreTab)
        }
    }

    fun openCasesTab(tab: CaseTab = CaseTab.Pending) {
        backStack.replaceWith(Screens.Main)
        launch {
            uiEvent.emit(MainUiEvent.OpenCasesTab(tab))
        }
    }

    fun selectBottomTab(tab: BottomNavItems) {
        selectedBottomTab = tab
        if (tab == BottomNavItems.Cases && !isCaseCountsLoaded) {
            refreshAllCounts()
        }
    }

    fun refreshAllCounts(appVersion: Int = 3069) {
        if (!isUserLoggedIn) return

        launch(loading = Loading.Secondary) {
            try {
                println("DEBUG: refreshAllCounts started parallel load")
                // Load all tab counts in parallel to avoid one failure blocking others
                CaseTab.entries.map { tab ->
                    async {
                        caseRepository.getInterviewList(
                            appVersion = appVersion,
                            type = tab.apiParam
                        )
                    }
                }.awaitAll()

                isCaseCountsLoaded = true
                println("DEBUG: refreshAllCounts completed successfully")
            } catch (e: Exception) {
                println("DEBUG: refreshAllCounts error: ${e.message}")
            }
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
            println("DEBUG: Case List received for ${tab.name} = ${list.size} items")

            interviewListState.value = list
        }
    }

    fun openCase(
        interview: Interview,
        sourceTab: CaseTab
    ) {
        val mode = if (sourceTab == CaseTab.Answered) {
            CaseDetailsMode.ANSWERED
        } else {
            CaseDetailsMode.NORMAL
        }

        backStack.add(
            Screens.CaseDetail(
                interviewId = interview.interviewId,
                mode = mode,
                selectedTab = sourceTab.name
            )
        )
    }


    fun logout() {
        viewModelScope.launch(mainContext) {
            val user = settings.user
            if (user.userName.isNotEmpty()) {
                notifierManager.unsubscribeFromTopic(FcmTopics.doctorCaseList(user.userName))
                notifierManager.unsubscribeFromTopic(FcmTopics.doctorInterviewUpdates(user.userName))
            }
            mainRepository.clearAllData()
            settings.clear()
            isCaseCountsLoaded = false
            backStack.replaceWith(Screens.Auth)
        }
    }

    fun loadAdviceList() {
        launch(loading = Loading.Secondary) {
            adviceListState.value = mainRepository.getAdviceList()
        }
    }

    fun saveAdvice(title: String, content: String) {
        viewModelScope.launch(mainContext) {
            loadingFlow.value = true
            val (isSuccess, errorMessage) = mainRepository.saveAdvice(title, content)
            loadingFlow.value = false
            if (isSuccess) {
                showSuccess("Advice saved successfully")
                loadAdviceList()
            } else {
                showError(errorMessage ?: "Failed to save advice")
            }
        }
    }

    fun saveInvestigation(title: String) {
        viewModelScope.launch(mainContext) {
            loadingFlow.value = true
            val (isSuccess, errorMessage) = mainRepository.saveInvestigation(title)
            loadingFlow.value = false
            if (isSuccess) {
                showSuccess("Investigation saved successfully")
                refreshSetupData()
            } else {
                showError(errorMessage ?: "Failed to save investigation")
            }
        }
    }

    fun refreshSetupData() {
        launch(loading = Loading.Secondary) {
            mainRepository.getSetupData(forceRefresh = true).collectLatest {
                // The setupDataState is already observing the repository flow
                // so it will update automatically.
            }
        }
    }

    fun changePassword(old: String, new: String) {
        viewModelScope.launch(mainContext) {
            loadingFlow.value = true
            val (isSuccess, message) = mainRepository.changePassword(old, new)
            loadingFlow.value = false
            if (isSuccess) {
                showSuccess("Password changed successfully. Please login again.")
                logout()
            } else {
                showError(message ?: "Failed to change password")
            }
        }
    }
}
