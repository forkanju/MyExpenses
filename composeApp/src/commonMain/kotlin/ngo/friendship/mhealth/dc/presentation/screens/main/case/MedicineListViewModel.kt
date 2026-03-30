package ngo.friendship.mhealth.dc.presentation.screens.main.case

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ngo.friendship.mhealth.dc.domain.model.Medicine
import ngo.friendship.mhealth.dc.domain.repository.MedicineListRepository
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.state.RequestState

class MedicineListViewModel(
    private val repository: MedicineListRepository
) : BaseViewModel() {
    private val _medicineListState =
        MutableStateFlow<RequestState<List<Medicine>>>(RequestState.Idle)
    val medicineListState: StateFlow<RequestState<List<Medicine>>> = _medicineListState

    fun loadMedicineList(
        userName: String,
        password: String,
        type: String = "Tab"
    ) {
        launch {
            _medicineListState.value = RequestState.Loading
            runCatching {
                repository.getMedicineList(
                    userName = userName,
                    password = password,
                    type = type
                )
            }.onSuccess { medicines ->
                _medicineListState.value = RequestState.Success(medicines)
            }.onFailure { throwable ->
                _medicineListState.value = RequestState.Error(
                    throwable.message ?: "Failed to load medicine list"
                )
            }
        }
    }

    fun resetMedicineListState() {
        _medicineListState.value = RequestState.Idle
    }
}