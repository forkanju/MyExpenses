package ngo.friendship.mhealth.dc.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.presentation.base.SnackbarController
import ngo.friendship.mhealth.dc.presentation.components.CommonTopBar
import ngo.friendship.mhealth.dc.presentation.components.PasswordSecureFloatingLabeledTextField
import ngo.friendship.mhealth.dc.presentation.components.PrimaryButton

@Composable
fun ChangePasswordScreen(
    onBack: () -> Unit,
    onChangePassword: (old: String, new: String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CommonTopBar(
                title = "Change Password",
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            PasswordSecureFloatingLabeledTextField(
                label = "Current Password",
                value = oldPassword,
                onValueChange = { oldPassword = it },
                placeholder = "Enter current password"
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordSecureFloatingLabeledTextField(
                label = "New Password",
                value = newPassword,
                onValueChange = { newPassword = it },
                placeholder = "Enter new password"
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordSecureFloatingLabeledTextField(
                label = "Confirm New Password",
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "Confirm new password"
            )

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                text = "CHANGE PASSWORD",
                onClick = {
                    focusManager.clearFocus()
                    if (oldPassword.isBlank()) {
                        SnackbarController.sendEvent("Please enter current password")
                        return@PrimaryButton
                    }
                    if (newPassword.isBlank()) {
                        SnackbarController.sendEvent("Please enter new password")
                        return@PrimaryButton
                    }
                    if (newPassword != confirmPassword) {
                        SnackbarController.sendEvent("Passwords do not match")
                        return@PrimaryButton
                    }
                    onChangePassword(oldPassword, newPassword)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
