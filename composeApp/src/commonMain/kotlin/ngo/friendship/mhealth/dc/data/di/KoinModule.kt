package ngo.friendship.mhealth.dc.data.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module

val sharedModule = module {
//    single<CustomerRepository> { CustomerRepositoryImpl() }
//    single<AdminRepository> { AdminRepositoryImpl() }
//    viewModelOf(::AuthViewModel)
//    viewModelOf(::HomeGraphViewModel)
//    viewModelOf(::ProfileViewModel)
//    viewModelOf(::ManageProductViewModel)
}

fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null //we will use this param in android context
) {
    startKoin {
        config?.invoke(this)
        modules(sharedModule)
    }
}

