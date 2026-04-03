@file:Suppress("unused")

package ngo.friendship.mhealth.dc.utils

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import ngo.friendship.mhealth.dc.isDebugBuild
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.ExperimentalExtendedContracts
import kotlin.contracts.contract
import kotlin.math.*
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.Instant

enum class ButtonState { Pressed, Idle }

fun Modifier.bounceClick(
    keepRipple: Boolean = true,
    onClick: () -> Unit = {}
) = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = if (keepRipple) LocalIndication.current else null, // Conditional ripple
        onClick = singleClick(onClick)
    )
}.bounceOnClick()

fun Modifier.bounceOnClick(pass: PointerEventPass = PointerEventPass.Main) = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scale by animateFloatAsState(
        if (buttonState == ButtonState.Pressed) 0.95f else 1f,
        label = ""
    )
    graphicsLayer {
        scaleX = scale
        scaleY = scale
    }.pointerInput(buttonState) {
        awaitPointerEventScope {
            buttonState = if (buttonState == ButtonState.Pressed) {
                waitForUpOrCancellation(pass)
                ButtonState.Idle
            } else {
                awaitFirstDown(false, pass)
                ButtonState.Pressed
            }
        }
    }
}

@Composable
fun singleClick(
    onClick: () -> Unit,
): () -> Unit {
    val lastClickTime = remember { mutableStateOf(0L) }

    return {
        val now = currentTimestamp
        if (now - lastClickTime.value >= 700L) {
            lastClickTime.value = now
            onClick()
        }
    }
}

fun Modifier.singleClickable(
    onClick: () -> Unit,
): Modifier = composed {
    this.clickable(onClick = singleClick(onClick))
}

@Composable
inline fun <reified T> rememberSaveableState(
    init: T
): MutableState<T> {
    val json = Json { ignoreUnknownKeys = true }

    return rememberSaveable(
        saver = Saver(
            save = { state -> json.encodeToString(state.value) },
            restore = { raw -> json.decodeFromString(raw) }
        )
    ) {
        mutableStateOf(init)
    }
}

fun TextFieldState.setText(text: String?) {
    edit {
        replace(0, length, text.orEmpty())
    }
}

/**Coroutines Extension Function*/
suspend fun <T, R> T.runIO(block: suspend T.() -> R) = withContext(Dispatchers.IO) {
    block()
}

@Suppress("FunctionName")
suspend fun <T, R> T.MAIN(block: suspend T.() -> R) = withContext(Dispatchers.Main) {
    block()
}

@Suppress("FunctionName")
suspend fun <T, R> T.DEFAULT(block: suspend T.() -> R) = withContext(Dispatchers.Default) {
    block()
}


inline fun <T, R> Flow<List<T>>.mapList(crossinline transform: suspend T.() -> R): Flow<List<R>> =
    this.map {
        it.map { value ->
            transform(value)
        }
    }

/**
 * Returns a list containing all elements except the element at the specified [index].
 */
fun <T> List<T>.minusAt(index: Int): List<T> {
    if (index !in indices) return this
    return take(index) + drop(index + 1)
}

/**
 * Returns a new list containing a specified number of random elements from the original list,
 * ensuring no duplicates are picked if the original list itself has no duplicates.
 *
 * @param count The number of random items to take.
 * @return A new list with the random items, or an empty list if not possible.
 */
fun <T> List<T>.takeRandom(count: Int): List<T> {
    if (isEmpty() || count <= 0)
        return emptyList()
    if (count >= size)
        return shuffled() // Shuffled already ensures distinctness from original
    val availableIndices = indices.toMutableList()
    val randomItems = mutableListOf<T>()

    repeat(min(count, size)) { // Ensure we don't try to pick more than available
        val randomListIndex = Random.nextInt(availableIndices.size)
        val originalItemIndex = availableIndices.removeAt(randomListIndex)
        randomItems.add(get(originalItemIndex))
    }
    return randomItems
}


//fun Any?.logJson(tag: String = "TAG") {
//    if (BuildConfig.IS_INTERNAL_TESTING) {
//        Log.i("log> '$tag'", "${this?.javaClass?.name}")
//        if (this is String)
//            com.orhanobut.logger.Logger.json(this)
//        else
//            com.orhanobut.logger.Logger.json(Gson().toJson(this))
//    }
//}

fun <T> T.getTag() = if (this == null) "TAG" else this::class.simpleName ?: "TAG"

