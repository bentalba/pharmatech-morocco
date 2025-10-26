package com.pharmatech.morocco.features.pharmacy.domain.model

import java.time.DayOfWeek
import java.time.LocalDate

/**
 * On-Call Pharmacy Data Model
 * Represents a pharmacy that is on-call (de garde) for a specific day
 */
data class OnCallPharmacy(
    val id: String,
    val pharmacy: Pharmacy,
    val city: MoroccanCity,
    val dayOfWeek: DayOfWeek,
    val date: LocalDate,
    val isNightShift: Boolean = false, // 24/7 or night-only
    val startTime: String = "09:00",
    val endTime: String = "09:00", // Next day if night shift
    val specialNotes: String? = null
)

/**
 * Moroccan Cities
 * Major cities in Morocco with on-call pharmacy services
 */
enum class MoroccanCity(val displayName: String, val displayNameAr: String) {
    KENITRA("Kénitra", "القنيطرة"),
    CASABLANCA("Casablanca", "الدار البيضاء"),
    RABAT("Rabat", "الرباط"),
    FES("Fès", "فاس"),
    MARRAKECH("Marrakech", "مراكش"),
    TANGIER("Tanger", "طنجة"),
    AGADIR("Agadir", "أكادير"),
    MEKNES("Meknès", "مكناس"),
    OUJDA("Oujda", "وجدة"),
    TETOUAN("Tétouan", "تطوان"),
    SALE("Salé", "سلا"),
    TEMARA("Témara", "تمارة"),
    MOHAMMEDIA("Mohammedia", "المحمدية"),
    EL_JADIDA("El Jadida", "الجديدة"),
    BENI_MELLAL("Béni Mellal", "بني ملال"),
    NADOR("Nador", "الناظور"),
    SAFI("Safi", "آسفي"),
    KHOURIBGA("Khouribga", "خريبكة");
    
    companion object {
        fun fromDisplayName(name: String): MoroccanCity? {
            return values().find { 
                it.displayName.equals(name, ignoreCase = true) || 
                it.displayNameAr == name 
            }
        }
        
        fun getAllCities(): List<MoroccanCity> = values().toList()
    }
}

/**
 * On-Call Pharmacy Service
 * Manages the on-call pharmacy schedule
 */
object OnCallPharmacyService {
    
    // Sample data - In production, this would come from an API
    private val schedules: MutableMap<String, List<OnCallPharmacy>> = mutableMapOf()
    
    /**
     * Get on-call pharmacies for a specific city and date
     */
    fun getOnCallPharmacies(
        city: MoroccanCity,
        date: LocalDate = LocalDate.now()
    ): List<OnCallPharmacy> {
        val key = "${city.name}_${date}"
        return schedules[key] ?: emptyList()
    }
    
    /**
     * Get on-call pharmacies for a specific city and day of week
     */
    fun getOnCallPharmaciesByDay(
        city: MoroccanCity,
        dayOfWeek: DayOfWeek
    ): List<OnCallPharmacy> {
        return schedules.values.flatten().filter { 
            it.city == city && it.dayOfWeek == dayOfWeek 
        }
    }
    
    /**
     * Check if a pharmacy is on-call for a specific date
     */
    fun isPharmacyOnCall(
        pharmacyId: String,
        date: LocalDate = LocalDate.now()
    ): Boolean {
        return schedules.values.flatten().any { 
            it.pharmacy.id == pharmacyId && it.date == date 
        }
    }
    
    /**
     * Get all on-call pharmacies for today
     */
    fun getTodayOnCallPharmacies(city: MoroccanCity): List<OnCallPharmacy> {
        return getOnCallPharmacies(city, LocalDate.now())
    }
    
    /**
     * Get all on-call pharmacies for tomorrow
     */
    fun getTomorrowOnCallPharmacies(city: MoroccanCity): List<OnCallPharmacy> {
        return getOnCallPharmacies(city, LocalDate.now().plusDays(1))
    }
    
    /**
     * Get on-call pharmacies for a date range
     */
    fun getOnCallPharmaciesInRange(
        city: MoroccanCity,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<OnCallPharmacy> {
        val result = mutableListOf<OnCallPharmacy>()
        var currentDate = startDate
        
        while (!currentDate.isAfter(endDate)) {
            result.addAll(getOnCallPharmacies(city, currentDate))
            currentDate = currentDate.plusDays(1)
        }
        
        return result
    }
    
    /**
     * Add on-call pharmacy to schedule
     * (Admin function - in production this would be done via API)
     */
    fun addOnCallPharmacy(onCallPharmacy: OnCallPharmacy) {
        val key = "${onCallPharmacy.city.name}_${onCallPharmacy.date}"
        val existing = schedules[key]?.toMutableList() ?: mutableListOf()
        existing.add(onCallPharmacy)
        schedules[key] = existing
    }
    
    /**
     * Add multiple on-call pharmacies
     */
    fun addOnCallPharmacies(pharmacies: List<OnCallPharmacy>) {
        pharmacies.forEach { addOnCallPharmacy(it) }
    }
    
    /**
     * Clear schedule for a specific city and date
     */
    fun clearSchedule(city: MoroccanCity, date: LocalDate) {
        val key = "${city.name}_${date}"
        schedules.remove(key)
    }
    
    /**
     * Clear all schedules
     */
    fun clearAllSchedules() {
        schedules.clear()
    }
    
    /**
     * Get schedule statistics
     */
    fun getScheduleStats(): ScheduleStats {
        val allPharmacies = schedules.values.flatten()
        return ScheduleStats(
            totalEntries = allPharmacies.size,
            citiesCount = allPharmacies.map { it.city }.distinct().size,
            nightShiftCount = allPharmacies.count { it.isNightShift },
            datesCount = allPharmacies.map { it.date }.distinct().size
        )
    }
}

/**
 * Schedule Statistics
 */
data class ScheduleStats(
    val totalEntries: Int,
    val citiesCount: Int,
    val nightShiftCount: Int,
    val datesCount: Int
)

/**
 * Extension Functions
 */
fun LocalDate.toDayOfWeekFrench(): String {
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> "Lundi"
        DayOfWeek.TUESDAY -> "Mardi"
        DayOfWeek.WEDNESDAY -> "Mercredi"
        DayOfWeek.THURSDAY -> "Jeudi"
        DayOfWeek.FRIDAY -> "Vendredi"
        DayOfWeek.SATURDAY -> "Samedi"
        DayOfWeek.SUNDAY -> "Dimanche"
    }
}

fun LocalDate.toDayOfWeekArabic(): String {
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> "الإثنين"
        DayOfWeek.TUESDAY -> "الثلاثاء"
        DayOfWeek.WEDNESDAY -> "الأربعاء"
        DayOfWeek.THURSDAY -> "الخميس"
        DayOfWeek.FRIDAY -> "الجمعة"
        DayOfWeek.SATURDAY -> "السبت"
        DayOfWeek.SUNDAY -> "الأحد"
    }
}

fun LocalDate.toFormattedDateFrench(): String {
    val months = listOf(
        "janvier", "février", "mars", "avril", "mai", "juin",
        "juillet", "août", "septembre", "octobre", "novembre", "décembre"
    )
    return "${toDayOfWeekFrench()}, $dayOfMonth ${months[monthValue - 1]} $year"
}
