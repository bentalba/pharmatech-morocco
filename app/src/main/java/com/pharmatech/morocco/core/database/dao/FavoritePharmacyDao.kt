package com.pharmatech.morocco.core.database.dao

import androidx.room.*
import com.pharmatech.morocco.core.database.entities.FavoritePharmacyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePharmacyDao {
    @Query("SELECT * FROM favorite_pharmacies WHERE userId = :userId ORDER BY addedAt DESC")
    fun getFavoritePharmacies(userId: String): Flow<List<FavoritePharmacyEntity>>

    @Query("SELECT * FROM favorite_pharmacies WHERE userId = :userId AND pharmacyId = :pharmacyId")
    suspend fun isFavorite(userId: String, pharmacyId: String): FavoritePharmacyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoritePharmacyEntity)

    @Query("DELETE FROM favorite_pharmacies WHERE userId = :userId AND pharmacyId = :pharmacyId")
    suspend fun removeFavorite(userId: String, pharmacyId: String)

    @Delete
    suspend fun deleteFavorite(favorite: FavoritePharmacyEntity)
}

