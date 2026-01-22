package ngo.friendship.mhealth.dc.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.presentation.screens.home.model.SegmentUi
import ngo.friendship.mhealth.dc.theme.FontSize


@Composable
fun SegmentedBarCard(
    segments: List<SegmentUi>,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(Modifier.padding(horizontal = 14.dp, vertical = 14.dp)) {

            val total = segments.sumOf { it.percent.toDouble() }
                .toFloat()
                .coerceAtLeast(1f)

            // The whole bar is clipped, so only the ends are rounded (like the screenshot)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                segments.forEach { s ->
                    val w = (s.percent / total).coerceAtLeast(0.0001f)

                    Box(
                        modifier = Modifier
                            .weight(w)
                            .fillMaxHeight()
                            .background(s.color),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${s.percent.toInt()}%",
                            color = Color.White,
                            fontSize = FontSize.SMALL,
                            
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}


