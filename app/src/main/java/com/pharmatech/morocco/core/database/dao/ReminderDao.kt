package com.pharmatech.morocco.core.database.dao

import androidx.room.*
import com.pharmatech.morocco.core.database.entities.ReminderEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminders WHERE userId = :userId ORDER BY scheduledTime ASC")
    fun getAllReminders(userId: String): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE userId = :userId AND isTaken = 0 AND isSkipped = 0 ORDER BY scheduledTime ASC")
    fun getPendingReminders(userId: String): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE userId = :userId AND scheduledTime BETWEEN :startDate AND :endDate ORDER BY scheduledTime ASC")
    fun getRemindersBetween(userId: String, startDate: Date, endDate: Date): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE id = :reminderId")
    fun getReminderById(reminderId: String): Flow<ReminderEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminders(reminders: List<ReminderEntity>)

    @Update
    suspend fun updateReminder(reminder: ReminderEntity)

    @Delete
    suspend fun deleteReminder(reminder: ReminderEntity)

    @Query("UPDATE reminders SET isTaken = 1, takenAt = :takenAt WHERE id = :reminderId")
    suspend fun markAsTaken(reminderId: String, takenAt: Date)

    @Query("UPDATE reminders SET isSkipped = 1, skippedReason = :reason WHERE id = :reminderId")
    suspend fun markAsSkipped(reminderId: String, reason: String?)

    @Query("DELETE FROM reminders WHERE scheduledTime < :date")
    suspend fun deleteOldReminders(date: Date)
}

