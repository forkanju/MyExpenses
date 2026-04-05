package ngo.friendship.mhealth.dc.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
