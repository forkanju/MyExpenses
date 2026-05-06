package ngo.friendship.mhealth.dc.presentation.screens.profile.beneficiary

import ngo.friendship.mhealth.dc.domain.model.BeneficiaryProfile
import ngo.friendship.mhealth.dc.domain.model.Interview

data class BeneficiaryProfileUiState(
    val beneficiaryId: Long = -1L,
    val beneficiaryCode: String = "",
    val beneficiaryName: String = "",
    val beneficiaryAge: String = "",
    val location: String = "",
    val questionnaireName: String = "",
    val isLoading: Boolean = false,
    val selectedTab: Int = 0, // Service list (07)
    val beneficiaryProfile: BeneficiaryProfile? = null
)

sealed interface BeneficiaryProfileIntent {
    data class LoadProfile(
        val beneficiaryId: Long,
        val beneficiaryCode: String = "",
        val beneficiaryName: String = "",
        val beneficiaryAge: String = "",
        val location: String = "",
        val questionnaireName: String = ""
    ) : BeneficiaryProfileIntent
    data class SelectTab(val index: Int) : BeneficiaryProfileIntent
    data object NavigateBack : BeneficiaryProfileIntent
}

sealed interface BeneficiaryProfileUiEffect {
    data object NavigateBack : BeneficiaryProfileUiEffect
}
