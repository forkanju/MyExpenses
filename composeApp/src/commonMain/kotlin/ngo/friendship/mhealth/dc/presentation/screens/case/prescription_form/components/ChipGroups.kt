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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    val chipBg = if (isAnsweredMode) Color(0xFFF0F0F0) else Color(0xFFF1F5F9)
    val chipBorder = if (isAnsweredMode) Color(0xFFC8C8C8) else Color(0xFFCBD5E1)
    val chipText = if (isAnsweredMode) Color(0xFF555555) else Color.Black

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