package com.pharmatech.morocco.core.database.dao

import androidx.room.*
import com.pharmatech.morocco.core.database.entities.HealthInsightEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthInsightDao {
    @Query("SELECT * FROM health_insights WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAllInsights(userId: String): Flow<List<HealthInsightEntity>>

    @Query("SELECT * FROM health_insights WHERE userId = :userId AND isRead = 0 ORDER BY priority DESC, createdAt DESC")
    fun getUnreadInsights(userId: String): Flow<List<HealthInsightEntity>>

    @Query("SELECT * FROM health_insights WHERE userId = :userId AND type = :type ORDER BY createdAt DESC")
    fun getInsightsByType(userId: String, type: String): Flow<List<HealthInsightEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInsight(insight: HealthInsightEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInsights(insights: List<HealthInsightEntity>)

    @Update
    suspend fun updateInsight(insight: HealthInsightEntity)

    @Query("UPDATE health_insights SET isRead = 1 WHERE id = :insightId")
    suspend fun markAsRead(insightId: String)

    @Delete
    suspend fun deleteInsight(insight: HealthInsightEntity)

    @Query("DELETE FROM health_insights WHERE expiresAt < :currentDate")
    suspend fun deleteExpiredInsights(currentDate: Long)
}

