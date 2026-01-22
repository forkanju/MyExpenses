package ngo.friendship.mhealth.dc.presentation.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.presentation.screens.home.model.KeyValueUi
import ngo.friendship.mhealth.dc.presentation.theme.FontSize
import ngo.friendship.mhealth.dc.presentation.theme.RobotoCondensedFont
import ngo.friendship.mhealth.dc.presentation.theme.TextSecondary

@Composable
fun KeyValueListCard(
    title: String,
    items: List<KeyValueUi>,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(
                title,
                fontSize = FontSize.REGULAR,
                fontFamily = RobotoCondensedFont(),
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
            )
            Spacer(Modifier.height(10.dp))

            items.forEachIndexed { idx, kv ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(kv.key, fontSize = FontSize.SMALL, fontFamily = RobotoCondensedFont(), color = TextSecondary)
                    Text(kv.value.toString(), fontSize = FontSize.SMALL, fontFamily = RobotoCondensedFont(), color = TextSecondary)
                }
                if (idx != items.lastIndex) Spacer(Modifier.height(3.dp))
            }
        }
    }
}

@Composable
fun TwoCardsRow(
    left: @Composable (Modifier) -> Unit,
    right: @Composable (Modifier) -> Unit
) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        left(Modifier.weight(1f))
        right(Modifier.weight(1f))
    }
}
