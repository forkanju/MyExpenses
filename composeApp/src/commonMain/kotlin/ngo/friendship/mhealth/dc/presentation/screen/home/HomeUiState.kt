package ngo.friendship.mhealth.dc.presentation.screens.home

import ngo.friendship.mhealth.dc.presentation.screens.home.model.KeyValueUi
import ngo.friendship.mhealth.dc.presentation.screens.home.model.SegmentUi
import ngo.friendship.mhealth.dc.presentation.screens.home.model.StatRingUi
import ngo.friendship.mhealth.dc.presentation.screens.home.model.TrendRowUi

data class HomeUiState(
    val title: String = "Today",
    val totalCaseText: String = "Total Case: 0",
    val stats: List<StatRingUi> = emptyList(),
    val trendRows: List<TrendRowUi> = emptyList(),
    val segments: List<SegmentUi> = emptyList(),
    val byServices: List<KeyValueUi> = emptyList(),
    val byArea: List<KeyValueUi> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false
)
