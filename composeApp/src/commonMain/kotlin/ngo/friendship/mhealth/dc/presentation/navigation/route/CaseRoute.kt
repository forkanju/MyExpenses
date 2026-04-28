@file:JvmName("CaseRouteKt")

package ngo.friendship.mhealth.dc.presentation.navigation.route

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.navigation.components.entryWithVM
import ngo.friendship.mhealth.dc.presentation.screens.case.CaseIntent
import ngo.friendship.mhealth.dc.presentation.screens.case.CaseUiEvent
import ngo.friendship.mhealth.dc.presentation.screens.case.CaseViewModel
import ngo.friendship.mhealth.dc.presentation.screens.case.case_detail.CaseDetailScreen
import kotlin.jvm.JvmName

fun EntryProviderScope<NavKey>.caseRoute(
    mainViewModel: MainViewModel,
    snackBarState: SnackbarHostState
) {
    entryWithVM<Screens.CaseDetail, CaseViewModel>(
        backStack = mainViewModel.backStack as androidx.navigation3.runtime.NavBackStack<NavKey>,
        snackBarState = snackBarState
    ) { screen ->
        val state by viewModel.state.collectAsState()
        val setupData by mainViewModel.setupDataState.collectAsState()

        LaunchedEffect(screen.interviewId) {
            viewModel.onIntent(CaseIntent.LoadInterviewDetails(screen.interviewId))
            viewModel.onIntent(CaseIntent.LoadQuestionAnswerData(screen.interviewId))
        }

        LaunchedEffect(Unit) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is CaseUiEvent.ShowSnackbar -> {
                        snackBarState.showSnackbar(event.message)
                    }
                    CaseUiEvent.NavigateBack -> {
                        backStack.removeLastOrNull()
                    }
                }
            }
        }

        CaseDetailScreen(
            state = state,
            setupData = setupData,
            mode = screen.mode,
            source = screen.source,
            onIntent = viewModel::onIntent,
            onFcmDetailsClick = {
                println("Fcm details clicked")
            },
            onCall = {
                println("Call clicked")
            },
            onWhatsApp = {
                println("WhatsApp clicked")
            },
            onBack = backStack::removeLastOrNull
        )
    }
}