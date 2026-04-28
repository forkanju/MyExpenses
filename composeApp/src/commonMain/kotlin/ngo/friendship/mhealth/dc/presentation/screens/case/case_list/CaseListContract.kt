package ngo.friendship.mhealth.dc.presentation.screens.case.case_list

import ngo.friendship.mhealth.dc.domain.model.Interview
import ngo.friendship.mhealth.dc.presentation.screens.case.case_list.components.CaseTab

data class CaseListState(
    val isLoading: Boolean = false,
    val interviews: List<Interview> = emptyList(),
    val filteredInterviews: List<Interview> = emptyList(),
    val selectedTab: CaseTab = CaseTab.Pending,
    val tabCounts: Map<CaseTab, Int> = emptyMap(),
    val searchQuery: String = "",
    val error: String? = null
)

sealed interface CaseListIntent {
    data class SelectTab(val tab: CaseTab) : CaseListIntent
    data class Search(val query: String) : CaseListIntent
    data object Refresh : CaseListIntent
    data object ClearError : CaseListIntent
    data class ClickCase(val interview: Interview) : CaseListIntent
    data object ClickFilter : CaseListIntent
}

sealed interface CaseListEffect {
    data class NavigateToDetails(val interview: Interview) : CaseListEffect
    data object OpenFilterSheet : CaseListEffect
}
