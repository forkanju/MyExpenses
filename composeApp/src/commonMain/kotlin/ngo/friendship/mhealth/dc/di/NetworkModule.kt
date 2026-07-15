package ngo.friendship.mhealth.dc.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
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
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableSharedFlow
import ngo.friendship.mhealth.dc.data.local.LocalSettings
import ngo.friendship.mhealth.dc.data.remote.ApiService
import ngo.friendship.mhealth.dc.domain.network.NetworkConfig
import ngo.friendship.mhealth.dc.utils.defJson
import ngo.friendship.mhealth.dc.utils.log
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
                url(NetworkConfig.getBaseUrl())
                accept(ContentType.Application.Json)
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 60_000L
                connectTimeoutMillis = 60_000L
                socketTimeoutMillis =  60_000L
            }
            install(HttpCache)
            install(ContentNegotiation) {
                json(defJson)
                register(ContentType.Text.Plain, KotlinxSerializationConverter(defJson))
            }
        }.apply {
            plugin(HttpSend).intercept { request ->
                val token = settings.token
                if (!token.isNullOrBlank()) {
                    request.header(HttpHeaders.Authorization, "Bearer $token")
                }
                val call = execute(request)
                if (call.response.status == HttpStatusCode.Unauthorized) {
                    settings.token = null
                    isUnauthorizedFlow.emit(true)
                }
                call
            }
        }
    }
}

val isUnauthorizedFlow = MutableSharedFlow<Boolean>()