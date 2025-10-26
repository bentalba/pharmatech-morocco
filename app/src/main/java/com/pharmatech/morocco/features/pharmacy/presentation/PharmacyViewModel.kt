package com.pharmatech.morocco.features.pharmacy.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pharmatech.morocco.core.database.entities.PharmacyEntity
import com.pharmatech.morocco.core.utils.Resource
import com.pharmatech.morocco.features.auth.data.repository.AuthRepository
import com.pharmatech.morocco.features.pharmacy.data.repository.PharmacyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PharmacyState(
    val pharmacies: List<PharmacyEntity> = emptyList(),
    val favoritePharmacies: List<PharmacyEntity> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val showOnlyOpen: Boolean = false,
    val show24HourOnly: Boolean = false,
    val showWithDelivery: Boolean = false
)

sealed class PharmacyEvent {
    data class ShowError(val message: String) : PharmacyEvent()
    data class FavoriteToggled(val isFavorite: Boolean) : PharmacyEvent()
    data class NavigateToDetail(val pharmacyId: String) : PharmacyEvent()
}

@HiltViewModel
class PharmacyViewModel @Inject constructor(
    private val pharmacyRepository: PharmacyRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PharmacyState())
    val state: StateFlow<PharmacyState> = _state.asStateFlow()

    private val _event = MutableStateFlow<PharmacyEvent?>(null)
    val event: StateFlow<PharmacyEvent?> = _event.asStateFlow()

    init {
        loadNearbyPharmacies()
        loadFavorites()
    }

    fun loadNearbyPharmacies(
        latitude: Double = 33.5731, // Default to Casablanca
        longitude: Double = -7.5898
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            pharmacyRepository.getNearbyPharmacies(latitude, longitude)
                .collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    pharmacies = resource.data ?: emptyList(),
                                    isLoading = false,
                                    error = null
                                )
                            }
                        }
                        is Resource.Error -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = resource.message
                                )
                            }
                            _event.value = PharmacyEvent.ShowError(
                                resource.message ?: "Failed to load pharmacies"
                            )
                        }
                        is Resource.Loading -> {
                            _state.update { it.copy(isLoading = true) }
                        }
                    }
                }
        }
    }

    fun searchPharmacies(query: String) {
        _state.update { it.copy(searchQuery = query) }

        if (query.isBlank()) {
            loadNearbyPharmacies()
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            pharmacyRepository.searchPharmacies(query)
                .collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    pharmacies = resource.data ?: emptyList(),
                                    isLoading = false,
                                    error = null
                                )
                            }
                        }
                        is Resource.Error -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = resource.message
                                )
                            }
                        }
                        is Resource.Loading -> {
                            _state.update { it.copy(isLoading = true) }
                        }
                    }
                }
        }
    }

    fun toggleFavorite(pharmacyId: String, pharmacyName: String) {
        val userId = authRepository.currentUser?.uid ?: return

        viewModelScope.launch {
            when (val result = pharmacyRepository.toggleFavorite(userId, pharmacyId, pharmacyName)) {
                is Resource.Success -> {
                    _event.value = PharmacyEvent.FavoriteToggled(result.data ?: false)
                    loadFavorites()
                }
                is Resource.Error -> {
                    _event.value = PharmacyEvent.ShowError(
                        result.message ?: "Failed to update favorite"
                    )
                }
                else -> {}
            }
        }
    }

    private fun loadFavorites() {
        val userId = authRepository.currentUser?.uid ?: return

        viewModelScope.launch {
            pharmacyRepository.getFavoritePharmacies(userId)
                .catch { e ->
                    _event.value = PharmacyEvent.ShowError("Failed to load favorites: ${e.message}")
                }
                .collect { favorites ->
                    _state.update { it.copy(favoritePharmacies = favorites) }
                }
        }
    }

    fun toggleFilter(filterType: String) {
        when (filterType) {
            "24hour" -> _state.update { it.copy(show24HourOnly = !it.show24HourOnly) }
            "delivery" -> _state.update { it.copy(showWithDelivery = !it.showWithDelivery) }
            "open" -> _state.update { it.copy(showOnlyOpen = !it.showOnlyOpen) }
        }
        applyFilters()
    }

    private fun applyFilters() {
        val currentState = _state.value
        val filtered = currentState.pharmacies.filter { pharmacy ->
            val matchesAll = true
            val matches24Hour = !currentState.show24HourOnly || pharmacy.is24Hours
            val matchesDelivery = !currentState.showWithDelivery || pharmacy.hasDelivery
            // Open filter would require current time check

            matchesAll && matches24Hour && matchesDelivery
        }

        _state.update { it.copy(pharmacies = filtered) }
    }

    fun clearEvent() {
        _event.value = null
    }

    fun refresh() {
        loadNearbyPharmacies()
    }
}

