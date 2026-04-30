package ngo.friendship.mhealth.dc.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ngo.friendship.mhealth.dc.utils.md5

@Serializable
data class SaveInvestigationReqDto(
    @SerialName("ORG_CODE")
    val orgCode: String,

    @SerialName("userCode")
    val userCode: String,

    @SerialName("pw")
    val pw: String,

    @SerialName("ORG_ID")
    val orgId: Int,

    @SerialName("imei")
    val imei: String,

    @SerialName("DEMO")
    val demo: Boolean,

    @SerialName("requestType")
    val requestType: String,

    @SerialName("requestName")
    val requestName: String,

    @SerialName("module_name")
    val moduleName: String,

    @SerialName("requestTime")
    val requestTime: String,

    @SerialName("requestAction")
    val requestAction: String,

    @SerialName("dataLength")
    val dataLength: Int,

    @SerialName("data")
    val data: Map<String, String>,

    @SerialName("lang")
    val lang: String,

    @SerialName("param1")
    val param1: SaveInvestigationParam1
) {
    companion object {
        fun build(
            userName: String,
            password: String,
            requestTime: String,
            investigationName: String
        ): SaveInvestigationReqDto {
            return SaveInvestigationReqDto(
                orgCode = "FR",
                userCode = userName.md5(),
                pw = password.md5(),
                orgId = 101,
                imei = "IMEI_FREE",
                demo = false,
                requestType = "DOCTOR_CENTER",
                requestName = "SAVE_INVESTIGATION",
                moduleName = "mHealth-FCM",
                requestTime = requestTime,
                requestAction = "",
                dataLength = 2,
                data = emptyMap(),
                lang = "bn",
                param1 = SaveInvestigationParam1(
                    investigationName = investigationName,
                    investigationCode = investigationName,
                    invResultUnit = "",
                    invResultRef = "",
                    state = 1,
                    param1 = "",
                    param2 = ""
                )
            )
        }
    }
}

@Serializable
data class SaveInvestigationParam1(
    @SerialName("investigation_name")
    val investigationName: String,
    @SerialName("investigation_code")
    val investigationCode: String,
    @SerialName("inv_result_unit")
    val invResultUnit: String,
    @SerialName("inv_result_ref")
    val invResultRef: String,
    @SerialName("state")
    val state: Int,
    @SerialName("PARAM_1")
    val param1: String,
    @SerialName("PARAM_2")
    val param2: String
)
