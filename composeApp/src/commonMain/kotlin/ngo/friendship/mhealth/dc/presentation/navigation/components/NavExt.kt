package ngo.friendship.mhealth.dc.presentation.navigation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.base.Info
import ngo.friendship.mhealth.dc.presentation.base.ObserveAsEvents
import ngo.friendship.mhealth.dc.presentation.base.SnackbarController
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import org.koin.compose.viewmodel.koinViewModel

/**
 * An extension function for [EntryProviderScope] that creates a composable destination with a ViewModel.
 *
 * This function simplifies the process of creating composable destinations that require a ViewModel.
 * It automatically handles the creation of the ViewModel, loading state, error handling, and success messages.
 *
 * @receiver EntryProviderScope
 * @param T The type of the destination.
 * @param VM The type of the ViewModel associated with the destination.
 * @param backStack The NavBackStack for navigation.
 * @param content The composable content to be displayed for this destination.
 *   It receives a [BaseContent] object containing the ViewModel and other necessary components.
 */
inline fun <reified T : NavKey, reified VM : BaseViewModel> EntryProviderScope<NavKey>.entryWithVM(
    backStack: NavBackStack<NavKey>,
    snackBarState : SnackbarHostState? = null,
    noinline content: @Composable (BaseContent<VM>.(T) -> Unit),
) {
    entry<T> { navEntry ->
        InitBaseVM<VM>(
            backStack = backStack,
            snackBarState = snackBarState,
            content = { snackBarState ->
                this.backStack = backStack
                content(
                    BaseContent(
                        entry = this@entryWithVM,
                        viewModel = this@InitBaseVM,
                        snackBarState = snackBarState,
                        backStack = backStack
                    ), navEntry
                )
            },
        )
    }
}

@Composable
inline fun <reified VM : BaseViewModel> InitBaseVM(
    backStack: NavBackStack<NavKey>,
    snackBarState: SnackbarHostState? = null,
    viewModel: VM? = null,
    noinline content: @Composable VM.(SnackbarHostState) -> Unit,
) {
    val viewModel = viewModel ?: koinViewModel<VM>()
    val isLoading by viewModel.loadingFlow.collectAsStateWithLifecycle()
    val snackBarState = snackBarState ?: remember { SnackbarHostState() }

    ObserveAsEvents(SnackbarController.events) {
        if (it.message.isBlank()) return@ObserveAsEvents
        snackBarState.currentSnackbarData?.dismiss()
        val result = snackBarState.showSnackbar(
            message = it.message,
            actionLabel = it.action?.label,
            withDismissAction = it.action?.onAction != null,
        )
        if (result == SnackbarResult.ActionPerformed)
            it.action?.onAction()
    }
    ObserveAsEvents(viewModel.errorFlow) {
        if (it.message.isBlank() || it.type != Info.Type.Default) return@ObserveAsEvents
        snackBarState.currentSnackbarData?.dismiss()
        if (it.message.length > 50)
            backStack.add(Screens.Dialog.Error(it.message))
        else
            snackBarState.showSnackbar(it.message)
    }
    ObserveAsEvents(viewModel.successFlow) {
        if (it.message.isBlank() || it.type != Info.Type.Default) return@ObserveAsEvents
        snackBarState.currentSnackbarData?.dismiss()
        snackBarState.showSnackbar(it.message)
    }
    content(viewModel, snackBarState)
    if (isLoading)
        LoadingLayout()
}

@Composable
fun LoadingLayout(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(.6F))
            .clickable(interactionSource = null, onClick = {})
    ) {
        ContainedLoadingIndicator(
            modifier = Modifier.size(60.dp)
        )
    }
}

data class BaseContent<VM : BaseViewModel>(
    val entry: EntryProviderScope<NavKey>,
    val viewModel: VM,
    val snackBarState: SnackbarHostState,
    val backStack: NavBackStack<NavKey>
)

fun <T> MutableList<T>.keepLast() {
    if (size > 1) {
        subList(0, size - 1).clear()
    }
}

fun <T> MutableList<T>.replaceWith(item: T) {
    add(item)
    keepLast()
}