package ngo.friendship.mhealth.dc

import androidx.compose.ui.platform.ClipEntry

@Suppress("EnumEntryName")
enum class Platform(var info: String = "") {
    Android,
    iOS
}

expect fun getPlatform(): Platform

expect fun getDeviceId(): String

expect suspend fun ClipEntry.getText() : String?

expect val isDebugBuild: Boolean
