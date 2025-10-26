package com.pharmatech.morocco.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val email: String,
    val displayName: String,
    val phoneNumber: String? = null,
    val photoUrl: String? = null,
    val dateOfBirth: Date? = null,
    val gender: String? = null,
    val bloodType: String? = null,
    val allergies: List<String> = emptyList(),
    val chronicConditions: List<String> = emptyList(),
    val emergencyContact: String? = null,
    val preferredLanguage: String = "fr",
    val isPremium: Boolean = false,
    val createdAt: Date,
    val lastUpdated: Date
)

