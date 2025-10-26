package com.pharmatech.morocco.features.hospital.domain.model

data class Hospital(
    val id: String,
    val name: String,
    val type: HospitalType,
    val region: String,
    val province: String,
    val city: String,
    val address: String?,
    val latitude: Double,
    val longitude: Double,
    val phoneNumber: String?,
    val emergencyServices: Boolean = true,
    val specializations: List<String> = emptyList(),
    val bedCapacity: Int? = null
)

enum class HospitalType(
    val abbreviation: String,
    val fullName: String,
    val description: String,
    val color: String
) {
    HP(
        "HP",
        "Hôpital Provincial/Préfectoral",
        "Hôpital provincial ou préfectoral offrant des services généraux",
        "#4682B4" // Steel Blue
    ),
    HR(
        "HR",
        "Hôpital Régional",
        "Hôpital régional avec services spécialisés",
        "#DC143C" // Crimson
    ),
    HIR(
        "HIR",
        "Hôpital Interrégional",
        "Hôpital interrégional avec services hautement spécialisés",
        "#800080" // Purple
    ),
    HPr(
        "HPr",
        "Hôpital de Proximité",
        "Hôpital de proximité pour soins de base",
        "#20B2AA" // Light Sea Green
    ),
    HPsyP(
        "HPsyP",
        "Hôpital Psychiatrique Provincial/Préfectoral",
        "Hôpital psychiatrique provincial ou préfectoral",
        "#9370DB" // Medium Purple
    ),
    CRO(
        "CRO",
        "Centre Régional d'Oncologie",
        "Centre régional spécialisé en oncologie",
        "#FF6347" // Tomato
    ),
    HPsyR(
        "HPsyR",
        "Hôpital Psychiatrique Régional",
        "Hôpital psychiatrique régional",
        "#BA55D3" // Medium Orchid
    ),
    CPU(
        "CPU",
        "Centre Psychiatrique Universitaire",
        "Centre psychiatrique universitaire avec formation et recherche",
        "#8A2BE2" // Blue Violet
    );

    companion object {
        fun fromAbbreviation(abbr: String): HospitalType? {
            return values().find { it.abbreviation == abbr }
        }
    }
}

// Primary Care Facility Types
enum class PrimaryCareType(
    val abbreviation: String,
    val fullName: String,
    val description: String
) {
    CSR1(
        "CSR-1",
        "Centre de Santé Rural niveau 1",
        "Centre de santé rural de niveau 1"
    ),
    CSR2(
        "CSR-2",
        "Centre de Santé Rural niveau 2",
        "Centre de santé rural de niveau 2"
    ),
    CSU1(
        "CSU-1",
        "Centre de Santé Urbain niveau 1",
        "Centre de santé urbain de niveau 1"
    ),
    CSU2(
        "CSU-2",
        "Centre de Santé Urbain niveau 2",
        "Centre de santé urbain de niveau 2"
    ),
    DR(
        "DR",
        "Dispensaire Rural",
        "Dispensaire en zone rurale"
    ),
    LSP(
        "LSP",
        "Laboratoire de Santé Publique",
        "Laboratoire de santé publique"
    ),
    CRSR(
        "CRSR",
        "Centre de Référence en Santé de Reproduction",
        "Centre de référence en santé de reproduction"
    ),
    CDTMR(
        "CDTMR",
        "Centre de Diagnostic et de Traitement des Maladies Respiratoires",
        "Centre de diagnostic et de traitement des maladies respiratoires"
    );

    companion object {
        fun fromAbbreviation(abbr: String): PrimaryCareType? {
            return values().find { it.abbreviation == abbr }
        }
    }
}

data class PrimaryCareFacility(
    val id: String,
    val name: String,
    val type: PrimaryCareType,
    val region: String,
    val province: String,
    val commune: String?,
    val address: String?,
    val latitude: Double,
    val longitude: Double,
    val phoneNumber: String?,
    val servicesOffered: List<String> = emptyList()
)

// Database object for hospitals and primary care facilities
object HealthFacilityDatabase {
    private val hospitals = mutableListOf<Hospital>()
    private val primaryCareFacilities = mutableListOf<PrimaryCareFacility>()

    fun addHospital(hospital: Hospital) {
        hospitals.add(hospital)
    }

    fun addPrimaryCareFacility(facility: PrimaryCareFacility) {
        primaryCareFacilities.add(facility)
    }

    fun getAllHospitals(): List<Hospital> = hospitals.toList()

    fun getHospitalsByType(type: HospitalType): List<Hospital> {
        return hospitals.filter { it.type == type }
    }

    fun getHospitalsByRegion(region: String): List<Hospital> {
        return hospitals.filter { it.region.equals(region, ignoreCase = true) }
    }

    fun getAllPrimaryCareFacilities(): List<PrimaryCareFacility> = primaryCareFacilities.toList()

    fun getPrimaryCareFacilitiesByType(type: PrimaryCareType): List<PrimaryCareFacility> {
        return primaryCareFacilities.filter { it.type == type }
    }

    fun getPrimaryCareFacilitiesByRegion(region: String): List<PrimaryCareFacility> {
        return primaryCareFacilities.filter { it.region.equals(region, ignoreCase = true) }
    }

    fun clearAll() {
        hospitals.clear()
        primaryCareFacilities.clear()
    }

    // Statistics
    fun getHospitalCountByType(): Map<HospitalType, Int> {
        return hospitals.groupBy { it.type }.mapValues { it.value.size }
    }

    fun getHospitalCountByRegion(): Map<String, Int> {
        return hospitals.groupBy { it.region }.mapValues { it.value.size }
    }
}