fun Any.cat(message: String) {
    message.log(getTag())
}

fun Any?.log(tag: String = "TAG"): Any? {
    if (!isDebugBuild) return null
    if (this is Throwable)
        Napier.i("log> '$tag' ${this.message}", this, tag = tag)
    else
        Napier.i("log> '$tag' $this : ${this?.let { this::class.simpleName }}", tag = tag)
    return this
}


// Helper for zero-padding hours/minutes, since String.format not allowed
private fun Int.pad2() = toString().padStart(2, '0')

// --- Extensions ---

val currentTimestamp: Long
    get() = Clock.System.now().toEpochMilliseconds()

fun Long.toDateTime(dateOrTime: Boolean = false): String {
    val zone = TimeZone.currentSystemDefault()
    val now = Clock.System.now().toLocalDateTime(zone)
    val dateTime = Instant.fromEpochMilliseconds(this).toLocalDateTime(zone)

    val format =
        if (dateTime.date == now.date && dateTime.month == now.month && dateTime.year == now.year) {
            // same day: "hh:mm a"
            val hour12 = if (dateTime.hour % 12 == 0) 12 else dateTime.hour % 12
            val txt = if (dateTime.hour < 12) "AM" else "PM"
            "${hour12.pad2()}:${dateTime.minute.pad2()} $txt"
        } else {
            // different day: "dd MMM, hh:mm a"
            val day = dateTime.day
            val monthName = dateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }
                .take(3) // Jan, Feb, etc.
            if (dateOrTime) return "$day $monthName, ${dateTime.year % 100}"
            val hour12 = if (dateTime.hour % 12 == 0) 12 else dateTime.hour % 12
            val txt = if (dateTime.hour < 12) "AM" else "PM"
            "$day $monthName, ${hour12.pad2()}:${dateTime.minute.pad2()} $txt"
        }

    return format
}

fun Long.toDayPassed(): String {
    val nowMs = currentTimestamp
    val msDiff = (nowMs - this).coerceAtLeast(0)
    val daysDiff = msDiff / 86_400_000L // milliseconds per day
    return "$daysDiff Days Ago"
}

typealias DateRange = String

fun Pair<Long, Long>.toDateRange(): DateRange {
    val (start, end) = this
    return "${start.toDateModern()} - ${end.toDateModern()}"
}

fun Pair<Long, Long>.toDayPassed(): Int {
    val (start, end) = this
    val diffDays = ((end - start).absoluteValue / 86_400_000L).toInt() + 1
    return diffDays
}

fun Long.toDuration(): Pair<String, Long> {
    val totalMinutes = this / 60_000
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    val formatted = if (hours > 0) "${hours}h, ${minutes}m" else "${minutes}m"
    return formatted to totalMinutes
}


fun Long.toTimeDate(): String {
    val localDateTime = Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())
    val hour12 = if (localDateTime.hour % 12 == 0) 12 else localDateTime.hour % 12
    val txt = if (localDateTime.hour < 12) "AM" else "PM"
    val timePart =
        "${hour12.pad2()}${txt}_${localDateTime.day.pad2()}-${localDateTime.month.number.pad2()}-${localDateTime.year}"
    return timePart
}

fun Long.toDate(): String {
    val date = Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault()).date
    return "%${date.day.pad2()}/${date.month.number.pad2()}/${date.year % 100}"
}

fun Long.toDateModern(): String {
    val date = Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault()).date
    val monthName = date.month.name.lowercase().replaceFirstChar { it.uppercase() }
    return "${date.day} $monthName, ${date.year}"
}

private fun Int.pad(length: Int): String =
    this.toString().padStart(length, '0') // More general version

fun Long.toDateServer(): String {
    val date = Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault()).date
    return "${date.year}-${date.month.number.pad2()}-${date.day.pad2()}"
}

fun Long.toDateTimeServer(): String {
    val ldt = Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())
    return "${ldt.year}-${ldt.month.number.pad2()}-${ldt.day.pad2()}" +
            " ${ldt.hour.pad2()}:${ldt.minute.pad2()}:${ldt.second.pad2()}"
}

fun Long.toDateTimeServerSlash(): String {
    val ldt = Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())
    return "${ldt.day.pad2()}/${ldt.month.number.pad2()}/${ldt.year}" +
            " ${ldt.hour.pad2()}:${ldt.minute.pad2()}:${ldt.second.pad2()}"
}

