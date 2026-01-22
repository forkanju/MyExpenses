package ngo.friendship.mhealth.dc.presentation.screens.case.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.presentation.theme.FontSize
import ngo.friendship.mhealth.dc.presentation.theme.TrendBlue


@Composable
fun AvatarTileWithBadges(
    modifier: Modifier = Modifier,
    photo: @Composable BoxScope.() -> Unit,
    timeText: String,
    idText: String
) {
    Box(modifier = modifier.clip(RoundedCornerShape(8.dp))) {
        photo()
        // Top Badge
        Surface(
            color = TrendBlue,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .height(14.dp)
        ) {
            Box(
                modifier = Modifier.padding(horizontal = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = timeText,
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        lineHeightStyle = LineHeightStyle(
                            alignment = LineHeightStyle.Alignment.Center,
                            trim = LineHeightStyle.Trim.Both
                        )
                    ),
                    maxLines = 1
                )
            }
        }
        // Bottom Overlay
        Box(
            modifier = Modifier.fillMaxWidth().height(20.dp).background(Color.Black.copy(0.5f))
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center
        ) {
            Text(idText, color = Color.White, fontSize = FontSize.EXTRA_SMALL, fontWeight = FontWeight.Bold)
        }
    }
}
