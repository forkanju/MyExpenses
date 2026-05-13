package ngo.friendship.mhealth.dc.presentation.navigation.route

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.screen.case.CaseViewModel
import ngo.friendship.mhealth.dc.presentation.screens.case.case_detail.LocalCaseDetailScreen
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.AdviceTemplateListScreen
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.DxListScreen
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.DxListViewModel
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.InvestigationsListScreen
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.LocalTreatmentDetailsScreen
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.LocalTreatmentScreen
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.MedicineListScreen
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.MedicineListViewModel
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.PrescriptionTemplateListScreen
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<NavKey>.dashboardRoute(
    mainViewModel: MainViewModel
) {
    entry<Screens.PrescriptionTemplateList> {
        PrescriptionTemplateListScreen(
            onBack = { mainViewModel.backStack.removeLastOrNull() },
            onAddTemplate = {
                mainViewModel.backStack.add(
                    Screens.CaseDetail(
                        interviewId = -1L,
                        source = Screens.CaseDetail.SOURCE_TEMPLATE_LIST
                    )
                )
            },
            onTemplateClick = { template ->
                mainViewModel.backStack.add(
                    Screens.CaseDetail(
                        interviewId = -1L,
                        source = Screens.CaseDetail.SOURCE_TEMPLATE_LIST,
                        template = template
                    )
                )
            }
        )
    }

    entry<Screens.DxList> {
        val viewModel = koinViewModel<DxListViewModel>()
        DxListScreen(
            viewModel = viewModel,
            onBack = { mainViewModel.backStack.removeLastOrNull() }
        )
    }

    entry<Screens.MedicineList> {
        val viewModel = koinViewModel<MedicineListViewModel>()
        MedicineListScreen(
            viewModel = viewModel,
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
            onNavigateToDetails = {
                mainViewModel.backStack.add(
                    Screens.LocalTreatmentDetails(
                        patientId = ""
                    )
                )
            },
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
            },
            onMoreClick = {
                mainViewModel.openMoreTab()
            }
        )
    }

    entry<Screens.LocalTreatmentDetails> {
        LocalTreatmentDetailsScreen(
            onBack = { mainViewModel.backStack.removeLastOrNull() }
        )
    }
}
