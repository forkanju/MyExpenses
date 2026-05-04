package ngo.friendship.mhealth.dc.presentation.screens.profile.beneficiary

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ngo.friendship.mhealth.dc.domain.model.Interview
import ngo.friendship.mhealth.dc.domain.repository.CaseRepository
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel

class BeneficiaryProfileViewModel(
    private val repository: CaseRepository,
) : BaseViewModel() {

    private val _state = MutableStateFlow(BeneficiaryProfileUiState())
    val state = _state.asStateFlow()

    private val _effect = Channel<BeneficiaryProfileUiEffect>()
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: BeneficiaryProfileIntent) {
        when (intent) {
            is BeneficiaryProfileIntent.LoadProfile -> {
                 _state.update { 
                     it.copy(
                         beneficiaryId = intent.beneficiaryId,
                         beneficiaryName = intent.beneficiaryName,
                         beneficiaryAge = intent.beneficiaryAge,
                         location = intent.location,
                         questionnaireName = intent.questionnaireName
                     ) 
                 }
                 loadBeneficiaryCases()
            }
            is BeneficiaryProfileIntent.SelectTab -> {
                _state.update { it.copy(selectedTab = intent.index) }
            }
            BeneficiaryProfileIntent.NavigateBack -> {
                launch { _effect.send(BeneficiaryProfileUiEffect.NavigateBack) }
            }
        }
    }

    private fun loadBeneficiaryCases() {
        // Placeholder for loading cases for a specific beneficiary
        // For now, we might load a generic list or just keep it empty
    }
}
