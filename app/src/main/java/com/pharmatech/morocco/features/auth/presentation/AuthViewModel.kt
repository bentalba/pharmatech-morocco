package com.pharmatech.morocco.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.pharmatech.morocco.core.utils.Resource
import com.pharmatech.morocco.features.auth.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val isLoading: Boolean = false,
    val user: FirebaseUser? = null,
    val error: String? = null,
    val isLoggedIn: Boolean = false
)

sealed class AuthEvent {
    data class ShowError(val message: String) : AuthEvent()
    data class ShowSuccess(val message: String) : AuthEvent()
    object NavigateToHome : AuthEvent()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _event = Channel<AuthEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        val currentUser = authRepository.currentUser
        _state.value = _state.value.copy(
            user = currentUser,
            isLoggedIn = currentUser != null
        )
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            user = resource.data,
                            isLoggedIn = true,
                            error = null
                        )
                        _event.trySend(AuthEvent.NavigateToHome)
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = resource.message
                        )
                        _event.trySend(AuthEvent.ShowError(resource.message ?: "Login failed"))
                    }
                }
            }
        }
    }

    fun register(email: String, password: String, displayName: String, phoneNumber: String?) {
        viewModelScope.launch {
            authRepository.register(email, password, displayName, phoneNumber).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            user = resource.data,
                            isLoggedIn = true,
                            error = null
                        )
                        _event.trySend(AuthEvent.NavigateToHome)
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = resource.message
                        )
                        _event.trySend(AuthEvent.ShowError(resource.message ?: "Registration failed"))
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _state.value = AuthState()
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            authRepository.resetPassword(email).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(isLoading = false)
                        _event.trySend(AuthEvent.ShowSuccess("Password reset email sent"))
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(isLoading = false)
                        _event.trySend(AuthEvent.ShowError(resource.message ?: "Failed to send reset email"))
                    }
                }
            }
        }
    }

}

