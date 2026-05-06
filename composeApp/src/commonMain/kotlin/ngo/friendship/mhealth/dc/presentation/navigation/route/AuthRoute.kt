package ngo.friendship.mhealth.dc.presentation.navigation.route

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.navigation.components.entryWithVM
import ngo.friendship.mhealth.dc.presentation.screen.login.ForgotPasswordScreen
import ngo.friendship.mhealth.dc.presentation.screen.login.LoginIntent
import ngo.friendship.mhealth.dc.presentation.screen.login.LoginScreen
import ngo.friendship.mhealth.dc.presentation.screen.login.LoginViewModel

fun EntryProviderScope<NavKey>.authRoute(
    mainViewModel: MainViewModel,
    snackBarState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    entryWithVM<Screens.Auth, LoginViewModel>(mainViewModel.backStack, snackBarState) {

        LoginScreen(
            onLoginClick = { userName, password ->
                viewModel.onIntent(LoginIntent.Login(userName, password))
            },
            onForgotPasswordClick = {
                backStack.add(Screens.ForgotPassword)
            },
            modifier = modifier
        )
    }
    // Note: ForgotPasswordScreen still uses LoginViewModel (formerly AuthViewModel) for now
    entryWithVM<Screens.ForgotPassword, LoginViewModel>(mainViewModel.backStack, snackBarState) {
        ForgotPasswordScreen(
            onBackToLoginClick = backStack::removeLastOrNull
        )
    }
}
