@file:JvmName("CaseRouteKt")

package ngo.friendship.mhealth.dc.presentation.navigation.route

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.navigation.components.entryWithVM
import ngo.friendship.mhealth.dc.presentation.screens.case.CaseIntent
import ngo.friendship.mhealth.dc.presentation.screens.case.CaseUiEvent
import ngo.friendship.mhealth.dc.presentation.screens.case.CaseViewModel
import ngo.friendship.mhealth.dc.presentation.screens.case.case_detail.CaseDetailScreen
import ngo.friendship.mhealth.dc.presentation.screens.case.case_list.components.CaseTab
import kotlin.jvm.JvmName

fun EntryProviderScope<NavKey>.caseRoute(
    mainViewModel: MainViewModel,
    snackBarState: SnackbarHostState
) {
    entryWithVM<Screens.CaseDetail, CaseViewModel>(
        backStack = mainViewModel.backStack,
        snackBarState = snackBarState
    ) { screen ->
        val uriHandler = LocalUriHandler.current
        val state by viewModel.state.collectAsStateWithLifecycle()
        val setupData by mainViewModel.setupDataState.collectAsStateWithLifecycle()
        val userProfile by mainViewModel.userProfileState.collectAsStateWithLifecycle()

        LaunchedEffect(screen.interviewId) {
            viewModel.onIntent(CaseIntent.LoadInterviewDetails(screen.interviewId))
            viewModel.onIntent(CaseIntent.LoadQuestionAnswerData(screen.interviewId))
        }

        LaunchedEffect(screen.mode) {
            viewModel.onIntent(CaseIntent.SetMode(screen.mode))
        }

        LaunchedEffect(screen.selectedTab) {
            // Find the CaseTab that matches the selectedTab string
            val tab = CaseTab.entries
                .find { it.label == screen.selectedTab }
                ?: CaseTab.Pending
            viewModel.onIntent(CaseIntent.SetSelectedTab(tab))
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
                state.interviewDetails.fcmInfo?.takeIf { it.isNotBlank() }?.let { fcmCode ->
                     backStack.add(Screens.FcmProfile(fcmCode = fcmCode))
                }
            },
            onBeneficiaryDetailsClick = {
                 backStack.add(
                     Screens.BeneficiaryProfile(
                        beneficiaryId = state.interviewDetails.beneficiaryId,
                        beneficiaryName = state.interviewDetails.beneficiaryName,
                        beneficiaryAge = state.interviewDetails.beneficiaryAge ?: "",
                        location = state.interviewDetails.location,
                        questionnaireName = state.interviewDetails.questionnaireName
                     )
                 )
            },
            onCall = {
                userProfile?.mobileNo?.takeIf { it.isNotBlank() }?.let { mobile ->
                    uriHandler.openUri("tel:$mobile")
                }
            },
            onWhatsApp = {
                userProfile?.mobileNo?.takeIf { it.isNotBlank() }?.let { mobile ->
                    uriHandler.openUri("https://api.whatsapp.com/send?phone=$mobile")
                }
            },
            onBack = backStack::removeLastOrNull
        )
    }
}