package ngo.friendship.mhealth.dc.presentation.screens.main.case.components.case_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.theme.BottomBarUnselected
import ngo.friendship.mhealth.dc.theme.FocusedBorderColor
import ngo.friendship.mhealth.dc.theme.UnfocusedBorderColor

@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    singleLine: Boolean = true,
    height: Dp = 32.dp,                 // 👈 compact like screenshot
    cornerRadius: Dp = 6.dp,
    borderWidth: Dp = 1.dp,             // 👈 thin stroke
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
    textStyle: TextStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
    placeholderStyle: TextStyle = LocalTextStyle.current.copy(
        fontSize = 13.sp,
        color = BottomBarUnselected
    ),
    borderColor: Color = UnfocusedBorderColor,         // light gray
    focusedBorderColor: Color = FocusedBorderColor,  // slightly darker on focus
    errorBorderColor: Color = MaterialTheme.colorScheme.error,
    backgroundColor: Color = Color.White
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val shape = RoundedCornerShape(cornerRadius)
    val strokeColor = when {
        isError -> errorBorderColor
        isFocused -> focusedBorderColor
        else -> borderColor
    }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        enabled = enabled,
        singleLine = singleLine,
        textStyle = textStyle.copy(
            color = if (enabled) textStyle.color else textStyle.color.copy(alpha = 0.45f)
        ),
        cursorBrush = SolidColor(value = MaterialTheme.colorScheme.primary),
        interactionSource = interactionSource,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape)
                    .background(backgroundColor)
                    .border(BorderStroke(borderWidth, strokeColor), shape)
                    .padding(paddingValues = contentPadding),
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
        }
    )
}
