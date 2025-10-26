package com.pharmatech.morocco.features.pharmacy.domain.model

data class Pharmacy(
    val id: String,
    val name: String,
    val address: String,
    val city: String,
    val sector: String,
    val phoneNumber: String,
    val latitude: Double,
    val longitude: Double,
    val plusCode: String,
    val paymentMethods: List<PaymentMethod>,
    val openingHours: List<OpeningHours>,
    val isOpen: Boolean = false
)

data class OpeningHours(
    val dayOfWeek: String,
    val morningOpen: String?,
    val morningClose: String?,
    val afternoonOpen: String?,
    val afternoonClose: String?
)

enum class PaymentMethod {
    CASH,
    CARD,
    MOBILE_PAYMENT
}

// Sample pharmacy data for Kenitra
object PharmacyData {
    val kenitiraPharmacy = Pharmacy(
        id = "pharmacy_kenitra_bir_rami_001",
        name = "Pharmacie Bir Rami",
        address = "Villa N°699, Rue Mehjoubi Mohamed, Lotissement Bir Rami Est",
        city = "KÉNITRA",
        sector = "QUARTIER BIR RAMI",
        phoneNumber = "08 08 68 49 98",
        latitude = 34.24532545335408,
        longitude = -6.5984582249030925,
        plusCode = "6CW2+2H Kenitra",
        paymentMethods = listOf(PaymentMethod.CASH),
        openingHours = listOf(
            OpeningHours("Lundi", "09:00", "12:30", "15:00", "19:30"),
            OpeningHours("Mardi", "09:00", "12:30", "15:00", "19:30"),
            OpeningHours("Mercredi", "09:00", "12:30", "15:00", "19:30"),
            OpeningHours("Jeudi", "09:00", "12:30", "15:00", "19:30"),
            OpeningHours("Vendredi", "09:00", "12:30", "15:00", "19:30"),
            OpeningHours("Samedi", "09:00", "13:00", null, null)
        )
    )

    fun getAllPharmacies(): List<Pharmacy> {
        return listOf(kenitiraPharmacy)
    }
}
