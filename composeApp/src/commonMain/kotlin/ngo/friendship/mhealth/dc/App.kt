package ngo.friendship.mhealth.dc

import androidx.compose.runtime.Composable
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import ngo.friendship.mhealth.dc.di.appModules
import ngo.friendship.mhealth.dc.presentation.navigation.SetupNavDisplay
import ngo.friendship.mhealth.dc.theme.FriendshipTheme
import org.koin.compose.KoinApplication
import org.koin.core.logger.Level
import org.koin.dsl.KoinConfiguration

@Composable
fun App() {
    if (isDebugBuild)
        Napier.base(DebugAntilog())
    KoinApplication(
        configuration = KoinConfiguration { modules(appModules) },
        logLevel = if (isDebugBuild) Level.DEBUG else Level.ERROR
    ) {
        FriendshipTheme {
            SetupNavDisplay()
        }
    }
}