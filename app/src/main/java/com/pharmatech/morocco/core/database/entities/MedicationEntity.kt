package com.pharmatech.morocco.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "medications")
data class MedicationEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val nameAr: String? = null,
    val nameFr: String? = null,
    val description: String,
    val descriptionAr: String? = null,
    val descriptionFr: String? = null,
    val category: String,
    val isOTC: Boolean,
    val activeIngredient: String,
    val dosageForm: String, // tablet, syrup, injection, etc.
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

