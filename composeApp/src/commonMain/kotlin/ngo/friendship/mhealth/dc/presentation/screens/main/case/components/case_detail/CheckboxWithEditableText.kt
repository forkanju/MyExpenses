package ngo.friendship.mhealth.dc.presentation.screens.main.case.components.case_detail

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
    enabled: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        // Checkbox
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = CheckboxDefaults.colors(
                checkedColor = PrimaryColor,
                uncheckedColor = Gray
            ),
            modifier = Modifier.size(20.dp)
        )

        Spacer(Modifier.width(width = 6.dp))

        // Text with underline
        Text(
            text = text,
            fontSize = 14.sp,
            color = PrimaryColor,
            textDecoration = TextDecoration.Underline
        )

        Spacer(Modifier.width(6.dp))

        // Edit icon
        Icon(
            painter = painterResource(Resources.Icon.Edit),
            contentDescription = "Edit",
            tint = PrimaryColor,
            modifier = Modifier
                .size(22.dp)
                .clickable(onClick = onEditClick)
        )
    }
}
