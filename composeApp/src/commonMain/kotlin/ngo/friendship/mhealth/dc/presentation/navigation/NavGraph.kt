package ngo.friendship.mhealth.dc.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ngo.friendship.mhealth.dc.presentation.screens.auth.ForgotPasswordScreen
import ngo.friendship.mhealth.dc.presentation.screens.auth.LoginScreen
import ngo.friendship.mhealth.dc.presentation.screens.home.MainGraphScreen

@Composable
fun SetupNavGraph(startDestination: Screen = Screen.Auth) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screen.Auth> {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(Screen.Main) {
                        popUpTo<Screen.Auth> { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onForgotPasswordClick = {
                    navController.navigate(Screen.ForgotPassword)
                }
            )
        }

        composable<Screen.Main> {
            MainGraphScreen()
        }

        composable<Screen.ForgotPassword> {
            ForgotPasswordScreen(
                onBackToLoginClick = {
                    navController.navigateUp()
                }
            )
        }
    }


}

