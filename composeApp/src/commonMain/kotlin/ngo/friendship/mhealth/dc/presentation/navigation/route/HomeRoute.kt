package ngo.friendship.mhealth.dc.presentation.navigation.route

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import kotlinx.coroutines.launch
import ngo.friendship.mhealth.dc.presentation.MainUiEvent
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.components.CustomTopBar
import ngo.friendship.mhealth.dc.presentation.navigation.BottomNavItems
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.navigation.components.BottomBar
import ngo.friendship.mhealth.dc.presentation.screen.case.case_list.components.CaseTab
import ngo.friendship.mhealth.dc.theme.Resources

fun EntryProviderScope<NavKey>.homeRoute(
    viewModel: MainViewModel
) {
    entry<Screens.Main> {
        val caseTabCounts by viewModel.caseTabCounts.collectAsStateWithLifecycle()
        val notificationCount = caseTabCounts[CaseTab.Pending] ?: 0
        val userProfile by viewModel.userProfileState.collectAsStateWithLifecycle()
        val networkStatus by viewModel.networkStatus.collectAsStateWithLifecycle()
        val pagerState = rememberPagerState(
            initialPage = viewModel.selectedBottomTab.ordinal,
            pageCount = { BottomNavItems.entries.size }
        )
        val scope = rememberCoroutineScope()

        LaunchedEffect(userProfile) {
            println("HomeRoute: userProfile changed: $userProfile")
            println("HomeRoute: mobileNo: ${userProfile?.mobileNo}")
            println("HomeRoute: userName: ${userProfile?.userName}, location: ${userProfile?.location}")
        }

        LaunchedEffect(Unit) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is MainUiEvent.OpenCasesTab -> {
                        viewModel.selectBottomTab(BottomNavItems.Cases)
                        viewModel.clearEvents()
                    }

                    MainUiEvent.OpenMoreTab -> {
                        viewModel.selectBottomTab(BottomNavItems.More)
                        viewModel.clearEvents()
                    }

                    else -> {}
                }
            }
        }

        LaunchedEffect(Unit) {
            snapshotFlow { pagerState.currentPage }.collect { page ->
                viewModel.selectBottomTab(BottomNavItems.entries[page])
                viewModel.refreshServerStatus()
                if (page == BottomNavItems.Cases.ordinal)
                    viewModel.loadInterviewList()
            }
        }

        LaunchedEffect(viewModel.selectedBottomTab) {
            if (pagerState.currentPage != viewModel.selectedBottomTab.ordinal)
                pagerState.animateScrollToPage(viewModel.selectedBottomTab.ordinal)
        }

        LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
            viewModel.refreshServerStatus()
            if (viewModel.selectedBottomTab == BottomNavItems.Cases || viewModel.interviewListState.value.isEmpty())
                viewModel.loadInterviewList()
        }

        Scaffold(
            topBar = {
                CustomTopBar(
                    notificationIcon = Resources.Icon.Notification,
                    notificationCount = notificationCount,
                    onNotificationClick = {
                        viewModel.openCasesTab(CaseTab.Pending)
                    },
                    userName = userProfile?.userName ?: "Doctor Center",
                    userSubtitle = userProfile?.location ?: "Friendship NGO",
                    profileImage = userProfile?.getProfileImageSource() ?: Resources.Icon.Profile,
                    onProfileClick = { viewModel.backStack.add(Screens.Dialog.ProfilePopup) },
                    networkStatus = networkStatus
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