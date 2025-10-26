package com.pharmatech.morocco.features.medication.data.repository

import com.pharmatech.morocco.core.database.dao.MedicationDao
import com.pharmatech.morocco.core.database.entities.MedicationEntity
import com.pharmatech.morocco.core.network.ApiService
import com.pharmatech.morocco.core.utils.Resource
import com.pharmatech.morocco.core.utils.NetworkMonitor
import com.pharmatech.morocco.core.utils.toEntity
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicationRepository @Inject constructor(
    private val apiService: ApiService,
    private val medicationDao: MedicationDao,
    private val networkMonitor: NetworkMonitor
) {
    fun searchMedications(query: String): Flow<Resource<List<MedicationEntity>>> = flow {
        emit(Resource.Loading())

        try {
            // Search locally first
            val localResults = medicationDao.searchMedications(query).first()
            if (localResults.isNotEmpty()) {
                emit(Resource.Success(localResults))
            }

            // Then fetch from network
            if (networkMonitor.isCurrentlyConnected()) {
                val response = apiService.searchMedications(query)
                if (response.isSuccessful) {
                    response.body()?.let { medications ->
                        val entities = medications.map { it.toEntity() }
                        medicationDao.insertMedications(entities)
                        emit(Resource.Success(entities))
                    }
                } else {
                    if (localResults.isEmpty()) {
                        emit(Resource.Error("Failed to search medications"))
                    }
                }
            } else {
                if (localResults.isEmpty()) {
                    emit(Resource.Error("No internet connection"))
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Error searching medications")
            emit(Resource.Error(e.message ?: "Search failed"))
        }
    }

    fun getMedicationByBarcode(barcode: String): Flow<Resource<MedicationEntity?>> = flow {
        emit(Resource.Loading())

        try {
            // Check local database first
            val localMedication = medicationDao.getMedicationByBarcode(barcode)
            if (localMedication != null) {
                emit(Resource.Success(localMedication))
            } else if (networkMonitor.isCurrentlyConnected()) {
                // Search online
                val response = apiService.searchMedications(barcode, "barcode")
                if (response.isSuccessful) {
                    response.body()?.firstOrNull()?.let { medication ->
                        val entity = medication.toEntity()
                        medicationDao.insertMedication(entity)
                        emit(Resource.Success(entity))
                    } ?: emit(Resource.Error("Medication not found"))
                } else {
                    emit(Resource.Error("Failed to find medication"))
                }
            } else {
                emit(Resource.Error("No internet connection"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Error finding medication by barcode")
            emit(Resource.Error(e.message ?: "Lookup failed"))
        }
    }

    fun getMedicationsByCategory(category: String): Flow<List<MedicationEntity>> {
        return medicationDao.getMedicationsByCategory(category)
    }

    fun getOTCMedications(): Flow<List<MedicationEntity>> {
        return medicationDao.getMedicationsByOTC(true)
    }

    fun getAllMedications(): Flow<List<MedicationEntity>> {
        return medicationDao.getAllMedications()
    }

    fun getMedicationById(medicationId: String): Flow<MedicationEntity?> {
        return medicationDao.getMedicationById(medicationId)
    }

    suspend fun addMedication(medication: MedicationEntity): Resource<Unit> {
        return try {
            medicationDao.insertMedication(medication)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error adding medication")
            Resource.Error(e.message ?: "Failed to add medication")
        }
    }
}

