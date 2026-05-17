package ngo.friendship.mhealth.dc.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.theme.PrimaryColor
import ngo.friendship.mhealth.dc.theme.RobotoCondensedFont

@Composable
fun FormActionRow(
    leftText: String,
    leftIcon: @Composable (() -> Unit)? = null,
    rightText: String,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    modifier: Modifier = Modifier,
    isAnsweredMode: Boolean = false
) {
    val actionColor = if (isAnsweredMode) Color(0xFF7A7A7A) else PrimaryColor

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(enabled = !isAnsweredMode, onClick = onLeftClick)
        ) {
            Text(
                text = leftText,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                fontFamily = RobotoCondensedFont(),
                color = actionColor,
                textDecoration = TextDecoration.Underline
            )
            if (leftIcon != null) {
                Spacer(Modifier.width(4.dp))
                leftIcon()
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(enabled = !isAnsweredMode, onClick = onRightClick)
        ) {
            Text(
                text = rightText,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                fontFamily = RobotoCondensedFont(),
                color = actionColor,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}