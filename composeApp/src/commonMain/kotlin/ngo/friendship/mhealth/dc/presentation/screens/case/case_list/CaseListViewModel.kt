package ngo.friendship.mhealth.dc.presentation.screens.case.case_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ngo.friendship.mhealth.dc.domain.repository.CaseRepository
import ngo.friendship.mhealth.dc.presentation.screens.case.case_list.components.CaseTab

class CaseListViewModel(
    private val repository: CaseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CaseListState())
    val state = _state.asStateFlow()

    private val _effect = Channel<CaseListEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadAllTabCounts()
        loadInterviews()
    }

    fun onIntent(intent: CaseListIntent) {
        when (intent) {
            is CaseListIntent.SelectTab -> {
                if (_state.value.selectedTab != intent.tab) {
                    _state.value = _state.value.copy(selectedTab = intent.tab)
                    loadInterviews()
                }
            }
            is CaseListIntent.Search -> {
                _state.value = _state.value.copy(searchQuery = intent.query)
                filterInterviews()
            }
            CaseListIntent.Refresh -> loadInterviews()
            CaseListIntent.ClearError -> _state.value = _state.value.copy(error = null)
            is CaseListIntent.ClickCase -> {
                viewModelScope.launch {
                    _effect.send(CaseListEffect.NavigateToDetails(intent.interview, state.value.selectedTab))
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
            println("DEBUG: CaseListViewModel.loadInterviews() started for tab=${_state.value.selectedTab}")
            _state.value = _state.value.copy(isLoading = true)
            try {
                // Mapping CaseTab to API type string using apiParam
                val type = _state.value.selectedTab.apiParam 
                println("DEBUG: CaseListViewModel calling repository for type=$type")
                val interviews = repository.getInterviewList(appVersion = 3069, type = type)
                
                println("DEBUG: CaseListViewModel received ${interviews.size} interviews")
                _state.value = _state.value.copy(
                    interviews = interviews,
                    isLoading = false
                )
                filterInterviews()
                updateTabCounts()
            } catch (e: Exception) {
                println("DEBUG: CaseListViewModel error: ${e.message}")
                e.printStackTrace()
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load cases"
                )
            }
        }
    }

    private fun filterInterviews() {
        val query = _state.value.searchQuery
        val all = _state.value.interviews
        
        val filtered = if (query.isBlank()) {
            all
        } else {
            all.filter { item ->
                item.beneficiaryName.contains(query, ignoreCase = true) ||
                item.beneficiaryCode.contains(query, ignoreCase = true) ||
                item.location.contains(query, ignoreCase = true) ||
                item.questionnaireName.contains(query, ignoreCase = true)
            }
        }
        _state.value = _state.value.copy(filteredInterviews = filtered)
    }

    private fun updateTabCounts() {
        // Logic to update counts for all tabs if needed, 
        // or just update for the current list size
        val currentCounts = _state.value.tabCounts.toMutableMap()
        currentCounts[_state.value.selectedTab] = _state.value.interviews.size
        _state.value = _state.value.copy(tabCounts = currentCounts)
    }

    private fun loadAllTabCounts() {
        viewModelScope.launch {
            val counts = _state.value.tabCounts.toMutableMap()
            CaseTab.entries.forEach { tab ->
                try {
                    val list = repository.getInterviewList(appVersion = 3069, type = tab.apiParam)
                    counts[tab] = list.size
                } catch (e: Exception) {
                    counts[tab] = 0
                }
            }
            _state.value = _state.value.copy(tabCounts = counts)
        }
    }
}
