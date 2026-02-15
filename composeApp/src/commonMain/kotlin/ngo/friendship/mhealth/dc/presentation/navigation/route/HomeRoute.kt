package ngo.friendship.mhealth.dc.presentation.navigation.route

import CustomTopBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.launch
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.BottomNavItems
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.navigation.components.BottomBar
import ngo.friendship.mhealth.dc.presentation.screens.auth.InterviewListViewModel
import ngo.friendship.mhealth.dc.theme.Resources
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<NavKey>.homeRoute(
    viewModel: MainViewModel
) {
    entry<Screens.Main> {
        val pagerState = rememberPagerState(pageCount = { BottomNavItems.entries.size })
        val scope = rememberCoroutineScope()

        //inject InterviewListViewModel
        val interviewVm = koinViewModel<InterviewListViewModel>().apply {
            backStack = viewModel.backStack
        }
        val interviewList by interviewVm.interviewListState.collectAsStateWithLifecycle()

        Scaffold(
            topBar = {
                CustomTopBar(
                    notificationIcon = Resources.Icon.Notification,    // bell
                    notificationCount = 10,
                    onNotificationClick = { /* open notifications */ },
                    userName = "Dr. Ahmed Imtiaz Abir",
                    userSubtitle = "Doctor Center, Head Office",
                    profileIcon = Resources.Icon.Profile,
                    onProfileClick = { viewModel.backStack.add(Screens.Dialog.ProfilePopup) }
                )
            },
            bottomBar = {

                Box(modifier = Modifier.navigationBarsPadding()) {
                    BottomBar(
                        pagerState = pagerState,
                        onItemClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(it)
                            }
                        }
                    )
                }

            }
        ) { innerPadding ->
            HomePagerRoute(
                pagerState = pagerState,
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding),
                interviewVm = interviewVm,
                interviewList = interviewList
            )
        }
//        BackHandler(pagerState.currentPage != 0) {
//
//        }
    }

}