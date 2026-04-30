package ngo.friendship.mhealth.dc.presentation.screens.dashboard

import androidx.navigation3.runtime.NavKey
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.model.SectionData

data class DashboardState(
    val sections: List<SectionData> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)


sealed interface DashboardIntent {
    data class Navigate(val route: NavKey) : DashboardIntent
    data object LoadDashboard : DashboardIntent
}

sealed interface DashboardEffect {
    data class NavigateTo(val route: NavKey) : DashboardEffect
}



