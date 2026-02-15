package ngo.friendship.mhealth.dc.presentation.navigation.route

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.navigation.components.entryWithVM
import ngo.friendship.mhealth.dc.presentation.screens.main.case.InterviewDetailScreen
import ngo.friendship.mhealth.dc.presentation.screens.main.case.InterviewDetailsViewModel

fun EntryProviderScope<NavKey>.detailRoute(
    mainViewModel: MainViewModel,
    snackBarState: SnackbarHostState
) {
    entryWithVM<Screens.InterviewDetails, InterviewDetailsViewModel>(
        backStack = mainViewModel.backStack,
        snackBarState = snackBarState
    ) {
        InterviewDetailScreen()
    }
}