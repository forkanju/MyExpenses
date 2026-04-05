package ngo.friendship.mhealth.dc.presentation.navigation.route

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.BottomNavItems
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.screens.case.case_list.CaseListScreen
import ngo.friendship.mhealth.dc.presentation.screens.main.dashboard.HealthDashboardScreen
import ngo.friendship.mhealth.dc.presentation.screens.main.home.HomeScreen

@Composable
fun HomePagerRoute(
    pagerState: PagerState,
    mainViewModel: MainViewModel,
    modifier: Modifier
) {
    val interviewList by mainViewModel.interviewListState.collectAsStateWithLifecycle()
    val isLoading by mainViewModel.loadingSecondaryFlow.collectAsStateWithLifecycle()

    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = {
            when (BottomNavItems.entries[pagerState.currentPage]) {
                BottomNavItems.Home -> Unit
                BottomNavItems.Cases -> mainViewModel.loadInterviewList(appVersion = 3069)
                BottomNavItems.Dashboard -> Unit
            }
        },
        modifier = modifier
    ) {
        HorizontalPager(
            state = pagerState,
        )
        { page ->
            when (BottomNavItems.entries[page]) {
                BottomNavItems.Home -> HomeScreen()

                BottomNavItems.Cases -> CaseListScreen(
                    interviewList = interviewList,
                    onCaseClick = {
                        mainViewModel.backStack.add(Screens.PrescriptionForm(it))
                    },
                    onFilterClick = {
                        // filter popup/dialog
                    }
                )

                BottomNavItems.Dashboard -> HealthDashboardScreen()
            }
        }
    }

}