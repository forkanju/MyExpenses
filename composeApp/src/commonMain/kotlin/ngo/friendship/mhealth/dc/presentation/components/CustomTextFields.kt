package ngo.friendship.mhealth.dc.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.presentation.theme.DisabledBorderColor
import ngo.friendship.mhealth.dc.presentation.theme.FocusedBorderColor
import ngo.friendship.mhealth.dc.presentation.theme.FontSize
import ngo.friendship.mhealth.dc.presentation.theme.RobotoCondensedFont
import ngo.friendship.mhealth.dc.presentation.theme.TextPrimary
import ngo.friendship.mhealth.dc.presentation.theme.Transparent
import ngo.friendship.mhealth.dc.presentation.theme.UnfocusedBorderColor

@Composable
fun TopLabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Type",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(
                fontFamily = RobotoCondensedFont(),
                fontSize = FontSize.REGULAR,
                fontWeight = FontWeight.Normal,
                color = TextPrimary.copy(alpha = 0.8f)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            readOnly = readOnly,
            singleLine = singleLine,
            isError = isError,
            placeholder = {
                if (placeholder.isNotBlank()) Text(
                    text = placeholder,
                    fontFamily = RobotoCondensedFont(),
                    fontSize = FontSize.REGULAR
                )
            },
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = keyboardOptions,
            supportingText = {
                if (supportingText != null) Text(supportingText)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = FocusedBorderColor,
                unfocusedBorderColor = UnfocusedBorderColor,
                disabledBorderColor = DisabledBorderColor,
                focusedContainerColor = Transparent,
                unfocusedContainerColor = Transparent,
                disabledContainerColor = Transparent
            )
        )
    }
}

@Composable
fun FloatingLabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp), // adjust if you want taller like screenshot
        enabled = enabled,
        readOnly = readOnly,
        singleLine = singleLine,
        isError = isError,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = RobotoCondensedFont(),
                    fontSize = FontSize.REGULAR,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.3.sp,
                    color = TextPrimary.copy(alpha = 0.8f)
                )
            )

        },
        placeholder = {
            if (placeholder.isNotBlank()) {
                Text(
                    text = placeholder,
                    fontFamily = RobotoCondensedFont(),
                    fontSize = FontSize.REGULAR,
                    color = TextPrimary.copy(alpha = 0.6f)
                )
            }
        },
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = keyboardOptions,
        supportingText = {
            supportingText?.let { Text(it) }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = FocusedBorderColor,
            unfocusedBorderColor = UnfocusedBorderColor,
            disabledBorderColor = DisabledBorderColor,
            focusedContainerColor = Transparent,
            unfocusedContainerColor = Transparent,
            disabledContainerColor = Transparent
        )
    )
}

@Composable
fun PasswordSecureFloatingLabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    supportingText: String? = null
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp),
        enabled = enabled,
        readOnly = readOnly,
        isError = isError,
        singleLine = true,

        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = RobotoCondensedFont(),
                    fontSize = FontSize.REGULAR,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.3.sp,
                    color = TextPrimary.copy(alpha = 0.8f)
                )
            )
        },

        placeholder = {
            if (placeholder.isNotBlank()) {
                Text(
                    text = placeholder,
                    fontFamily = RobotoCondensedFont(),
                    fontSize = FontSize.REGULAR,
                    color = TextPrimary.copy(alpha = 0.6f)
                )
            }
        },
        visualTransformation =
            if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),

        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),

        trailingIcon = {
            IconButton(
                onClick = { passwordVisible = !passwordVisible },
                enabled = enabled
            ) {
                Icon(
                    imageVector =
                        if (passwordVisible) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff,
                    contentDescription =
                        if (passwordVisible) "Hide password"
                        else "Show password",
                    tint = TextPrimary,
                    modifier = Modifier.alpha(0.6f)
                )
            }
        },

        supportingText = {
            supportingText?.let {
                Text(
                    text = it,
                    fontFamily = RobotoCondensedFont(),
                    fontSize = FontSize.SMALL
                )
            }
        },

        shape = RoundedCornerShape(8.dp),

        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = FocusedBorderColor,
            unfocusedBorderColor = UnfocusedBorderColor,
            disabledBorderColor = DisabledBorderColor,
            focusedContainerColor = Transparent,
            unfocusedContainerColor = Transparent,
            disabledContainerColor = Transparent
        )
    )
}


