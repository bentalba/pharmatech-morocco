package com.pharmatech.morocco.features.auth.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pharmatech.morocco.core.network.ApiService
import com.pharmatech.morocco.core.network.models.LoginRequest
import com.pharmatech.morocco.core.network.models.RegisterRequest
import com.pharmatech.morocco.core.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val firebaseAuth: FirebaseAuth
) {

    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    fun isUserLoggedIn(): Boolean = currentUser != null

    suspend fun login(email: String, password: String): Flow<Resource<FirebaseUser>> = flow {
        try {
            emit(Resource.Loading())

            // Firebase authentication
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = authResult.user

            if (user != null) {
                // Optional: Call backend API
                val response = apiService.login(LoginRequest(email, password))
                if (response.isSuccessful && response.body()?.success == true) {
                    Timber.d("Backend login successful")
                    // Store token if needed
                }

                emit(Resource.Success(user))
            } else {
                emit(Resource.Error("Login failed"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Login error")
            emit(Resource.Error(e.message ?: "An error occurred during login"))
        }
    }

    suspend fun register(
        email: String,
        password: String,
        displayName: String,
        phoneNumber: String?
    ): Flow<Resource<FirebaseUser>> = flow {
        try {
            emit(Resource.Loading())

            // Firebase authentication
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = authResult.user

            if (user != null) {
                // Optional: Call backend API
                val response = apiService.register(
                    RegisterRequest(email, password, displayName, phoneNumber)
                )
                if (response.isSuccessful && response.body()?.success == true) {
                    Timber.d("Backend registration successful")
                }

                emit(Resource.Success(user))
            } else {
                emit(Resource.Error("Registration failed"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Registration error")
            emit(Resource.Error(e.message ?: "An error occurred during registration"))
        }
    }

    suspend fun logout() {
        firebaseAuth.signOut()
    }

    suspend fun resetPassword(email: String): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            firebaseAuth.sendPasswordResetEmail(email).await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            Timber.e(e, "Password reset error")
            emit(Resource.Error(e.message ?: "Failed to send reset email"))
        }
    }
}

