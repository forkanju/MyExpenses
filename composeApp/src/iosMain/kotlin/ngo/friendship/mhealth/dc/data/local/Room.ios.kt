package ngo.friendship.mhealth.dc.data.local

import androidx.room3.Room
import androidx.room3.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.scope.Scope
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual fun Scope.getAppDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = getDatabasePath(DatabasePaths.HEALTH_DB)
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun getDatabasePath(databaseName: String): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return "${documentDirectory?.path}/$databaseName"
}