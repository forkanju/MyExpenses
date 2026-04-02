package ngo.friendship.mhealth.dc.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.serialization.saved
import androidx.navigation3.runtime.NavBackStack
import ngo.friendship.mhealth.dc.data.local.LocalSettings
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.BottomNavItems
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

    var selectedBottomTab by mutableStateOf(BottomNavItems.Home)
        private set

    fun selectBottomTab(tab: BottomNavItems){
        selectedBottomTab = tab
    }

    fun logout() {
        settings.clear()
        backStack.replaceWith(Screens.Auth)
    }
}