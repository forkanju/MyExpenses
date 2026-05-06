package ngo.friendship.mhealth.dc.presentation.screens.home

sealed interface HomeIntent {
    data object LoadDashboard : HomeIntent
}
