@file:JvmName("PrescriptionRouteKt")

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
import ngo.friendship.mhealth.dc.presentation.screens.case.CaseUiEvent
import ngo.friendship.mhealth.dc.presentation.screens.case.CaseViewModel
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.PrescriptionFormScreen
import kotlin.jvm.JvmName

fun EntryProviderScope<NavKey>.caseRoute(
    mainViewModel: MainViewModel,
    snackBarState: SnackbarHostState
) {
    entryWithVM<Screens.PrescriptionForm, CaseViewModel>(
        backStack = mainViewModel.backStack,
        snackBarState = snackBarState
    ) { screen ->
        val interviewDetails by viewModel.interviewDetailsState.collectAsState()
        val setupData by mainViewModel.setupDataState.collectAsState()
        val medicineList by viewModel.medicineListState.collectAsState()
        val formState by viewModel.formState.collectAsState()

        LaunchedEffect(screen.interviewId) {
            viewModel.loadInterviewDetails(screen.interviewId)
            viewModel.loadQuestionAnswerData(screen.interviewId)
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

        PrescriptionFormScreen(
            formState = formState,
            setupData = setupData,
            interviewDetails = interviewDetails,
            medicineList = medicineList,
            mode = screen.mode,
            source = screen.source,
            onUpdate = viewModel::updateFormState,
            onSave = viewModel::saveDoctorFeedback,
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