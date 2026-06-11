package ngo.friendship.mhealth.dc.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Transaction
import kotlinx.coroutines.flow.Flow
import ngo.friendship.mhealth.dc.domain.model.Diagnosis
import ngo.friendship.mhealth.dc.domain.model.Investigation
import ngo.friendship.mhealth.dc.domain.model.MedicineBrandType
import ngo.friendship.mhealth.dc.domain.model.ReferralCenter
import ngo.friendship.mhealth.dc.domain.model.SetupData

@Dao
interface SetupDataDao {
    @Query("SELECT * FROM MedicineBrandType")
    fun observeMedicineBrandTypes(): Flow<List<MedicineBrandType>>

    @Query("SELECT * FROM Investigation")
    fun observeInvestigations(): Flow<List<Investigation>>

    @Query("SELECT * FROM Diagnosis")
    fun observeDiagnoses(): Flow<List<Diagnosis>>

    @Query("SELECT * FROM ReferralCenter")
    fun observeReferralCenters(): Flow<List<ReferralCenter>>

    @Query("SELECT * FROM MedicineBrandType")
    suspend fun getMedicineBrandTypes(): List<MedicineBrandType>
    @Query("SELECT * FROM Investigation")
    suspend fun getInvestigations(): List<Investigation>
    @Query("SELECT * FROM Diagnosis")
    suspend fun getDiagnoses(): List<Diagnosis>
    @Query("SELECT * FROM ReferralCenter")
    suspend fun getReferralCenters(): List<ReferralCenter>

    @Insert
    suspend fun insertMedicineBrandTypes(list: List<MedicineBrandType>)
    @Insert
    suspend fun insertInvestigations(list: List<Investigation>)
    @Insert
    suspend fun insertDiagnoses(list: List<Diagnosis>)
    @Insert
    suspend fun insertReferralCenters(list: List<ReferralCenter>)
    
    @Query("DELETE FROM MedicineBrandType")
    suspend fun deleteMedicineBrandTypes()
    @Query("DELETE FROM Investigation")
    suspend fun deleteInvestigations()
    @Query("DELETE FROM Diagnosis")
    suspend fun deleteDiagnoses()
    @Query("DELETE FROM ReferralCenter")
    suspend fun deleteReferralCenters()

    @Transaction
    suspend fun deleteAllData() {
        deleteMedicineBrandTypes()
        deleteInvestigations()
        deleteDiagnoses()
        deleteReferralCenters()
    }

    @Transaction
    suspend fun replaceData(setupData: SetupData) {
        deleteAllData()
        insertMedicineBrandTypes(setupData.medicineBrandTypes)
        insertInvestigations(setupData.investigations)
        insertDiagnoses(setupData.diagnoses)
        insertReferralCenters(setupData.referralCenters)
    }
}