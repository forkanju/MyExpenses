package ngo.friendship.mhealth.dc.presentation.state

import ngo.friendship.mhealth.dc.presentation.navigation.Screen
import ngo.friendship.mhealth.dc.theme.Resources
import org.jetbrains.compose.resources.DrawableResource

enum class BottomBarDestination(
    val icon: DrawableResource,
    val title: String,
    val screen: Screen
) {
    Home(
        icon = Resources.Icon.Home,
        title = "Home",
        screen = Screen.Home
    ),
    Case(
        icon = Resources.Icon.Case,
        title = "Case",
        screen = Screen.Case
    ),
    More(
        icon = Resources.Icon.More,
        title = "More",
        screen = Screen.More
    )
}