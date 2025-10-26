package com.pharmatech.morocco.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "medication_history")
data class MedicationHistoryEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val medicationId: String,
    val medicationName: String,
    val dosage: String,
    val takenAt: Date,
    val wasOnTime: Boolean,
    val scheduledTime: Date,
    val notes: String? = null,
    val sideEffectsReported: List<String> = emptyList(),
    val effectiveness: Int? = null, // 1-5 rating
    val createdAt: Date
)

