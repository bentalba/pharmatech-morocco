package com.pharmatech.morocco.features.profile.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pharmatech.morocco.core.database.dao.UserDao
import com.pharmatech.morocco.core.database.entities.UserEntity
import com.pharmatech.morocco.core.network.ApiService
import com.pharmatech.morocco.core.network.models.UpdateProfileRequest
import com.pharmatech.morocco.core.utils.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userDao: UserDao,
    private val apiService: ApiService
) {
    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    fun getCurrentUserId(): String? = currentUser?.uid

    fun getUserProfile(userId: String): Flow<UserEntity?> {
        return userDao.getUserById(userId)
    }

    suspend fun updateProfile(
        userId: String,
        displayName: String? = null,
        phoneNumber: String? = null,
        photoUrl: String? = null,
        dateOfBirth: Date? = null,
        gender: String? = null,
        bloodType: String? = null,
        allergies: List<String>? = null,
        chronicConditions: List<String>? = null,
        emergencyContact: String? = null,
        preferredLanguage: String? = null
    ): Resource<Unit> {
        return try {
            val currentProfile = userDao.getUserById(userId).first()

            val updatedProfile = currentProfile?.copy(
                displayName = displayName ?: currentProfile.displayName,
                phoneNumber = phoneNumber ?: currentProfile.phoneNumber,
                photoUrl = photoUrl ?: currentProfile.photoUrl,
                dateOfBirth = dateOfBirth ?: currentProfile.dateOfBirth,
                gender = gender ?: currentProfile.gender,
                bloodType = bloodType ?: currentProfile.bloodType,
                allergies = allergies ?: currentProfile.allergies,
                chronicConditions = chronicConditions ?: currentProfile.chronicConditions,
                emergencyContact = emergencyContact ?: currentProfile.emergencyContact,
                preferredLanguage = preferredLanguage ?: currentProfile.preferredLanguage,
                lastUpdated = Date()
            ) ?: UserEntity(
                id = userId,
                email = currentUser?.email ?: "",
                displayName = displayName ?: "",
                phoneNumber = phoneNumber,
                photoUrl = photoUrl,
                dateOfBirth = dateOfBirth,
                gender = gender,
                bloodType = bloodType,
                allergies = allergies ?: emptyList(),
                chronicConditions = chronicConditions ?: emptyList(),
                emergencyContact = emergencyContact,
                preferredLanguage = preferredLanguage ?: "fr",
                isPremium = false,
                createdAt = Date(),
                lastUpdated = Date()
            )

            userDao.insertUser(updatedProfile)

            // Sync with backend if available
            try {
                apiService.updateProfile(
                    UpdateProfileRequest(
                        displayName = displayName,
                        phoneNumber = phoneNumber,
                        photoUrl = photoUrl,
                        dateOfBirth = dateOfBirth?.toString(),
                        gender = gender,
                        bloodType = bloodType,
                        allergies = allergies,
                        chronicConditions = chronicConditions,
                        emergencyContact = emergencyContact,
                        preferredLanguage = preferredLanguage
                    )
                )
            } catch (e: Exception) {
                Timber.w(e, "Failed to sync profile with backend")
            }

            Resource.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error updating profile")
            Resource.Error(e.message ?: "Failed to update profile")
        }
    }

    suspend fun createUserProfile(user: FirebaseUser): Resource<Unit> {
        return try {
            val userEntity = UserEntity(
                id = user.uid,
                email = user.email ?: "",
                displayName = user.displayName ?: "",
                phoneNumber = user.phoneNumber,
                photoUrl = user.photoUrl?.toString(),
                dateOfBirth = null,
                gender = null,
                bloodType = null,
                allergies = emptyList(),
                chronicConditions = emptyList(),
                emergencyContact = null,
                preferredLanguage = "fr",
                isPremium = false,
                createdAt = Date(),
                lastUpdated = Date()
            )

            userDao.insertUser(userEntity)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error creating user profile")
            Resource.Error(e.message ?: "Failed to create profile")
        }
    }

    suspend fun deleteAccount(userId: String): Resource<Unit> {
        return try {
            // Delete from local database
            val user = userDao.getUserById(userId).first()
            user?.let { userDao.deleteUser(it) }

            // Delete Firebase user
            currentUser?.delete()?.await()

            Resource.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error deleting account")
            Resource.Error(e.message ?: "Failed to delete account")
        }
    }

    suspend fun upgradeToPremium(userId: String): Resource<Unit> {
        return try {
            val user = userDao.getUserById(userId).first()
            user?.let {
                userDao.updateUser(it.copy(isPremium = true, lastUpdated = Date()))
            }
            Resource.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error upgrading to premium")
            Resource.Error(e.message ?: "Failed to upgrade")
        }
    }
}

