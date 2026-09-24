package ngo.friendship.mhealth.dc.presentation.navigation.route

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.screen.profile.fcm.FcmProfileScreen

fun EntryProviderScope<NavKey>.fcmProfileRoute(
    mainViewModel: MainViewModel
) {
    entry<Screens.FcmProfile> { screen ->
        FcmProfileScreen(
            fcmCode = screen.fcmCode,
            fcmProfile = null,
            onBack = {
                mainViewModel.backStack.removeLastOrNull()
            }
        )
    }
}
