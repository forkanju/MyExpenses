package ngo.friendship.mhealth.dc.data.local

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase
import org.koin.core.scope.Scope

actual fun Scope.getAppDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val context = get<Context>().applicationContext
    val dbFile = context.getDatabasePath(DatabasePaths.HEALTH_DB)
    return Room.databaseBuilder<AppDatabase>(
        context = context,
        name = dbFile.absolutePath
    )
}