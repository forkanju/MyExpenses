package ngo.friendship.mhealth.dc.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.serialization.saved
import androidx.navigation3.runtime.NavBackStack
import ngo.friendship.mhealth.dc.data.local.LocalSettings
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.navigation.components.replaceWith
import ngo.friendship.mhealth.dc.presentation.navigation.navConfiguration

class MainViewModel(
    val settings: LocalSettings,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val isUserLoggedIn
        get() = settings.isUserLoggedIn

    override var backStack by savedStateHandle.saved(
        configuration = navConfiguration
    ) {
        NavBackStack(if (isUserLoggedIn) Screens.Main else Screens.Auth)
    }

    fun logout() {
        settings.clear()
        backStack.replaceWith(Screens.Auth)
    }
}