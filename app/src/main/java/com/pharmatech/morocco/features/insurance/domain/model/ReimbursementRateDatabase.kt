package com.pharmatech.morocco.features.insurance.domain.model

/**
 * Static database for reimbursement rates
 * Contains hardcoded reimbursement percentages for medications by insurance provider
 *
 * This follows the same pattern as MedicationDatabase for consistency
 *
 * Data Format:
 * - Each ReimbursementRate specifies a medication ID, provider, and percentage
 * - Percentage of 0.0 means medication is not eligible for that provider
 * - Medications not listed here are assumed to have 0% reimbursement
 *
 * TODO: Replace this sample data with actual CSV data provided by user
 */
object ReimbursementRateDatabase {

    /**
     * Static list of reimbursement rates
     *
     * Sample data includes:
     * - COTAREG (existing medication): 70% CNSS, 80% CNOPS (high reimbursement for essential hypertension med)
     * - Doliprane samples: 30% CNSS, 50% CNOPS (mentioned in user request)
     * - Various other examples to demonstrate different coverage levels
     */
    private val rates: List<ReimbursementRate> = listOf(
        // COTAREG - Existing medication from database
        // High reimbursement as it's essential hypertension medication
        ReimbursementRate("med_cotareg_160_12_5", InsuranceProvider.CNSS, 70.0),
        ReimbursementRate("med_cotareg_160_12_5", InsuranceProvider.CNOPS, 80.0),

        // DOLIPRANE samples (mentioned in user's original request)
        // These IDs will need corresponding medications added to MedicationDatabase
        ReimbursementRate("med_doliprane_1000", InsuranceProvider.CNSS, 30.0),
        ReimbursementRate("med_doliprane_1000", InsuranceProvider.CNOPS, 50.0),

        ReimbursementRate("med_doliprane_500", InsuranceProvider.CNSS, 30.0),
        ReimbursementRate("med_doliprane_500", InsuranceProvider.CNOPS, 50.0),

        // ASPEGIC - Common pain reliever
        ReimbursementRate("med_aspegic_500", InsuranceProvider.CNSS, 25.0),
        ReimbursementRate("med_aspegic_500", InsuranceProvider.CNOPS, 40.0),

        // CORTISONE - High reimbursement for essential medication
        ReimbursementRate("med_cortisone_10", InsuranceProvider.CNSS, 70.0),
        ReimbursementRate("med_cortisone_10", InsuranceProvider.CNOPS, 80.0),

        // VITAMIN C - Low/no reimbursement (not essential)
        ReimbursementRate("med_vitamin_c", InsuranceProvider.CNSS, 0.0),
        ReimbursementRate("med_vitamin_c", InsuranceProvider.CNOPS, 0.0),

        // ANTIBIOTIC - Different coverage between providers
        ReimbursementRate("med_amoxicillin_500", InsuranceProvider.CNSS, 60.0),
        ReimbursementRate("med_amoxicillin_500", InsuranceProvider.CNOPS, 70.0),

        // INSULIN - Essential medication, high coverage
        ReimbursementRate("med_insulin_100ui", InsuranceProvider.CNSS, 90.0),
        ReimbursementRate("med_insulin_100ui", InsuranceProvider.CNOPS, 95.0),

        // Example of medication covered by CNOPS but not CNSS
        ReimbursementRate("med_expensive_cancer_drug", InsuranceProvider.CNSS, 0.0),
        ReimbursementRate("med_expensive_cancer_drug", InsuranceProvider.CNOPS, 80.0)
    )

    /**
     * Get reimbursement percentage for specific medication and provider
     * @param medicationId The unique identifier of the medication
     * @param provider The insurance provider (CNSS or CNOPS)
     * @return Reimbursement percentage (0.0 to 100.0) or null if not found
     */
    fun getRateForMedication(medicationId: String, provider: InsuranceProvider): Double? {
        return rates.find { rate ->
            rate.medicationId == medicationId && rate.provider == provider
        }?.percentage
    }

    /**
     * Get all reimbursement rates for a specific provider
     * Used for filtering eligible medications in search results
     * @param provider The insurance provider
     * @return List of all reimbursement rates for this provider
     */
    fun getAllRatesForProvider(provider: InsuranceProvider): List<ReimbursementRate> {
        return rates.filter { it.provider == provider }
    }

    /**
     * Check if a medication is eligible for reimbursement with a specific provider
     * @param medicationId The unique identifier of the medication
     * @param provider The insurance provider
     * @return true if reimbursement percentage > 0%, false otherwise
     */
    fun isEligible(medicationId: String, provider: InsuranceProvider): Boolean {
        val rate = getRateForMedication(medicationId, provider)
        return rate != null && rate > 0.0
    }

    /**
     * Get all medication IDs that are eligible for at least one provider
     * Useful for determining which medications should be highlighted in search
     * @return Set of medication IDs that have some reimbursement coverage
     */
    fun getAllEligibleMedicationIds(): Set<String> {
        return rates.filter { it.percentage > 0.0 }
            .map { it.medicationId }
            .toSet()
    }

    /**
     * Get all rates for a specific medication across all providers
     * @param medicationId The medication ID
     * @return List of reimbursement rates for this medication with different providers
     */
    fun getRatesForMedication(medicationId: String): List<ReimbursementRate> {
        return rates.filter { it.medicationId == medicationId }
    }

    /**
     * Get statistics about coverage
     * Useful for debugging and understanding the data
     */
    fun getCoverageStats(): Map<String, Any> {
        val totalMedications = rates.map { it.medicationId }.distinct().size
        val eligibleCNSS = rates.filter { it.provider == InsuranceProvider.CNSS && it.percentage > 0.0 }.size
        val eligibleCNOPS = rates.filter { it.provider == InsuranceProvider.CNOPS && it.percentage > 0.0 }.size
        val averageRateCNSS = rates.filter { it.provider == InsuranceProvider.CNSS && it.percentage > 0.0 }
            .map { it.percentage }.average().takeIf { !it.isNaN() } ?: 0.0
        val averageRateCNOPS = rates.filter { it.provider == InsuranceProvider.CNOPS && it.percentage > 0.0 }
            .map { it.percentage }.average().takeIf { !it.isNaN() } ?: 0.0

        return mapOf(
            "totalMedications" to totalMedications,
            "eligibleCNSS" to eligibleCNSS,
            "eligibleCNOPS" to eligibleCNOPS,
            "averageRateCNSS" to averageRateCNSS,
            "averageRateCNOPS" to averageRateCNOPS
        )
    }
}