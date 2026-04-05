package ngo.friendship.mhealth.dc.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left action (e.g., "Next follow-up:")
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = onLeftClick)
        ) {
            Text(
                text = leftText,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                fontFamily = RobotoCondensedFont(),
                color = PrimaryColor,
                textDecoration = TextDecoration.Underline
            )
            if (leftIcon != null) {
                Spacer(Modifier.width(4.dp))
                leftIcon()
            }
        }

        // Right action (e.g., "Save as a template")
        TextButton(onClick = onRightClick) {
            Text(
                text = rightText,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                fontFamily = RobotoCondensedFont(),
                color = PrimaryColor,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}


