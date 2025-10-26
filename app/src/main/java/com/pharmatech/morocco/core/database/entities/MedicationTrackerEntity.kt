package com.pharmatech.morocco.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "medication_tracker")
data class MedicationTrackerEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val medicationId: String,
    val medicationName: String,
    val dosage: String,
    val frequency: String, // daily, twice_daily, weekly, etc.
    val timeOfDay: List<String>, // morning, afternoon, evening, night
    val startDate: Date,
    val endDate: Date? = null,
    val instructions: String? = null,
    val isActive: Boolean = true,
    val color: String = "#667EEA",
    val reminderEnabled: Boolean = true,
    val createdAt: Date,
    val lastUpdated: Date
)

