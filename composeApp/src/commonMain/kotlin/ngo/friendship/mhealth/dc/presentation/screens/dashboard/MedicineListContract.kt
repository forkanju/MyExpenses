package ngo.friendship.mhealth.dc.presentation.screens.dashboard

import ngo.friendship.mhealth.dc.domain.model.Medicine
import ngo.friendship.mhealth.dc.domain.model.MedicineBrandType

data class MedicineListState(
    val isLoading: Boolean = false,
    val medicines: List<Medicine> = emptyList(),
    val filteredMedicines: List<Medicine> = emptyList(),
    val medicineTypes: List<MedicineBrandType> = emptyList(),
    val searchQuery: String = "",
    val showNewMedicineDialog: Boolean = false
)

sealed interface MedicineListIntent {
    data object LoadMedicines : MedicineListIntent
    data class SearchQueryChanged(val query: String) : MedicineListIntent
    data object ToggleNewMedicineDialog : MedicineListIntent
    data class SaveMedicine(val type: String, val genericName: String) : MedicineListIntent
}

sealed interface MedicineListEffect {
    data class ShowSnackbar(val message: String) : MedicineListEffect
}
