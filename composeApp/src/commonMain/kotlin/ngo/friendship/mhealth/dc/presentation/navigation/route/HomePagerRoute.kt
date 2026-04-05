package ngo.friendship.mhealth.dc.presentation.navigation.route

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.BottomNavItems
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.screens.main.case_list.CaseListScreen
import ngo.friendship.mhealth.dc.presentation.screens.main.home.HomeScreen
import ngo.friendship.mhealth.dc.presentation.screens.main.dashboard.HealthDashboardScreen

@Composable
fun HomePagerRoute(
    pagerState: PagerState,
    mainViewModel: MainViewModel,
    modifier: Modifier
) {
    val interviewList by mainViewModel.interviewListState.collectAsStateWithLifecycle()

    LaunchedEffect(mainViewModel.selectedBottomTab){
        pagerState.animateScrollToPage(mainViewModel.selectedBottomTab.ordinal)
    }

    HorizontalPager(
        state = pagerState,
    ) { page ->
        when (BottomNavItems.entries[page]) {
            BottomNavItems.Home -> HomeScreen(
                modifier = modifier
            )

            BottomNavItems.Cases -> CaseListScreen(
                modifier = modifier,
                interviewList = interviewList,
                onCaseClick = {
                    mainViewModel.backStack.add(Screens.PrescriptionForm(it))
                },
                onFilterClick = {
                    // filter popup/dialog
                }
            )

            BottomNavItems.Dashboard -> HealthDashboardScreen(
                modifier = modifier
            )
        }
    }
}