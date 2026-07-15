package ngo.friendship.mhealth.dc.presentation.screen.home.components

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.presentation.screens.home.model.KeyValueUi
import ngo.friendship.mhealth.dc.theme.FontSize
import ngo.friendship.mhealth.dc.theme.TextSecondary

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

                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
            )
            Spacer(Modifier.height(10.dp))

            if (items.isEmpty()) {
                Text(
                    "No data found",
                    fontSize = FontSize.SMALL,
                    color = TextSecondary,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            } else {
                items.forEachIndexed { idx, kv ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            kv.key,
                            fontSize = FontSize.SMALL,
                            color = TextSecondary,
                            modifier = Modifier.weight(0.8f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            kv.value.toString(),
                            fontSize = FontSize.SMALL,
                            color = TextSecondary,
                            modifier = Modifier.weight(0.2f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.End
                        )
                    }
                    if (idx != items.lastIndex) Spacer(Modifier.height(3.dp))
                }
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
