package ngo.friendship.mhealth.dc.presentation.screen.case

import ngo.friendship.mhealth.dc.presentation.base.SnackbarType

sealed interface CaseUiEvent {
    data class ShowSnackbar(val message: String, val type: SnackbarType = SnackbarType.DEFAULT) : CaseUiEvent
    data object NavigateBack : CaseUiEvent
}