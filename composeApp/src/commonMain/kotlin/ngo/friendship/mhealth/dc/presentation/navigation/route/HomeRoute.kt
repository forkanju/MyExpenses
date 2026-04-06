package ngo.friendship.mhealth.dc.presentation.navigation.route

import CustomTopBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import kotlinx.coroutines.launch
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.BottomNavItems
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.navigation.components.BottomBar
import ngo.friendship.mhealth.dc.theme.Resources

fun EntryProviderScope<NavKey>.homeRoute(
    viewModel: MainViewModel
) {
    entry<Screens.Main> {
        val pagerState = rememberPagerState(pageCount = { BottomNavItems.entries.size })
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            snapshotFlow { pagerState.currentPage }.collect { page ->
                viewModel.selectBottomTab(BottomNavItems.entries[page])
                if (page == BottomNavItems.Cases.ordinal)
                    viewModel.loadInterviewList()
            }
        }

        LaunchedEffect(viewModel.selectedBottomTab) {
            if (pagerState.currentPage != viewModel.selectedBottomTab.ordinal)
                pagerState.animateScrollToPage(viewModel.selectedBottomTab.ordinal)
        }

        LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
            if (viewModel.selectedBottomTab == BottomNavItems.Cases || viewModel.interviewListState.value.isEmpty())
                viewModel.loadInterviewList()
        }

        Scaffold(
            topBar = {
                CustomTopBar(
                    notificationIcon = Resources.Icon.Notification,
                    notificationCount = 10,
                    onNotificationClick = {
                        println("Notification clicked")
                    },
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
                mainViewModel = viewModel,
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
            )
        }
        NavigationBackHandler(
            state = rememberNavigationEventState(NavigationEventInfo.None),
            isBackEnabled = pagerState.currentPage != 0,
            onBackCompleted = {
                scope.launch {
                    pagerState.animateScrollToPage(0)
                }
            }
        )
    }

}