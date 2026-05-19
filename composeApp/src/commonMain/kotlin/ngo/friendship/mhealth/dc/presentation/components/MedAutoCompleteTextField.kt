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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.*
import ngo.friendship.mhealth.dc.theme.*

@Composable
fun MedAutoCompleteTextField(
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
        fontSize = 11.sp,
        color = BottomBarUnselected
    ),
    borderColor: Color = UnfocusedBorderColor,
    focusedBorderColor: Color = FocusedBorderColor,
    errorBorderColor: Color = MaterialTheme.colorScheme.error,
    backgroundColor: Color = Color.White,
    maxSuggestions: Int = 5,
    onSuggestionSelected: (TextFieldValue) -> Unit = onValueChange,
    isAnsweredMode: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var suppressNextExpand by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var selectingSuggestion by remember { mutableStateOf(false) }

    val shape = RoundedCornerShape(cornerRadius)

    val strokeColor = when {
        isError -> errorBorderColor
        isAnsweredMode -> Color(0xFFC7C7C7)
        isFocused -> focusedBorderColor
        else -> borderColor
    }

    val bgColor = if (isAnsweredMode) Color(0xFFF7F7F7) else backgroundColor
    val textColor = if (isAnsweredMode) Color(0xFF4F4F4F) else textStyle.color

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
                color = if (enabled) textColor else textColor.copy(alpha = 0.45f)
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape)
                        .background(bgColor)
                        .border(BorderStroke(borderWidth, strokeColor), shape)
                        .padding(contentPadding),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.text.isBlank()) {
                        Text(
                            text = placeholder,
                            style = placeholderStyle.copy(
                                color = if (isAnsweredMode) Color(0xFF8A8A8A) else placeholderStyle.color
                            ),
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
                colors = CardDefaults.cardColors(
                    containerColor = if (isAnsweredMode) Color(0xFFF7F7F7) else Color.White
                ),
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
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = item,
                                fontSize = 13.sp,
                                color = textColor
                            )
                        }

                        if (index != filteredSuggestions.lastIndex) {
                            HorizontalDivider(
                                color = if (isAnsweredMode) Color(0xFFDADADA) else Color(0xFFE7E7E7),
                                thickness = 1.dp
                            )
                        }
                    }
                }
            }
        }
    }
}