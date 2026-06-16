package ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.model

data class CustomMessageState(
    val messageText: String = "",
    val isFcmChecked: Boolean = true,
    val isBeneficiaryChecked: Boolean = true,
    val phoneNumber: String = ""
)