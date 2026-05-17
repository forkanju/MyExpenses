package ngo.friendship.mhealth.dc.presentation.navigation.route

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import ngo.friendship.mhealth.dc.presentation.MainUiEvent
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.BottomNavItems
import ngo.friendship.mhealth.dc.presentation.screen.case.case_list.CaseListScreen
import ngo.friendship.mhealth.dc.presentation.screen.case.case_list.components.CaseTab
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.DashboardScreen
import ngo.friendship.mhealth.dc.presentation.screens.home.HomeScreen

@Composable
fun HomePagerRoute(
    pagerState: PagerState,
    mainViewModel: MainViewModel,
    modifier: Modifier
) {
    val isLoading by mainViewModel.loadingSecondaryFlow.collectAsStateWithLifecycle()
    var forcedCaseTab by remember { mutableStateOf<CaseTab?>(null) }

    LaunchedEffect(Unit) {
        mainViewModel.uiEvent.collectLatest { event ->
            when (event) {
                is MainUiEvent.OpenCasesTab -> {
                    val casesIndex = BottomNavItems.entries.indexOf(BottomNavItems.Cases)
                    println("HomePagerRoute: moving to Cases page = $casesIndex, tab = ${event.tab}")

                    forcedCaseTab = event.tab
                    if (casesIndex != -1) {
                        pagerState.scrollToPage(casesIndex)
                        mainViewModel.selectBottomTab(BottomNavItems.Cases)
                    }

                    mainViewModel.clearEvents()
                }

                MainUiEvent.OpenMoreTab -> {
                    val moreIndex = BottomNavItems.entries.indexOf(BottomNavItems.More)
                    println("HomePagerRoute: moving to More page = $moreIndex")

                    if (moreIndex != -1) {
                        pagerState.scrollToPage(moreIndex)
                        mainViewModel.selectBottomTab(BottomNavItems.More)
                    }

                    mainViewModel.clearEvents()
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

                BottomNavItems.More -> Unit
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
                    initialTab = forcedCaseTab,
                    onTabConsumed = { forcedCaseTab = null },
                    onNavigateToDetails = { interview, sourceTab ->
                        forcedCaseTab = null // Clear it once we navigate away or interact
                        mainViewModel.openCase(
                            interview = interview,
                            sourceTab = sourceTab
                        )
                    }
                )

                BottomNavItems.More -> DashboardScreen(
                    onNavigate = { route ->
                        mainViewModel.backStack.add(route)
                    }
                )
            }
        }
    }
}