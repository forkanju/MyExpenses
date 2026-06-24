package ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.*
import androidx.compose.material3.ripple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.*
import ngo.friendship.mhealth.dc.theme.*

@Composable
fun PrescriptionActionRowAligned(
    doseValue: String,
    onDoseChange: (String) -> Unit,

    daysValue: String,
    onDaysChange: (String) -> Unit,

    quantityValue: String,
    onQuantityChange: (String) -> Unit,

    toggleValue: MealTime,
    onToggleChange: (MealTime) -> Unit,

    onMessageClick: () -> Unit,
    onAddClick: () -> Unit,

    modifier: Modifier = Modifier,
    isAnsweredMode: Boolean = false,
    showMealTime: Boolean = true,
    doseSuggestions: List<String> = emptyList(),
    noteValue: String = "",
    onNoteChange: (String) -> Unit = {}
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            UnderlineTextFieldMini(
                value = doseValue,
                onValueChange = onDoseChange,
                width = 80.dp,
                isAnsweredMode = isAnsweredMode,
                placeholder = "Dose",
                suggestions = doseSuggestions
            )

            UnderlineTextFieldMini(
                value = daysValue,
                onValueChange = onDaysChange,
                width = 30.dp,
                isAnsweredMode = isAnsweredMode,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = "Days"
            )

            UnderlineTextFieldMini(
                value = quantityValue,
                onValueChange = onQuantityChange,
                width = 30.dp,
                isAnsweredMode = isAnsweredMode,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = "Qty"
            )

            if (showMealTime) {
                MealTimeToggle(
                    value = toggleValue,
                    onChange = onToggleChange,
                    isAnsweredMode = isAnsweredMode
                )
            }

            AddMini(
                onClick = onAddClick,
                isAnsweredMode = isAnsweredMode
            )
        }

        Spacer(Modifier.height(8.dp))
        UnderlineTextFieldMini(
            value = noteValue,
            onValueChange = onNoteChange,
            width = 0.dp, // Take full width
            modifier = Modifier.fillMaxWidth(),
            isAnsweredMode = isAnsweredMode,
            placeholder = "Advice for this medicine (for FCM view)..."
        )
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
fun MealTimeToggle(
    value: MealTime,
    onChange: (MealTime) -> Unit,
    modifier: Modifier = Modifier,
    isAnsweredMode: Boolean = false
) {
    val bgColor = if (isAnsweredMode) Color(0xFFF7F7F7) else Color.White
    val borderColor = if (isAnsweredMode) Color(0xFFC7C7C7) else UnfocusedBorderColor
    val textColor = if (isAnsweredMode) Color(0xFF4F4F4F) else TextPrimary
    val iconColor = if (isAnsweredMode) Color(0xFF6A6A6A) else Color(0xFF6B7280)

    Surface(
        modifier = modifier
            .height(28.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple()
            ) {
                onChange(
                    if (value == MealTime.BEFORE) MealTime.AFTER else MealTime.BEFORE
                )
            },
        shape = RoundedCornerShape(14.dp),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Restaurant,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(14.dp)
            )

            Spacer(Modifier.width(6.dp))

            Text(
                text = if (value == MealTime.BEFORE) "Before" else "After",
                fontSize = 12.sp,
                color = textColor,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun AddMini(
    onClick: () -> Unit,
    isAnsweredMode: Boolean = false
) {
    val bgColor = if (isAnsweredMode) Color(0xFF9E9E9E) else Color(0xFF1D4ED8)

    Column(
        modifier = Modifier.width(46.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(bgColor)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = "Add",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnderlineTextFieldMini(
    value: String,
    onValueChange: (String) -> Unit,
    width: Dp,
    modifier: Modifier = Modifier,
    isAnsweredMode: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    placeholder: String = "",
    suggestions: List<String> = emptyList()
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val textColor = if (isAnsweredMode) Color(0xFF4F4F4F) else Gray
    val dividerColor = when {
        isAnsweredMode -> Color(0xFFC7C7C7)
        isFocused -> FocusedBorderColor
        else -> Gray
    }
    val dividerHeight = if (isFocused) 1.5.dp else 1.dp

    val finalModifier = if (modifier == Modifier) Modifier.width(width)
    else modifier

    var expanded by remember { mutableStateOf(false) }
    val filteredSuggestions = remember(value, suggestions) {
        if (value.isEmpty()) emptyList()
        else suggestions.filter { it.contains(value, ignoreCase = true) && it != value }
    }

    LaunchedEffect(filteredSuggestions) {
        expanded = filteredSuggestions.isNotEmpty() && !isAnsweredMode
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (!isAnsweredMode) expanded = it },
        modifier = finalModifier
    ) {
        Column(modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, true)) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = textColor, fontSize = 13.sp),
                singleLine = true,
                readOnly = isAnsweredMode,
                keyboardOptions = keyboardOptions,
                interactionSource = interactionSource,
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (value.isEmpty()) {
                            Text(placeholder, color = Gray.copy(alpha = 0.5f), fontSize = 13.sp)
                        }
                        innerTextField()
                    }
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dividerHeight)
                    .background(dividerColor)
            )
        }

        if (filteredSuggestions.isNotEmpty() && !isAnsweredMode) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(IntrinsicSize.Max).background(Color.White)
            ) {
                filteredSuggestions.forEach { suggestion ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = suggestion,
                                fontSize = 12.sp,
                                color = TextPrimary
                            )
                        },
                        onClick = {
                            onValueChange(suggestion)
                            expanded = false
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.heightIn(min = 32.dp)
                    )
                }
            }
        }
    }
}
