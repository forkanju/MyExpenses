package ngo.friendship.mhealth.dc.presentation.screens.case.case_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.theme.FontSize


@Composable
fun AvatarBadge(
    modifier: Modifier = Modifier,
    photo: @Composable BoxScope.() -> Unit,
    idText: String
) {
    Box(modifier = modifier.clip(RoundedCornerShape(8.dp))) {
        photo()

        // Bottom Overlay
        Box(
            modifier = Modifier.fillMaxWidth().height(20.dp).background(Color.Black.copy(0.5f))
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center
        ) {
            Text(
                idText,
                color = Color.White,
                fontSize = FontSize.EXTRA_SMALL,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
