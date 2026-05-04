package ngo.friendship.mhealth.dc.presentation.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import fcmProfileRoute
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.components.InitBaseVM
import ngo.friendship.mhealth.dc.presentation.navigation.route.authRoute
import ngo.friendship.mhealth.dc.presentation.navigation.route.beneficiaryProfileRoute
import ngo.friendship.mhealth.dc.presentation.navigation.route.caseRoute
import ngo.friendship.mhealth.dc.presentation.navigation.route.dialogRoute
import ngo.friendship.mhealth.dc.presentation.navigation.route.homeRoute
import ngo.friendship.mhealth.dc.presentation.navigation.route.dashboardRoute
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
                        // Material 3 Forward: Slide in from right with fade, slide out to left with fade
                        (slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(
                                durationMillis = 400,
                                easing = FastOutSlowInEasing
                            )
                        ) + fadeIn(animationSpec = tween(400))).togetherWith(
                            slideOutHorizontally(
                                targetOffsetX = { -it / 3 },
                                animationSpec = tween(
                                    durationMillis = 400,
                                    easing = FastOutSlowInEasing
                                )
                            ) + fadeOut(animationSpec = tween(400))
                        )
                    },
                    popTransitionSpec = {
                        // Material 3 Backward: Slide in from left with fade, slide out to right with fade
                        (slideInHorizontally(
                            initialOffsetX = { -it / 3 },
                            animationSpec = tween(
                                durationMillis = 400,
                                easing = FastOutSlowInEasing
                            )
                        ) + fadeIn(animationSpec = tween(400))).togetherWith(
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(
                                    durationMillis = 400,
                                    easing = FastOutSlowInEasing
                                )
                            ) + fadeOut(animationSpec = tween(400))
                        )
                    },
                    predictivePopTransitionSpec = { offset ->
                        // Predictive back: Interactive slide that follows the gesture offset
                        (slideInHorizontally(
                            initialOffsetX = { -it / 3 + offset },
                            animationSpec = tween(durationMillis = 0)
                        ) + fadeIn()).togetherWith(
                            slideOutHorizontally(
                                targetOffsetX = { offset },
                                animationSpec = tween(durationMillis = 0)
                            ) + fadeOut()
                        )
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

                        dashboardRoute(
                            mainViewModel = viewModel
                        )

                        caseRoute(
                            mainViewModel = viewModel,
                            snackBarState = snackBarState,
                        )

                        fcmProfileRoute(
                            mainViewModel = viewModel
                        )

                        beneficiaryProfileRoute(
                            mainViewModel = viewModel
                        )
                    }
                )
            }
        )
    }
}

