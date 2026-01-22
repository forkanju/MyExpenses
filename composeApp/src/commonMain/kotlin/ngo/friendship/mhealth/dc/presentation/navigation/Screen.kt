package ngo.friendship.mhealth.dc.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Auth : Screen()

    @Serializable
    data object ForgotPassword : Screen()

    // Bottom nav container (host)
    @Serializable
    data object Main : Screen()

    // Bottom tabs
    @Serializable
    data object Home : Screen()

    @Serializable
    data object Case : Screen()

    @Serializable
    data object More : Screen()

}