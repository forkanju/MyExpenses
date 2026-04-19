package ngo.friendship.mhealth.dc.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.*
import ngo.friendship.mhealth.dc.theme.*

/* ------------------------------------------------------ */
/* 1. MAIN ROW (Dose + Generic Name) */
/* ------------------------------------------------------ */

@Composable
fun DoseAndDrugAutoCompleteRow(
    leftValue: String,
    leftItems: List<String>,
    onLeftSelect: (String) -> Unit,
    rightValue: TextFieldValue,
    onRightValueChange: (TextFieldValue) -> Unit,
    suggestions: List<String>,
    rightPlaceholder: String = "Type generic name",
    onSuggestionSelected: (TextFieldValue) -> Unit,
    isAnsweredMode: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AppExposedDropdownField(
            modifier = Modifier.weight(0.9f),
            value = leftValue,
            items = leftItems,
            onSelect = onLeftSelect,
            isAnsweredMode = isAnsweredMode
        )

        GenericNameAutoCompleteTextField(
            modifier = Modifier.weight(1.6f),
            value = rightValue,
            onValueChange = onRightValueChange,
            suggestions = suggestions,
            placeholder = rightPlaceholder,
//            onSuggestionSelected = onSuggestionSelected,
            isAnsweredMode = isAnsweredMode
        )
    }
}

/* ------------------------------------------------------ */
/* 2. LEFT SMALL DROPDOWN */
/* ------------------------------------------------------ */

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
    isAnsweredMode: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor = when {
        isError -> MaterialTheme.colorScheme.error
        isAnsweredMode -> Color(0xFFC7C7C7)
        isFocused -> FocusedBorderColor
        else -> UnfocusedBorderColor
    }

    val bgColor = if (isAnsweredMode) Color(0xFFF7F7F7) else Color.White
    val textColor = if (isAnsweredMode) Color(0xFF4F4F4F) else DarkerGray
    val iconColor = if (isAnsweredMode) Color(0xFF6A6A6A) else BottomBarUnselected

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = !expanded }
    ) {
        BasicTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
                .height(32.dp),
            interactionSource = interactionSource,
            textStyle = TextStyle(fontSize = 13.sp, color = textColor),
            decorationBox = {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(6.dp))
                        .background(bgColor)
                        .border(BorderStroke(1.dp, borderColor), RoundedCornerShape(6.dp))
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (value.isBlank()) placeholder else value,
                        color = textColor,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = iconColor
                    )
                }
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(bgColor)
        ) {
            items.forEach {
                DropdownMenuItem(
                    text = { Text(it, color = textColor) },
                    onClick = {
                        onSelect(it)
                        expanded = false
                    }
                )
            }
        }
    }
}

/* ------------------------------------------------------ */
/* 3. GENERIC NAME FIELD */
/* ------------------------------------------------------ */

@Composable
fun GenericNameAutoCompleteTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    suggestions: List<String>,
    placeholder: String,
    modifier: Modifier = Modifier,
    isAnsweredMode: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusManager = LocalFocusManager.current

    var expanded by remember { mutableStateOf(false) }

    val borderColor = when {
        isAnsweredMode -> Color(0xFFC7C7C7)
        isFocused -> FocusedBorderColor
        else -> UnfocusedBorderColor
    }

    val bgColor = if (isAnsweredMode) Color(0xFFF7F7F7) else Color.White
    val textColor = if (isAnsweredMode) Color(0xFF4F4F4F) else DarkerGray

    val filtered = suggestions.filter {
        it.contains(value.text, true)
    }.take(5)

    Column(modifier = modifier.fillMaxWidth()) {
        BasicTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                expanded = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp),
            textStyle = TextStyle(color = textColor, fontSize = 13.sp),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            interactionSource = interactionSource,
            decorationBox = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(6.dp))
                        .background(bgColor)
                        .border(BorderStroke(1.dp, borderColor), RoundedCornerShape(6.dp))
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.text.isEmpty()) {
                        Text(placeholder, color = Gray)
                    }
                    it()
                }
            }
        )

        AnimatedVisibility(expanded && filtered.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                colors = CardDefaults.cardColors(containerColor = bgColor)
            ) {
                Column {
                    filtered.forEachIndexed { index, item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onValueChange(
                                        TextFieldValue(item, TextRange(item.length))
                                    )
                                    expanded = false
                                    focusManager.clearFocus()
                                }
                                .padding(12.dp)
                        ) {
                            Text(item, color = textColor)
                        }

                        if (index != filtered.lastIndex) {
                            HorizontalDivider(color = Color(0xFFDADADA))
                        }
                    }
                }
            }
        }
    }
}