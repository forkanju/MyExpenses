package ngo.friendship.mhealth.dc.presentation.screens.case


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
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.SuggestionChip
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.theme.BottomBarUnselected
import ngo.friendship.mhealth.dc.theme.FocusedBorderColor
import ngo.friendship.mhealth.dc.theme.UnfocusedBorderColor

@Composable
fun HistoryTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    history: List<String>,
    onHistorySelected: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
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
    showHistoryChips: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusManager = LocalFocusManager.current

    var expanded by remember { mutableStateOf(false) }
    var suppressNextExpand by remember { mutableStateOf(false) }
    var selectingSuggestion by remember { mutableStateOf(false) }

    val shape = RoundedCornerShape(cornerRadius)
    val strokeColor = when {
        isError -> errorBorderColor
        isFocused -> focusedBorderColor
        else -> borderColor
    }

    val filteredSuggestions = remember(value.text, history) {
        val query = value.text.trim()
        if (query.isBlank()) {
            history.take(maxSuggestions)
        } else {
            history
                .filter { it.contains(query, ignoreCase = true) }
                .distinct()
                .take(maxSuggestions)
        }
    }

    LaunchedEffect(value.text, isFocused, filteredSuggestions) {
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
                Column {
                    filteredSuggestions.forEachIndexed { index, item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectingSuggestion = true
                                    suppressNextExpand = true
                                    val selected = TextFieldValue(
                                        text = item,
                                        selection = TextRange(item.length)
                                    )
                                    onHistorySelected(selected)
                                    expanded = false
                                    focusManager.clearFocus()
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = item,
                                style = textStyle
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

        if (showHistoryChips && history.isNotEmpty()) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                history.take(8).forEach { item ->
                    SuggestionChip(
                        onClick = {
                            onHistorySelected(
                                TextFieldValue(
                                    text = item,
                                    selection = TextRange(item.length)
                                )
                            )
                        },
                        label = {
                            Text(
                                text = item,
                                maxLines = 1
                            )
                        }
                    )
                }
            }
        }
    }
}