fun Long.toTime(): String {
    val ldt = Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())
    val hour12 = if (ldt.hour % 12 == 0) 12 else ldt.hour % 12
    val txt = if (ldt.hour < 12) "AM" else "PM"
    return "${hour12.pad2()}:${ldt.minute.pad2()} $txt"
}

fun Long.toTimeServer(): String {
    val ldt = Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())
    return "${ldt.hour.pad2()}:${ldt.minute.pad2()}"
}

fun Long.toTimeHyphen(): String {
    val ldt = Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())
    val hour12 = if (ldt.hour % 12 == 0) 12 else ldt.hour % 12
    val txt = if (ldt.hour < 12) "AM" else "PM"
    return "${hour12.pad2()}-${ldt.minute.pad2()}$txt"
}

fun Long.toClockTime(): Triple<Int, Int, Int> {
    val ldt = Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.currentSystemDefault())
    return Triple(ldt.hour, ldt.minute, ldt.second)
}

fun Long.toFirstMonthDate(): Long {
    val ldt = Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.currentSystemDefault())
    val firstDay =
        LocalDate(ldt.year, ldt.month, 1).atStartOfDayIn(TimeZone.currentSystemDefault())
    return firstDay.toEpochMilliseconds()
}

fun Long.toYear(): String {
    val year = Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault()).year
    return year.toString()
}

fun String.toTimeStamp(): Long {
    // Parses yyyy-MM-dd or yyyy-MM-dd HH:mm:ss
    return try {
        if (this.contains(" ")) {
            val ldt = LocalDateTime.parse(this)
            ldt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        } else {
            val ld = LocalDate.parse(this)
            ld.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds() + (3600000 * 6) // add 6 hour for google calender
        }
    } catch (e: Exception) {
        0L
    }
}

fun Long.toMonthDay(): Int {
    return Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault()).day
}

fun Long.toMonthCount(): Int {
    return Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault()).month.number
}


fun String.to12HourFormat(): String {
    val parts = this.split(":").map { it.toInt() }
    val hours = parts[0]
    val minutes = parts[1]

    return when {
        hours == 0 -> "12:${minutes.pad2()}AM"
        hours < 12 -> "${hours}:${minutes.pad2()}AM"
        hours == 12 -> "${hours}:${minutes.pad2()}PM"
        else -> "${hours - 12}:${minutes.pad2()}PM"
    }
}


fun Int.toTimer(): Triple<Int, Int, Int> {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60
    return Triple(hours, minutes, seconds)
}


fun String.toUppercaseType(): String =
    trim().replace(Regex("(?<=[a-z])(?=[A-Z])"), " ")
        .split("[\\s\\-_]+".toRegex())
        .filter { it.isNotEmpty() }
        .joinToString(" ") {
            it.lowercase().replaceFirstChar(Char::uppercaseChar)
        }

fun String.toTitleName(): String =
    trim().lowercase()
        .split("[\\s\\-_]+".toRegex())
        .joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }

fun String.toInitial(): String =
    trim().split("[\\s\\-_]+".toRegex())
        .filter { it.isNotEmpty() }
        .map { it.first().uppercaseChar() }
        .joinToString("")

fun <T> throttleLatest(
    intervalMs: Long = 300L,
    coroutineScope: CoroutineScope,
    destinationFunction: (T) -> Unit,
): (T) -> Unit {
    var throttleJob: Job? = null
    var latestParam: T
    return { param: T ->
        latestParam = param
        if (throttleJob?.isCompleted != false) {
            throttleJob = coroutineScope.launch {
                delay(intervalMs)
                latestParam.let(destinationFunction)
            }
        }
    }
}

fun <T> throttleFirst(
    skipMs: Long = 300L,
    coroutineScope: CoroutineScope,
    destinationFunction: (T) -> Unit,
): (T) -> Unit {
    var throttleJob: Job? = null
    return { param: T ->
        if (throttleJob?.isCompleted != false) {
            throttleJob = coroutineScope.launch {
                destinationFunction(param)
                delay(skipMs)
            }
        }
    }
}

fun <T> debounce(
    waitMs: Long = 300L,
    coroutineScope: CoroutineScope,
    destinationFunction: (T) -> Unit,
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        debounceJob?.cancel()
        debounceJob = coroutineScope.launch {
            delay(waitMs)
            destinationFunction(param)
        }
    }
}

