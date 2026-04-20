package ngo.friendship.mhealth.dc.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.theme.Gray
import ngo.friendship.mhealth.dc.theme.PrimaryColor
import ngo.friendship.mhealth.dc.theme.Resources
import org.jetbrains.compose.resources.painterResource

@Composable
fun CheckboxWithEditableText(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isAnsweredMode: Boolean = false
) {

    val checkedColor = if (isAnsweredMode) Color(0xFF7A7A7A) else PrimaryColor
    val uncheckedColor = if (isAnsweredMode) Color(0xFFB0B0B0) else Gray
    val textColor = if (isAnsweredMode) Color(0xFF6A6A6A) else PrimaryColor
    val iconColor = if (isAnsweredMode) Color(0xFF7A7A7A) else PrimaryColor

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
    ) {

        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = CheckboxDefaults.colors(
                checkedColor = checkedColor,
                uncheckedColor = uncheckedColor,
                checkmarkColor = Color.White
            ),
            modifier = Modifier.size(20.dp)
        )

        Spacer(Modifier.width(6.dp))

        Text(
            text = text,
            fontSize = 14.sp,
            color = textColor,
            textDecoration = TextDecoration.Underline
        )

        Spacer(Modifier.width(6.dp))

        Icon(
            painter = painterResource(Resources.Icon.Edit),
            contentDescription = "Edit",
            tint = iconColor,
            modifier = Modifier
                .size(22.dp)
                .clickable(enabled = !isAnsweredMode, onClick = onEditClick)
        )
    }
}