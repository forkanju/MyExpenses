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
import ngo.friendship.mhealth.dc.presentation.screen.case.CaseIntent
import ngo.friendship.mhealth.dc.presentation.screen.case.CaseUiEvent
import ngo.friendship.mhealth.dc.presentation.base.ColoredSnackbarVisuals
import ngo.friendship.mhealth.dc.presentation.screen.case.CaseViewModel
import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.CaseDetailScreen
import ngo.friendship.mhealth.dc.presentation.screen.case.case_list.components.CaseTab
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

        LaunchedEffect(
            screen.interviewId,
            screen.mode,
            screen.selectedTab,
            screen.template
        ) {
            val selectedTab = CaseTab.entries
                .find {
                    it.name == screen.selectedTab ||
                            it.label == screen.selectedTab ||
                            it.apiParam == screen.selectedTab
                }
                ?: CaseTab.Pending

            println("DEBUG_CASE_ROUTE screen.selectedTab=${screen.selectedTab}, resolved=$selectedTab")

            viewModel.onIntent(CaseIntent.SetMode(screen.mode))
            viewModel.onIntent(CaseIntent.SetSelectedTab(selectedTab))

            if (screen.template != null) {
                viewModel.onIntent(CaseIntent.LoadFromTemplate(screen.template))
            } else {
                viewModel.onIntent(
                    CaseIntent.OpenCaseFromTab(
                        interviewId = screen.interviewId,
                        sourceTab = selectedTab
                    )
                )
            }
        }

        LaunchedEffect(Unit) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is CaseUiEvent.ShowSnackbar -> {
                        snackBarState.showSnackbar(
                            ColoredSnackbarVisuals(
                                message = event.message,
                                type = event.type
                            )
                        )
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
                        beneficiaryCode = state.interviewDetails.beneficiaryCode,
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
            onMoreClick = {
                backStack.add(Screens.Dashboard)
            },
            onBack = backStack::removeLastOrNull
        )
    }
}