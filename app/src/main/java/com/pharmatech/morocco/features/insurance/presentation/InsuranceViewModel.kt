package com.pharmatech.morocco.features.insurance.presentation

import androidx.lifecycle.viewModelScope
import com.pharmatech.morocco.core.base.BaseViewModel
import com.pharmatech.morocco.features.insurance.data.repository.InsuranceRepository
import com.pharmatech.morocco.features.insurance.data.repository.InsuranceRepositoryImpl
import com.pharmatech.morocco.features.insurance.domain.model.InsuranceProvider
import com.pharmatech.morocco.features.insurance.domain.model.ReimbursementResult
import com.pharmatech.morocco.features.medication.domain.model.Medication
import kotlinx.coroutines.launch

/**
 * State class for Insurance Portal screen
 * Contains all UI state needed for the reimbursement calculator
 */
data class InsuranceState(
    val selectedProvider: InsuranceProvider? = null,
    val selectedMedication: Medication? = null,
    val searchQuery: String = "",
    val medicationSearchResults: List<Pair<Medication, Boolean>> = emptyList(),
    val showMedicationDialog: Boolean = false,
    val calculationResult: ReimbursementResult? = null,
    val showProviderDialog: Boolean = false
)

/**
 * Events for Insurance Portal screen
 * Currently minimal as most actions are handled through state updates
 */
sealed class InsuranceEvent {
    // Future events can be added here if needed
    // For now, most actions are handled through state updates
}

/**
 * ViewModel for Insurance Portal screen
 * Manages state for the reimbursement calculator functionality
 *
 * Follows the BaseViewModel pattern used throughout the app
 */
class InsuranceViewModel(
    private val repository: InsuranceRepository = InsuranceRepositoryImpl()
) : BaseViewModel<InsuranceState, InsuranceEvent>(InsuranceState()) {

    /**
     * Handle provider selection from dropdown dialog
     * Updates selected provider, clears previous calculation and medication selection
     */
    fun onProviderSelected(provider: InsuranceProvider) {
        setState {
            copy(
                selectedProvider = provider,
                calculationResult = null, // Clear previous calculation
                selectedMedication = null, // Provider changed, need to reselect medication
                showProviderDialog = false, // Close dialog
                searchQuery = "", // Clear any search in progress
                medicationSearchResults = emptyList() // Clear search results
            )
        }
    }

    /**
     * Handle changes to medication search query
     * Searches medications and checks eligibility against selected provider
     */
    fun onMedicationSearchQueryChanged(query: String) {
        setState { copy(searchQuery = query) }

        // Only search if we have a selected provider (needed for eligibility checking)
        state.value.selectedProvider?.let { provider ->
            viewModelScope.launch {
                val results = repository.searchEligibleMedications(query, provider)
                setState {
                    copy(medicationSearchResults = results)
                }
            }
        }
    }

    /**
     * Handle medication selection from search dialog
     * Updates selected medication, closes dialog, clears search
     */
    fun onMedicationSelected(medication: Medication) {
        setState {
            copy(
                selectedMedication = medication,
                showMedicationDialog = false, // Close dialog
                searchQuery = "", // Clear search
                medicationSearchResults = emptyList(), // Clear search results
                calculationResult = null // Clear previous calculation (medication changed)
            )
        }
    }

    /**
     * Handle calculate button click
     * Validates selections and performs reimbursement calculation
     */
    fun onCalculateClicked() {
        val currentState = state.value
        val provider = currentState.selectedProvider
        val medication = currentState.selectedMedication

        // Validate: both provider and medication must be selected
        if (provider != null && medication != null) {
            viewModelScope.launch {
                val result = repository.calculateReimbursement(
                    medicationId = medication.id,
                    provider = provider
                )

                // Update state with calculation result
                // Even if result is 0% reimbursement, we still show it to user
                setState {
                    copy(calculationResult = result)
                }
            }
        }
        // If validation fails, do nothing (button should be disabled anyway)
    }

    /**
     * Show provider selection dialog
     */
    fun onShowProviderDialog() {
        setState { copy(showProviderDialog = true) }
    }

    /**
     * Dismiss provider selection dialog
     */
    fun onDismissProviderDialog() {
        setState { copy(showProviderDialog = false) }
    }

    /**
     * Show medication search dialog
     * Only allowed if provider is selected
     */
    fun onShowMedicationDialog() {
        // Only allow if provider is selected (needed for eligibility checking)
        if (state.value.selectedProvider != null) {
            setState {
                copy(
                    showMedicationDialog = true,
                    searchQuery = "", // Start with fresh search
                    medicationSearchResults = emptyList() // Clear previous results
                )
            }
        }
    }

    /**
     * Dismiss medication search dialog
     */
    fun onDismissMedicationDialog() {
        setState {
            copy(
                showMedicationDialog = false,
                searchQuery = "", // Clear search when closing
                medicationSearchResults = emptyList() // Clear results
            )
        }
    }

    /**
     * Check if calculate button should be enabled
     * Button enabled only when both provider and medication are selected
     */
    fun isCalculateEnabled(): Boolean {
        val currentState = state.value
        return currentState.selectedProvider != null && currentState.selectedMedication != null
    }

    /**
     * Check if medication selection is enabled
     * Medication selection only allowed after provider is selected
     */
    fun isMedicationSelectionEnabled(): Boolean {
        return state.value.selectedProvider != null
    }
}