fun debounce(
    coroutineScope: CoroutineScope,
    waitMs: Long = 300L,
    desFun: () -> Unit,
): () -> Unit {
    var debounceJob: Job? = null
    return {
        debounceJob?.cancel()
        debounceJob = coroutineScope.launch(Dispatchers.IO) {
            delay(waitMs)
            desFun.invoke()
        }
    }
}

/**
 * Returns 'true' if this string only has numbers else 'false'
 */
fun String.hasNumberOnly() = this.matches("-?\\d+(\\.\\d+)?".toRegex())


fun Boolean.toYesNo() = if (this) "YES" else "NO"
fun Boolean?.isTrue() = this == true
fun Boolean?.isFalse() = this == null || !this

inline fun Boolean?.isTrue(next: () -> Unit): Boolean? {
    if (isTrue()) next()
    return this
}

inline fun Boolean?.isFalse(next: () -> Unit): Boolean? {
    if (isFalse()) next()
    return this
}

inline fun String?.isNotEmpty(next: (String) -> Unit): String? {
    if (!isNullOrEmpty()) next(this)
    return this
}

inline fun <T> Collection<T>?.isEmpty(next: () -> Unit): Collection<T>? {
    if (isNullOrEmpty()) next()
    return this
}

inline fun <T> Collection<T>?.isNotEmpty(next: (List<T>) -> Unit): Collection<T>? {
    if (!isNullOrEmpty()) next(toList())
    return this
}

fun String?.isBanglaText() = this == null || matches("[\\u0980-\\u09FF\\s,।_/-]+".toRegex())

fun String?.isValidEmail(): Boolean {
    if (isNullOrBlank()) return false
    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    return emailRegex.matches(this)
}


fun String.isValidPhoneBD(): Boolean {
    val phoneNumberRegexBd = "^(?:\\+?88)?01[3-9]\\d{8}$".toRegex()
    return matches(phoneNumberRegexBd)
}

fun String.isValidPhoneUS(): Boolean {
    val usPhoneNumberRegex =
        """^(?:\+?1[-. ]?)?\(?([2-9][0-8][0-9])\)?[-. ]?([2-9][0-9]{2})[-. ]?([0-9]{4})$""".toRegex()
    return matches(usPhoneNumberRegex)
}

fun String.isValidPassword(): Boolean {
    val hasUpper = any { it.isUpperCase() }
    val hasLower = any { it.isLowerCase() }
    val hasNumber = any { it.isDigit() }
    val isGraterThan8 = length >= 8
//    val hasSpecial = this.any { "!@#\$%^&*()-_=+[]{};:'\",.<>?/\\|`~".contains(it) }
    return hasUpper && hasLower && hasNumber && isGraterThan8
}

fun String?.isJson(): Boolean {
    if (this == null) return false
    // A regex pattern for valid JSON
    val jsonPattern = """^\s*(\{.*\}|\[.*])\s*$""".toRegex()
    // Check if the string matches the JSON pattern
    return jsonPattern.matches(this)
}

fun String.isValidURL(): Boolean {
    val urlRegex = Regex("^(https?|ftp)://[a-zA-Z0-9.-]+(:[0-9]+)?(/.*)?$", RegexOption.IGNORE_CASE)
    return urlRegex.matches(this)
}


fun <T> T.isNull() = this == null
fun <T> T.isNotNull() = this != null

inline fun <T> T?.isNull(next: () -> Unit): T? {
    if (this == null) next()
    return this
}

inline fun <T> T?.isNotNull(next: (T) -> Unit): T? {
    if (this != null) next(this)
    return this
}

fun Int?.isNullOrZero() = this == null || this == 0
fun Double?.toOneIfZero() = if (this == 0.0 || this == null) 1.0 else this
fun String?.toNAifEmpty() =
    if (isNullOrEmpty() || isBlank() || (this == "0" || this == "0.0")) "N/A" else this

fun String?.toNullifEmpty() =
    if (isNullOrEmpty() || isBlank() || (this == "0" || this == "0.0")) null else this

fun String?.toDoubleOrZero() =
    this?.replace("[^\\d.]".toRegex(), "")?.toDoubleOrNull().orZero().roundTo(2)

