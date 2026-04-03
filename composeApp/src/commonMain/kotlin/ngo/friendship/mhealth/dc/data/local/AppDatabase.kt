package ngo.friendship.mhealth.dc.data.local

import androidx.room3.*
import ngo.friendship.mhealth.dc.data.local.dao.SetupDataDao
import ngo.friendship.mhealth.dc.domain.model.Diagnosis
import ngo.friendship.mhealth.dc.domain.model.Investigation
import ngo.friendship.mhealth.dc.domain.model.MedicineBrandType
import ngo.friendship.mhealth.dc.domain.model.ReferralCenter

@Database(
    entities = [
        MedicineBrandType::class,
        Investigation::class,
        Diagnosis::class,
        ReferralCenter::class,
    ],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun setupDataDao(): SetupDataDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}