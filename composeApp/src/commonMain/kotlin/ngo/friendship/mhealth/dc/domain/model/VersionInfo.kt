package ngo.friendship.mhealth.dc.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class VersionInfo(
    val versionName: String,
    val versionCode: Int,
    val releaseDate: String,
    val features: List<String> = emptyList(),
    val bugFixes: List<String> = emptyList()
)
