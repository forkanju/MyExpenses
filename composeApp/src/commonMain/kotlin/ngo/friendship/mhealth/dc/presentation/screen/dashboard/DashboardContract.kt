package ngo.friendship.mhealth.dc.presentation.screen.dashboard

import androidx.navigation3.runtime.NavKey
import ngo.friendship.mhealth.dc.domain.model.SetupData
import ngo.friendship.mhealth.dc.presentation.base.SnackbarType
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.model.SectionData

data class DashboardState(
    val sections: List<SectionData> = emptyList(),
    val setupData: SetupData = SetupData(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)


sealed interface DashboardIntent {
    data class Navigate(val route: NavKey) : DashboardIntent
    data object LoadDashboard : DashboardIntent
    data object Refresh : DashboardIntent
    data object ClearError : DashboardIntent
    data class ShowSnackbar(val message: String, val type: SnackbarType = SnackbarType.DEFAULT) : DashboardIntent
}

sealed interface DashboardEffect {
    data class NavigateTo(val route: NavKey) : DashboardEffect
    data class ShowSnackbar(val message: String, val type: SnackbarType = SnackbarType.DEFAULT) : DashboardEffect
}



