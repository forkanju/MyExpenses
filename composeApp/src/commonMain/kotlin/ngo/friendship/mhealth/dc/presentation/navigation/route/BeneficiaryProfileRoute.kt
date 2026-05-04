package ngo.friendship.mhealth.dc.presentation.navigation.route

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.screens.profile.beneficiary.BeneficiaryProfileScreen
import ngo.friendship.mhealth.dc.presentation.MainViewModel

fun EntryProviderScope<NavKey>.beneficiaryProfileRoute(
    mainViewModel: MainViewModel
) {
    entry<Screens.BeneficiaryProfile> { screen ->
        BeneficiaryProfileScreen(
            beneficiaryId = screen.beneficiaryId,
            beneficiaryName = screen.beneficiaryName,
            beneficiaryAge = screen.beneficiaryAge,
            location = screen.location,
            questionnaireName = screen.questionnaireName,
            onBack = {
                mainViewModel.backStack.removeLastOrNull()
            }
        )
    }
}
