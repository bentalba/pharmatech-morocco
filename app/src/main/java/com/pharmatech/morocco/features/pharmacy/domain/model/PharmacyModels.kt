package com.pharmatech.morocco.features.pharmacy.domain.model

import java.util.Date

data class Pharmacy(
    val id: String,
    val name: String,
    val nameAr: String? = null,
    val address: String,
    val addressAr: String? = null,
    val city: String,
    val latitude: Double,
    val longitude: Double,
    val phoneNumber: String,
    val email: String? = null,
    val website: String? = null,
    val openingHours: String,
    val is24Hours: Boolean = false,
    val hasDelivery: Boolean = false,
    val hasOnlineOrdering: Boolean = false,
    val hasParking: Boolean = false,
    val isGuardPharmacy: Boolean = false,
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    val imageUrl: String? = null,
    val services: List<String> = emptyList(),
    val distance: Double? = null, // in meters
    val lastUpdated: Date
)

data class PharmacyReview(
    val id: String,
    val userId: String,
    val userName: String,
    val pharmacyId: String,
    val rating: Double,
    val comment: String,
    val createdAt: Date
)

