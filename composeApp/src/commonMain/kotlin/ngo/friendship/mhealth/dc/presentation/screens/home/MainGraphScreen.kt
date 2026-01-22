package ngo.friendship.mhealth.dc.presentation.screens.home

import ContentWithMessageBar
import CustomTopBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ngo.friendship.mhealth.dc.domain.BottomBarDestination
import ngo.friendship.mhealth.dc.presentation.components.BottomBar
import ngo.friendship.mhealth.dc.presentation.navigation.Screen
import ngo.friendship.mhealth.dc.presentation.screens.case.CaseScreen
import ngo.friendship.mhealth.dc.ui.theme.Resources
import ngo.friendship.mhealth.dc.ui.theme.Surface
import ngo.friendship.mhealth.dc.ui.theme.SurfaceBrand
import ngo.friendship.mhealth.dc.ui.theme.SurfaceError
import ngo.friendship.mhealth.dc.ui.theme.TextPrimary
import ngo.friendship.mhealth.dc.ui.theme.TextWhite
import rememberMessageBarState


@Composable
fun MainGraphScreen() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState()
    val selectedDestination by remember {
        derivedStateOf {
            val route = currentRoute.value?.destination?.route.toString()
            when {
                route.contains(BottomBarDestination.Home.screen.toString()) -> BottomBarDestination.Home
                route.contains(BottomBarDestination.Case.screen.toString()) -> BottomBarDestination.Case
                route.contains(BottomBarDestination.More.screen.toString()) -> BottomBarDestination.More
                else -> BottomBarDestination.Home
            }
        }
    }
    val messageBarState = rememberMessageBarState()

    Scaffold(
        containerColor = Surface,
        topBar = {
            CustomTopBar(
                notificationIcon = Resources.Icon.Notification,    // bell
                notificationCount = 10,
                onNotificationClick = { /* open notifications */ },
                userName = "Dr. Ahmed Imtiaz Abir",
                userSubtitle = "Doctor Center, Head Office",
                profileIcon = Resources.Icon.Profile,
                onProfileClick = { /* open profile */ }
            )
        }
    ) { padding ->
        ContentWithMessageBar(
            contentBackgroundColor = Surface,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                ),
            messageBarState = messageBarState,
            errorMaxLines = 2,
            errorContainerColor = SurfaceError,
            errorContentColor = TextWhite,
            successContainerColor = SurfaceBrand,
            successContentColor = TextPrimary
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                NavHost(
                    modifier = Modifier.weight(1f),
                    navController = navController,
                    startDestination = Screen.Home
                ) {
                    composable<Screen.Home> {
                        HomeScreen()
                    }
                    composable<Screen.Case> { CaseScreen() }
                    composable<Screen.More> { MoreScreen() }
                }

                Box(modifier = Modifier) {
                    BottomBar(
                        selected = selectedDestination,
                        onSelect = { destination ->
                            navController.navigate(destination.screen) {
                                launchSingleTop = true
                                popUpTo<Screen.Home> {
                                    saveState = true
                                    inclusive = false
                                }
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    }
}
