package com.pharmatech.morocco.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pharmatech.morocco.core.database.entities.PharmacyEntity
import com.pharmatech.morocco.core.database.entities.ReminderEntity
import com.pharmatech.morocco.core.utils.Resource
import com.pharmatech.morocco.features.auth.data.repository.AuthRepository
import com.pharmatech.morocco.features.pharmacy.data.repository.PharmacyRepository
import com.pharmatech.morocco.features.tracker.data.repository.TrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

data class HomeState(
    val userName: String = "",
    val userId: String = "",
    val todayMedicationCount: Int = 0,
    val takenMedicationCount: Int = 0,
    val nextMedicationTime: String = "",
    val adherenceRate: Float = 0f,
    val nearbyPharmacies: List<PharmacyEntity> = emptyList(),
    val todayReminders: List<ReminderEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class HomeEvent {
    data class ShowError(val message: String) : HomeEvent()
    data class NavigateToMedication(val reminderId: String) : HomeEvent()
    object RefreshSuccess : HomeEvent()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val pharmacyRepository: PharmacyRepository,
    private val trackerRepository: TrackerRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _event = MutableStateFlow<HomeEvent?>(null)
    val event: StateFlow<HomeEvent?> = _event.asStateFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        val userId = authRepository.currentUser?.uid ?: return
        val userName = authRepository.currentUser?.displayName ?: "User"

        _state.update { it.copy(
            isLoading = true,
            userId = userId,
            userName = userName
        ) }

        // Load today's medications
        viewModelScope.launch {
            trackerRepository.getTodayReminders(userId)
                .catch { e ->
                    _event.value = HomeEvent.ShowError(e.message ?: "Failed to load reminders")
                }
                .collect { reminders ->
                    val pending = reminders.filter { !it.isTaken && !it.isSkipped }
                    val taken = reminders.filter { it.isTaken }

                    _state.update { currentState ->
                        currentState.copy(
                            todayReminders = reminders,
                            todayMedicationCount = pending.size,
                            takenMedicationCount = taken.size,
                            nextMedicationTime = pending.firstOrNull()?.scheduledTime?.let { date ->
                                SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
                            } ?: "No pending medications",
                            isLoading = false
                        )
                    }
                }
        }

        // Load adherence rate
        viewModelScope.launch {
            trackerRepository.getAdherenceRate(userId, 7)
                .catch { e ->
                    _event.value = HomeEvent.ShowError("Failed to load adherence: ${e.message}")
                }
                .collect { rate ->
                    _state.update { it.copy(adherenceRate = rate) }
                }
        }

        // Load nearby pharmacies (mock location for now)
        viewModelScope.launch {
            // TODO: Get actual location from location service
            val mockLatitude = 33.5731 // Casablanca
            val mockLongitude = -7.5898

            pharmacyRepository.getNearbyPharmacies(mockLatitude, mockLongitude, 5000)
                .catch { e ->
                    _event.value = HomeEvent.ShowError("Failed to load pharmacies: ${e.message}")
                }
                .collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _state.update { it.copy(
                                nearbyPharmacies = resource.data?.take(3) ?: emptyList()
                            ) }
                        }
                        is Resource.Error -> {
                            _event.value = HomeEvent.ShowError(resource.message ?: "Unknown error")
                        }
                        is Resource.Loading -> {
                            // Handle loading if needed
                        }
                    }
                }
        }
    }

    fun refreshDashboard() {
        loadDashboardData()
    }

    fun markMedicationTaken(reminderId: String) {
        viewModelScope.launch {
            when (val result = trackerRepository.markMedicationTaken(reminderId)) {
                is Resource.Success -> {
                    _event.value = HomeEvent.RefreshSuccess
                    loadDashboardData()
                }
                is Resource.Error -> {
                    _event.value = HomeEvent.ShowError(result.message ?: "Failed to mark as taken")
                }
                else -> {}
            }
        }
    }

    fun skipMedication(reminderId: String, reason: String? = null) {
        viewModelScope.launch {
            when (val result = trackerRepository.skipMedication(reminderId, reason)) {
                is Resource.Success -> {
                    _event.value = HomeEvent.RefreshSuccess
                    loadDashboardData()
                }
                is Resource.Error -> {
                    _event.value = HomeEvent.ShowError(result.message ?: "Failed to skip")
                }
                else -> {}
            }
        }
    }

    fun clearEvent() {
        _event.value = null
    }
}

