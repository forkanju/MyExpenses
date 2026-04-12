package ngo.friendship.mhealth.dc.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.theme.BottomBarUnselected
import ngo.friendship.mhealth.dc.theme.DarkerGray
import ngo.friendship.mhealth.dc.theme.FocusedBorderColor
import ngo.friendship.mhealth.dc.theme.Gray
import ngo.friendship.mhealth.dc.theme.UnfocusedBorderColor

@Composable
fun DoseAndDrugDropdownRow(
    leftValue: String,
    leftItems: List<String>,
    onLeftSelect: (String) -> Unit,

    rightValue: String,
    rightItems: List<String>,
    onRightSelect: (String) -> Unit,

    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,

    height: Dp = 32.dp,
    cornerRadius: Dp = 6.dp,
    borderWidth: Dp = 1.dp,

    borderColor: Color = UnfocusedBorderColor,
    focusedBorderColor: Color = FocusedBorderColor,
    errorBorderColor: Color = MaterialTheme.colorScheme.error,
    backgroundColor: Color = Color.White,

    textStyle: TextStyle = LocalTextStyle.current.copy(
        fontSize = 13.sp,
        color = DarkerGray
    ),
    placeholderStyle: TextStyle = LocalTextStyle.current.copy(
        fontSize = 13.sp,
        color = Gray
    ),
    rightPlaceholder: String = "Select"
) {
    var leftExpanded by remember { mutableStateOf(false) }
    var rightExpanded by remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val shape = RoundedCornerShape(cornerRadius)
    val strokeColor = when {
        isError -> errorBorderColor
        isFocused -> focusedBorderColor
        else -> borderColor
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(shape)
            .background(backgroundColor)
            .border(BorderStroke(borderWidth, strokeColor), shape)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ---------- LEFT SMALL DROPDOWN ("Cap") ----------
            Box(
                modifier = Modifier
                    .padding(start = 6.dp)
                    .height(height - 8.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .clickable(enabled = enabled) { leftExpanded = true }

                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = leftValue,
                        style = textStyle.copy(color = DarkerGray),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowDown,
                        contentDescription = null,
                        tint = DarkerGray,
                        modifier = Modifier.size(16.dp)
                    )
                }

                DropdownMenu(
                    expanded = leftExpanded,
                    onDismissRequest = { leftExpanded = false }
                ) {
                    leftItems.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                onLeftSelect(item)
                                leftExpanded = false
                            }
                        )
                    }
                }
            }

            // Divider
            Box(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .fillMaxHeight()
                    .width(width = 1.dp)
                    .background(color = UnfocusedBorderColor)
            )

            // ---------- RIGHT BIG DROPDOWN ----------
            Box(
                modifier = Modifier
                    .weight(weight = 1f)
                    .fillMaxHeight()
                    .clickable(enabled = enabled) { rightExpanded = true }
                    .padding(end = 6.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(weight = 1f)
                            .padding(start = 4.dp)
                    ) {
                        if (rightValue.isBlank()) {
                            Text(
                                text = rightPlaceholder,
                                style = placeholderStyle,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        } else {
                            Text(
                                text = rightValue,
                                style = textStyle,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowDown,
                        contentDescription = null,
                        tint = DarkerGray,
                        modifier = Modifier.size(18.dp)
                    )
                }

                DropdownMenu(
                    expanded = rightExpanded,
                    onDismissRequest = { rightExpanded = false }
                ) {
                    rightItems.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                onRightSelect(item)
                                rightExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DoseAndDrugAutoCompleteRow(
    leftValue: String,
    leftItems: List<String>,
    onLeftSelect: (String) -> Unit,
    rightValue: TextFieldValue,
    onRightValueChange: (TextFieldValue) -> Unit,
    suggestions: List<String>,
    rightPlaceholder: String = "Type generic name",
    onSuggestionSelected: (TextFieldValue) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AppExposedDropdownField(
            modifier = Modifier.weight(0.9f),
            value = leftValue,
            items = leftItems,
            onSelect = onLeftSelect
        )
        GenericNameAutoCompleteTextField(
            modifier = Modifier.weight(1.6f),
            value = rightValue,
            onValueChange = onRightValueChange,
            suggestions = suggestions,
            placeholder = rightPlaceholder,
            onSuggestionSelected = onSuggestionSelected
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppExposedDropdownField(
    modifier: Modifier = Modifier,
    value: String,
    items: List<String>,
    placeholder: String = "Select",
    onSelect: (String) -> Unit,
    enabled: Boolean = true,
    isError: Boolean = false,
    singleLine: Boolean = true,
    height: Dp = 32.dp,
    cornerRadius: Dp = 6.dp,
    borderWidth: Dp = 1.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
    textStyle: TextStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
    placeholderStyle: TextStyle = LocalTextStyle.current.copy(
        fontSize = 13.sp,
        color = BottomBarUnselected
    ),
    borderColor: Color = UnfocusedBorderColor,
    focusedBorderColor: Color = FocusedBorderColor,
    errorBorderColor: Color = MaterialTheme.colorScheme.error,
    backgroundColor: Color = Color.White
) {
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val shape = RoundedCornerShape(cornerRadius)
    val strokeColor = when {
        isError -> errorBorderColor
        isFocused || expanded -> focusedBorderColor
        else -> borderColor
    }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = {
            if (enabled) expanded = !expanded
        }
    ) {
        BasicTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
                .height(height),
            enabled = enabled,
            readOnly = true,
            singleLine = singleLine,
            textStyle = textStyle.copy(
                color = if (enabled) textStyle.color else textStyle.color.copy(alpha = 0.45f)
            ),
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape)
                        .background(backgroundColor)
                        .border(BorderStroke(borderWidth, strokeColor), shape)
                        .padding(contentPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isBlank()) {
                            Text(
                                text = placeholder,
                                style = placeholderStyle,
                                maxLines = 1
                            )
                        }
                        innerTextField()
                    }

                    Icon(
                        imageVector = if (expanded) {
                            Icons.Default.KeyboardArrowUp
                        } else {
                            Icons.Default.KeyboardArrowDown
                        },
                        contentDescription = if (expanded) "Collapse" else "Expand",
                        tint = BottomBarUnselected
                    )
                }
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            style = textStyle
                        )
                    },
                    onClick = {
                        onSelect(item)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun GenericNameAutoCompleteTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    suggestions: List<String>,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    singleLine: Boolean = true,
    height: Dp = 32.dp,
    cornerRadius: Dp = 6.dp,
    borderWidth: Dp = 1.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
    textStyle: TextStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
    placeholderStyle: TextStyle = LocalTextStyle.current.copy(
        fontSize = 13.sp,
        color = BottomBarUnselected
    ),
    borderColor: Color = UnfocusedBorderColor,
    focusedBorderColor: Color = FocusedBorderColor,
    errorBorderColor: Color = MaterialTheme.colorScheme.error,
    backgroundColor: Color = Color.White,
    maxSuggestions: Int = 5,
    onSuggestionSelected: (TextFieldValue) -> Unit = onValueChange
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusManager = LocalFocusManager.current

    var suppressNextExpand by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var selectingSuggestion by remember { mutableStateOf(false) }

    val shape = RoundedCornerShape(cornerRadius)
    val strokeColor = when {
        isError -> errorBorderColor
        isFocused -> focusedBorderColor
        else -> borderColor
    }

    val filteredSuggestions = remember(value, suggestions) {
        if (value.text.isBlank()) {
            emptyList()
        } else {
            suggestions
                .filter { it.contains(value.text, ignoreCase = true) }
                .distinct()
                .take(maxSuggestions)
        }
    }

    LaunchedEffect(value, isFocused, filteredSuggestions) {
        expanded = when {
            suppressNextExpand -> false
            !isFocused -> false
            else -> filteredSuggestions.isNotEmpty()
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        BasicTextField(
            value = value,
            onValueChange = {
                if (selectingSuggestion) {
                    selectingSuggestion = false
                    return@BasicTextField
                }
                suppressNextExpand = false
                onValueChange(it)
                expanded = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            enabled = enabled,
            singleLine = singleLine,
            textStyle = textStyle.copy(
                color = if (enabled) textStyle.color else textStyle.color.copy(alpha = 0.45f)
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape)
                        .background(backgroundColor)
                        .border(BorderStroke(borderWidth, strokeColor), shape)
                        .padding(contentPadding),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.text.isBlank()) {
                        Text(
                            text = placeholder,
                            style = placeholderStyle,
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
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    filteredSuggestions.forEachIndexed { index, item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectingSuggestion = true
                                    suppressNextExpand = true
                                    onSuggestionSelected(
                                        TextFieldValue(
                                            text = item,
                                            selection = TextRange(item.length)
                                        )
                                    )
                                    expanded = false
                                    focusManager.clearFocus()
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = item,
                                fontSize = 13.sp
                            )
                        }

                        if (index != filteredSuggestions.lastIndex) {
                            HorizontalDivider(
                                color = Color(0xFFE7E7E7),
                                thickness = 1.dp
                            )
                        }
                    }
                }
            }
        }
    }
}