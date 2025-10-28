package com.pharmatech.morocco.features.insurance.domain.model

import com.pharmatech.morocco.features.medication.domain.model.Medication

/**
 * Insurance provider enum representing available insurance companies in Morocco
 */
enum class InsuranceProvider(
    val displayName: String,
    val displayNameAr: String
) {
    CNSS(
        displayName = "CNSS",
        displayNameAr = "الصندوق الوطني للضمان الاجتماعي"
    ),
    CNOPS(
        displayName = "CNOPS",
        displayNameAr = "الصندوق الوطني لمنظمات الاحتياط الاجتماعي"
    )
}

/**
 * Represents a reimbursement rate for a specific medication and insurance provider
 * @param medicationId The unique identifier of the medication (matches Medication.id)
 * @param provider The insurance provider (CNSS or CNOPS)
 * @param percentage The reimbursement percentage (0.0 to 100.0)
 */
data class ReimbursementRate(
    val medicationId: String,
    val provider: InsuranceProvider,
    val percentage: Double
)

/**
 * Result of a reimbursement calculation
 * Contains all information needed to display the calculation to the user
 * @param medication The medication being calculated
 * @param provider The insurance provider selected
 * @param originalPrice The original price of the medication (PPV)
 * @param reimbursementPercentage The percentage that will be reimbursed
 * @param refundAmount The amount that will be refunded (calculated)
 * @param finalCost The amount the user must pay out of pocket (calculated)
 */
data class ReimbursementResult(
    val medication: Medication,
    val provider: InsuranceProvider,
    val originalPrice: Double,
    val reimbursementPercentage: Double,
    val refundAmount: Double,
    val finalCost: Double
) {
    companion object {
        /**
         * Factory method to create a ReimbursementResult with calculated values
         * @param medication The medication
         * @param provider The insurance provider
         * @param reimbursementPercentage The reimbursement percentage (0.0 if not eligible)
         * @return ReimbursementResult with calculated refund and final cost
         */
        fun calculate(
            medication: Medication,
            provider: InsuranceProvider,
            reimbursementPercentage: Double
        ): ReimbursementResult {
            val originalPrice = medication.ppv
            val refundAmount = originalPrice * (reimbursementPercentage / 100.0)
            val finalCost = originalPrice - refundAmount

            return ReimbursementResult(
                medication = medication,
                provider = provider,
                originalPrice = originalPrice,
                reimbursementPercentage = reimbursementPercentage,
                refundAmount = refundAmount,
                finalCost = finalCost
            )
        }
    }
}
