package ngo.friendship.mhealth.dc.di

import ngo.friendship.mhealth.dc.data.local.LocalSettings
import ngo.friendship.mhealth.dc.data.repository.AuthRepositoryImpl
import ngo.friendship.mhealth.dc.data.repository.InterviewDetailsRepositoryImpl
import ngo.friendship.mhealth.dc.data.repository.InterviewListRepositoryImpl
import ngo.friendship.mhealth.dc.data.repository.SetupDataRepositoryImpl
import ngo.friendship.mhealth.dc.domain.network.ConnectionListener
import ngo.friendship.mhealth.dc.domain.repository.AuthRepository
import ngo.friendship.mhealth.dc.domain.repository.InterviewDetailsRepository
import ngo.friendship.mhealth.dc.domain.repository.InterviewListRepository
import ngo.friendship.mhealth.dc.domain.repository.SetupDataRepository
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.screens.auth.AuthViewModel
import ngo.friendship.mhealth.dc.presentation.screens.main.case.InterviewListViewModel
import ngo.friendship.mhealth.dc.presentation.screens.main.case.InterviewDetailsViewModel
import ngo.friendship.mhealth.dc.presentation.screens.main.case.SetupDataViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect fun platformModule(): Module

val viewModelModule = module {
    viewModelOf(constructor = ::AuthViewModel)
    viewModelOf(constructor = ::MainViewModel)
    viewModelOf(constructor = ::InterviewListViewModel)
    viewModelOf(constructor = ::InterviewDetailsViewModel)
    viewModelOf(constructor = ::SetupDataViewModel)
}

val dataModule = module {
    singleOf(constructor = ::AuthRepositoryImpl) bind AuthRepository::class
    singleOf(constructor = ::InterviewListRepositoryImpl) bind InterviewListRepository::class
    singleOf(constructor = ::InterviewDetailsRepositoryImpl) bind InterviewDetailsRepository::class
    singleOf(constructor = ::SetupDataRepositoryImpl) bind SetupDataRepository::class
}

val instantModule = module(createdAtStart = true) {
    singleOf(constructor = ::LocalSettings)
    singleOf(constructor = ::ConnectionListener)
}

val appModules = listOf(
    platformModule(),
    viewModelModule,
    dataModule,
    networkModule,
    instantModule
)

