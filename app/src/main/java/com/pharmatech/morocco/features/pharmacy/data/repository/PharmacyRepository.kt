package com.pharmatech.morocco.features.pharmacy.data.repository

import com.pharmatech.morocco.core.database.dao.PharmacyDao
import com.pharmatech.morocco.core.database.dao.FavoritePharmacyDao
import com.pharmatech.morocco.core.database.entities.PharmacyEntity
import com.pharmatech.morocco.core.database.entities.FavoritePharmacyEntity
import com.pharmatech.morocco.core.network.ApiService
import com.pharmatech.morocco.core.utils.Resource
import com.pharmatech.morocco.core.utils.NetworkMonitor
import com.pharmatech.morocco.core.utils.toEntity
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PharmacyRepository @Inject constructor(
    private val apiService: ApiService,
    private val pharmacyDao: PharmacyDao,
    private val favoriteDao: FavoritePharmacyDao,
    private val networkMonitor: NetworkMonitor
) {
    fun getNearbyPharmacies(
        latitude: Double,
        longitude: Double,
        radius: Int = 5000
    ): Flow<Resource<List<PharmacyEntity>>> = flow {
        emit(Resource.Loading())

        // First emit cached data
        pharmacyDao.getAllPharmacies().first().let { cached ->
            if (cached.isNotEmpty()) {
                emit(Resource.Success(cached))
            }
        }

        // Then fetch from network if online
        if (networkMonitor.isCurrentlyConnected()) {
            try {
                val response = apiService.getPharmacies(latitude, longitude, radius)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val entities = body.map { it.toEntity() }
                        pharmacyDao.insertPharmacies(entities)
                        emit(Resource.Success(entities))
                    } else {
                        // Response successful but body is null
                        val cached = pharmacyDao.getAllPharmacies().first()
                        if (cached.isNotEmpty()) {
                            emit(Resource.Success(cached))
                        } else {
                            emit(Resource.Error("No pharmacy data available"))
                        }
                    }
                } else {
                    emit(Resource.Error("Failed to fetch pharmacies: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Timber.e(e, "Error fetching pharmacies")
                // Still return cached data if available
                val cached = pharmacyDao.getAllPharmacies().first()
                if (cached.isNotEmpty()) {
                    emit(Resource.Success(cached))
                } else {
                    emit(Resource.Error(e.message ?: "Unknown error occurred"))
                }
            }
        } else {
            // If no cache and no network
            val cached = pharmacyDao.getAllPharmacies().first()
            if (cached.isEmpty()) {
                emit(Resource.Error("No internet connection"))
            }
        }
    }

    fun searchPharmacies(query: String): Flow<Resource<List<PharmacyEntity>>> = flow {
        emit(Resource.Loading())

        try {
            // Search in local database first
            val localResults = pharmacyDao.getAllPharmacies().first()
                .filter {
                    it.name.contains(query, ignoreCase = true) ||
                    it.address.contains(query, ignoreCase = true) ||
                    it.city.contains(query, ignoreCase = true)
                }

            if (localResults.isNotEmpty()) {
                emit(Resource.Success(localResults))
            }

            // Then search online if connected
            if (networkMonitor.isCurrentlyConnected()) {
                val response = apiService.searchPharmacies(query)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val entities = body.map { it.toEntity() }
                        pharmacyDao.insertPharmacies(entities)
                        emit(Resource.Success(entities))
                    } else if (localResults.isEmpty()) {
                        emit(Resource.Error("No pharmacies found for: $query"))
                    }
                } else if (localResults.isEmpty()) {
                    emit(Resource.Error("Search failed: ${response.code()} ${response.message()}"))
                }
            } else if (localResults.isEmpty()) {
                emit(Resource.Error("No results found offline"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Error searching pharmacies")
            emit(Resource.Error(e.message ?: "Search failed"))
        }
    }

    suspend fun toggleFavorite(
        userId: String,
        pharmacyId: String,
        pharmacyName: String
    ): Resource<Boolean> {
        return try {
            val existing = favoriteDao.isFavorite(userId, pharmacyId)

            if (existing != null) {
                favoriteDao.removeFavorite(userId, pharmacyId)
                Resource.Success(false)
            } else {
                favoriteDao.addFavorite(
                    FavoritePharmacyEntity(
                        userId = userId,
                        pharmacyId = pharmacyId,
                        pharmacyName = pharmacyName,
                        addedAt = Date()
                    )
                )
                Resource.Success(true)
            }
        } catch (e: Exception) {
            Timber.e(e, "Error toggling favorite")
            Resource.Error(e.message ?: "Failed to update favorite")
        }
    }

    fun getFavoritePharmacies(userId: String): Flow<List<PharmacyEntity>> {
        return favoriteDao.getFavoritePharmacies(userId).map { favorites ->
            val pharmacyIds = favorites.map { it.pharmacyId }
            pharmacyDao.getAllPharmacies().first().filter { it.id in pharmacyIds }
        }
    }

    fun getPharmacyById(pharmacyId: String): Flow<PharmacyEntity?> {
        return pharmacyDao.getPharmacyById(pharmacyId)
    }

    fun get24HourPharmacies(): Flow<List<PharmacyEntity>> {
        return pharmacyDao.get24HourPharmacies()
    }
}

