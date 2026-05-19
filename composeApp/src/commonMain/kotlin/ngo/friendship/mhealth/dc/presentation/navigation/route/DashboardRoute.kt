package ngo.friendship.mhealth.dc.presentation.navigation.route

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.screen.case.CaseViewModel
import ngo.friendship.mhealth.dc.presentation.screens.case.case_detail.LocalCaseDetailScreen
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.AdviceTemplateListScreen
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.DxListScreen
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.DashboardScreen
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.DxListViewModel
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.InvestigationsListScreen
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.LocalTreatmentDetailsScreen
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.LocalTreatmentScreen
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.MedicineListScreen
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.MedicineListViewModel
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.PrescriptionTemplateListScreen
import ngo.friendship.mhealth.dc.theme.PrimaryBlue
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
fun EntryProviderScope<NavKey>.dashboardRoute(
    mainViewModel: MainViewModel
) {
    entry<Screens.Dashboard> {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Dashboard",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { mainViewModel.backStack.removeLastOrNull() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = PrimaryBlue
                    )
                )
            }
        ) { padding ->
            DashboardScreen(
                onNavigate = { route ->
                    mainViewModel.backStack.add(route)
                },
                modifier = Modifier.padding(padding)
            )
        }
    }

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
