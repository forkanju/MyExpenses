package ngo.friendship.mhealth.dc.presentation.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ngo.friendship.mhealth.dc.domain.repository.CaseRepository
import ngo.friendship.mhealth.dc.domain.repository.MainRepository

class MedicineListViewModel(
    private val mainRepository: MainRepository,
    private val caseRepository: CaseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MedicineListState())
    val state: StateFlow<MedicineListState> = _state.asStateFlow()

    private val _effect = Channel<MedicineListEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadSetupData()
        observeMedicines()
        onIntent(MedicineListIntent.LoadMedicines)
    }

    private fun observeMedicines() {
        viewModelScope.launch {
            caseRepository.observeAllMedicines().collect { medicines ->
                _state.update { it.copy(medicines = medicines) }
                filterMedicines()
            }
        }
    }

    fun onIntent(intent: MedicineListIntent) {
        when (intent) {
            is MedicineListIntent.LoadMedicines -> loadMedicines()
            is MedicineListIntent.SearchQueryChanged -> {
                _state.update { it.copy(searchQuery = intent.query) }
                filterMedicines()
            }
            is MedicineListIntent.ToggleNewMedicineDialog -> {
                _state.update { it.copy(showNewMedicineDialog = !it.showNewMedicineDialog) }
            }
            is MedicineListIntent.SaveMedicine -> saveMedicine(intent.type, intent.genericName)
            MedicineListIntent.Refresh -> {
                loadSetupData(forceRefresh = true)
                loadMedicines(forceRefresh = true)
            }
        }
    }

    private fun loadSetupData(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            mainRepository.getSetupData(forceRefresh = forceRefresh).collect { setupData ->
                _state.update { it.copy(medicineTypes = setupData.medicineBrandTypes) }
            }
        }
    }

    private fun loadMedicines(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            if (forceRefresh) {
                _state.update { it.copy(isRefreshing = true) }
            } else {
                _state.update { it.copy(isLoading = true) }
            }
            try {
                // Refresh common types to populate local DB
                val types = listOf("Tab", "Syp", "Cap", "Inj")
                types.forEach { type ->
                    caseRepository.getMedicineList(type, forceRefresh = forceRefresh)
                }
                
                _state.update { 
                    it.copy(
                        isLoading = false,
                        isRefreshing = false
                    ) 
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, isRefreshing = false) }
                _effect.send(MedicineListEffect.ShowSnackbar("Failed to load medicines"))
            }
        }
    }

    private fun filterMedicines() {
        _state.update { currentState ->
            val filtered = if (currentState.searchQuery.isBlank()) {
                currentState.medicines
            } else {
                currentState.medicines.filter {
                    it.genericName.contains(currentState.searchQuery, ignoreCase = true) ||
                            it.brandName.contains(currentState.searchQuery, ignoreCase = true)
                }
            }
            currentState.copy(filteredMedicines = filtered)
        }
    }

    private fun saveMedicine(type: String, genericName: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, showNewMedicineDialog = false) }
            val (isSuccess, errorMessage) = mainRepository.saveMedicine(
                genericName = genericName,
                brandName = genericName,
                type = type,
                strength = ""
            )
            _state.update { it.copy(isLoading = false) }
            if (isSuccess) {
                _effect.send(MedicineListEffect.ShowSnackbar("Medicine saved successfully"))
                // No need to call loadMedicines manually, observeMedicines will pick up DB changes
                // from mainRepository.saveMedicine() which calls medResponse...insertMedicines
            } else {
                _effect.send(MedicineListEffect.ShowSnackbar(errorMessage ?: "Failed to save medicine"))
            }
        }
    }
}
