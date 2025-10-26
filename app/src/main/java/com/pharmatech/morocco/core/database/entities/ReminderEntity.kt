package com.pharmatech.morocco.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val trackerId: String,
    val medicationName: String,
    val scheduledTime: Date,
    val isTaken: Boolean = false,
    val takenAt: Date? = null,
    val isSkipped: Boolean = false,
    val skippedReason: String? = null,
    val notificationSent: Boolean = false,
    val createdAt: Date
)

