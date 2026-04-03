package ngo.friendship.mhealth.dc.di

import ngo.friendship.mhealth.dc.data.local.AppDatabase
import ngo.friendship.mhealth.dc.data.local.buildPlatformWise
import ngo.friendship.mhealth.dc.data.local.getAppDatabaseBuilder
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        getAppDatabaseBuilder()
            .buildPlatformWise()
    }
}