package ngo.friendship.mhealth.dc.domain.model

data class BeneficiaryProfile(
    val benefName: String,
    val benefCode: String,
    val benefId: Long,
    val locationName: String,
    val dob: String,
    val gender: String,
    val mobileNumber: String,
    val guardianName: String,
    val relationGuardian: String,
    val maritalStatus: String,
    val occupation: String,
    val religion: String,
    val serviceList: List<BeneficiaryServiceItem>
)

data class BeneficiaryServiceItem(
    val status: String,
    val referredTo: String,
    val lastOpenedBy: String,
    val caseName: String,
    val interviewTime: String
)
