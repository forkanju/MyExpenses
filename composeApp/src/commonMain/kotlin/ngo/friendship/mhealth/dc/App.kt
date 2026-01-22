package ngo.friendship.mhealth.dc

import androidx.compose.runtime.Composable
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import ngo.friendship.mhealth.dc.di.appModules
import ngo.friendship.mhealth.dc.presentation.navigation.SetupNavGraph
import ngo.friendship.mhealth.dc.theme.FriendshipTheme
import org.koin.compose.KoinMultiplatformApplication
import org.koin.dsl.KoinConfiguration

@Composable
fun App() {
    if(BuildKonfig.IS_DEBUG)
        Napier.base(DebugAntilog())
    KoinMultiplatformApplication(config = KoinConfiguration { modules(appModules) }) {
        FriendshipTheme {
            SetupNavGraph()
        }
    }
}