package ngo.friendship.mhealth.dc.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
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
import ngo.friendship.mhealth.dc.fcm.FcmTopics
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.BottomNavItems
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.navigation.components.replaceWith
import ngo.friendship.mhealth.dc.presentation.navigation.navConfiguration
import ngo.friendship.mhealth.dc.presentation.screens.case.CaseDetailsMode
import ngo.friendship.mhealth.dc.presentation.screens.case.case_list.components.CaseTab
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.AdviceItemData
import ngo.friendship.mhealth.dc.utils.log

sealed interface MainUiEvent {
    data object Idle : MainUiEvent
    data object OpenCasesTab : MainUiEvent
}

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

    val setupDataState = mainRepository.getSetupData().stateIn(
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
            println("DEBUG: Case List received for ${tab.name} = ${list.size} items")

            interviewListState.value = list

            caseTabCounts = caseTabCounts.toMutableMap().apply {
                this[tab] = list.size
            }
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
        viewModelScope.launch {
            val user = settings.user
            if (user.userName.isNotEmpty()) {
                notifierManager.unsubscribeFromTopic(FcmTopics.doctorCaseList(user.userName))
                notifierManager.unsubscribeFromTopic(FcmTopics.doctorInterviewUpdates(user.userName))
            }
            settings.clear()
            backStack.replaceWith(Screens.Auth)
        }
    }

    fun loadAdviceList() {
        launch(loading = Loading.Secondary) {
            adviceListState.value = mainRepository.getAdviceList()
        }
    }

    fun saveAdvice(title: String, content: String) {
        viewModelScope.launch {
            loadingFlow.value = true
            val isSuccess = mainRepository.saveAdvice(title, content)
            loadingFlow.value = false
            if (isSuccess) {
                showSuccess("Advice saved successfully")
                loadAdviceList()
            } else {
                showError("Failed to save advice")
            }
        }
    }

    fun saveInvestigation(title: String) {
        viewModelScope.launch {
            loadingFlow.value = true
            val isSuccess = mainRepository.saveInvestigation(title)
            loadingFlow.value = false
            if (isSuccess) {
                showSuccess("Investigation saved successfully")
            } else {
                showError("Failed to save investigation")
            }
        }
    }
}