fun Int?.orMinusOne() = this ?: -1
fun Int?.orZero() = this ?: 0
fun Long?.orZero() = this ?: 0L
fun Double?.orZero() = this ?: 0.0
fun Float?.orZero() = this ?: 0.0F
fun String?.orZero() = this?.toIntOrNull() ?: 0
fun String?.orZeroD() = this?.toDoubleOrNull() ?: 0.0
fun Boolean?.orFalse() = this == true
fun Boolean.orNull() = if (!this) null else true

fun Int?.isZero() = this == null || this == 0
fun Int?.isMinusOne() = this == null || this == -1
fun Long?.isZero() = this == null || this == 0L
fun Double?.isZero() = this == null || this == 0.0
fun Float?.isZero() = this == null || this == 0.0F
fun Float?.isMinusOne() = this == null || this == -1.0F
fun Float?.isZeroOrMinusOne() = this == null || this == 0.0F || this == -1.0F
fun String?.isZero() = this == null || this.toIntOrNull() == 0
fun String?.isZeroD() = this == null || this.toDoubleOrNull() == 0.0

fun Int.asList(initialIndex: Int = 0) : List<Int> {
    if (initialIndex > this) return emptyList()
    val list = mutableListOf<Int>()
    for (i in initialIndex..< this) {
        list.add(i)
    }
    return list
}

inline fun <T> tryGet(data: () -> T): T? =
    try {
        data()
    } catch (t: Throwable) {
        null
    }

inline fun <T> tryInMain(crossinline data: suspend CoroutineScope.() -> T) =
    CoroutineScope(Dispatchers.Main.immediate).launch {
        try {
            data()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

inline fun <T> T.applyIf(ifTrue: Boolean, block: T.() -> Unit): T {
    if (ifTrue) block.invoke(this)
    return this
}

inline fun Boolean.ifTrue(block: Boolean.() -> Unit): Boolean {
    if (this) block.invoke(true)
    return this
}

fun String?.getPercentage(total: Int): Int {
    if (total == 0 || isNullOrEmpty() || toDoubleOrNull() == null) return 100
    return toDoubleOrZero().getPercentage(total)
}

fun Number.getPercentageFloat(total: Number): Double {
    if (total == 0) return 100.0
    return (toDouble().times(100.0).div(total.toDouble())).roundTo(2)
}

fun Number.getPercentage(total: Number): Int {
    if (total == 0) return 100
    return (toDouble().times(100.0).div(total.toDouble())).toInt()
}

//fun Int.getPercentageTxt(total: Int): String {
//    if (total == 0) return "100%"
//    return "${times(100).div(total)}%"
//}
fun Number.getPercentageTxt(total: Number, asInteger: Boolean = false): String {
    if (total == 0.0) return "100%"
    return toDouble().times(100).div(total.toDouble()).run {
        if (asInteger) roundToInt() else roundTo(2)
    }.toString() + "%"

}

fun Number.getGrowthTxt(lastValue: Number, decimalPoint: Int = 2): String {
    if (lastValue == 0.0) return "100%"
    return "${
        this.toDouble()
            .minus(lastValue.toDouble())
            .times(100.0)
            .div(lastValue.toDouble())
            .roundTo(decimalPoint)
    }%"
}

fun Number.roundTo(decimals: Int): Double {
    val factor = 10.0.pow(decimals)
    return round(toDouble() * factor) / factor
}

fun Number.roundBy(step: Double = 0.25): Float {
    return (round(toDouble() / step) * step).coerceIn(0.0, 1.0).toFloat()
}

fun Float.mapRatingToList(listSize: Int = 5): List<Float> {
    val clampedRating = coerceIn(0f, listSize.toFloat())
    val fullStars = clampedRating.toInt()
    val partial = clampedRating - fullStars

    return List(listSize) { index ->
        when {
            index < fullStars -> 1f
            index == fullStars && partial > 0f -> partial
            else -> 0f
        }
    }
}


fun Number.addPercentage(decimals: Int = 2): String {
    return "${roundTo(decimals)}%"
}

fun Int.addPercentage(): String {
    return "$this%"
}

//fun Double?.toTK(): String {
//    if (this == null) return "৳0"
//    return (userNullable?.currencySymbol ?: "৳") + when {
//        (this / 100000).toInt() != 0 -> "%,.1f".format(this / 100000) + "Lac"
//        (this / 1000).toInt() != 0 -> "%,.1f".format(this / 1000) + "K"
//        else -> "%,.1f".format(this)
//    }
//}
//
//fun Int.toTK(): String {
//    return (userNullable?.currencySymbol ?: "৳") + when {
//        this / 100000 != 0 -> "%,d".format(this / 100000) + "Lac"
//        this / 1000 != 0 -> "%,d".format(this / 1000) + "K"
//        else -> "%,d".format(this)
//    }
//}


//fun Double?.addTK(decimalPoint: Int = 2): String {
//    if (this == null) return "৳0"
//    return (userNullable?.currencySymbol ?: "৳") + "%.${decimalPoint}f".format(this)
//}


fun String.getOptimizedPrintText(totalLength: Int): String {
    val newText: String = if (this.length > totalLength) {
        this.substring(0, totalLength)
    } else {
        this
    }
    return newText
}

val defJson by lazy {
    Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        explicitNulls = false
    }
}

