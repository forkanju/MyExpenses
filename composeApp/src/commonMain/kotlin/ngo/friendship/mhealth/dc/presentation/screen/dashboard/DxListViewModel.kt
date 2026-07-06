package ngo.friendship.mhealth.dc.presentation.screen.dashboard

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withTimeoutOrNull
import ngo.friendship.mhealth.dc.domain.repository.MainRepository
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel

class DxListViewModel(
    private val mainRepository: MainRepository
) : BaseViewModel() {

    private val _state = MutableStateFlow(DxListState())
    val state = _state.asStateFlow()

    private val _effect = Channel<DxListEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        observeDxItems()
        loadDxItems(forceRefresh = false)
    }

    private fun observeDxItems() {
        launch(loading = Loading.Gone) {
            mainRepository.getSetupData(forceRefresh = false).collect { setupData ->
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
        }
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
        launch(loading = if (forceRefresh) Loading.Gone else Loading.Primary) {
            if (forceRefresh) {
                _state.update { it.copy(isRefreshing = true) }
            }

            try {
                // withTimeoutOrNull ensures that even if Room/Network hangs, the loading will hide.
                // 35 seconds total (allowing for the 30s network timeout)
                withTimeoutOrNull(35_000L) {
                    mainRepository.getSetupData(forceRefresh = forceRefresh).take(1).collect {}
                }
            } finally {
                _state.update { it.copy(isRefreshing = false) }
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
        launch {
            _state.update { it.copy(showNewDxDialog = false) }
            val (success, errorMessage) = mainRepository.saveDiagnosis(title)
            if (success) {
                _effect.send(DxListEffect.DxCreated)
                loadDxItems(forceRefresh = true)
            } else {
                showError(errorMessage ?: "Failed to save diagnosis")
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
