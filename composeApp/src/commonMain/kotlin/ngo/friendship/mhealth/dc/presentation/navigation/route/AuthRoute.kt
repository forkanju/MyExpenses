package ngo.friendship.mhealth.dc.presentation.navigation.route

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.navigation.components.entryWithVM
import ngo.friendship.mhealth.dc.presentation.screens.auth.AuthViewModel
import ngo.friendship.mhealth.dc.presentation.screens.auth.ForgotPasswordScreen
import ngo.friendship.mhealth.dc.presentation.screens.auth.LoginScreen

fun EntryProviderScope<NavKey>.authRoute(
    mainViewModel: MainViewModel,
    snackBarState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    entryWithVM<Screens.Auth, AuthViewModel>(mainViewModel.backStack, snackBarState) {

        LoginScreen(
            onLoginClick = { userName, password ->
                viewModel.login(
                    userName = userName,
                    password = password
                )
            },
            onForgotPasswordClick = {
                backStack.add(Screens.ForgotPassword)
            },
            modifier = modifier
        )
    }
    entryWithVM<Screens.ForgotPassword, AuthViewModel>(mainViewModel.backStack, snackBarState) {
        ForgotPasswordScreen(
            onBackToLoginClick = backStack::removeLastOrNull
        )
    }
}