/**
 * Deserializes a JSON string to an object of type T.
 * The class T must be annotated with @Serializable.
 */
@OptIn(ExperimentalContracts::class, ExperimentalExtendedContracts::class)
inline fun <reified T> String?.fromJson(): T? {
    contract {
        (this@fromJson != null) implies (returnsNotNull())
    }
    this ?: return null
    return  defJson.decodeFromString<T>(this)
}

// --- More Type-Safe and Recommended Alternatives for toJson and toObject ---

/**
 * Serializes an object of a known @Serializable type T to its JSON string representation.
 */
inline fun <reified T : Any> T?.toJson(): String {
    if (this == null) return "{}"
    return defJson.encodeToString(serializer(),this)
}

fun String?.toUppercaseAllWordRegex(): String? {
    return this?.replace(Regex("\\b\\w")) { it.value.uppercase() }
}


fun main() {
    data class Item(val id: Int = 1)
    data class YK(
        val name: String = "Ma:hd:i",
        val age: Int = 24,
        val list: List<Item> = listOf(Item(1), Item(2), Item(3), Item(4)),
        val list2: List<Item> = listOf(),
    )
}

/*fun Context.getGeoAddress(lat: Double, lon: Double): String? {
    return try {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: MutableList<Address>? = geocoder.getFromLocation(lat, lon, 1)
        if (!addresses.isNullOrEmpty()) {
            val address: Address = addresses[0]
            val addressLines = (0..address.maxAddressLineIndex).map { index ->
                address.getAddressLine(index)
            }
            addressLines.joinToString("\n")
        } else {
            null
        }
    } catch (e: Exception) {
        Log.e("Geocoder Error", "Error fetching address", e)
        null
    }
}*/

//fun Context?.getGeoAddress(location: Location, onSuccess: (String) -> Unit){
//    this ?: return
//    MainScope().launch {
//        getGeoAddress(location.latitude, location.longitude)?.let {
//            onSuccess(it)
//        }
//    }
//}
//
//@Suppress("DEPRECATION")
//suspend fun Context?.getGeoAddress(lat: Double, lon: Double): String? {
//    return withContext(Dispatchers.IO) {
//        this@getGeoAddress ?: return@withContext null
//        try {
//            val geocoder = Geocoder(this@getGeoAddress, Locale.getDefault())
//            val addresses: MutableList<Address>? = geocoder.getFromLocation(lat, lon, 1)
//            if (!addresses.isNullOrEmpty()) {
//                val address: Address = addresses[0]
//                val addressLines = (0..address.maxAddressLineIndex).map { index ->
//                    address.getAddressLine(index)
//                }
//                addressLines.joinToString("\n")
//            } else {
//                null
//            }
//        } catch (e: Exception) {
//            Log.e("Geocoder Error", "Error fetching address", e)
//            null
//        }
//    }
//}


fun String.toUiDateTime(): String {
    return try {
        // Backend: "2026-01-29 16:30:22.0"
        val cleaned = this
            .substringBefore(".")   // remove ".0"
            .replace(" ", "T")      // ISO format

        val dt = LocalDateTime.parse(cleaned)

        val hour = dt.hour.toString().padStart(2, '0')
        val minute = dt.minute.toString().padStart(2, '0')
        val day = dt.day.toString().padStart(2, '0')

        val month = dt.month.name
            .lowercase()
            .replaceFirstChar { it.uppercase() }
            .take(3)

        "$hour:$minute, $day $month ${dt.year}"
    } catch (e: Exception) {
        this // fallback
    }
}
