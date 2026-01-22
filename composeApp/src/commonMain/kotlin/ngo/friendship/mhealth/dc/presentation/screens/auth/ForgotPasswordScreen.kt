package ngo.friendship.mhealth.dc.presentation.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.presentation.components.FloatingLabeledTextField
import ngo.friendship.mhealth.dc.presentation.components.PrimaryButton
import ngo.friendship.mhealth.dc.presentation.theme.BebasNeueFont
import ngo.friendship.mhealth.dc.presentation.theme.FontSize
import ngo.friendship.mhealth.dc.presentation.theme.RobotoCondensedFont
import ngo.friendship.mhealth.dc.presentation.theme.TextPrimary

@Composable
fun ForgotPasswordScreen(
    onSendResetLinkClick: (emailOrUsername: String) -> Unit = {},
    onBackToLoginClick: () -> Unit = {}
) {
    var emailOrUsername by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .imePadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Title
        Text(
            text = "Forgot Password",
            style = MaterialTheme.typography.titleLarge.copy(
                fontFamily = BebasNeueFont(),
                fontSize = FontSize.LARGE,
                color = TextPrimary
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Enter your email or username. We’ll send a reset link.",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = RobotoCondensedFont(),
                fontSize = FontSize.REGULAR,
                fontWeight = FontWeight.Normal,
                color = TextPrimary
            )
        )

        Spacer(modifier = Modifier.height(28.dp))

        FloatingLabeledTextField(
            label = "Email / Username",
            value = emailOrUsername,
            onValueChange = {
                emailOrUsername = it
                errorText = null
            },
            placeholder = "Enter email or username",
            isError = errorText != null,
            supportingText = errorText,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Send reset link",
            onClick = {
                val trimmed = emailOrUsername.trim()
                if (trimmed.isEmpty()) {
                    errorText = "Please enter email or username"
                } else {
                    onSendResetLinkClick(trimmed)
                }
            },
            enabled = emailOrUsername.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        PrimaryButton(
            text = "Back to login",
            onClick = onBackToLoginClick,
            enabled = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
