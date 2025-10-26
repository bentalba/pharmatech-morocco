package com.pharmatech.morocco.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "favorite_pharmacies")
data class FavoritePharmacyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String,
    val pharmacyId: String,
    val pharmacyName: String,
    val addedAt: Date
)

