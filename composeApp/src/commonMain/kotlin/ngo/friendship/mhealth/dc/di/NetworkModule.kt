package ngo.friendship.mhealth.dc.di

import ngo.friendship.mhealth.dc.data.local.LocalSettings
import ngo.friendship.mhealth.dc.utils.log
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ngo.friendship.mhealth.dc.BuildKonfig
import ngo.friendship.mhealth.dc.data.remote.ApiService
import org.koin.dsl.module
import ro.cosminmihu.ktor.monitor.ContentLength
import ro.cosminmihu.ktor.monitor.KtorMonitorLogging
import ro.cosminmihu.ktor.monitor.RetentionPeriod

val networkModule = module {
    single {
        ApiService(get())
    }
    single {
        val settings = get<LocalSettings>()
        HttpClient {
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        message.log("Ktor")
                    }
                }
            }
            install(KtorMonitorLogging) {
                showNotification = true
                retentionPeriod = RetentionPeriod.OneWeek
                maxContentLength = ContentLength.Full
            }
            install(DefaultRequest) {
                url(BuildKonfig.BASE_URL)
                accept(ContentType.Application.Json)
                settings.token?.let {
                    header(HttpHeaders.Authorization, "Bearer $it")
                }
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 20_000L
                connectTimeoutMillis = 20_000L
                socketTimeoutMillis = 20_000L
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                    explicitNulls = false
                })
            }
        }.apply {
            plugin(HttpSend).intercept { request ->
                execute(request).apply {
                    if (response.status == HttpStatusCode.Unauthorized)
                        settings.token = null
                }
            }
        }
    }
}