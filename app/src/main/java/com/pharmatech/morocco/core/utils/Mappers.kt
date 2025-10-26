package com.pharmatech.morocco.core.utils

import com.pharmatech.morocco.core.database.entities.*
import com.pharmatech.morocco.core.network.models.*
import com.pharmatech.morocco.features.medication.domain.model.Medication
import com.pharmatech.morocco.features.medication.domain.model.MedicationTracker
import com.pharmatech.morocco.features.pharmacy.domain.model.Pharmacy
import java.text.SimpleDateFormat
import java.util.*

// Entity to Domain Model Mappers

fun MedicationEntity.toDomainModel(): Medication {
    return Medication(
        id = id,
        name = name,
        nameAr = nameAr,
        nameFr = nameFr,
        description = description,
        category = category,
        isOTC = isOTC,
        activeIngredient = activeIngredient,
        dosageForm = dosageForm,
        strength = strength,
        manufacturer = manufacturer,
        imageUrl = imageUrl,
        barcode = barcode,
        sideEffects = sideEffects,
        contraindications = contraindications,
        interactions = interactions,
        storageConditions = storageConditions,
        price = price,
        lastUpdated = lastUpdated
    )
}

fun PharmacyEntity.toDomainModel(): Pharmacy {
    return Pharmacy(
        id = id,
        name = name,
        nameAr = nameAr,
        address = address,
        addressAr = addressAr,
        city = city,
        latitude = latitude,
        longitude = longitude,
        phoneNumber = phoneNumber,
        email = email,
        website = website,
        openingHours = openingHours,
        is24Hours = is24Hours,
        hasDelivery = hasDelivery,
        hasOnlineOrdering = hasOnlineOrdering,
        hasParking = hasParking,
        isGuardPharmacy = isGuardPharmacy,
        rating = rating,
        reviewCount = reviewCount,
        imageUrl = imageUrl,
        services = services,
        lastUpdated = lastUpdated
    )
}

fun MedicationTrackerEntity.toDomainModel(): MedicationTracker {
    return MedicationTracker(
        id = id,
        userId = userId,
        medicationId = medicationId,
        medicationName = medicationName,
        dosage = dosage,
        frequency = frequency,
        timeOfDay = timeOfDay,
        startDate = startDate,
        endDate = endDate,
        instructions = instructions,
        isActive = isActive,
        color = color,
        reminderEnabled = reminderEnabled
    )
}

// Network Response to Entity Mappers

fun MedicationResponse.toEntity(): MedicationEntity {
    return MedicationEntity(
        id = id,
        name = name,
        nameAr = nameAr,
        nameFr = nameFr,
        description = description,
        category = category,
        isOTC = isOTC,
        activeIngredient = activeIngredient,
        dosageForm = dosageForm,
        strength = strength,
        manufacturer = manufacturer,
        imageUrl = imageUrl,
        barcode = barcode,
        sideEffects = emptyList(),
        contraindications = emptyList(),
        interactions = emptyList(),
        storageConditions = "",
        price = price,
        lastUpdated = Date()
    )
}

fun PharmacyResponse.toEntity(): PharmacyEntity {
    return PharmacyEntity(
        id = id,
        name = name,
        nameAr = nameAr,
        address = address,
        addressAr = addressAr,
        city = city,
        latitude = latitude,
        longitude = longitude,
        phoneNumber = phoneNumber,
        email = email,
        website = website,
        openingHours = openingHours,
        is24Hours = is24Hours,
        hasDelivery = hasDelivery,
        hasOnlineOrdering = hasOnlineOrdering,
        hasParking = hasParking,
        isGuardPharmacy = isGuardPharmacy,
        rating = rating,
        reviewCount = reviewCount,
        imageUrl = imageUrl,
        services = services,
        lastUpdated = Date()
    )
}

// Domain Model to Entity Mappers

fun Medication.toEntity(): MedicationEntity {
    return MedicationEntity(
        id = id,
        name = name,
        nameAr = nameAr,
        nameFr = nameFr,
        description = description,
        category = category,
        isOTC = isOTC,
        activeIngredient = activeIngredient,
        dosageForm = dosageForm,
        strength = strength,
        manufacturer = manufacturer,
        imageUrl = imageUrl,
        barcode = barcode,
        sideEffects = sideEffects,
        contraindications = contraindications,
        interactions = interactions,
        storageConditions = storageConditions,
        price = price,
        lastUpdated = lastUpdated
    )
}

fun Pharmacy.toEntity(): PharmacyEntity {
    return PharmacyEntity(
        id = id,
        name = name,
        nameAr = nameAr,
        address = address,
        addressAr = addressAr,
        city = city,
        latitude = latitude,
        longitude = longitude,
        phoneNumber = phoneNumber,
        email = email,
        website = website,
        openingHours = openingHours,
        is24Hours = is24Hours,
        hasDelivery = hasDelivery,
        hasOnlineOrdering = hasOnlineOrdering,
        hasParking = hasParking,
        isGuardPharmacy = isGuardPharmacy,
        rating = rating,
        reviewCount = reviewCount,
        imageUrl = imageUrl,
        services = services,
        lastUpdated = lastUpdated
    )
}

fun MedicationTracker.toEntity(): MedicationTrackerEntity {
    return MedicationTrackerEntity(
        id = id,
        userId = userId,
        medicationId = medicationId,
        medicationName = medicationName,
        dosage = dosage,
        frequency = frequency,
        timeOfDay = timeOfDay,
        startDate = startDate,
        endDate = endDate,
        instructions = instructions,
        isActive = isActive,
        color = color,
        reminderEnabled = reminderEnabled,
        createdAt = Date(),
        lastUpdated = Date()
    )
}

