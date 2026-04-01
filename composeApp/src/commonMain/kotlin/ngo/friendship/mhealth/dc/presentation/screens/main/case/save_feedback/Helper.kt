package ngo.friendship.mhealth.dc.presentation.screens.main.case.save_feedback

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import io.kotzilla.sdk.logger.Logger
import ngo.friendship.mhealth.dc.domain.model.Diagnosis
import ngo.friendship.mhealth.dc.domain.model.Investigation

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

@Composable
fun SelectedItemChip(
    text: String,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF1F5F9),
        border = BorderStroke(1.dp, Color(0xFFCBD5E1))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "✕",
                modifier = Modifier.clickable { onRemove() }
            )
        }
    }
}

@Composable
fun DiagnosisChipGroup(
    items: List<Diagnosis>,
    onRemove: (Diagnosis) -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            SelectedItemChip(
                text = item.diagName,
                onRemove = { onRemove(item) }
            )
        }
    }
}

@Composable
fun InvestigationChipGroup(
    items: List<Investigation>,
    onRemove: (Investigation) -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            SelectedItemChip(
                text = item.investigationName,
                onRemove = { onRemove(item) }
            )
        }
    }
}

