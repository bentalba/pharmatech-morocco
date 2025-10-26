package com.pharmatech.morocco.features.tracker.data.repository

import com.pharmatech.morocco.core.database.dao.MedicationTrackerDao
import com.pharmatech.morocco.core.database.dao.ReminderDao
import com.pharmatech.morocco.core.database.dao.MedicationHistoryDao
import com.pharmatech.morocco.core.database.entities.*
import com.pharmatech.morocco.core.utils.Resource
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackerRepository @Inject constructor(
    private val trackerDao: MedicationTrackerDao,
    private val reminderDao: ReminderDao,
    private val historyDao: MedicationHistoryDao
) {
    fun getActiveTrackers(userId: String): Flow<List<MedicationTrackerEntity>> {
        return trackerDao.getActiveMedicationTrackers(userId)
    }

    suspend fun addMedicationTracker(tracker: MedicationTrackerEntity): Resource<Unit> {
        return try {
            trackerDao.insertTracker(tracker)

            // Create reminders for the next 7 days
            createRemindersForTracker(tracker)

            Resource.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error adding medication tracker")
            Resource.Error(e.message ?: "Failed to add medication")
        }
    }

    private suspend fun createRemindersForTracker(tracker: MedicationTrackerEntity) {
        val reminders = mutableListOf<ReminderEntity>()
        val calendar = Calendar.getInstance()

        for (day in 0..6) {
            tracker.timeOfDay.forEach { time ->
                calendar.time = Date()
                calendar.add(Calendar.DAY_OF_YEAR, day)

                // Parse time (e.g., "08:00" or "Morning")
                try {
                    if (time.contains(":")) {
                        val timeParts = time.split(":")
                        calendar.set(Calendar.HOUR_OF_DAY, timeParts[0].toInt())
                        calendar.set(Calendar.MINUTE, timeParts[1].toInt())
                    } else {
                        // Default times for named periods
                        val hour = when (time.lowercase()) {
                            "morning" -> 8
                            "afternoon" -> 14
                            "evening" -> 18
                            "night" -> 21
                            else -> 9
                        }
                        calendar.set(Calendar.HOUR_OF_DAY, hour)
                        calendar.set(Calendar.MINUTE, 0)
                    }
                    calendar.set(Calendar.SECOND, 0)

                    reminders.add(
                        ReminderEntity(
                            id = UUID.randomUUID().toString(),
                            userId = tracker.userId,
                            trackerId = tracker.id,
                            medicationName = tracker.medicationName,
                            scheduledTime = calendar.time,
                            isTaken = false,
                            isSkipped = false,
                            notificationSent = false,
                            createdAt = Date()
                        )
                    )
                } catch (e: Exception) {
                    Timber.e(e, "Error parsing time: $time")
                }
            }
        }

        if (reminders.isNotEmpty()) {
            reminderDao.insertReminders(reminders)
        }
    }

    suspend fun markMedicationTaken(
        reminderId: String,
        takenAt: Date = Date()
    ): Resource<Unit> {
        return try {
            reminderDao.markAsTaken(reminderId, takenAt)

            // Add to history
            val reminder = reminderDao.getReminderById(reminderId).first()
            reminder?.let {
                val tracker = trackerDao.getTrackerById(it.trackerId).first()
                historyDao.insertHistory(
                    MedicationHistoryEntity(
                        id = UUID.randomUUID().toString(),
                        userId = it.userId,
                        medicationId = it.trackerId,
                        medicationName = it.medicationName,
                        dosage = tracker?.dosage ?: "",
                        takenAt = takenAt,
                        wasOnTime = isOnTime(it.scheduledTime, takenAt),
                        scheduledTime = it.scheduledTime,
                        createdAt = Date()
                    )
                )
            }

            Resource.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error marking medication as taken")
            Resource.Error(e.message ?: "Failed to mark as taken")
        }
    }

    suspend fun skipMedication(reminderId: String, reason: String?): Resource<Unit> {
        return try {
            reminderDao.markAsSkipped(reminderId, reason)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error skipping medication")
            Resource.Error(e.message ?: "Failed to skip medication")
        }
    }

    private fun isOnTime(scheduled: Date, taken: Date): Boolean {
        val diff = kotlin.math.abs(scheduled.time - taken.time)
        return diff < 30 * 60 * 1000 // Within 30 minutes
    }

    fun getTodayReminders(userId: String): Flow<List<ReminderEntity>> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startOfDay = calendar.time

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val endOfDay = calendar.time

        return reminderDao.getRemindersBetween(userId, startOfDay, endOfDay)
    }

    fun getPendingReminders(userId: String): Flow<List<ReminderEntity>> {
        return reminderDao.getPendingReminders(userId)
    }

    fun getAdherenceRate(userId: String, days: Int = 7): Flow<Float> {
        return flow {
            try {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_YEAR, -days)
                val startDate = calendar.time

                val reminders = reminderDao.getRemindersBetween(userId, startDate, Date()).first()

                if (reminders.isEmpty()) {
                    emit(100f) // No reminders = perfect adherence
                } else {
                    val takenCount = reminders.count { it.isTaken }
                    val adherenceRate = (takenCount.toFloat() / reminders.size) * 100
                    emit(adherenceRate)
                }
            } catch (e: Exception) {
                Timber.e(e, "Error calculating adherence rate")
                emit(0f)
            }
        }
    }

    fun getMedicationHistory(userId: String): Flow<List<MedicationHistoryEntity>> {
        return historyDao.getMedicationHistory(userId)
    }

    suspend fun updateTracker(tracker: MedicationTrackerEntity): Resource<Unit> {
        return try {
            trackerDao.updateTracker(tracker)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error updating tracker")
            Resource.Error(e.message ?: "Failed to update tracker")
        }
    }

    suspend fun deleteTracker(trackerId: String): Resource<Unit> {
        return try {
            trackerDao.deactivateTracker(trackerId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error deleting tracker")
            Resource.Error(e.message ?: "Failed to delete tracker")
        }
    }
}

