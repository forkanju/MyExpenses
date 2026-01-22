package ngo.friendship.mhealth.dc.presentation.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import doctorcenter.composeapp.generated.resources.Res
import doctorcenter.composeapp.generated.resources.logo_dc_mh
import ngo.friendship.mhealth.dc.presentation.base.SnackbarController
import ngo.friendship.mhealth.dc.presentation.components.AppLogo
import ngo.friendship.mhealth.dc.presentation.components.FloatingLabeledTextField
import ngo.friendship.mhealth.dc.presentation.components.PasswordSecureFloatingLabeledTextField
import ngo.friendship.mhealth.dc.presentation.components.PrimaryButton
import ngo.friendship.mhealth.dc.theme.FontSize
import ngo.friendship.mhealth.dc.theme.FriendshipTheme
import ngo.friendship.mhealth.dc.theme.TextPrimary

@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit = { _, _ -> },
    onForgotPasswordClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(scrollState)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ✅ pushes content down when space is available
        Spacer(modifier = Modifier.weight(1f))

        AppLogo(
            iconLogo = Res.drawable.logo_dc_mh,
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        FloatingLabeledTextField(
            label = "Username",
            value = email,
            onValueChange = { email = it },
            placeholder = "Enter your username",
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        PasswordSecureFloatingLabeledTextField(
            label = "Password",
            value = password,
            onValueChange = { password = it },
            placeholder = "Enter your password"
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Forgot Password?",
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable {
                        focusManager.clearFocus()
                        onForgotPasswordClick()
                    }
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelLarge.copy(

                    fontSize = FontSize.REGULAR,
                    fontWeight = FontWeight.Normal,
                    textDecoration = TextDecoration.Underline,
                    color = TextPrimary.copy(alpha = 0.6f)
                )
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryButton(
            text = "Sign in",
            onClick = {
                if (email.isBlank()) {
                    SnackbarController.sendEvent("Please enter email or username")
                    return@PrimaryButton
                }
                if (password.isBlank()) {
                    SnackbarController.sendEvent("Please enter password")
                    return@PrimaryButton
                }
                focusManager.clearFocus()
                onLoginClick(email, password)
            },
//            enabled = email.isNotBlank() && password.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPrev() {
    FriendshipTheme {
        LoginScreen()
    }
}

