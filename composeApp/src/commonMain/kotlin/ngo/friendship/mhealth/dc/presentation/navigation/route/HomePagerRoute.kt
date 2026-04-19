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

        if (currentTab == BottomNavItems.Cases) {
            mainViewModel.initializeCases()
        }
    }

    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = {
            when (BottomNavItems.entries[pagerState.currentPage]) {
                BottomNavItems.Home -> Unit
                BottomNavItems.Cases -> mainViewModel.loadInterviewList(
                    tab = mainViewModel.selectedCaseTab
                )

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
                    interviewList = interviewList,
                    selectedTab = mainViewModel.selectedCaseTab,
                    tabCounts = mainViewModel.caseTabCounts,
                    onTabSelect = mainViewModel::selectCaseTab,
                    onCaseClick = { interview ->
                        mainViewModel.openCase(interview)
                    },
                    onFilterClick = {

                    }
                )

                BottomNavItems.Dashboard -> HealthDashboardScreen()
            }
        }
    }
}