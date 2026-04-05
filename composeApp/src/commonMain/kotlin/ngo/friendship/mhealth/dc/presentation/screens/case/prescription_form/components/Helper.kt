package ngo.friendship.mhealth.dc.presentation.screens.main.prescription_form.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.domain.model.Diagnosis
import ngo.friendship.mhealth.dc.domain.model.Investigation
import ngo.friendship.mhealth.dc.presentation.screens.main.prescription_form.model.DoctorFeedbackFormState

fun addDiagnosis(
    state: DoctorFeedbackFormState,
    item: Diagnosis
): DoctorFeedbackFormState {
    if (state.selectedDiagnoses.any { it.diagId == item.diagId }) return state
    return state.copy(selectedDiagnoses = state.selectedDiagnoses + item)
}

fun removeDiagnosis(
    state: DoctorFeedbackFormState,
    item: Diagnosis
): DoctorFeedbackFormState {
    return state.copy(
        selectedDiagnoses = state.selectedDiagnoses.filterNot { it.diagId == item.diagId }
    )
}

fun addInvestigation(
    state: DoctorFeedbackFormState,
    item: Investigation
): DoctorFeedbackFormState {
    if (state.selectedInvestigations.any { it.investigationId == item.investigationId }) return state
    return state.copy(selectedInvestigations = state.selectedInvestigations + item)
}

fun removeInvestigation(
    state: DoctorFeedbackFormState,
    item: Investigation
): DoctorFeedbackFormState {
    return state.copy(
        selectedInvestigations = state.selectedInvestigations.filterNot {
            it.investigationId == item.investigationId
        }
    )
}

