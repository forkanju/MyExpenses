package ngo.friendship.mhealth.dc.presentation.navigation.route

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import org.koin.compose.viewmodel.koinViewModel
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.screens.more.*

fun EntryProviderScope<NavKey>.moreRoute(
    mainViewModel: MainViewModel
) {
    entry<Screens.PrescriptionTemplateList> {
        val viewModel = koinViewModel<MoreViewModel>()
        val templates by viewModel.templates.collectAsStateWithLifecycle()
        PrescriptionTemplateListScreen(
            templates = templates,
            onBack = { mainViewModel.backStack.removeLastOrNull() }
        )
    }

    entry<Screens.DxList> {
        DxListScreen(
            onBack = { mainViewModel.backStack.removeLastOrNull() }
        )
    }

    entry<Screens.MedicineList> {
        MedicineListScreen(
            onBack = { mainViewModel.backStack.removeLastOrNull() }
        )
    }

    entry<Screens.AdviceTemplateList> {
        AdviceTemplateListScreen(
            onBack = { mainViewModel.backStack.removeLastOrNull() }
        )
    }

    entry<Screens.InvestigationsList> {
        InvestigationsListScreen(
            onBack = { mainViewModel.backStack.removeLastOrNull() }
        )
    }

    entry<Screens.LocalTreatment> {
        LocalTreatmentScreen(
            onBack = { mainViewModel.backStack.removeLastOrNull() },
            onNavigateToDetails = { mainViewModel.backStack.add(Screens.LocalTreatmentDetails(patientId = "")) }
        )
    }

    entry<Screens.LocalTreatmentDetails> {
        LocalTreatmentDetailsScreen(
            onBack = { mainViewModel.backStack.removeLastOrNull() }
        )
    }
}
