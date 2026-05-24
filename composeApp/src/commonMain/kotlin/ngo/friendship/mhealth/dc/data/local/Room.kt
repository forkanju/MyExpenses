package ngo.friendship.mhealth.dc.data.local

import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import ngo.friendship.mhealth.dc.Platform
import ngo.friendship.mhealth.dc.getPlatform
import ngo.friendship.mhealth.dc.utils.applyIf
import org.koin.core.scope.Scope

object DatabasePaths {
    const val HEALTH_DB = "friendship_health.db"
}

expect fun Scope.getAppDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>

fun <T : RoomDatabase> RoomDatabase.Builder<T>.buildPlatformWise(): T =
    setQueryCoroutineContext(Dispatchers.IO)
        .fallbackToDestructiveMigration()
        .applyIf(getPlatform() == Platform.iOS) {
            setDriver(BundledSQLiteDriver())
        }.build()