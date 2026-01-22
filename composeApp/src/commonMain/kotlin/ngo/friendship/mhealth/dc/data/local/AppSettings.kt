package ngo.friendship.mhealth.dc.data.local

import com.russhwolf.settings.Settings
import ngo.friendship.mhealth.dc.domain.model.User
import ngo.friendship.mhealth.dc.utils.fromJson
import ngo.friendship.mhealth.dc.utils.toJson

class AppSettings {
    private val settings = Settings()

    private var _token: String? = null
    private var _userInfo: User? = null

    val isUserLoggedIn
        get() = token != null

    var token
        get() = _token ?: settings.getStringOrNull("token").also { _token = it }
        set(value) = settings.putOrRemove("token", value).also { _token = value }

    var userInfo
        get() = _userInfo ?: (settings.getStringOrNull("userInfo").fromJson<User>() ?: User()).also { _userInfo = it }
        set(value) = settings.putOrRemove("userInfo", value).also { _userInfo = value }

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