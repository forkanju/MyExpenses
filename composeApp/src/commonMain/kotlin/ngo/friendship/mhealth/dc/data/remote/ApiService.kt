package ngo.friendship.mhealth.dc.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import ngo.friendship.mhealth.dc.data.remote.dto.AdviceListReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.AdviceListResDto
import ngo.friendship.mhealth.dc.data.remote.dto.BeneficiaryProfileReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.BeneficiaryProfileResDto
import ngo.friendship.mhealth.dc.data.remote.dto.ChangePasswordReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.ChangePasswordResDto
import ngo.friendship.mhealth.dc.data.remote.dto.DashboardDataReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.DashboardDataResDto
import ngo.friendship.mhealth.dc.data.remote.dto.DoctorProfileReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.DoctorProfileResDto
import ngo.friendship.mhealth.dc.data.remote.dto.FcmProfileReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.FcmProfileResDto
import ngo.friendship.mhealth.dc.data.remote.dto.InterviewDetailsReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.InterviewDetailsResDto
import ngo.friendship.mhealth.dc.data.remote.dto.InterviewListReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.InterviewListResDto
import ngo.friendship.mhealth.dc.data.remote.dto.LoginRequestDto
import ngo.friendship.mhealth.dc.data.remote.dto.LoginResponseDto
import ngo.friendship.mhealth.dc.data.remote.dto.MedicineListReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.MedicineListResDto
import ngo.friendship.mhealth.dc.data.remote.dto.PrescriptionTemplateReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.PrescriptionTemplateResDto
import ngo.friendship.mhealth.dc.data.remote.dto.QuestionAnswerDataResDto
import ngo.friendship.mhealth.dc.data.remote.dto.QuestionAnswerJsonReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.DoctorFeedbackReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.DoctorFeedbackResDto
import ngo.friendship.mhealth.dc.data.remote.dto.SaveAdviceReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.SaveAdviceResDto
import ngo.friendship.mhealth.dc.data.remote.dto.SaveDiagnosisReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.SaveDiagnosisResDto
import ngo.friendship.mhealth.dc.data.remote.dto.SaveInvestigationReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.SaveInvestigationResDto
import ngo.friendship.mhealth.dc.data.remote.dto.SaveMedicineReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.SaveMedicineResDto
import ngo.friendship.mhealth.dc.data.remote.dto.SaveDoctorFeedbackReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.SaveDoctorFeedbackResDto
import ngo.friendship.mhealth.dc.data.remote.dto.SetupDataReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.SetupDataResDto
import ngo.friendship.mhealth.dc.data.remote.dto.UpdateInterviewStatusReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.UpdateInterviewStatusResDto
import ngo.friendship.mhealth.dc.domain.network.processFormDataRequest
import kotlinx.serialization.json.JsonObject
import ngo.friendship.mhealth.dc.utils.Constants

