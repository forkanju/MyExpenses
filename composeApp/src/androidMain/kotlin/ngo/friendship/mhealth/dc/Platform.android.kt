package ngo.friendship.mhealth.dc

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.compose.ui.platform.ClipEntry
import ngo.friendship.mhealth.dc.utils.tryGet

actual fun getPlatform(): Platform= Platform.Android.apply {
    info = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual suspend fun ClipEntry.getText(): String? {
    return tryGet {
        val itemCount = clipData.itemCount
        var textFull = ""
        for (i in 0 ..< itemCount) {
            val item = clipData.getItemAt(i)
            val text = item?.text
            if (text != null)
                textFull += text
        }
        textFull.ifEmpty { null }
    }
}

actual val isDebugBuild
    get() = CMPApplication.isDebug

val androidContext: Context
    get() = (CMPApplication.context as Context)

@SuppressLint("HardwareIds")
actual fun getDeviceId() : String =
    "${getPlatform().name}-${Settings.Secure.getString(androidContext.contentResolver, Settings.Secure.ANDROID_ID)}"