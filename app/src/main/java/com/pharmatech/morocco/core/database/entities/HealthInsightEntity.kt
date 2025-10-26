package com.pharmatech.morocco.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "health_insights")
data class HealthInsightEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val type: String, // reminder, warning, tip, achievement
    val title: String,
    val titleAr: String? = null,
    val titleFr: String? = null,
    val message: String,
    val messageAr: String? = null,
    val messageFr: String? = null,
    val priority: String, // low, medium, high
    val isRead: Boolean = false,
    val actionRequired: Boolean = false,
    val actionUrl: String? = null,
    val createdAt: Date,
    val expiresAt: Date? = null
)

