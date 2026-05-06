package ngo.friendship.mhealth.dc.presentation.screens.case

sealed interface CaseUiEvent {
    data class ShowSnackbar(val message: String) : CaseUiEvent
    data object NavigateBack : CaseUiEvent
}