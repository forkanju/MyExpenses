package ngo.friendship.mhealth.dc.presentation.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update
import ngo.friendship.mhealth.dc.domain.repository.MainRepository

class DxListViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DxListState())
    val state = _state.asStateFlow()

    private val _effect = Channel<DxListEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadDxItems()
    }

    fun onIntent(intent: DxListIntent) {
        when (intent) {
            is DxListIntent.SelectFilter -> {
                _state.update { it.copy(selectedFilter = intent.filter) }
                filterDxItems()
            }
            is DxListIntent.Search -> {
                _state.update { it.copy(searchQuery = intent.query) }
                filterDxItems()
            }
            is DxListIntent.ShowNewDxDialog -> {
                _state.update { it.copy(showNewDxDialog = intent.show) }
            }
            is DxListIntent.CreateDx -> {
                createDx(intent.title, intent.advices)
            }
            is DxListIntent.ToggleExpand -> {
                toggleExpand(intent.title)
            }
            DxListIntent.ClearError -> {
                _state.update { it.copy(error = null) }
            }
            DxListIntent.Refresh -> {
                loadDxItems(forceRefresh = true)
            }
        }
    }

    private fun loadDxItems(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            if (forceRefresh) {
                _state.update { it.copy(isRefreshing = true) }
            } else {
                _state.update { it.copy(isLoading = true) }
            }
            try {
                mainRepository.getSetupData(forceRefresh = forceRefresh).collect { setupData ->
                    val items = setupData.diagnoses.map {
                        DxItemData(
                            title = it.diagName.replace("_", " "),
                            subtitle = "ID: ${it.diagId}",
                            details = emptyList()
                        )
                    }
                    _state.update {
                        it.copy(
                            dxItems = items,
                        )
                    }
                    filterDxItems()
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Failed to load diagnoses") }
            } finally {
                _state.update { it.copy(isLoading = false, isRefreshing = false) }
            }
        }
    }

    private fun filterDxItems() {
        _state.update { currentState ->
            val query = currentState.searchQuery
            val all = currentState.dxItems
            
            val filtered = if (query.isBlank()) {
                all
            } else {
                all.filter { item ->
                    item.title.contains(query, ignoreCase = true) ||
                    item.subtitle.contains(query, ignoreCase = true) ||
                    item.details.any { it.contains(query, ignoreCase = true) }
                }
            }
            currentState.copy(filteredDxItems = filtered)
        }
    }

    private fun createDx(title: String, advices: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, showNewDxDialog = false) }
            try {
                val (success, errorMessage) = mainRepository.saveDiagnosis(title)
                if (success) {
                    _effect.send(DxListEffect.DxCreated)
                    loadDxItems(forceRefresh = true)
                } else {
                    _state.update { it.copy(error = errorMessage ?: "Failed to save diagnosis") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Failed to save diagnosis") }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun toggleExpand(title: String) {
        _state.update { currentState ->
            val updatedItems = currentState.dxItems.map {
                if (it.title == title) it.copy(isExpanded = !it.isExpanded) else it
            }
            currentState.copy(dxItems = updatedItems)
        }
        filterDxItems()
    }
}
