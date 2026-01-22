package ngo.friendship.mhealth.dc.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ngo.friendship.mhealth.dc.data.repository.AuthRepositoryImpl
import ngo.friendship.mhealth.dc.domain.repository.AuthRepository
import ngo.friendship.mhealth.dc.presentation.screens.auth.AuthViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    viewModelOf(::AuthViewModel)

}

fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null //we will use this param in android context
) {
    startKoin {
        config?.invoke(this)
        modules(sharedModule)
    }
}

