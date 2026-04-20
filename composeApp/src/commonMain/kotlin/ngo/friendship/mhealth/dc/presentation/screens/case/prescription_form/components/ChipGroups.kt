package ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.domain.model.Diagnosis
import ngo.friendship.mhealth.dc.domain.model.Investigation

@Composable
fun SelectedItemChip(
    text: String,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
    isAnsweredMode: Boolean = false
) {
    val colorScheme = MaterialTheme.colorScheme

    // Using semantic Material3 tokens
    val chipBg = if (isAnsweredMode) colorScheme.surfaceVariant else colorScheme.background
    val chipBorder = if (isAnsweredMode) colorScheme.outlineVariant else colorScheme.outline
    val chipText = if (isAnsweredMode) colorScheme.onSurfaceVariant else colorScheme.onSurface

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = chipBg,
        border = BorderStroke(1.dp, chipBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text, color = chipText)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "✕",
                color = chipText,
                modifier = Modifier.clickable(enabled = !isAnsweredMode) { onRemove() }
            )
        }
    }
}

@Composable
fun DiagnosisChipGroup(
    items: List<Diagnosis>,
    onRemove: (Diagnosis) -> Unit,
    isAnsweredMode: Boolean = false
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            SelectedItemChip(
                text = item.diagName,
                onRemove = { onRemove(item) },
                isAnsweredMode = isAnsweredMode
            )
        }
    }
}

@Composable
fun InvestigationChipGroup(
    items: List<Investigation>,
    onRemove: (Investigation) -> Unit,
    isAnsweredMode: Boolean = false
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            SelectedItemChip(
                text = item.investigationName,
                onRemove = { onRemove(item) },
                isAnsweredMode = isAnsweredMode
            )
        }
    }
}