package ngo.friendship.mhealth.dc.di

import ngo.friendship.mhealth.dc.data.local.LocalSettings
import ngo.friendship.mhealth.dc.data.repository.AuthRepositoryImpl
import ngo.friendship.mhealth.dc.data.repository.CaseRepositoryImpl
import ngo.friendship.mhealth.dc.data.repository.MainRepositoryImpl
import ngo.friendship.mhealth.dc.data.repository.MoreRepositoryImpl
import ngo.friendship.mhealth.dc.domain.repository.AuthRepository
import ngo.friendship.mhealth.dc.domain.repository.CaseRepository
import ngo.friendship.mhealth.dc.domain.repository.MainRepository
import ngo.friendship.mhealth.dc.domain.repository.MoreRepository
import ngo.friendship.mhealth.dc.fcm.AppNotifierManager
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.screens.auth.AuthViewModel
import ngo.friendship.mhealth.dc.presentation.screens.case.CaseViewModel
import ngo.friendship.mhealth.dc.presentation.screens.case.case_list.CaseListViewModel
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.DashboardViewModel
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.DxListViewModel
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.MedicineListViewModel
import ngo.friendship.mhealth.dc.presentation.screens.home.HomeViewModel
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.MoreViewModel
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
    viewModelOf(constructor = ::CaseListViewModel)
    viewModelOf(constructor = ::DashboardViewModel)
    viewModelOf(constructor = ::DxListViewModel)
    viewModelOf(constructor = ::MedicineListViewModel)
    viewModelOf(constructor = ::MoreViewModel)
    viewModelOf(constructor = ::HomeViewModel)
}

val dataModule = module {
    singleOf(constructor = ::AuthRepositoryImpl) bind AuthRepository::class
    singleOf(constructor = ::CaseRepositoryImpl) bind CaseRepository::class
    singleOf(constructor = ::MainRepositoryImpl) bind MainRepository::class
    singleOf(constructor = ::MoreRepositoryImpl) bind MoreRepository::class
}

val instantModule = module(createdAtStart = true) {
    singleOf(constructor = ::LocalSettings)
}

val fcmModule = module {
    single { AppNotifierManager() }
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

