package ngo.friendship.mhealth.dc.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BeneficiaryProfileResDto(
    @SerialName("responseType") val responseType: String? = null,
    @SerialName("data") val data: Data? = null,
    @SerialName("errorDesc") val errorDesc: String? = null,
    @SerialName("responseCode") val responseCode: String? = null,
    @SerialName("responseName") val responseName: String? = null,
    @SerialName("param1") val param1: Map<String, String>? = null
) {
    @Serializable
    data class Data(
        @SerialName("beneficiaryProfile") val beneficiaryProfile: BeneficiaryProfile? = null
    )

    @Serializable
    data class BeneficiaryProfile(
        @SerialName("GUARDIAN_NAME") val guardianName: String? = null,
        @SerialName("ORG_ID") val orgId: Int? = null,
        @SerialName("BENEF_NAME") val benefName: String? = null,
        @SerialName("BIRTH_REG_NUMBER") val birthRegNumber: String? = null,
        @SerialName("MOBILE_NUMBER") val mobileNumber: String? = null,
        @SerialName("MARITAL_STATUS") val maritalStatus: String? = null,
        @SerialName("HH_NUMBER") val hhNumber: String? = null,
        @SerialName("AGREED_MOBILE_COMM") val agreedMobileComm: String? = null,
        @SerialName("MOBILE_COMM") val mobileComm: String? = null,
        @SerialName("STATE") val state: Int? = null,
        @SerialName("USER_ID") val userId: Int? = null,
        @SerialName("RECORD_DATE") val recordDate: String? = null,
        @SerialName("GUARDIAN_NAME_LOCAL") val guardianNameLocal: String? = null,
        @SerialName("BENEF_EPI") val benefEpi: String? = null,
        @SerialName("LAST_UPDATE_DATE") val lastUpdateDate: String? = null,
        @SerialName("BENEF_ID") val benefId: Long? = null,
        @SerialName("MOBILE_COMM_LANG") val mobileCommLang: String? = null,
        @SerialName("FAMILY_HEAD") val familyHead: Int? = null,
        @SerialName("RELATION_GUARDIAN") val relationGuardian: String? = null,
        @SerialName("NATIONAL_ID") val nationalId: String? = null,
        @SerialName("DOB") val dob: String? = null,
        @SerialName("BENEF_CODE") val benefCode: String? = null,
        @SerialName("GENDER") val gender: String? = null,
        @SerialName("LOCATION_ID") val locationId: Int? = null,
        @SerialName("service_list") val serviceList: List<ServiceItem> = emptyList(),
        @SerialName("VERSION_NO") val versionNo: Int? = null,
        @SerialName("BID") val bid: Long? = null,
        @SerialName("OCCUPATION_HER_HUSBAND") val occupationHerHusband: String? = null,
        @SerialName("REF_DATA_ID") val refDataId: Long? = null,
        @SerialName("BENEF_COVID") val benefCovid: String? = null,
        @SerialName("HH_ID") val hhId: Int? = null,
        @SerialName("EDU_LEVEL") val eduLevel: String? = null,
        @SerialName("BENEF_NAME_LOCAL") val benefNameLocal: String? = null,
        @SerialName("REG_DATE") val regDate: String? = null,
        @SerialName("BENEF_TT") val benefTt: String? = null,
        @SerialName("CREATE_DATE") val createDate: String? = null,
        @SerialName("RELIGION_OTHER_SPECIFIC") val religionOtherSpecific: String? = null,
        @SerialName("RELIGION") val religion: String? = null,
        @SerialName("UPDATED_BY") val updatedBy: Int? = null,
        @SerialName("OCCUPATION") val occupation: String? = null,
        @SerialName("BENEF_IMAGE_PATH") val benefImagePath: String? = null,
        @SerialName("LOCATION_NAME") val locationName: String? = null,
        @SerialName("BENEF_MMR") val benefMmr: String? = null
    )

    @Serializable
    data class ServiceItem(
        @SerialName("STATUS") val status: String? = null,
        @SerialName("refered_to") val referedTo: String? = null,
        @SerialName("LAST_OPENED_BY") val lastOpenedBy: String? = null,
        @SerialName("case_name") val caseName: String? = null,
        @SerialName("interview_time") val interviewTime: String? = null
    )
}
