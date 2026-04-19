package ngo.friendship.mhealth.dc.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.theme.DarkerGray
import ngo.friendship.mhealth.dc.theme.FocusedBorderColor
import ngo.friendship.mhealth.dc.theme.Gray
import ngo.friendship.mhealth.dc.theme.GrayLighter
import ngo.friendship.mhealth.dc.theme.RobotoCondensedFont
import ngo.friendship.mhealth.dc.theme.TextDarkerGray
import ngo.friendship.mhealth.dc.theme.UnfocusedBorderColor


@Composable
fun <T> FormDropdownField(
    label: String? = null,
    placeholder: String,
    options: List<T>,
    selected: T? = null,
    getLabel: (T) -> String,
    onSelectedChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null,
    isAnsweredMode: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    val labelColor = if (isAnsweredMode) Color(0xFF666666) else TextDarkerGray
    val borderColor = when {
        isError -> MaterialTheme.colorScheme.error
        isAnsweredMode -> Color(0xFFC7C7C7)
        else -> UnfocusedBorderColor
    }
    val fieldBackground = if (isAnsweredMode) Color(0xFFF7F7F7) else Color.Transparent
    val dropdownBackground = if (isAnsweredMode) Color(0xFFF7F7F7) else Color.White
    val textColorSelected = if (isAnsweredMode) Color(0xFF4F4F4F) else DarkerGray
    val iconColor = if (isAnsweredMode) Color(0xFF6A6A6A) else DarkerGray

    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null) {
            Text(
                text = label,
                style = TextStyle(
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    fontFamily = RobotoCondensedFont(),
                    color = labelColor
                )
            )
            Spacer(Modifier.height(6.dp))
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .clickable(enabled = enabled) { expanded = true },
                shape = androidx.compose.foundation.shape.RoundedCornerShape(6.dp),
                color = fieldBackground,
                border = BorderStroke(1.dp, borderColor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val showText = selected?.let(getLabel)?.takeIf { it.isNotBlank() }

                    Text(
                        text = showText ?: placeholder,
                        modifier = Modifier.weight(1f),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = RobotoCondensedFont(),
                            fontWeight = FontWeight.Normal
                        ),
                        color = if (showText == null) Gray else textColorSelected,
                        fontStyle = if (showText == null) FontStyle.Italic else FontStyle.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowDown,
                        contentDescription = "Open",
                        tint = iconColor
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                offset = DpOffset(0.dp, 4.dp),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .widthIn(min = 150.dp, max = 250.dp)
                    .heightIn(max = 200.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                tonalElevation = 2.dp,
                shadowElevation = 8.dp,
                containerColor = dropdownBackground,
                border = BorderStroke(1.dp, GrayLighter)
            ) {
                options.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = getLabel(item),
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = RobotoCondensedFont(),
                                    fontWeight = FontWeight.Normal
                                ),
                                color = textColorSelected
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
@Composable
fun <T> FormAutoCompleteDropdownField(
    label: String? = null,
    placeholder: String,
    options: List<T>,
    selected: T? = null,
    getLabel: (T) -> String,
    onSelectedChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null,
    maxSuggestions: Int = 5,
    height: Dp = 44.dp,
    cornerRadius: Dp = 6.dp,
    borderWidth: Dp = 1.dp,
    isAnsweredMode: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusManager = LocalFocusManager.current

    var value by remember(selected) {
        mutableStateOf(
            TextFieldValue(
                text = selected?.let(getLabel).orEmpty(),
                selection = TextRange(selected?.let(getLabel)?.length ?: 0)
            )
        )
    }

    var expanded by remember { mutableStateOf(false) }
    var suppressNextExpand by remember { mutableStateOf(false) }
    var selectingSuggestion by remember { mutableStateOf(false) }

    val filteredOptions = remember(value.text, options) {
        if (value.text.isBlank()) {
            emptyList()
        } else {
            options
                .filter { getLabel(it).contains(value.text, ignoreCase = true) }
                .distinctBy { getLabel(it) }
                .take(maxSuggestions)
        }
    }

    LaunchedEffect(value.text, isFocused, filteredOptions, suppressNextExpand) {
        expanded = when {
            suppressNextExpand -> false
            !isFocused -> false
            else -> filteredOptions.isNotEmpty()
        }
    }

    val shape = androidx.compose.foundation.shape.RoundedCornerShape(cornerRadius)

    val labelColor = if (isAnsweredMode) Color(0xFF666666) else TextDarkerGray
    val fieldBackground = if (isAnsweredMode) Color(0xFFF7F7F7) else Color.White
    val textColor = if (enabled) {
        if (isAnsweredMode) Color(0xFF4F4F4F) else DarkerGray
    } else {
        DarkerGray.copy(alpha = 0.45f)
    }

    val strokeColor = when {
        isError -> MaterialTheme.colorScheme.error
        isFocused && isAnsweredMode -> Color(0xFFA8A8A8)
        isFocused -> FocusedBorderColor
        isAnsweredMode -> Color(0xFFC7C7C7)
        else -> UnfocusedBorderColor
    }

    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null) {
            Text(
                text = label,
                style = TextStyle(
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    fontFamily = RobotoCondensedFont(),
                    color = labelColor
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        BasicTextField(
            value = value,
            onValueChange = {
                if (selectingSuggestion) {
                    selectingSuggestion = false
                    return@BasicTextField
                }

                suppressNextExpand = false
                value = it
                expanded = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            enabled = enabled,
            singleLine = true,
            interactionSource = interactionSource,
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontFamily = RobotoCondensedFont(),
                color = textColor
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape)
                        .background(fieldBackground)
                        .border(
                            border = BorderStroke(borderWidth, strokeColor),
                            shape = shape
                        )
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.text.isBlank()) {
                        Text(
                            text = placeholder,
                            color = Gray,
                            fontStyle = FontStyle.Italic,
                            fontSize = 14.sp,
                            fontFamily = RobotoCondensedFont(),
                            maxLines = 1
                        )
                    }
                    innerTextField()
                }
            }
        )

        AnimatedVisibility(visible = expanded) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = fieldBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    filteredOptions.forEachIndexed { index, item ->
                        val itemLabel = getLabel(item)

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectingSuggestion = true
                                    suppressNextExpand = true

                                    value = TextFieldValue(
                                        text = itemLabel,
                                        selection = TextRange(itemLabel.length)
                                    )

                                    onSelectedChange(item)
                                    expanded = false
                                    focusManager.clearFocus()
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = itemLabel,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = RobotoCondensedFont(),
                                    fontWeight = FontWeight.Normal,
                                    color = textColor
                                )
                            )
                        }

                        if (index != filteredOptions.lastIndex) {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = GrayLighter
                            )
                        }
                    }
                }
            }
        }

        if (supportingText != null) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = supportingText,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = RobotoCondensedFont()
                ),
                color = if (isError) MaterialTheme.colorScheme.error else Gray
            )
        }
    }
}