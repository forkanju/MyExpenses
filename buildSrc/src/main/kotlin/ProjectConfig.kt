@Suppress("ConstPropertyName")
object ProjectConfig {
    private const val versionMajor = 1
    private const val versionMinor = 0
    private const val versionPatch = 0

    const val packageName = "ngo.friendship.mhealth.dc"
    const val packageNameCommon = "$packageName.common"

    const val minSdk = 26
    const val compileSdk = 36
    const val targetSdk = 36
    val versionCode = genVersionCode(versionMajor, versionMinor, versionPatch)
    val versionName = genVersionName(versionMajor, versionMinor, versionPatch)

    const val IS_DEBUG = true

    const val javaVersion = 21
    const val BASE_URL_LIVE = "https://apps.friendship.ngo"
    const val BASE_URL_DEV = "http://devs2.apps.friendship.ngo:8080"

    fun genVersionCode(major: Int, minor: Int, patch: Int) = major * 10_000 + minor * 100 + patch
    fun genVersionName(major: Int, minor: Int, patch: Int) = "$major.$minor.$patch"
}