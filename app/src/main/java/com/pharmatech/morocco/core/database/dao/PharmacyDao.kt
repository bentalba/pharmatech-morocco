package com.pharmatech.morocco.core.database.dao

import androidx.room.*
import com.pharmatech.morocco.core.database.entities.PharmacyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PharmacyDao {
    @Query("SELECT * FROM pharmacies")
    fun getAllPharmacies(): Flow<List<PharmacyEntity>>

    @Query("SELECT * FROM pharmacies WHERE id = :pharmacyId")
    fun getPharmacyById(pharmacyId: String): Flow<PharmacyEntity?>

    @Query("SELECT * FROM pharmacies WHERE is24Hours = 1")
    fun get24HourPharmacies(): Flow<List<PharmacyEntity>>

    @Query("SELECT * FROM pharmacies WHERE city = :city")
    fun getPharmaciesByCity(city: String): Flow<List<PharmacyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPharmacy(pharmacy: PharmacyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPharmacies(pharmacies: List<PharmacyEntity>)

    @Update
    suspend fun updatePharmacy(pharmacy: PharmacyEntity)

    @Delete
    suspend fun deletePharmacy(pharmacy: PharmacyEntity)

    @Query("DELETE FROM pharmacies")
    suspend fun deleteAllPharmacies()
}

