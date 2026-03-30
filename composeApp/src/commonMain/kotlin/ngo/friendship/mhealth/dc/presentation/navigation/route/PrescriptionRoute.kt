package ngo.friendship.mhealth.dc.presentation.navigation.route

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ngo.friendship.mhealth.dc.domain.model.SetupData
import ngo.friendship.mhealth.dc.presentation.screens.main.case.InterviewDetailsViewModel
import ngo.friendship.mhealth.dc.presentation.screens.main.case.MedicineListViewModel
import ngo.friendship.mhealth.dc.presentation.screens.main.case.SetupDataViewModel
import ngo.friendship.mhealth.dc.presentation.screens.main.case.components.case_detail.PrescriptionScreen
import ngo.friendship.mhealth.dc.presentation.state.RequestState
import ngo.friendship.mhealth.dc.theme.errorLight
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PrescriptionRoute(
    interviewId: Long,
    modifier: Modifier = Modifier,
    interviewDetailsViewModel: InterviewDetailsViewModel,
    setupDataViewModel: SetupDataViewModel = koinViewModel(),
    medicineListViewModel: MedicineListViewModel = koinViewModel()
) {
    val detailsState by interviewDetailsViewModel.interviewDetailsState.collectAsState()
    val setupDataState by setupDataViewModel.setupDataState.collectAsState()
    val medicineListState by medicineListViewModel.medicineListState.collectAsState()


    LaunchedEffect(interviewId) {
        interviewDetailsViewModel.loadInterviewDetails(
            userName = "sharif.dr",
            password = "1234",
            interviewId = interviewId
        )
        setupDataViewModel.loadSetupData(
            userName = "sharif.dr",
            password = "1234"
        )
        medicineListViewModel.loadMedicineList(
            userName = "sharif.dr",
            password = "1234",
            type = "Tab"
        )
    }

    when {
        detailsState is RequestState.Idle ||
                detailsState is RequestState.Loading ||
                setupDataState is RequestState.Idle ||
                setupDataState is RequestState.Loading
            -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        detailsState is RequestState.Error -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (detailsState as RequestState.Error).message,
                    color = errorLight
                )
            }
        }

        setupDataState is RequestState.Error -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (setupDataState as RequestState.Error).message,
                    color = errorLight
                )
            }
        }

        detailsState is RequestState.Success &&
                setupDataState is RequestState.Success -> {
            PrescriptionScreen(
                modifier = modifier,
                interviewDetailsState = detailsState,
                setupData = (setupDataState as RequestState.Success<SetupData>).data,
                medicineListState = medicineListState
            )
        }

        else -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No data available")
            }
        }
    }
}