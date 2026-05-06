package ngo.friendship.mhealth.dc.presentation.screens.case.case_detail.model

data class CustomMessageState(
    val messageText: String = "",
    val isFcmChecked: Boolean = false,
    val isBeneficiaryChecked: Boolean = false,
    val phoneNumber: String = ""
)