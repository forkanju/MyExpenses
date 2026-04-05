@file:JvmName("PrescriptionRouteKt")

package ngo.friendship.mhealth.dc.presentation.navigation.route

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.delay
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.navigation.components.entryWithVM
import ngo.friendship.mhealth.dc.presentation.screens.case.CaseViewModel
import ngo.friendship.mhealth.dc.presentation.screens.main.prescription_form.PrescriptionFormScreen
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

        LaunchedEffect(Unit) {
            viewModel.loadInterviewDetails(screen.interviewId)
            delay(500L)
            snapshotFlow { isLoading }.collect {
                if (!isLoading && interviewDetails.interviewId == -1L)
                    backStack.removeLastOrNull()
            }
        }

        PrescriptionFormScreen(
            setupData = setupData,
            interviewDetails = interviewDetails,
            medicineList = medicineList,
            onSave = {
                viewModel.saveDoctorFeedback(formState = it)
            },
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