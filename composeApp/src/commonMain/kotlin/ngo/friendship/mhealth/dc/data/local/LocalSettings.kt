package ngo.friendship.mhealth.dc.data.local

import com.russhwolf.settings.Settings
import ngo.friendship.mhealth.dc.domain.model.SetupData
import ngo.friendship.mhealth.dc.domain.model.User
import ngo.friendship.mhealth.dc.utils.fromJson
import ngo.friendship.mhealth.dc.utils.toJson

class LocalSettings {
    private val settings = Settings()

    private var _token: String? = null
    private var _user: User? = null

    val isUserLoggedIn
        get() = token != null

    var token
        get() = _token ?: settings.getStringOrNull("token").also { _token = it }
        set(value) = settings.putOrRemove("token", value).also { _token = value }

    var user
        get() = _user ?: (settings.getStringOrNull("user").fromJson<User>() ?: User()).also { _user = it }
        set(value) = settings.putOrRemove("user", value).also { _user = value }

    fun clear() {
        settings.clear()
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