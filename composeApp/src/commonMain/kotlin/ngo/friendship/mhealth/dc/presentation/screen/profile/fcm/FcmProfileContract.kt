package ngo.friendship.mhealth.dc.presentation.screen.profile.fcm

import ngo.friendship.mhealth.dc.domain.model.FcmProfile

data class FcmProfileUiState(
    val beneficiaryId: Long = -1L,
    val isLoading: Boolean = false,
    val fcmProfile: FcmProfile? = null,
    val selectedTab: Int = 0, // Service list (07)
    val cases: List<FcmCaseItem> = emptyList()
)

data class FcmCaseItem(
    val id: String,
    val title: String,
    val dx: String,
    val prescription: String,
    val date: String,
    val imageUrl: String? = null
)

sealed interface FcmProfileIntent {
    data class LoadProfile(val fcmCode: String) : FcmProfileIntent
    data class SetFcmProfile(val fcmProfile: FcmProfile?) : FcmProfileIntent
    data class SelectTab(val index: Int) : FcmProfileIntent
    data object NavigateBack : FcmProfileIntent
}

sealed interface FcmProfileUiEffect {
    data object NavigateBack : FcmProfileUiEffect
}
