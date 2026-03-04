package ngo.friendship.mhealth.dc.presentation.screens.main.case.components.case_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.theme.DarkerGray
import ngo.friendship.mhealth.dc.theme.Gray
import ngo.friendship.mhealth.dc.theme.GrayLighter
import ngo.friendship.mhealth.dc.theme.RobotoCondensedFont
import ngo.friendship.mhealth.dc.theme.TextDarkerGray
import ngo.friendship.mhealth.dc.theme.UnfocusedBorderColor

@Composable
fun FormDropdownField(
    label: String? = null,
    placeholder: String,
    options: List<String>,
    selected: String? = "",
    onSelectedChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        // Top label
        if (label != null) {
            Text(
                text = label,
                style = TextStyle(
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    fontFamily = RobotoCondensedFont(),
                    color = TextDarkerGray
                )
            )
            Spacer(Modifier.height(6.dp))
        }

        // Dropdown container
        Box(modifier = Modifier.fillMaxWidth()) {
            // Outlined field container
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .clickable(enabled = enabled) { expanded = true },
                shape = RoundedCornerShape(6.dp),
                color = Color.Transparent,
                border = BorderStroke(
                    1.dp,
                    when {
                        isError -> MaterialTheme.colorScheme.error
                        else -> UnfocusedBorderColor
                    }
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val showText = selected?.takeIf { it.isNotBlank() }
                    Text(
                        text = showText ?: placeholder,
                        modifier = Modifier.weight(1f),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = RobotoCondensedFont(),
                            fontWeight = FontWeight.Normal
                        ),
                        color = if (showText == null) Gray else DarkerGray,
                        fontStyle = if (showText == null) FontStyle.Italic else FontStyle.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowDown,
                        contentDescription = "Open",
                        tint = DarkerGray
                    )
                }
            }

            // Dropdown menu positioned to the right
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                offset = DpOffset(x = 0.dp, y = 4.dp), // ✅ Offset: 0 horizontal, 4dp below
                modifier = Modifier
                    .align(Alignment.TopEnd) // ✅ Align to top-right
                    .widthIn(min = 150.dp, max = 250.dp)
                    .heightIn(max = 200.dp),
                shape = RoundedCornerShape(8.dp),
                tonalElevation = 2.dp,
                shadowElevation = 8.dp,
                containerColor = Color.White,
                border = BorderStroke(1.dp, GrayLighter)
            ) {
                options.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = item,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = RobotoCondensedFont(),
                                    fontWeight = FontWeight.Normal
                                ),
                                color = DarkerGray
                            )
                        },
                        onClick = {
                            expanded = false
                            onSelectedChange(item)
                        },
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                    )
                    if (item != options.last()) {
                        HorizontalDivider(
                            thickness = 0.5.dp,
                            color = GrayLighter
                        )
                    }
                }
            }
        }

        // Supporting text
        if (supportingText != null) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = supportingText,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = RobotoCondensedFont(),
                    fontWeight = FontWeight.Normal
                ),
                color = if (isError) MaterialTheme.colorScheme.error else Gray
            )
        }
    }
}