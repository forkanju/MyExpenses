package ngo.friendship.mhealth.dc.presentation.screens.main.home.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.presentation.screens.main.home.model.StatRingUi
import ngo.friendship.mhealth.dc.theme.FontSize
import ngo.friendship.mhealth.dc.theme.RingBarBG
import ngo.friendship.mhealth.dc.theme.TextSecondary

@Composable
fun StatRingCard(
    ui: StatRingUi,
    modifier: Modifier = Modifier,
    corner: Dp = 8.dp,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(corner),
        color = Color.White,
        tonalElevation = 0.dp,
        shadowElevation = 2.dp
    ) {
        Column(
            Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RingProgress(
                value = ui.value,
                max = ui.max,
                color = ui.color,
                size = 70.dp,
                stroke = 8.dp
            )
            Spacer(Modifier.height(8.dp))
            Text(ui.label, fontSize = FontSize.SMALL,   color = TextSecondary)
        }
    }
}

@Composable
fun RingProgress(
    value: Int,
    max: Int,
    color: Color,
    size: Dp,
    stroke: Dp,
    modifier: Modifier = Modifier
) {
    val progress = (value.toFloat() / max.toFloat()).coerceIn(0f, 1f)

    Box(modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val strokePx = stroke.toPx()
            // background ring
            drawArc(
                color = RingBarBG,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
            // progress ring
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
        }
        Text(
            text = value.toString(),
            fontSize = FontSize.MEDIUM,
            
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}
