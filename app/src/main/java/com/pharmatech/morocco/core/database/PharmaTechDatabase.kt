package com.pharmatech.morocco.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pharmatech.morocco.core.database.converters.DateConverter
import com.pharmatech.morocco.core.database.converters.ListConverter
import com.pharmatech.morocco.core.database.dao.*
import com.pharmatech.morocco.core.database.entities.*

@Database(
    entities = [
        UserEntity::class,
        PharmacyEntity::class,
        MedicationEntity::class,
        MedicationTrackerEntity::class,
        ReminderEntity::class,
        FavoritePharmacyEntity::class,
        MedicationHistoryEntity::class,
        HealthInsightEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(DateConverter::class, ListConverter::class)
abstract class PharmaTechDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun pharmacyDao(): PharmacyDao
    abstract fun medicationDao(): MedicationDao
    abstract fun trackerDao(): MedicationTrackerDao
    abstract fun reminderDao(): ReminderDao
    abstract fun favoriteDao(): FavoritePharmacyDao
    abstract fun historyDao(): MedicationHistoryDao
    abstract fun insightDao(): HealthInsightDao
}

