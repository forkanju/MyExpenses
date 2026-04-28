package ngo.friendship.mhealth.dc.presentation.navigation.route

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ngo.friendship.mhealth.dc.domain.model.SetupData
import ngo.friendship.mhealth.dc.presentation.screens.case.CaseViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import org.koin.compose.viewmodel.koinViewModel
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.screens.case.case_detail.LocalCaseDetailScreen
import ngo.friendship.mhealth.dc.presentation.screens.more.*

fun EntryProviderScope<NavKey>.moreRoute(
    mainViewModel: MainViewModel
) {
    entry<Screens.PrescriptionTemplateList> {
        val viewModel = koinViewModel<MoreViewModel>()
        val templates by viewModel.templates.collectAsStateWithLifecycle()
        PrescriptionTemplateListScreen(
            templates = templates,
            onBack = { mainViewModel.backStack.removeLastOrNull() },
            onAddTemplate = {
                mainViewModel.backStack.add(
                    Screens.CaseDetail(
                        interviewId = -1L,
                        source = Screens.CaseDetail.SOURCE_TEMPLATE_LIST
                    )
                )
            }
        )
    }

    entry<Screens.DxList> {
        DxListScreen(
            viewModel = mainViewModel,
            onBack = { mainViewModel.backStack.removeLastOrNull() }
        )
    }

    entry<Screens.MedicineList> {
        MedicineListScreen(
            viewModel = mainViewModel,
            onBack = { mainViewModel.backStack.removeLastOrNull() }
        )
    }

    entry<Screens.AdviceTemplateList> {
        AdviceTemplateListScreen(
            viewModel = mainViewModel,
            onBack = { mainViewModel.backStack.removeLastOrNull() }
        )
    }

    entry<Screens.InvestigationsList> {
        InvestigationsListScreen(
            viewModel = mainViewModel,
            onBack = { mainViewModel.backStack.removeLastOrNull() }
        )
    }

    entry<Screens.LocalTreatment> {
        LocalTreatmentScreen(
            onBack = { mainViewModel.backStack.removeLastOrNull() },
            onNavigateToDetails = { mainViewModel.backStack.add(Screens.LocalTreatmentDetails(patientId = "")) },
            onAddClick = { mainViewModel.backStack.add(Screens.LocalPrescriptionForm) }
        )
    }

    entry<Screens.LocalPrescriptionForm> {
        val viewModel = koinViewModel<CaseViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val setupData by mainViewModel.setupDataState.collectAsState()
        LocalCaseDetailScreen(
            state = state,
            setupData = setupData,
            onIntent = viewModel::onIntent,
            onBack = { mainViewModel.backStack.removeLastOrNull() },
            onSave = {
                // Handle save
                mainViewModel.backStack.removeLastOrNull()
            }
        )
    }

    entry<Screens.LocalTreatmentDetails> {
        LocalTreatmentDetailsScreen(
            onBack = { mainViewModel.backStack.removeLastOrNull() }
        )
    }
}
