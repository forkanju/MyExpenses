package ngo.friendship.mhealth.dc.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.theme.DarkerGray
import ngo.friendship.mhealth.dc.theme.FontSize
import ngo.friendship.mhealth.dc.theme.Gray
import ngo.friendship.mhealth.dc.theme.RobotoCondensedFont
import ngo.friendship.mhealth.dc.theme.TextPrimary

@Composable
fun LabeledFormTextField(
    label: String? = null,
    placeholder: String,
    value: String = "",
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    height: Int = 44,
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
    keyboardActions: KeyboardActions? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    isAnsweredMode: Boolean = false
) {
    val focusManager = LocalFocusManager.current
    val effectiveKeyboardActions = keyboardActions ?: KeyboardActions(
        onNext = {
            focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down)
        }
    )
    val labelColor = if (isAnsweredMode) Color(0xFF666666) else TextPrimary
    val fieldTextColor = if (isAnsweredMode) Color(0xFF4F4F4F) else DarkerGray
    val borderColor = when {
        isError -> MaterialTheme.colorScheme.error
        isAnsweredMode -> Color(0xFFC7C7C7)
        else -> Color(0xFFCBD5E1)
    }
    val fieldBackground = if (isAnsweredMode) Color(0xFFF7F7F7) else Color.Transparent

    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null) {
            Text(
                text = label,
                style = TextStyle(
                    fontStyle = FontStyle.Italic,
                    fontSize = FontSize.REGULAR,
                    fontFamily = RobotoCondensedFont(),
                    color = labelColor
                )
            )
            Spacer(Modifier.height(6.dp))
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(height.dp),
            enabled = enabled,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = fieldTextColor
            ),
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            keyboardActions = effectiveKeyboardActions,
            decorationBox = { innerTextField ->
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(6.dp),
                    color = fieldBackground,
                    border = BorderStroke(
                        width = 1.dp,
                        color = borderColor
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top
                    ) {
                        if (leadingIcon != null) {
                            Box(modifier = Modifier.padding(top = if (singleLine) 0.dp else 12.dp)) {
                                leadingIcon()
                            }
                            Spacer(Modifier.width(8.dp))
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = if (singleLine) 0.dp else 12.dp),
                            contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart
                        ) {
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Gray,
                                    fontStyle = FontStyle.Italic,
                                    fontFamily = RobotoCondensedFont(),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            innerTextField()
                        }

                        if (trailingIcon != null) {
                            Spacer(Modifier.width(8.dp))
                            Box(modifier = Modifier.padding(top = if (singleLine) 0.dp else 12.dp)) {
                                trailingIcon()
                            }
                        }
                    }
                }
            }
        )

        if (supportingText != null) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = supportingText,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = RobotoCondensedFont(),
                color = if (isError) MaterialTheme.colorScheme.error else Gray
            )
        }
    }
}