class ApiService(
    private val client: HttpClient
) {
    suspend fun login(request: LoginRequestDto): LoginResponseDto = client.processFormDataRequest(
        url = "mHealthEnt_gateway/api/usergate", body = request
    )

    suspend fun getInterviewList(
        request: InterviewListReqDto, appVersion: Int
    ): InterviewListResDto = client.processFormDataRequest(
        url = "mHealthEnt_gateway/api/usergate", body = request, request = {
            header("app_version", appVersion.toString())
        })

    suspend fun getInterviewDetails(
        request: InterviewDetailsReqDto
    ): InterviewDetailsResDto {
        return client.processFormDataRequest<InterviewDetailsReqDto, InterviewDetailsResDto>(
            url = "mHealthEnt_gateway/api/usergate", body = request
        )
    }

    suspend fun getSetupData(
        request: SetupDataReqDto
    ): SetupDataResDto {
        return client.processFormDataRequest<SetupDataReqDto, SetupDataResDto>(
            url = "mHealthEnt_gateway/api/usergate",
            body = request
        )
    }

    suspend fun getMedicineList(
        request: MedicineListReqDto
    ): MedicineListResDto {
        return client.processFormDataRequest<MedicineListReqDto, MedicineListResDto>(
            url = "mHealthEnt_gateway/api/usergate",
            body = request
        )
    }

    suspend fun saveDoctorFeedback(
        request: SaveDoctorFeedbackReqDto
    ): SaveDoctorFeedbackResDto {
        return client.processFormDataRequest<SaveDoctorFeedbackReqDto, SaveDoctorFeedbackResDto>(
            url = "mHealthEnt_gateway/api/usergate",
            body = request
        )
    }

    suspend fun saveDiagnosis(
        request: SaveDiagnosisReqDto
    ): SaveDiagnosisResDto {
        return client.processFormDataRequest<SaveDiagnosisReqDto, SaveDiagnosisResDto>(
            url = "mHealthEnt_gateway/api/usergate",
            body = request
        )
    }

    suspend fun saveInvestigation(
        request: SaveInvestigationReqDto
    ): SaveInvestigationResDto {
        return client.processFormDataRequest<SaveInvestigationReqDto, SaveInvestigationResDto>(
            url = "mHealthEnt_gateway/api/usergate",
            body = request
        )
    }

    suspend fun saveMedicine(
        request: SaveMedicineReqDto
    ): SaveMedicineResDto {
        return client.processFormDataRequest<SaveMedicineReqDto, SaveMedicineResDto>(
            url = "mHealthEnt_gateway/api/usergate",
            body = request
        )
    }

    suspend fun getDoctorFeedback(
        request: DoctorFeedbackReqDto
    ): DoctorFeedbackResDto {
        return client.processFormDataRequest<DoctorFeedbackReqDto, DoctorFeedbackResDto>(
            url = "mHealthEnt_gateway/api/usergate",
            body = request
        )
    }

    suspend fun getQuestionAnswerData(
        request: QuestionAnswerJsonReqDto
    ): QuestionAnswerDataResDto {
        return client.processFormDataRequest<QuestionAnswerJsonReqDto, QuestionAnswerDataResDto>(
            url = "mHealthEnt_gateway/api/usergate",
            body = request
        )
    }

    suspend fun updateInterviewStatus(
        request: UpdateInterviewStatusReqDto
    ): UpdateInterviewStatusResDto = client.processFormDataRequest(
        url = "mHealthEnt_gateway/api/usergate",
        body = request,
    )

    suspend fun getDoctorProfile(
        request: DoctorProfileReqDto
    ): DoctorProfileResDto = client.processFormDataRequest(
        url = "mHealthEnt_gateway/api/usergate",
        body = request,
    )

    suspend fun changePassword(
        request: ChangePasswordReqDto
    ): ChangePasswordResDto = client.processFormDataRequest(
        url = "mHealthEnt_gateway/api/usergate",
        body = request,
    )

    suspend fun getFcmProfile(
        request: FcmProfileReqDto
    ): FcmProfileResDto = client.processFormDataRequest(
        url = "mHealthEnt_gateway/api/usergate",
        body = request,
    )

    suspend fun getDashboardData(
        request: DashboardDataReqDto
    ): DashboardDataResDto = client.processFormDataRequest(
        url = "mHealthEnt_gateway/api/usergate",
        body = request,
    )

    suspend fun getBeneficiaryProfile(
        request: BeneficiaryProfileReqDto
    ): BeneficiaryProfileResDto = client.processFormDataRequest(
        url = "mHealthEnt_gateway/api/usergate",
        body = request,
    )

    suspend fun getPrescriptionTemplates(
        request: PrescriptionTemplateReqDto
    ): PrescriptionTemplateResDto = client.processFormDataRequest(
        url = "mHealthEnt_gateway/api/usergate",
        body = request,
    )

    suspend fun getAdviceList(
        request: AdviceListReqDto
    ): AdviceListResDto = client.processFormDataRequest(
        url = "mHealthEnt_gateway/api/usergate",
        body = request,
    )

    suspend fun saveAdvice(
        request: SaveAdviceReqDto
    ): SaveAdviceResDto = client.processFormDataRequest(
        url = "mHealthEnt_gateway/api/usergate",
        body = request,
    )

    suspend fun sendSms(
        msisdn: String,
        message: String
    ): JsonObject {
        return client.get("https://smsplus.sslwireless.com/api/v3/send-sms") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            header(HttpHeaders.Accept, ContentType.Application.Json)
            header(HttpHeaders.AcceptCharset, "utf-8")
            parameter("api_token",  Constants.SMS_API_TOKEN)
            parameter("sid", Constants.SMS_SID)
            parameter("msisdn", msisdn)
            parameter("sms", message)
            parameter("csms_id", "123456")
        }.body()
    }


}