package ngo.friendship.mhealth.dc.di

import ngo.friendship.mhealth.dc.data.local.LocalSettings
import ngo.friendship.mhealth.dc.data.repository.AuthRepositoryImpl
import ngo.friendship.mhealth.dc.data.repository.CaseRepositoryImpl
import ngo.friendship.mhealth.dc.data.repository.MainRepositoryImpl
import ngo.friendship.mhealth.dc.domain.repository.AuthRepository
import ngo.friendship.mhealth.dc.domain.repository.CaseRepository
import ngo.friendship.mhealth.dc.domain.repository.MainRepository
import ngo.friendship.mhealth.dc.fcm.NotifierManager
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.screens.auth.AuthViewModel
import ngo.friendship.mhealth.dc.presentation.screens.case.CaseViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect fun platformModule(): Module

val viewModelModule = module {
    viewModelOf(constructor = ::AuthViewModel)
    viewModelOf(constructor = ::MainViewModel)
    viewModelOf(constructor = ::CaseViewModel)
}

val dataModule = module {
    singleOf(constructor = ::AuthRepositoryImpl) bind AuthRepository::class
    singleOf(constructor = ::CaseRepositoryImpl) bind CaseRepository::class
    singleOf(constructor = ::MainRepositoryImpl) bind MainRepository::class
}

val instantModule = module(createdAtStart = true) {
    singleOf(constructor = ::LocalSettings)
}

val fcmModule = module {
    single { NotifierManager() }
}

val appModules = listOf(
    platformModule(),
    viewModelModule,
    databaseModule,
    dataModule,
    networkModule,
    instantModule,
    fcmModule
)

