package ngo.friendship.mhealth.dc.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ngo.friendship.mhealth.dc.data.local.AppSettings
import ngo.friendship.mhealth.dc.data.repository.AuthRepositoryImpl
import ngo.friendship.mhealth.dc.domain.network.ConnectionListener
import ngo.friendship.mhealth.dc.domain.repository.AuthRepository
import ngo.friendship.mhealth.dc.presentation.screens.auth.AuthViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect fun platformModule(): Module

val viewModelModule = module {
    viewModelOf(::AuthViewModel)
}

val dataModule = module {
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
}

val instantModule = module(createdAtStart = true) {
    singleOf(::AppSettings)
    singleOf(::ConnectionListener)
}

val appModules = listOf(
    platformModule(),
    viewModelModule,
    dataModule,
    networkModule,
    instantModule
)

