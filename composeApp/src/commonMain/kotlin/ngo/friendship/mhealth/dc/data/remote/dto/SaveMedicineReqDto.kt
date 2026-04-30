package ngo.friendship.mhealth.dc.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ngo.friendship.mhealth.dc.utils.md5

@Serializable
data class SaveMedicineReqDto(
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
    val param1: SaveMedicineParam1
) {
    companion object {
        fun build(
            userName: String,
            password: String,
            requestTime: String,
            genericName: String,
            brandName: String,
            type: String,
            strength: String
        ): SaveMedicineReqDto {
            // Extract numeric part from strength if possible, or use a default
            val strengthValue = strength.filter { it.isDigit() }.toDoubleOrNull() ?: 0.0
            val measureUnit = strength.filter { it.isLetter() }.ifEmpty { "mg" }

            return SaveMedicineReqDto(
                orgCode = "FR",
                userCode = userName.md5(),
                pw = password.md5(),
                orgId = 101,
                imei = "IMEI_FREE",
                demo = false,
                requestType = "DOCTOR_CENTER",
                requestName = "SAVE_MEDICINE",
                moduleName = "mHealth-FCM",
                requestTime = requestTime,
                requestAction = "",
                dataLength = 2,
                data = emptyMap(),
                lang = "bn",
                param1 = SaveMedicineParam1(
                    manufId = 2,
                    brandName = brandName,
                    strength = strengthValue,
                    measureUnit = measureUnit,
                    type = type,
                    unitPurchasePrice = 0.80,
                    unitSalesPrice = 1.50,
                    boxSize = 100,
                    genericName = genericName,
                    unitType = "Pcs"
                )
            )
        }
    }
}

@Serializable
data class SaveMedicineParam1(
    @SerialName("manuf_id")
    val manufId: Int,
    @SerialName("brand_name")
    val brandName: String,
    @SerialName("strength")
    val strength: Double,
    @SerialName("measure_unit")
    val measureUnit: String,
    @SerialName("type")
    val type: String,
    @SerialName("unit_purchase_price")
    val unitPurchasePrice: Double,
    @SerialName("unit_sales_price")
    val unitSalesPrice: Double,
    @SerialName("box_size")
    val boxSize: Int,
    @SerialName("generic_name")
    val genericName: String,
    @SerialName("unit_type")
    val unitType: String
)
