package ngo.friendship.mhealth.dc.presentation.screens.main.case.components.case_detail

import androidx.compose.runtime.Composable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
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
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Top label like "DX"
        if (label != null) {
            Text(
                text = label,
                style = TextStyle(
                    fontStyle = FontStyle.Italic,
                    fontSize = FontSize.REGULAR,
                    fontFamily = RobotoCondensedFont(),
                    color = TextPrimary
                ),
                color = DarkerGray
            )
            Spacer(Modifier.height(6.dp))
        }

        // Outlined text field
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            enabled = enabled,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = DarkerGray
            ),
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            decorationBox = { innerTextField ->
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(6.dp),
                    color = Color.Transparent,
                    border = BorderStroke(
                        width = 1.dp,
                        color = when {
                            isError -> MaterialTheme.colorScheme.error
                            else -> Color(0xFFCBD5E1)
                        }
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Leading icon
                        if (leadingIcon != null) {
                            leadingIcon()
                            Spacer(Modifier.width(8.dp))
                        }

                        // Text field content
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            // Placeholder
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
                            // Actual input
                            innerTextField()
                        }

                        // Trailing icon
                        if (trailingIcon != null) {
                            Spacer(Modifier.width(8.dp))
                            trailingIcon()
                        }
                    }
                }
            }
        )

        // Supporting text
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

