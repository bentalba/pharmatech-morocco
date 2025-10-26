package com.pharmatech.morocco.features.medication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pharmatech.morocco.core.database.entities.MedicationEntity
import com.pharmatech.morocco.core.utils.Resource
import com.pharmatech.morocco.features.medication.data.repository.MedicationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MedicationState(
    val medications: List<MedicationEntity> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: String? = null,
    val showOTCOnly: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class MedicationEvent {
    data class ShowError(val message: String) : MedicationEvent()
    data class NavigateToDetail(val medicationId: String) : MedicationEvent()
    data class ScanResult(val medication: MedicationEntity?) : MedicationEvent()
}

@HiltViewModel
class MedicationViewModel @Inject constructor(
    private val medicationRepository: MedicationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MedicationState())
    val state: StateFlow<MedicationState> = _state.asStateFlow()

    private val _event = MutableStateFlow<MedicationEvent?>(null)
    val event: StateFlow<MedicationEvent?> = _event.asStateFlow()

    init {
        loadAllMedications()
    }

    fun loadAllMedications() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            medicationRepository.getAllMedications()
                .catch { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                    _event.value = MedicationEvent.ShowError(e.message ?: "Failed to load medications")
                }
                .collect { medications ->
                    _state.update {
                        it.copy(
                            medications = medications,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun searchMedications(query: String) {
        _state.update { it.copy(searchQuery = query) }

        if (query.isBlank()) {
            loadAllMedications()
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            medicationRepository.searchMedications(query)
                .collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    medications = resource.data ?: emptyList(),
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
                            _event.value = MedicationEvent.ShowError(
                                resource.message ?: "Search failed"
                            )
                        }
                        is Resource.Loading -> {
                            _state.update { it.copy(isLoading = true) }
                        }
                    }
                }
        }
    }

    fun scanBarcode(barcode: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            medicationRepository.getMedicationByBarcode(barcode)
                .collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _state.update { it.copy(isLoading = false) }
                            _event.value = MedicationEvent.ScanResult(resource.data)
                        }
                        is Resource.Error -> {
                            _state.update { it.copy(isLoading = false) }
                            _event.value = MedicationEvent.ShowError(
                                resource.message ?: "Medication not found"
                            )
                        }
                        is Resource.Loading -> {
                            _state.update { it.copy(isLoading = true) }
                        }
                    }
                }
        }
    }

    fun filterByCategory(category: String?) {
        _state.update { it.copy(selectedCategory = category) }

        if (category == null) {
            loadAllMedications()
            return
        }

        viewModelScope.launch {
            medicationRepository.getMedicationsByCategory(category)
                .catch { e ->
                    _event.value = MedicationEvent.ShowError("Filter failed: ${e.message}")
                }
                .collect { medications ->
                    _state.update { it.copy(medications = medications) }
                }
        }
    }

    fun toggleOTCFilter() {
        val newValue = !_state.value.showOTCOnly
        _state.update { it.copy(showOTCOnly = newValue) }

        if (newValue) {
            viewModelScope.launch {
                medicationRepository.getOTCMedications()
                    .catch { e ->
                        _event.value = MedicationEvent.ShowError("Filter failed: ${e.message}")
                    }
                    .collect { medications ->
                        _state.update { it.copy(medications = medications) }
                    }
            }
        } else {
            loadAllMedications()
        }
    }

    fun clearEvent() {
        _event.value = null
    }

    fun refresh() {
        loadAllMedications()
    }
}

