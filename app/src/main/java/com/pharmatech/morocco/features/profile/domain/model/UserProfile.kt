package com.pharmatech.morocco.features.profile.domain.model

import com.pharmatech.morocco.features.medication.domain.model.Medication
import java.time.LocalTime

/**
 * User Profile Data Model
 * Represents a user's profile and their medication tracking
 */
data class UserProfile(
    val id: String,
    val name: String,
    val email: String? = null,
    val phoneNumber: String? = null,
    val city: String = "Casablanca",
    val photoUrl: String? = null,
    val medications: List<UserMedication> = emptyList(),
    val allergies: List<String> = emptyList(),
    val chronicConditions: List<String> = emptyList(),
    val bloodType: String? = null,
    val dateOfBirth: String? = null,
    val gender: String? = null,
    val emergencyContact: EmergencyContact? = null,
    val preferredPharmacyId: String? = null,
    val isLoggedIn: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * User Medication
 * Represents a medication that a user is taking with tracking information
 */
data class UserMedication(
    val id: String,
    val medicationId: String,
    val medicationName: String,
    val dosage: String,
    val frequency: MedicationFrequency,
    val startDate: String,
    val endDate: String? = null,
    val reminderEnabled: Boolean = false,
    val reminderTimes: List<String> = emptyList(), // HH:mm format
    val notes: String? = null,
    val prescribedBy: String? = null,
    val refillReminder: Boolean = false,
    val stockQuantity: Int = 0,
    val lowStockThreshold: Int = 5,
    val isActive: Boolean = true
)

/**
 * Medication Frequency
 */
enum class MedicationFrequency(val displayName: String, val displayNameAr: String) {
    ONCE_DAILY("Une fois par jour", "مرة واحدة في اليوم"),
    TWICE_DAILY("Deux fois par jour", "مرتين في اليوم"),
    THREE_TIMES_DAILY("Trois fois par jour", "ثلاث مرات في اليوم"),
    FOUR_TIMES_DAILY("Quatre fois par jour", "أربع مرات في اليوم"),
    EVERY_12_HOURS("Toutes les 12 heures", "كل 12 ساعة"),
    EVERY_8_HOURS("Toutes les 8 heures", "كل 8 ساعات"),
    EVERY_6_HOURS("Toutes les 6 heures", "كل 6 ساعات"),
    AS_NEEDED("Au besoin", "عند الحاجة"),
    WEEKLY("Une fois par semaine", "مرة في الأسبوع"),
    CUSTOM("Personnalisé", "مخصص")
}

/**
 * Emergency Contact
 */
data class EmergencyContact(
    val name: String,
    val relationship: String,
    val phoneNumber: String,
    val email: String? = null
)

/**
 * Guest Profile
 * Default profile for users who haven't created an account
 */
object GuestProfile {
    fun create(city: String = "Casablanca"): UserProfile {
        return UserProfile(
            id = "guest",
            name = "Invité",
            email = null,
            phoneNumber = null,
            city = city,
            photoUrl = null,
            medications = emptyList(),
            allergies = emptyList(),
            chronicConditions = emptyList(),
            bloodType = null,
            dateOfBirth = null,
            gender = null,
            emergencyContact = null,
            preferredPharmacyId = null,
            isLoggedIn = false,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    }
}

/**
 * Medication Reminder
 * Represents a scheduled reminder for taking medication
 */
data class MedicationReminder(
    val id: String,
    val userMedicationId: String,
    val time: LocalTime,
    val enabled: Boolean = true,
    val dayOfWeek: List<Int>? = null // null means every day, otherwise specific days (1-7, Monday-Sunday)
)

/**
 * Medication History Entry
 * Records when a user took their medication
 */
data class MedicationHistoryEntry(
    val id: String,
    val userMedicationId: String,
    val timestamp: Long,
    val taken: Boolean,
    val notes: String? = null,
    val skippedReason: String? = null
)

/**
 * User Profile Extensions
 */
fun UserProfile.isGuest(): Boolean = !isLoggedIn

fun UserProfile.hasActiveMedications(): Boolean = 
    medications.any { it.isActive }

fun UserProfile.getActiveMedications(): List<UserMedication> = 
    medications.filter { it.isActive }

fun UserProfile.getMedicationsNeedingRefill(): List<UserMedication> =
    medications.filter { it.isActive && it.stockQuantity <= it.lowStockThreshold }

fun UserProfile.withUpdatedMedication(medication: UserMedication): UserProfile {
    val updatedMedications = medications.toMutableList()
    val index = updatedMedications.indexOfFirst { it.id == medication.id }
    if (index != -1) {
        updatedMedications[index] = medication
    } else {
        updatedMedications.add(medication)
    }
    return copy(medications = updatedMedications, updatedAt = System.currentTimeMillis())
}

fun UserProfile.withRemovedMedication(medicationId: String): UserProfile {
    return copy(
        medications = medications.filterNot { it.id == medicationId },
        updatedAt = System.currentTimeMillis()
    )
}
