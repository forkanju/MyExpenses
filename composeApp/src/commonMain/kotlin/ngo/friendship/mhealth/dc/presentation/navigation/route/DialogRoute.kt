package ngo.friendship.mhealth.dc.presentation.navigation.route

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.DialogSceneStrategy
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.ConfirmAction
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.screen.profile.ProfileEvent
import ngo.friendship.mhealth.dc.presentation.screen.profile.ProfilePopup
import ngo.friendship.mhealth.dc.presentation.screen.profile.ProfileUiState

fun EntryProviderScope<NavKey>.dialogRoute(
    viewModel: MainViewModel
) {
    entry<Screens.Dialog.ProfilePopup>(
        metadata = DialogSceneStrategy.dialog(
            DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnClickOutside = true,
                dismissOnBackPress = true
            )
        )
    ) {
        val userProfile by viewModel.userProfileState.collectAsStateWithLifecycle()
        ProfilePopup(
            uiState = ProfileUiState(
                name = userProfile?.userName ?: "Doctor Center",
                designation = userProfile?.location ?: "Friendship NGO",
                email = userProfile?.email ?: "",
                mobileNo = userProfile?.mobileNo ?: "",
                profileImage = userProfile?.getProfileImageSource()
            ),
            onDismiss = { viewModel.backStack.removeLastOrNull() },
            onEvent = { event ->
                when (event) {
                    ProfileEvent.OnSignOutClick -> {
                        viewModel.backStack.removeLastOrNull()
                        viewModel.logout()
                    }

                    ProfileEvent.OnChangePasswordClick -> {
                        viewModel.backStack.removeLastOrNull()
                        viewModel.backStack.add(Screens.ChangePassword)
                    }

                    ProfileEvent.OnCheckUpdateClick -> {
                        viewModel.showSuccess("The app is already up to date")
                    }

                    ProfileEvent.OnVersionClick -> {
                        viewModel.backStack.removeLastOrNull()
                        viewModel.backStack.add(Screens.VersionHistory)
                    }

                    else -> {
                        viewModel.backStack.removeLastOrNull()
                    }
                }

            }
        )
    }

    entry<Screens.Dialog.Error>(
        metadata = DialogSceneStrategy.dialog(DialogProperties())
    ) { data ->
        AlertDialog(
            title = { Text(text = data.title) },
            text = {
                SelectionContainer {
                    Text(text = data.body)
                }
            },
            onDismissRequest = viewModel.backStack::removeLastOrNull,
            confirmButton = {
                TextButton(
                    onClick = viewModel.backStack::removeLastOrNull,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Text(text = "Close")
                }
            }
        )
    }

    entry<Screens.Dialog.Confirmation>(
        metadata = DialogSceneStrategy.dialog(DialogProperties())
    ) { data ->
        AlertDialog(
            title = { Text(text = data.title) },
            text = {
                SelectionContainer {
                    Text(text = data.message, style = MaterialTheme.typography.bodyMedium)
                }
            },
            onDismissRequest = viewModel.backStack::removeLastOrNull,
            confirmButton = {
                TextButton(
                    onClick = {
                        when (data.action) {
                            ConfirmAction.Exit ->
                                while (viewModel.backStack.size > 1) {
                                    viewModel.backStack.removeLast()
                                }

                            ConfirmAction.Logout ->
                                viewModel.logout()
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = if (data.isConfirmRed)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Text(text = data.confirmText)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = viewModel.backStack::removeLastOrNull,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Text(text = data.cancelText)
                }
            }
        )
    }
}