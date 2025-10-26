package com.pharmatech.morocco.core.database.dao

import androidx.room.*
import com.pharmatech.morocco.core.database.entities.MedicationTrackerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationTrackerDao {
    @Query("SELECT * FROM medication_tracker WHERE userId = :userId AND isActive = 1 ORDER BY createdAt DESC")
    fun getActiveMedicationTrackers(userId: String): Flow<List<MedicationTrackerEntity>>

    @Query("SELECT * FROM medication_tracker WHERE id = :trackerId")
    fun getTrackerById(trackerId: String): Flow<MedicationTrackerEntity?>

    @Query("SELECT * FROM medication_tracker WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAllTrackers(userId: String): Flow<List<MedicationTrackerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracker(tracker: MedicationTrackerEntity)

    @Update
    suspend fun updateTracker(tracker: MedicationTrackerEntity)

    @Delete
    suspend fun deleteTracker(tracker: MedicationTrackerEntity)

    @Query("UPDATE medication_tracker SET isActive = 0 WHERE id = :trackerId")
    suspend fun deactivateTracker(trackerId: String)
}

