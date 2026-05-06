package ngo.friendship.mhealth.dc.presentation.screens.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ngo.friendship.mhealth.dc.domain.model.PrescriptionTemplate
import ngo.friendship.mhealth.dc.domain.repository.MoreRepository
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel

class MoreViewModel(
    private val repository: MoreRepository
) : BaseViewModel() {

    private val _templates = MutableStateFlow<List<PrescriptionTemplate>>(emptyList())
    val templates: StateFlow<List<PrescriptionTemplate>> = _templates.asStateFlow()

    var searchQuery by mutableStateOf("")
        private set

    init {
        loadTemplates()
    }

    private fun loadTemplates() {
        viewModelScope.launch {
            repository.getPrescriptionTemplates().collectLatest {
                _templates.value = it
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        searchQuery = query
        // Implement search filtering logic if needed
    }

    fun deleteTemplate(id: String) {
        viewModelScope.launch {
            repository.deleteTemplate(id)
        }
    }
}
