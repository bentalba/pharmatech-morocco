package com.pharmatech.morocco.features.tracker.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pharmatech.morocco.core.database.entities.MedicationHistoryEntity
import com.pharmatech.morocco.core.database.entities.MedicationTrackerEntity
import com.pharmatech.morocco.core.database.entities.ReminderEntity
import com.pharmatech.morocco.core.utils.Resource
import com.pharmatech.morocco.features.auth.data.repository.AuthRepository
import com.pharmatech.morocco.features.tracker.data.repository.TrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TrackerState(
    val activeTrackers: List<MedicationTrackerEntity> = emptyList(),
    val todayReminders: List<ReminderEntity> = emptyList(),
    val pendingReminders: List<ReminderEntity> = emptyList(),
    val medicationHistory: List<MedicationHistoryEntity> = emptyList(),
    val adherenceRate: Float = 0f,
    val totalDoses: Int = 0,
    val takenDoses: Int = 0,
    val missedDoses: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class TrackerEvent {
    data class ShowError(val message: String) : TrackerEvent()
    data class ShowSuccess(val message: String) : TrackerEvent()
    data class NavigateToAddMedication(val trackerId: String? = null) : TrackerEvent()
}

@HiltViewModel
class TrackerViewModel @Inject constructor(
    private val trackerRepository: TrackerRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TrackerState())
    val state: StateFlow<TrackerState> = _state.asStateFlow()

    private val _event = MutableStateFlow<TrackerEvent?>(null)
    val event: StateFlow<TrackerEvent?> = _event.asStateFlow()

    init {
        loadTrackerData()
    }

    fun loadTrackerData() {
        val userId = authRepository.currentUser?.uid ?: return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            // Load active trackers
            trackerRepository.getActiveTrackers(userId)
                .catch { e ->
                    _event.value = TrackerEvent.ShowError("Failed to load trackers: ${e.message}")
                }
                .collect { trackers ->
                    _state.update { it.copy(activeTrackers = trackers, isLoading = false) }
                }
        }

        // Load today's reminders
        viewModelScope.launch {
            trackerRepository.getTodayReminders(userId)
                .catch { e ->
                    _event.value = TrackerEvent.ShowError("Failed to load reminders: ${e.message}")
                }
                .collect { reminders ->
                    val pending = reminders.filter { !it.isTaken && !it.isSkipped }
                    val taken = reminders.filter { it.isTaken }
                    val missed = reminders.filter { it.isSkipped }

                    _state.update {
                        it.copy(
                            todayReminders = reminders,
                            pendingReminders = pending,
                            totalDoses = reminders.size,
                            takenDoses = taken.size,
                            missedDoses = missed.size
                        )
                    }
                }
        }

        // Load adherence rate
        viewModelScope.launch {
            trackerRepository.getAdherenceRate(userId, 7)
                .catch { e ->
                    _event.value = TrackerEvent.ShowError("Failed to calculate adherence: ${e.message}")
                }
                .collect { rate ->
                    _state.update { it.copy(adherenceRate = rate) }
                }
        }

        // Load medication history
        viewModelScope.launch {
            trackerRepository.getMedicationHistory(userId)
                .catch { e ->
                    _event.value = TrackerEvent.ShowError("Failed to load history: ${e.message}")
                }
                .collect { history ->
                    _state.update { it.copy(medicationHistory = history) }
                }
        }
    }

    fun markMedicationTaken(reminderId: String) {
        viewModelScope.launch {
            when (val result = trackerRepository.markMedicationTaken(reminderId)) {
                is Resource.Success -> {
                    _event.value = TrackerEvent.ShowSuccess("Medication marked as taken")
                    loadTrackerData()
                }
                is Resource.Error -> {
                    _event.value = TrackerEvent.ShowError(
                        result.message ?: "Failed to mark as taken"
                    )
                }
                else -> {}
            }
        }
    }

    fun skipMedication(reminderId: String, reason: String? = null) {
        viewModelScope.launch {
            when (val result = trackerRepository.skipMedication(reminderId, reason)) {
                is Resource.Success -> {
                    _event.value = TrackerEvent.ShowSuccess("Medication skipped")
                    loadTrackerData()
                }
                is Resource.Error -> {
                    _event.value = TrackerEvent.ShowError(
                        result.message ?: "Failed to skip medication"
                    )
                }
                else -> {}
            }
        }
    }

    fun addMedicationTracker(tracker: MedicationTrackerEntity) {
        viewModelScope.launch {
            when (val result = trackerRepository.addMedicationTracker(tracker)) {
                is Resource.Success -> {
                    _event.value = TrackerEvent.ShowSuccess("Medication added successfully")
                    loadTrackerData()
                }
                is Resource.Error -> {
                    _event.value = TrackerEvent.ShowError(
                        result.message ?: "Failed to add medication"
                    )
                }
                else -> {}
            }
        }
    }

    fun updateTracker(tracker: MedicationTrackerEntity) {
        viewModelScope.launch {
            when (val result = trackerRepository.updateTracker(tracker)) {
                is Resource.Success -> {
                    _event.value = TrackerEvent.ShowSuccess("Tracker updated")
                    loadTrackerData()
                }
                is Resource.Error -> {
                    _event.value = TrackerEvent.ShowError(
                        result.message ?: "Failed to update tracker"
                    )
                }
                else -> {}
            }
        }
    }

    fun deleteTracker(trackerId: String) {
        viewModelScope.launch {
            when (val result = trackerRepository.deleteTracker(trackerId)) {
                is Resource.Success -> {
                    _event.value = TrackerEvent.ShowSuccess("Tracker deleted")
                    loadTrackerData()
                }
                is Resource.Error -> {
                    _event.value = TrackerEvent.ShowError(
                        result.message ?: "Failed to delete tracker"
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
        loadTrackerData()
    }
}

