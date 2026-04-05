package ngo.friendship.mhealth.dc.presentation.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.components.InitBaseVM
import ngo.friendship.mhealth.dc.presentation.navigation.route.authRoute
import ngo.friendship.mhealth.dc.presentation.navigation.route.caseRoute
import ngo.friendship.mhealth.dc.presentation.navigation.route.dialogRoute
import ngo.friendship.mhealth.dc.presentation.navigation.route.homeRoute
import ngo.friendship.mhealth.dc.theme.Dimen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SetupNavDisplay(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<MainViewModel>()
    InitBaseVM(
        backStack = viewModel.backStack,
        viewModel = viewModel,
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            snackbarHost = {
                SnackbarHost(hostState = snackBarState) { data ->
                    Snackbar(
                        snackbarData = data,
                        shape = MaterialTheme.shapes.large,
                        modifier = Modifier.padding(Dimen.Standard)
                    )
                }
            },
            content = { paddingValues ->
                NavDisplay(
                    backStack = backStack,
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator()
                    ),
                    sceneStrategies = listOf(
                        SinglePaneSceneStrategy(),
                        DialogSceneStrategy()
                    ),
                    transitionSpec = {
                        // Slide in from right when navigating forward
                        slideInHorizontally(initialOffsetX = { it }) togetherWith
                                slideOutHorizontally(targetOffsetX = { -it })
                    },
                    popTransitionSpec = {
                        // Slide in from left when navigating back
                        slideInHorizontally(initialOffsetX = { -it + 1000 }) togetherWith
                                slideOutHorizontally(targetOffsetX = { it })
                    },
                    predictivePopTransitionSpec = {
                        // Slide in from left when navigating back
                        slideInHorizontally(initialOffsetX = { -it + 1000 }) togetherWith
                                slideOutHorizontally(targetOffsetX = { it })
                    },
                    entryProvider = entryProvider {
                        dialogRoute(
                            viewModel = viewModel
                        )
                        authRoute(
                            mainViewModel = viewModel,
                            snackBarState = snackBarState,
                            modifier = Modifier
                                .padding(paddingValues)
                                .imePadding()
                        )
                        homeRoute(
                            viewModel = viewModel
                        )

                        caseRoute(
                            mainViewModel = viewModel,
                            snackBarState = snackBarState,
                        )
                    }
                )
            }
        )
    }
}

