import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.screens.profile.fcm.FcmProfileScreen
import ngo.friendship.mhealth.dc.domain.model.FcmProfile
import ngo.friendship.mhealth.dc.presentation.screens.profile.fcm.FcmProfileIntent
import ngo.friendship.mhealth.dc.presentation.MainViewModel

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
