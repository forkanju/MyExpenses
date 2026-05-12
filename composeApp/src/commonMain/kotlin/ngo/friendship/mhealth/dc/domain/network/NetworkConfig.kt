package ngo.friendship.mhealth.dc.domain.network

import ngo.friendship.mhealth.dc.isDebugBuild

object NetworkConfig {
    const val BASE_URL_PROD = "https://apps.friendship.ngo"
//    const val BASE_URL_DEV = "http://devs2.apps.friendship.ngo:8080"
    const val BASE_URL_DEV = "http://10.11.9.49:8080"

    fun getBaseUrl(): String =
        if (isDebugBuild) BASE_URL_DEV else BASE_URL_PROD

    fun getBaseHost(): String {
        val url = getBaseUrl()
        return url.substringAfter("://").substringBefore("/")
    }
}