package ngo.friendship.mhealth.dc.presentation.screens.dashboard

data class DxItemData(
    val title: String,
    val subtitle: String,
    val details: List<String> = emptyList(),
    val isExpanded: Boolean = false
)

data class DxListState(
    val isLoading: Boolean = false,
    val selectedFilter: String = "All",
    val filters: List<String> = listOf("All", "Recent Used", "Recent Updated"),
    val searchQuery: String = "",
    val showNewDxDialog: Boolean = false,
    val dxItems: List<DxItemData> = emptyList(),
    val filteredDxItems: List<DxItemData> = emptyList(),
    val error: String? = null
)

sealed interface DxListIntent {
    data class SelectFilter(val filter: String) : DxListIntent
    data class Search(val query: String) : DxListIntent
    data class ShowNewDxDialog(val show: Boolean) : DxListIntent
    data class CreateDx(val title: String, val advices: String) : DxListIntent
    data class ToggleExpand(val title: String) : DxListIntent
    data object ClearError : DxListIntent
}

sealed interface DxListEffect {
    data object DxCreated : DxListEffect
}
