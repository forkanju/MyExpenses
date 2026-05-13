package ngo.friendship.mhealth.dc.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import ngo.friendship.mhealth.dc.domain.model.Medicine

@Dao
interface MedicineDao {
    @Query("SELECT * FROM Medicine")
    suspend fun getAllMedicines(): List<Medicine>

    @Query("SELECT * FROM Medicine WHERE type = :type")
    suspend fun getMedicinesByType(type: String): List<Medicine>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicines(medicines: List<Medicine>)

    @Query("DELETE FROM Medicine")
    suspend fun deleteAllMedicines()

    @Query("DELETE FROM Medicine WHERE type = :type")
    suspend fun deleteMedicinesByType(type: String)
}
