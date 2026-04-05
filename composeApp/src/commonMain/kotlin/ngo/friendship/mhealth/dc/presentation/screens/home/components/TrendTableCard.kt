package ngo.friendship.mhealth.dc.presentation.screens.main.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.presentation.screens.main.home.model.TrendRowUi
import ngo.friendship.mhealth.dc.theme.FontSize
import ngo.friendship.mhealth.dc.theme.TextSecondary

@Composable
fun TrendTableCard(
    header1: String,
    header2: String,
    header3: String,
    rows: List<TrendRowUi>,
    colors: Triple<Color, Color, Color>,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(Modifier.padding(12.dp)) {
            // Header row
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = "", weight = 1.2f, isHeader = true)
                TableCell(text = header1, weight = 1f, isHeader = true)
                TableCell(text = header2, weight = 1f, isHeader = true)
                TableCell(text = header3, weight = 1f, isHeader = true)
            }

            HorizontalDivider(color = TextSecondary)

            rows.forEachIndexed { idx, r ->
                Row(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    TableCell(text = r.name, weight = 1.2f)
                    TableCell(text = r.in30Min.toString(), weight = 1f, valueColor = colors.first)
                    TableCell(
                        text = r.after30Min.toString(),
                        weight = 1f,
                        valueColor = colors.second
                    )
                    TableCell(
                        text = r.after2Hours.toString(),
                        weight = 1f,
                        valueColor = colors.third
                    )
                }
                if (idx != rows.lastIndex)
                    HorizontalDivider()
            }
        }
    }
}

@Composable
private fun RowScope.TableCell(
    text: String,
    weight: Float,
    isHeader: Boolean = false,
    valueColor: Color = TextSecondary
) {
    Text(
        text = text,
        modifier = Modifier.weight(weight),
        fontSize = if (isHeader) FontSize.SMALL else FontSize.REGULAR,
        
        fontWeight = if (isHeader) FontWeight.SemiBold else FontWeight.Normal,
        color = valueColor,
        textAlign = if (weight == 1.2f) TextAlign.Start else TextAlign.Center
    )
}
