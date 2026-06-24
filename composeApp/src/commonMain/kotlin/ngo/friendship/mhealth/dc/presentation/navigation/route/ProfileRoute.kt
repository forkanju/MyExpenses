package ngo.friendship.mhealth.dc.presentation.navigation.route

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.screen.profile.version.VersionDetailsScreen
import ngo.friendship.mhealth.dc.presentation.screen.profile.version.VersionHistoryScreen
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

    entry<Screens.VersionHistory> {
        VersionHistoryScreen(
            onVersionClick = { version ->
                mainViewModel.backStack.add(Screens.VersionDetails(version))
            },
            onBack = { mainViewModel.backStack.removeLastOrNull() }
        )
    }

    entry<Screens.VersionDetails> { screen ->
        VersionDetailsScreen(
            version = screen.version,
            onBack = { mainViewModel.backStack.removeLastOrNull() }
        )
    }
}
