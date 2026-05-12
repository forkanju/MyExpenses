package ngo.friendship.mhealth.dc.presentation.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ngo.friendship.mhealth.dc.domain.repository.MainRepository
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.DxItemData
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.DxListEffect
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.DxListIntent
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.DxListState

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
                _state.value = _state.value.copy(selectedFilter = intent.filter)
                filterDxItems()
            }
            is DxListIntent.Search -> {
                _state.value = _state.value.copy(searchQuery = intent.query)
                filterDxItems()
            }
            is DxListIntent.ShowNewDxDialog -> {
                _state.value = _state.value.copy(showNewDxDialog = intent.show)
            }
            is DxListIntent.CreateDx -> {
                createDx(intent.title, intent.advices)
            }
            is DxListIntent.ToggleExpand -> {
                toggleExpand(intent.title)
            }
            DxListIntent.ClearError -> {
                _state.value = _state.value.copy(error = null)
            }
        }
    }

    private fun loadDxItems() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            mainRepository.getSetupData().collect { setupData ->
                val items = setupData.diagnoses.map {
                    DxItemData(
                        title = it.diagName.replace("_", " "),
                        subtitle = "ID: ${it.diagId}",
                        details = emptyList()
                    )
                }
                _state.value = _state.value.copy(
                    dxItems = items.ifEmpty { _state.value.dxItems },
                    isLoading = false
                )
                filterDxItems()
            }
        }
    }

    private fun filterDxItems() {
        val query = _state.value.searchQuery
        val all = _state.value.dxItems
        
        val filtered = if (query.isBlank()) {
            all
        } else {
            all.filter { item ->
                item.title.contains(query, ignoreCase = true) ||
                item.subtitle.contains(query, ignoreCase = true) ||
                item.details.any { it.contains(query, ignoreCase = true) }
            }
        }
        _state.value = _state.value.copy(filteredDxItems = filtered)
    }

    private fun createDx(title: String, advices: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, showNewDxDialog = false)
            val success = mainRepository.saveDiagnosis(title)
            if (success) {
                _effect.send(DxListEffect.DxCreated)
                // Refresh data if needed, though getSetupData collect should handle it if it emits new values
            } else {
                _state.value = _state.value.copy(error = "Failed to save diagnosis")
            }
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    private fun toggleExpand(title: String) {
        val updatedItems = _state.value.dxItems.map {
            if (it.title == title) it.copy(isExpanded = !it.isExpanded) else it
        }
        _state.value = _state.value.copy(dxItems = updatedItems)
        filterDxItems()
    }
}
