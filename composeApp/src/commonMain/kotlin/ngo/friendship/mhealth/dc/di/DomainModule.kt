package ngo.friendship.mhealth.dc.di

import ngo.friendship.mhealth.dc.domain.usecase.LoginUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::LoginUseCase)
}
