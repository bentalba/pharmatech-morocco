package com.pharmatech.morocco.features.medication.domain.model

import java.util.Date

data class Medication(
    val id: String,
    val name: String,
    val nameAr: String? = null,
    val nameFr: String? = null,
    val description: String,
    val category: String,
    val isOTC: Boolean,
    val activeIngredient: String,
    val dosageForm: String,
    val strength: String,
    val manufacturer: String,
    val imageUrl: String? = null,
    val barcode: String? = null,
    val sideEffects: List<String> = emptyList(),
    val contraindications: List<String> = emptyList(),
    val interactions: List<String> = emptyList(),
    val storageConditions: String,
    val price: Double? = null,
    val lastUpdated: Date
)

data class MedicationTracker(
    val id: String,
    val userId: String,
    val medicationId: String,
    val medicationName: String,
    val dosage: String,
    val frequency: String,
    val timeOfDay: List<String>,
    val startDate: Date,
    val endDate: Date? = null,
    val instructions: String? = null,
    val isActive: Boolean = true,
    val color: String = "#667EEA",
    val reminderEnabled: Boolean = true
)

data class MedicationReminder(
    val id: String,
    val trackerId: String,
    val medicationName: String,
    val scheduledTime: Date,
    val isTaken: Boolean = false,
    val takenAt: Date? = null,
    val isSkipped: Boolean = false,
    val skippedReason: String? = null
)

