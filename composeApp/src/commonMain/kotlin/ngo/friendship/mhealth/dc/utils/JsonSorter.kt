package ngo.friendship.mhealth.dc.utils

import kotlinx.serialization.json.*

object JsonSorter {

    private val KEY_ORDER = listOf(
        "MED_ID",
        "GEN_NAME",
        "MED_TYPE",
        "MED_NAME",
        "MED_QTY",
        "SALE_QTY",
        "MED_DURATION",
        "MTR",
        "MTR_LBL",
        "MTR_SF",
        "AFM",
        "AFM_SF",
        "SF",
        "SMS_SF"
    )

    /**
     * Returns a new JsonObject with keys inserted in desired order.
     */
    fun sortJson(input: JsonObject): JsonObject {
        return buildJsonObject {
            // Add ordered keys first
            for (key in KEY_ORDER) {
                if (input.containsKey(key)) {
                    put(key, input[key]!!)
                }
            }

            // Add remaining keys
            for ((key, value) in input) {
                if (!KEY_ORDER.contains(key)) {
                    put(key, value)
                }
            }
        }
    }

    /**
     * Sort JsonArray of JsonObjects.
     */
    fun sortJsonArray(array: JsonArray): JsonArray {
        return buildJsonArray {
            for (item in array) {
                if (item is JsonObject) {
                    add(sortJson(item))
                } else {
                    add(item)
                }
            }
        }
    }

    /**
     * Convert JsonObject to ordered JSON String.
     */
    fun sortJsonToString(input: JsonObject): String {
        return sortJson(input).toString()
    }

    /**
     * Convert JsonArray to ordered JSON String.
     */
    fun sortJsonArrayToString(array: JsonArray): String {
        return sortJsonArray(array).toString()
    }

    /**
     * Convert JsonArray to Pipe Separated Ordered JSON.
     */
    fun convertJsonArrayToPipeSeparated(jsonArray: JsonArray): String {
        val sortedArray = sortJsonArray(jsonArray)
        return sortedArray.joinToString("|") { it.toString() }
    }
}
