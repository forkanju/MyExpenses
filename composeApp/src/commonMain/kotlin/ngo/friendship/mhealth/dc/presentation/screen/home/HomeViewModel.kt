package ngo.friendship.mhealth.dc.presentation.screen.home

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import ngo.friendship.mhealth.dc.data.local.LocalSettings
import ngo.friendship.mhealth.dc.data.remote.ApiService
import ngo.friendship.mhealth.dc.data.remote.dto.DashboardDataReqDto
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.screens.home.HomeIntent
import ngo.friendship.mhealth.dc.presentation.screens.home.HomeUiEvent
import ngo.friendship.mhealth.dc.presentation.screens.home.HomeUiState
import ngo.friendship.mhealth.dc.presentation.screens.home.model.KeyValueUi
import ngo.friendship.mhealth.dc.presentation.screens.home.model.SegmentUi
import ngo.friendship.mhealth.dc.presentation.screens.home.model.StatRingUi
import ngo.friendship.mhealth.dc.presentation.screens.home.model.TrendRowUi
import ngo.friendship.mhealth.dc.theme.RingBarBlue
import ngo.friendship.mhealth.dc.theme.RingBarGreen
import ngo.friendship.mhealth.dc.theme.RingBarRed
import ngo.friendship.mhealth.dc.theme.TrendBlue
import ngo.friendship.mhealth.dc.theme.TrendRed

class HomeViewModel(
    private val api: ApiService,
    private val localSettings: LocalSettings
) : BaseViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<HomeUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        onIntent(HomeIntent.LoadDashboard)
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.LoadDashboard -> loadDashboard()
        }
    }

    private fun loadDashboard() {
        launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val response = api.getDashboardData(
                    DashboardDataReqDto.build(
                        userName = localSettings.user.userName,
                        password = localSettings.user.password
                    )
                )
//                println("Dashboard_Response: ${response.data?.statusSummary}")
//                println("Dashboard_Response: ${response.data?.topUpazila}")
//                println("Dashboard_Response: ${response.data?.timeSummary}")
//                println("Dashboard_Response: ${response.data?.topQuestionnaires}")

                val statusSummary = response.data?.statusSummary
                val timeSummary = response.data?.timeSummary
                val total = statusSummary?.total ?: 0

                val stats = listOf(
                    StatRingUi(
                        label = "Pending",
                        value = statusSummary?.pending ?: 0,
                        color = RingBarRed,
                        max = total.coerceAtLeast(minimumValue = 1)
                    ),
                    StatRingUi(
                        label = "Answered",
                        value = statusSummary?.answered ?: 0,
                        color = RingBarGreen,
                        max = total.coerceAtLeast(minimumValue = 1)
                    ),
                    StatRingUi(
                        label = "Referred",
                        value = statusSummary?.referred ?: 0,
                        color = RingBarBlue,
                        max = total.coerceAtLeast(minimumValue = 1)
                    )
                )

                val trendRows = listOf(
                    TrendRowUi(
                        name = "Total",
                        in30Min = timeSummary?.in30Min ?: 0,
                        after30Min = timeSummary?.after30Min ?: 0,
                        after2Hours = timeSummary?.after2Hours ?: 0
                    )
                )

                val totalTime =
                    ((timeSummary?.in30Min ?: 0) + (timeSummary?.after30Min
                        ?: 0) + (timeSummary?.after2Hours
                        ?: 0)).coerceAtLeast(minimumValue = 1).toFloat()
                val segments = listOf(
                    SegmentUi(
                        label = "in30",
                        percent = (timeSummary?.in30Min ?: 0) / totalTime * 100f,
                        color = RingBarGreen
                    ),
                    SegmentUi(
                        label = "after30",
                        percent = (timeSummary?.after30Min ?: 0) / totalTime * 100f,
                        color = TrendBlue
                    ),
                    SegmentUi(
                        label = "after2h",
                        percent = (timeSummary?.after2Hours ?: 0) / totalTime * 100f,
                        color = TrendRed
                    )
                )

                val byServices = response.data?.topQuestionnaires?.map {
                    KeyValueUi(it.questionnaireTitle ?: "", it.totalCount ?: 0)
                } ?: emptyList()

                val byArea = response.data?.topUpazila?.map {
                    KeyValueUi(it.upazilaName ?: "", it.totalService ?: 0)
                } ?: emptyList()

                _state.update {
                    it.copy(
                        title = "Today",
                        totalCaseText = "Total Case: $total",
                        stats = stats,
                        trendRows = trendRows,
                        segments = segments,
                        byServices = byServices,
                        byArea = byArea,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                _uiEvent.send(HomeUiEvent.ShowError(e.message ?: "Error loading dashboard"))
            }
        }
    }
}