package com.pharmatech.morocco.core.database.dao

import androidx.room.*
import com.pharmatech.morocco.core.database.entities.MedicationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {
    @Query("SELECT * FROM medications ORDER BY name ASC")
    fun getAllMedications(): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medications WHERE id = :medicationId")
    fun getMedicationById(medicationId: String): Flow<MedicationEntity?>

    @Query("SELECT * FROM medications WHERE category = :category ORDER BY name ASC")
    fun getMedicationsByCategory(category: String): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medications WHERE isOTC = :isOTC ORDER BY name ASC")
    fun getMedicationsByOTC(isOTC: Boolean): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medications WHERE name LIKE '%' || :query || '%' OR activeIngredient LIKE '%' || :query || '%'")
    fun searchMedications(query: String): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medications WHERE barcode = :barcode")
    suspend fun getMedicationByBarcode(barcode: String): MedicationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(medication: MedicationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedications(medications: List<MedicationEntity>)

    @Update
    suspend fun updateMedication(medication: MedicationEntity)

    @Delete
    suspend fun deleteMedication(medication: MedicationEntity)

    @Query("DELETE FROM medications")
    suspend fun deleteAllMedications()
}

