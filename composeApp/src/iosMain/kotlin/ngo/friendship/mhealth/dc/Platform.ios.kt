package ngo.friendship.mhealth.dc

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry
import platform.UIKit.UIDevice
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
actual fun getPlatform(): Platform = Platform.iOS.apply {
    info = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

@OptIn(ExperimentalComposeUiApi::class)
actual suspend fun ClipEntry.getText() = getPlainText()

@OptIn(ExperimentalNativeApi::class)
actual val isDebugBuild: Boolean
    get() = kotlin.native.Platform.isDebugBinary

actual fun getDeviceId(): String =
    "${getPlatform().name}-${UIDevice().identifierForVendor?.UUIDString}"