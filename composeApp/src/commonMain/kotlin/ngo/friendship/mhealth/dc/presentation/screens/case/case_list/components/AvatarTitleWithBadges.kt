package ngo.friendship.mhealth.dc.presentation.screens.case.case_list.components

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
import ngo.friendship.mhealth.dc.theme.FontSize
import ngo.friendship.mhealth.dc.theme.TrendBlue

@Composable
fun AvatarTileWithBadges(
    modifier: Modifier = Modifier,
    photo: @Composable BoxScope.() -> Unit,
    timeText: String,
    idText: String,
    isAnsweredStyle: Boolean = false
) {
    val topBadgeColor = if (isAnsweredStyle) Color.DarkGray else TrendBlue
    val bottomOverlayColor = if (isAnsweredStyle) Color.Black.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.5f)

    Box(modifier = modifier.clip(RoundedCornerShape(8.dp))) {
        photo()

        Surface(
            color = topBadgeColor,
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(bottomOverlayColor)
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = idText,
                color = Color.White,
                fontSize = FontSize.EXTRA_SMALL,
                fontWeight = FontWeight.Bold
            )
        }
    }
}