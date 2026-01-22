package ngo.friendship.mhealth.dc.presentation.navigation.route

import HealthDashboardScreen
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.BottomNavItems
import ngo.friendship.mhealth.dc.presentation.screens.case.CaseScreen
import ngo.friendship.mhealth.dc.presentation.screens.home.HomeScreen

@Composable
fun HomePagerRoute(
    pagerState: PagerState,
    viewModel: MainViewModel,
    modifier: Modifier
) {
    HorizontalPager(
        state = pagerState,
    ) { page ->
        when (BottomNavItems.entries[page]) {
            BottomNavItems.Home -> HomeScreen(
                modifier = modifier
            )

            BottomNavItems.Case -> CaseScreen(
                modifier = modifier
            )

            BottomNavItems.Dashboard -> HealthDashboardScreen(
                modifier = modifier
            )
        }
    }
}