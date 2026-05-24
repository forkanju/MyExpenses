package ngo.friendship.mhealth.dc.data.local

import androidx.room3.*
import ngo.friendship.mhealth.dc.data.local.dao.*
import ngo.friendship.mhealth.dc.domain.model.*

@Database(
    entities = [
        MedicineBrandType::class,
        Investigation::class,
        Diagnosis::class,
        ReferralCenter::class,
        UserProfile::class,
        Medicine::class,
    ],
    version = 2
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun setupDataDao(): SetupDataDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun medicineDao(): MedicineDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}