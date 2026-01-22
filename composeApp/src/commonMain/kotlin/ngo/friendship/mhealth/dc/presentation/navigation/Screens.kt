package ngo.friendship.mhealth.dc.presentation.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule

@Serializable
object Screens {
    @Serializable data object Auth : NavKey
    @Serializable data object ForgotPassword : NavKey
    @Serializable data object Main : NavKey

    @Serializable
    object Dialog {
        @Serializable data class Error(
            val body: String,
            val title: String = "Warning"
        ) : NavKey
        @Serializable data class Confirmation(
            val isConfirmRed: Boolean = false,
            val message: String,
            val title: String = "Confirmation",
            val confirmText: String = "Confirm",
            val cancelText: String = "Cancel",
            val action : ConfirmAction,
        ) : NavKey
    }

}

enum class ConfirmAction {
    Logout,
    Exit,
}

// Create a SerializersModule to define polymorphism
val navKeySerializersModule = SerializersModule {
    polymorphic(NavKey::class, Screens.Auth::class, Screens.Auth.serializer())
    polymorphic(NavKey::class, Screens.ForgotPassword::class, Screens.ForgotPassword.serializer())
    polymorphic(NavKey::class, Screens.Main::class, Screens.Main.serializer())
    polymorphic(NavKey::class, Screens.Dialog.Error::class, Screens.Dialog.Error.serializer())
    polymorphic(NavKey::class, Screens.Dialog.Confirmation::class, Screens.Dialog.Confirmation.serializer())
}

val navConfiguration = SavedStateConfiguration {
    serializersModule = navKeySerializersModule
}