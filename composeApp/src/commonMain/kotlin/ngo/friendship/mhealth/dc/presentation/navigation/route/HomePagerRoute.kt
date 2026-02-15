package ngo.friendship.mhealth.dc.presentation.navigation.route

import HealthDashboardScreen
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ngo.friendship.mhealth.dc.domain.model.Interview
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.BottomNavItems
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.screens.auth.InterviewListViewModel
import ngo.friendship.mhealth.dc.presentation.screens.main.case.CaseScreen
import ngo.friendship.mhealth.dc.presentation.screens.main.home.HomeScreen

@Composable
fun HomePagerRoute(
    pagerState: PagerState,
    viewModel: MainViewModel,
    modifier: Modifier,
    interviewVm: InterviewListViewModel,
    interviewList: List<Interview>,
) {
    HorizontalPager(
        state = pagerState,
    ) { page ->
        when (BottomNavItems.entries[page]) {
            BottomNavItems.Home -> HomeScreen(
                modifier = modifier
            )

            BottomNavItems.Case -> CaseScreen(
                modifier = modifier,
                interviewVm = interviewVm,
                interviewList = interviewList,
                onCaseClick = { interview ->
                    // তুমি চাইলে এখানে details screen এ navigate করবে
                    viewModel.backStack.add(Screens.InterviewDetails(interview.interviewId))
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