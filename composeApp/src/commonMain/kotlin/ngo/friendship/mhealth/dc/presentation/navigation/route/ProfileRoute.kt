package ngo.friendship.mhealth.dc.presentation.navigation.route

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.screens.profile.ChangePasswordScreen

fun EntryProviderScope<NavKey>.profileRoute(
    mainViewModel: MainViewModel
) {
    entry<Screens.ChangePassword> {
        ChangePasswordScreen(
            onBack = { mainViewModel.backStack.removeLastOrNull() },
            onChangePassword = { old, new ->
                mainViewModel.changePassword(old, new)
            }
        )
    }
}
