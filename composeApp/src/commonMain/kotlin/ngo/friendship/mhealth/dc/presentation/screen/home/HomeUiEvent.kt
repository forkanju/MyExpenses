package ngo.friendship.mhealth.dc.presentation.screens.home

sealed interface HomeUiEvent {
    data class ShowError(val message: String) : HomeUiEvent
}
