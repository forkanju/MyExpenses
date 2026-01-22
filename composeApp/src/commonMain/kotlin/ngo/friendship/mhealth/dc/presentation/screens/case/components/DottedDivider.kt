package ngo.friendship.mhealth.dc.presentation.screens.case.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp


@Composable
fun DottedDivider() {
    Canvas(Modifier.fillMaxWidth().height(1.dp)) {
        val pathEffect =
            PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        drawLine(
            color = Color(0xFFE2E2E2),
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect,
            strokeWidth = 2f
        )
    }
}

