package com.pharmatech.morocco.core.database.dao

import androidx.room.*
import com.pharmatech.morocco.core.database.entities.MedicationHistoryEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface MedicationHistoryDao {
    @Query("SELECT * FROM medication_history WHERE userId = :userId ORDER BY takenAt DESC")
    fun getMedicationHistory(userId: String): Flow<List<MedicationHistoryEntity>>

    @Query("SELECT * FROM medication_history WHERE userId = :userId AND takenAt BETWEEN :startDate AND :endDate ORDER BY takenAt DESC")
    fun getHistoryBetween(userId: String, startDate: Date, endDate: Date): Flow<List<MedicationHistoryEntity>>

    @Query("SELECT * FROM medication_history WHERE userId = :userId AND medicationId = :medicationId ORDER BY takenAt DESC")
    fun getHistoryForMedication(userId: String, medicationId: String): Flow<List<MedicationHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: MedicationHistoryEntity)

    @Delete
    suspend fun deleteHistory(history: MedicationHistoryEntity)

    @Query("DELETE FROM medication_history WHERE takenAt < :date")
    suspend fun deleteOldHistory(date: Date)
}

