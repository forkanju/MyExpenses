package ngo.friendship.mhealth.dc.presentation.screen.case.case_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ngo.friendship.mhealth.dc.domain.repository.CaseRepository
import ngo.friendship.mhealth.dc.presentation.screen.case.case_list.components.CaseTab

class CaseListViewModel(
    private val repository: CaseRepository
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(CaseTab.Pending)
    private val _searchQuery = MutableStateFlow("")
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val state: StateFlow<CaseListState> = combine(
        repository.observeCases(),
        _selectedTab,
        _searchQuery,
        _isLoading,
        _error
    ) { allInterviews, tab, query, loading, err ->

        // Deriving tab counts from the full list of interviews in the repository
        val counts = CaseTab.entries.associateWith { t ->
            allInterviews.count { it.status == t.apiParam }
        }

        // Filtering interviews by current tab and search query
        val filtered = allInterviews.filter { it.status == tab.apiParam }
            .filter { item ->
                query.isBlank() ||
                        item.beneficiaryName.contains(query, ignoreCase = true) ||
                        item.beneficiaryCode.contains(query, ignoreCase = true) ||
                        item.location.contains(query, ignoreCase = true) ||
                        item.questionnaireName.contains(query, ignoreCase = true)
            }
            .sortedByDescending { it.startTime }

        CaseListState(
            isLoading = loading,
            interviews = allInterviews.filter { it.status == tab.apiParam }.sortedByDescending { it.startTime },
            filteredInterviews = filtered,
            selectedTab = tab,
            tabCounts = counts,
            searchQuery = query,
            error = err
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        CaseListState(isLoading = true)
    )

    private val _effect = Channel<CaseListEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        // Initial data loading
        loadAllTabCounts()
        loadInterviews()
    }

    fun onIntent(intent: CaseListIntent) {
        when (intent) {
            is CaseListIntent.SelectTab -> {
                if (_selectedTab.value != intent.tab) {
                    _selectedTab.value = intent.tab
                    loadInterviews()
                }
            }

            is CaseListIntent.Search -> {
                _searchQuery.value = intent.query
            }

            CaseListIntent.Refresh -> loadInterviews()
            CaseListIntent.ClearError -> _error.value = null
            is CaseListIntent.ClickCase -> {
                viewModelScope.launch {
                    // Automatically mark as opened when clicked
                    repository.markAsOpened(intent.interview.interviewId.toString())
                    _effect.send(
                        CaseListEffect.NavigateToDetails(
                            intent.interview,
                            _selectedTab.value
                        )
                    )
                }
            }

            CaseListIntent.ClickFilter -> {
                viewModelScope.launch {
                    _effect.send(CaseListEffect.OpenFilterSheet)
                }
            }
        }
    }

    private fun loadInterviews() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Repository will update its internal flow, which triggers state update
                repository.getInterviewList(appVersion = 3069, type = _selectedTab.value.apiParam)
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load cases"
                _isLoading.value = false
            }
        }
    }

    private fun loadAllTabCounts() {
        viewModelScope.launch {
            CaseTab.entries.forEach { tab ->
                try {
                    // Fetch each tab to populate the repository's internal list
                    repository.getInterviewList(appVersion = 3069, type = tab.apiParam)
                } catch (e: Exception) {
                    // Ignore individual tab loading errors for background count update
                }
            }
        }
    }
}
