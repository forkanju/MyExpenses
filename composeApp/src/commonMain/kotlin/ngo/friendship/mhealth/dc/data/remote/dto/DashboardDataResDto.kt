package ngo.friendship.mhealth.dc.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DashboardDataResDto(
    @SerialName("responseType") val responseType: String? = null,
    @SerialName("data") val data: DashboardData? = null,
    @SerialName("errorDesc") val errorDesc: String? = null,
    @SerialName("responseTime") val responseTime: String? = null,
    @SerialName("dataLength") val dataLength: Int? = null,
    @SerialName("execTime") val execTime: Int? = null,
    @SerialName("errorCode") val errorCode: String? = null,
    @SerialName("responseName") val responseName: String? = null,
    @SerialName("responseCode") val responseCode: String? = null
)

@Serializable
data class DashboardData(
    @SerialName("top_upazila") val topUpazila: List<UpazilaData>? = null,
    @SerialName("time_summary") val timeSummary: TimeSummary? = null,
    @SerialName("top_questionnaires") val topQuestionnaires: List<QuestionnaireData>? = null,
    @SerialName("status_summary") val statusSummary: StatusSummary? = null
)

@Serializable
data class UpazilaData(
    @SerialName("UPAZILA_NAME") val upazilaName: String? = null,
    @SerialName("total_service") val totalService: Int? = null
)

@Serializable
data class TimeSummary(
    @SerialName("in_30_min") val in30Min: Int? = null,
    @SerialName("after_30_min") val after30Min: Int? = null,
    @SerialName("after_2_hours") val after2Hours: Int? = null
)

@Serializable
data class QuestionnaireData(
    @SerialName("QUESTIONNAIRE_TITLE") val questionnaireTitle: String? = null,
    @SerialName("total_count") val totalCount: Int? = null
)

@Serializable
data class StatusSummary(
    @SerialName("total") val total: Int? = null,
    @SerialName("answered") val answered: Int? = null,
    @SerialName("pending") val pending: Int? = null,
    @SerialName("referred") val referred: Int? = null
)