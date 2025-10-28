package com.pharmatech.morocco.features.insurance.data.repository

import com.pharmatech.morocco.features.insurance.domain.model.InsuranceProvider
import com.pharmatech.morocco.features.insurance.domain.model.ReimbursementRateDatabase
import com.pharmatech.morocco.features.insurance.domain.model.ReimbursementResult
import com.pharmatech.morocco.features.medication.domain.model.Medication
import com.pharmatech.morocco.features.medication.domain.model.MedicationDatabase

/**
 * Repository interface for insurance-related operations
 * Follows the repository pattern used throughout the app
 */
interface InsuranceRepository {
    /**
     * Calculate reimbursement for a specific medication and insurance provider
     * @param medicationId The unique identifier of the medication
     * @param provider The insurance provider (CNSS or CNOPS)
     * @return ReimbursementResult with calculated values, or null if medication not found
     */
    fun calculateReimbursement(
        medicationId: String,
        provider: InsuranceProvider
    ): ReimbursementResult?

    /**
     * Search medications and determine eligibility for a specific provider
     * @param query Search query (medication name, composition, etc.)
     * @param provider The insurance provider to check eligibility against
     * @return List of pairs: (Medication, isEligible)
     */
    fun searchEligibleMedications(
        query: String,
        provider: InsuranceProvider
    ): List<Pair<Medication, Boolean>>
}

/**
 * Implementation of InsuranceRepository
 * Uses static databases (MedicationDatabase and ReimbursementRateDatabase)
 */
class InsuranceRepositoryImpl : InsuranceRepository {

    /**
     * Calculate reimbursement for a medication
     *
     * Logic:
     * 1. Get medication from MedicationDatabase
     * 2. If not found → return null
     * 3. Get reimbursement rate from ReimbursementRateDatabase
     * 4. If rate not found → use 0% (not eligible, but still show result)
     * 5. Calculate refund and final cost
     * 6. Return ReimbursementResult
     */
    override fun calculateReimbursement(
        medicationId: String,
        provider: InsuranceProvider
    ): ReimbursementResult? {
        // Step 1: Get medication
        val medication = MedicationDatabase.getMedicationById(medicationId)
            ?: return null // Medication not found

        // Step 2: Get reimbursement rate (0.0 if not found)
        val reimbursementPercentage = ReimbursementRateDatabase.getRateForMedication(
            medicationId = medicationId,
            provider = provider
        ) ?: 0.0 // Not eligible if not in database

        // Step 3: Calculate and return result
        return ReimbursementResult.calculate(
            medication = medication,
            provider = provider,
            reimbursementPercentage = reimbursementPercentage
        )
    }

    /**
     * Search medications and check eligibility
     *
     * Logic:
     * 1. Search medications using MedicationDatabase
     * 2. For each medication, check if eligible with ReimbursementRateDatabase
     * 3. Return list of (Medication, isEligible) pairs
     *
     * This allows UI to:
     * - Show all matching medications
     * - Highlight eligible ones with checkmark
     * - Show non-eligible ones with muted styling
     */
    override fun searchEligibleMedications(
        query: String,
        provider: InsuranceProvider
    ): List<Pair<Medication, Boolean>> {
        // Step 1: Search medications
        val medications = MedicationDatabase.searchMedications(query)

        // Step 2: Check eligibility for each
        return medications.map { medication ->
            val isEligible = ReimbursementRateDatabase.isEligible(
                medicationId = medication.id,
                provider = provider
            )
            Pair(medication, isEligible)
        }
    }
}
