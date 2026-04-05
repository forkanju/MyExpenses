package ngo.friendship.mhealth.dc.presentation.screens.main.case_list.model

data class CaseItemUi(
    val id: String, // ✅ REQUIRED for key
    val name: String,
    val gender: String,
    val age: Int,
    val locationLine: String,
    val problemTitle: String,
    val refBy: String,
    val atText: String,
    val interviewText: String,
    val avatarText: String = "",
    val badgeTop: String = "00:25",
    val badgeBottom: String = "30230",
)


