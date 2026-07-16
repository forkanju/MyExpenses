package ngo.friendship.mhealth.dc.data.local

import com.russhwolf.settings.Settings
import ngo.friendship.mhealth.dc.domain.model.QuestionAnswerJson
import ngo.friendship.mhealth.dc.domain.model.SetupData
import ngo.friendship.mhealth.dc.domain.model.User
import ngo.friendship.mhealth.dc.utils.fromJson
import ngo.friendship.mhealth.dc.utils.toJson

class LocalSettings {
    private val settings = Settings()

    private var _token: String? = null
    private var _user: User? = null

    val isUserLoggedIn
        get() = !token.isNullOrBlank()

    var token
        get() = _token ?: settings.getStringOrNull("token").also { _token = it }
        set(value) = settings.putOrRemove("token", value).also { _token = value }

    var user
        get() = _user ?: (settings.getStringOrNull("user").fromJson<User>() ?: User()).also { _user = it }
        set(value) = settings.putOrRemove("user", value).also { _user = value }

    var doseHistory: List<String>
        get() = settings.getStringOrNull("dose_history").fromJson<List<String>>() ?: emptyList()
        set(value) = settings.putString("dose_history", value.toJson())

    var questionAnswerData: QuestionAnswerJson?
        get() = settings.getStringOrNull("qa_data").fromJson<QuestionAnswerJson>()
        set(value) = settings.putOrRemove("qa_data", value)

    fun saveDoseToHistory(dose: String) {
        val current = doseHistory.toMutableList()
        current.remove(dose)
        current.add(0, dose)
        if (current.size > 50) {
            val trimmed = current.take(50)
            doseHistory = trimmed
        } else {
            doseHistory = current
        }
    }

    fun clear() {
        settings.clear()
        _token = null
        _user = null
    }

    inline fun <reified T> Settings.putOrRemove(key: String, value: T?) {
        if (value == null) {
            remove(key)
            return
        }
        when(value::class) {
            Int::class -> putInt(key, value as Int)
            Long::class -> putLong(key, value as Long)
            String::class -> putString(key, value as String)
            Float::class -> putFloat(key, value as Float)
            Double::class -> putDouble(key, value as Double)
            Boolean::class -> putBoolean(key, value as Boolean)
            else -> putString(key, value.toJson())
        }
    }
}