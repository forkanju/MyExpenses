package ngo.friendship.mhealth.dc.presentation.screens.case.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.presentation.components.CompactTextStyle
import ngo.friendship.mhealth.dc.theme.FontSize

@Composable
fun TopTabsRow(
    tabs: List<Pair<CaseTab, Int>>,
    selected: CaseTab,
    onSelect: (CaseTab) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 8.dp)
            .padding(top = 12.dp, bottom = 4.dp), // Reduced vertical padding
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tabs.forEach { (tab, count) ->
            val isSelected = tab == selected
            Text(
                text = "${tab.label} (${count.toString().padStart(2, '0')})",
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onSelect(tab) }
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                style = CompactTextStyle(
                    fontSize = FontSize.SMALL,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isSelected) tab.color else Color(0xFF666666)
                )
            )
        }
    }
}