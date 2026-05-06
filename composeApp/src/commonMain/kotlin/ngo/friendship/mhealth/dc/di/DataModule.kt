package ngo.friendship.mhealth.dc.di

import ngo.friendship.mhealth.dc.data.local.LocalSettings
import ngo.friendship.mhealth.dc.data.repository.LoginRepositoryImpl
import ngo.friendship.mhealth.dc.data.repository.CaseRepositoryImpl
import ngo.friendship.mhealth.dc.data.repository.MainRepositoryImpl
import ngo.friendship.mhealth.dc.data.repository.MoreRepositoryImpl
import ngo.friendship.mhealth.dc.domain.repository.LoginRepository
import ngo.friendship.mhealth.dc.domain.repository.CaseRepository
import ngo.friendship.mhealth.dc.domain.repository.MainRepository
import ngo.friendship.mhealth.dc.domain.repository.MoreRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    singleOf(::LoginRepositoryImpl) bind LoginRepository::class
    singleOf(::CaseRepositoryImpl) bind CaseRepository::class
    singleOf(::MainRepositoryImpl) bind MainRepository::class
    singleOf(::MoreRepositoryImpl) bind MoreRepository::class
    
    singleOf(::LocalSettings)
}
