package ngo.friendship.mhealth.dc.di

import ngo.friendship.mhealth.dc.notification.AppNotifierManager
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformModule(): Module

val fcmModule = module {
    single { AppNotifierManager() }
}

val appModules = listOf(
    platformModule(),
    presentationModule,
    domainModule,
    dataModule,
    networkModule,
    databaseModule,
    fcmModule
)
