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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.DialogSceneStrategy
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.ConfirmAction
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.screens.more.components.NewDxDialog
import ngo.friendship.mhealth.dc.presentation.screens.profile.ProfileEvent
import ngo.friendship.mhealth.dc.presentation.screens.profile.ProfilePopup

fun EntryProviderScope<NavKey>.dialogRoute(
    viewModel: MainViewModel
) {
    entry<Screens.Dialog.NewDx>(
        metadata = DialogSceneStrategy.dialog(
            DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnClickOutside = true,
                dismissOnBackPress = true
            )
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.35f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    viewModel.backStack.removeLastOrNull()
                },
            contentAlignment = Alignment.Center
        ) {
            NewDxDialog(
                onDismiss = { viewModel.backStack.removeLastOrNull() },
                onCreate = { title, advices ->
                    // Handle DX creation
                    viewModel.backStack.removeLastOrNull()
                }
            )
        }
    }

    entry<Screens.Dialog.ProfilePopup>(
        metadata = DialogSceneStrategy.dialog(
            DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnClickOutside = true,
                dismissOnBackPress = true
            )
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.35f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    viewModel.backStack.removeLastOrNull()
                },
            contentAlignment = Alignment.Center
        ) {
            ProfilePopup(
                onDismiss = { viewModel.backStack.removeLastOrNull() },
                onEvent = { event ->
                    when (event) {
                        ProfileEvent.OnSignOutClick -> {
                            viewModel.backStack.removeLastOrNull()
                            viewModel.logout()
                        }

                        else -> {
                            viewModel.backStack.removeLastOrNull()
                        }
                    }

                }
            )
        }
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
                                viewModel.backStack.clear()

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