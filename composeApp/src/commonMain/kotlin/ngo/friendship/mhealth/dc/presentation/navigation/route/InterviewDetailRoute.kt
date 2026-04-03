@file:JvmName("PrescriptionRouteKt")

package ngo.friendship.mhealth.dc.presentation.navigation.route

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.BottomNavItems
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.navigation.components.entryWithVM
import ngo.friendship.mhealth.dc.presentation.screens.main.prescription_form.PrescriptionFormViewModel
import ngo.friendship.mhealth.dc.presentation.screens.main.prescription_form.PrescriptionFormScreen
import kotlin.jvm.JvmName

fun EntryProviderScope<NavKey>.detailRoute(
    mainViewModel: MainViewModel,
    snackBarState: SnackbarHostState
) {
    entryWithVM<Screens.PrescriptionForm, PrescriptionFormViewModel>(
        backStack = mainViewModel.backStack,
        snackBarState = snackBarState
    ) { screen ->
        val interviewDetails by viewModel.interviewDetailsState.collectAsState()
        val setupData by mainViewModel.setupDataState.collectAsState()
        val medicineList by viewModel.medicineListState.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.loadInterviewDetails(screen.interviewId)
        }

        PrescriptionFormScreen(
            setupData = setupData,
            interviewDetails = interviewDetails,
            medicineList = medicineList,
            onSave = {
                viewModel.saveDoctorFeedback(formState = it) {
                    mainViewModel.selectBottomTab(BottomNavItems.Cases)
                }
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
            onBack = mainViewModel.backStack::removeLastOrNull
        )
    }
}