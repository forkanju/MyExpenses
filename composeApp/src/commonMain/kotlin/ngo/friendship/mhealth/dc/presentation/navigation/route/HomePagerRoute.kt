package ngo.friendship.mhealth.dc.presentation.navigation.route

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import ngo.friendship.mhealth.dc.presentation.MainUiEvent
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.BottomNavItems
import ngo.friendship.mhealth.dc.presentation.screens.case.case_list.CaseListScreen
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.HealthDashboardScreen
import ngo.friendship.mhealth.dc.presentation.screens.home.HomeScreen

@Composable
fun HomePagerRoute(
    pagerState: PagerState,
    mainViewModel: MainViewModel,
    modifier: Modifier
) {
    val isLoading by mainViewModel.loadingSecondaryFlow.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        mainViewModel.uiEvent.collectLatest { event ->
            when (event) {
                MainUiEvent.OpenCasesTab -> {
                    val casesIndex = BottomNavItems.entries.indexOf(BottomNavItems.Cases)
                    println("HomePagerRoute: moving to Cases page = $casesIndex")

                    if (casesIndex != -1) {
                        pagerState.scrollToPage(casesIndex)
                        mainViewModel.selectBottomTab(BottomNavItems.Cases)
                    }

                    mainViewModel.clearOpenCasesEvent()
                }

                MainUiEvent.Idle -> Unit
            }
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        val currentTab = BottomNavItems.entries[pagerState.currentPage]
        mainViewModel.selectBottomTab(currentTab)
    }

    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = {
            when (BottomNavItems.entries[pagerState.currentPage]) {
                BottomNavItems.Home -> Unit
                BottomNavItems.Cases -> {
                    // Refresh is now handled internally by CaseListViewModel
                    // or can be triggered via a shared event if needed.
                }

                BottomNavItems.Dashboard -> Unit
            }
        },
        modifier = modifier
    ) {
        HorizontalPager(
            state = pagerState,
        ) { page ->
            when (BottomNavItems.entries[page]) {
                BottomNavItems.Home -> HomeScreen()

                BottomNavItems.Cases -> CaseListScreen(
                    onNavigateToDetails = { interview ->
                        mainViewModel.openCase(interview)
                    }
                )

                BottomNavItems.Dashboard -> HealthDashboardScreen(
                    onNavigate = { route ->
                        mainViewModel.backStack.add(route)
                    }
                )
            }
        }
    }
}