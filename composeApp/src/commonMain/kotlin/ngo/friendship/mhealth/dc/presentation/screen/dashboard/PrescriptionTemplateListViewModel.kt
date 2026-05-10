package ngo.friendship.mhealth.dc.presentation.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ngo.friendship.mhealth.dc.domain.repository.MainRepository

class PrescriptionTemplateListViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PrescriptionTemplateListState())
    val state = _state.asStateFlow()

    init {
        loadTemplates()
    }

    fun onIntent(intent: PrescriptionTemplateListIntent) {
        when (intent) {
            PrescriptionTemplateListIntent.LoadTemplates -> loadTemplates()
            is PrescriptionTemplateListIntent.Search -> {
                // Handle search if needed
            }
        }
    }

    private fun loadTemplates() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val templates = mainRepository.getPrescriptionTemplates()
                _state.value = _state.value.copy(
                    templates = templates,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "An error occurred"
                )
            }
        }
    }
}
