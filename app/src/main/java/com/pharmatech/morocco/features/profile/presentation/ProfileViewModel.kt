package com.pharmatech.morocco.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pharmatech.morocco.core.database.entities.UserEntity
import com.pharmatech.morocco.core.utils.Resource
import com.pharmatech.morocco.features.auth.data.repository.AuthRepository
import com.pharmatech.morocco.features.profile.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

data class ProfileState(
    val user: UserEntity? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class ProfileEvent {
    data class ShowError(val message: String) : ProfileEvent()
    data class ShowSuccess(val message: String) : ProfileEvent()
    object NavigateToLogin : ProfileEvent()
    object NavigateToEditProfile : ProfileEvent()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _event = MutableStateFlow<ProfileEvent?>(null)
    val event: StateFlow<ProfileEvent?> = _event.asStateFlow()

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        val userId = authRepository.currentUser?.uid ?: run {
            _event.value = ProfileEvent.NavigateToLogin
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            userRepository.getUserProfile(userId)
                .catch { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                    _event.value = ProfileEvent.ShowError("Failed to load profile: ${e.message}")
                }
                .collect { user ->
                    if (user == null) {
                        // Create profile for first time
                        authRepository.currentUser?.let { firebaseUser ->
                            userRepository.createUserProfile(firebaseUser)
                        }
                    }
                    _state.update {
                        it.copy(
                            user = user,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun updateProfile(
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
    ) {
        val userId = authRepository.currentUser?.uid ?: return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = userRepository.updateProfile(
                userId = userId,
                displayName = displayName,
                phoneNumber = phoneNumber,
                photoUrl = photoUrl,
                dateOfBirth = dateOfBirth,
                gender = gender,
                bloodType = bloodType,
                allergies = allergies,
                chronicConditions = chronicConditions,
                emergencyContact = emergencyContact,
                preferredLanguage = preferredLanguage
            )) {
                is Resource.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.value = ProfileEvent.ShowSuccess("Profile updated successfully")
                    loadUserProfile()
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.value = ProfileEvent.ShowError(
                        result.message ?: "Failed to update profile"
                    )
                }
                else -> {}
            }
        }
    }

    fun updateHealthInfo(
        bloodType: String?,
        allergies: List<String>?,
        chronicConditions: List<String>?
    ) {
        updateProfile(
            bloodType = bloodType,
            allergies = allergies,
            chronicConditions = chronicConditions
        )
    }

    fun updateEmergencyContact(contact: String?) {
        updateProfile(emergencyContact = contact)
    }

    fun changeLanguage(language: String) {
        updateProfile(preferredLanguage = language)
    }

    fun upgradeToPremium() {
        val userId = authRepository.currentUser?.uid ?: return

        viewModelScope.launch {
            when (val result = userRepository.upgradeToPremium(userId)) {
                is Resource.Success -> {
                    _event.value = ProfileEvent.ShowSuccess("Upgraded to Premium!")
                    loadUserProfile()
                }
                is Resource.Error -> {
                    _event.value = ProfileEvent.ShowError(
                        result.message ?: "Failed to upgrade"
                    )
                }
                else -> {}
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _event.value = ProfileEvent.NavigateToLogin
        }
    }

    fun deleteAccount() {
        val userId = authRepository.currentUser?.uid ?: return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = userRepository.deleteAccount(userId)) {
                is Resource.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.value = ProfileEvent.NavigateToLogin
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.value = ProfileEvent.ShowError(
                        result.message ?: "Failed to delete account"
                    )
                }
                else -> {}
            }
        }
    }

    fun clearEvent() {
        _event.value = null
    }

    fun refresh() {
        loadUserProfile()
    }
}

