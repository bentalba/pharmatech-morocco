package com.pharmatech.morocco.features.medication.domain.model

/**
 * Medication Data Model
 * Represents a pharmaceutical product in Morocco
 */
data class Medication(
    val id: String,
    val name: String,
    val nameAr: String? = null,
    val dosage: String,
    val pharmaceuticalForm: String,
    val composition: String,
    val therapeuticClass: TherapeuticClass,
    val packSize: String,
    val ppv: Double, // Public Price including VAT (Prix Public TTC)
    val priceHospital: Double? = null,
    val priceWholesaler: Double? = null,
    val manufacturer: String,
    val distributor: String,
    val country: String = "Morocco",
    val barcode: String? = null,
    val isNew: Boolean = false,
    val registrationNumber: String? = null,
    val indications: String,
    val contraindications: String? = null,
    val posology: String? = null,
    val sideEffects: String? = null,
    val interactions: String? = null,
    val warnings: String? = null,
    val storageConditions: String? = null,
    val prescriptionRequired: Boolean = true,
    val isGeneric: Boolean = false,
    val referenceProductId: String? = null,
    val imageUrl: String? = null
)

/**
 * Therapeutic Classification System
 * Based on Anatomical Therapeutic Chemical (ATC) Classification
 */
enum class TherapeuticClass(val displayName: String, val displayNameAr: String) {
    CARDIOVASCULAR("Cardiovasculaire", "القلب والأوعية الدموية"),
    ANTIBIOTICS("Antibiotiques", "المضادات الحيوية"),
    ANALGESICS("Analgésiques", "المسكنات"),
    ANTI_INFLAMMATORY("Anti-inflammatoires", "مضادات الالتهاب"),
    RESPIRATORY("Respiratoire", "الجهاز التنفسي"),
    DIGESTIVE("Digestif", "الجهاز الهضمي"),
    NERVOUS_SYSTEM("Système Nerveux", "الجهاز العصبي"),
    ENDOCRINE("Endocrinien", "الغدد الصماء"),
    DERMATOLOGY("Dermatologie", "الأمراض الجلدية"),
    OPHTHALMOLOGY("Ophtalmologie", "طب العيون"),
    VITAMINS("Vitamines et Minéraux", "الفيتامينات والمعادن"),
    OTHER("Autre", "أخرى")
}

/**
 * Medication Database
 * Static repository of medications (can be replaced with API/Room DB later)
 */
object MedicationDatabase {
    
    /**
     * COTAREG - Valsartan + Hydrochlorothiazide
     * Antihypertensive combination
     */
    private val cotareg = Medication(
        id = "med_cotareg_160_12_5",
        name = "COTAREG 160 MG / 12,5 MG",
        nameAr = "كوتاريج 160 مغ / 12,5 مغ",
        dosage = "160 mg / 12,5 mg",
        pharmaceuticalForm = "Comprimé pelliculé",
        composition = "Valsartan 160 mg + Hydrochlorothiazide 12,5 mg",
        therapeuticClass = TherapeuticClass.CARDIOVASCULAR,
        packSize = "28 comprimés",
        ppv = 145.20,
        priceHospital = 90.70,
        priceWholesaler = 116.00,
        manufacturer = "Novartis Pharma AG, Suisse",
        distributor = "NOVARTIS PHARMA MAROC",
        country = "Morocco",
        barcode = "6111111111111",
        isNew = true,
        registrationNumber = "B-17456",
        indications = """
            Traitement de l'hypertension artérielle essentielle.
            
            COTAREG est une association fixe de valsartan et d'hydrochlorothiazide. L'association fixe est indiquée chez les patients dont la pression artérielle n'est pas suffisamment contrôlée par le valsartan ou l'hydrochlorothiazide en monothérapie.
            
            COTAREG 160 mg/12,5 mg peut être utilisé chez les patients dont la pression artérielle n'est pas suffisamment contrôlée par COTAREG 160 mg/12,5 mg.
        """.trimIndent(),
        contraindications = """
            - Hypersensibilité aux substances actives ou à l'un des excipients
            - Insuffisance hépatique sévère, cirrhose biliaire et cholestase
            - Insuffisance rénale sévère (clairance de la créatinine < 30 ml/min)
            - Anurie
            - Hypokaliémie ou hypercalcémie réfractaires au traitement
            - Grossesse (2ème et 3ème trimestres)
            - Allaitement
        """.trimIndent(),
        posology = """
            La dose recommandée est d'un comprimé par jour.
            
            L'effet antihypertenseur est largement obtenu dans les 2 semaines suivant l'instauration du traitement et l'effet maximal est atteint après 4 semaines.
            
            Lorsqu'un contrôle supplémentaire de la pression artérielle est nécessaire, la dose peut être augmentée à 160 mg/25 mg (1 comprimé de COTAREG 160 mg/25 mg) ou à 320 mg/12,5 mg (1 comprimé de COTAREG 320 mg/12,5 mg) ou à 320 mg/25 mg (1 comprimé de COTAREG 320 mg/25 mg).
        """.trimIndent(),
        sideEffects = """
            Effets indésirables fréquents (≥1/100, <1/10):
            - Vertiges
            - Fatigue
            - Hypotension (y compris hypotension orthostatique)
            
            Effets indésirables peu fréquents (≥1/1000, <1/100):
            - Toux
            - Diarrhée
            - Nausées
            - Hypokaliémie
            - Hyperuricémie
            - Augmentation de la créatininémie
        """.trimIndent(),
        interactions = """
            Interactions nécessitant une précaution d'emploi:
            - Anti-inflammatoires non stéroïdiens (AINS)
            - Lithium
            - Autres antihypertenseurs
            - Médicaments hyperkaliémiants
            - Médicaments hypokaliémiants
            
            Associations déconseillées:
            - Aliskiren chez les patients diabétiques ou insuffisants rénaux
        """.trimIndent(),
        warnings = """
            Mises en garde spéciales:
            - Surveillance de la fonction rénale
            - Surveillance des électrolytes (potassium, sodium)
            - Risque d'hypotension chez les patients présentant une déplétion volémique
            - Déséquilibre électrolytique
            - Grossesse: arrêt immédiat en cas de grossesse
        """.trimIndent(),
        storageConditions = "À conserver à une température ne dépassant pas 30°C. Conserver dans l'emballage d'origine.",
        prescriptionRequired = true,
        isGeneric = false,
        referenceProductId = null,
        imageUrl = null
    )
    
    /**
     * Get all medications
     * Returns list of all available medications
     */
    fun getAllMedications(): List<Medication> {
        return listOf(cotareg)
    }
    
    /**
     * Search medications by name or composition
     */
    fun searchMedications(query: String): List<Medication> {
        if (query.isBlank()) return getAllMedications()
        
        val searchQuery = query.lowercase().trim()
        return getAllMedications().filter { medication ->
            medication.name.lowercase().contains(searchQuery) ||
            medication.composition.lowercase().contains(searchQuery) ||
            medication.manufacturer.lowercase().contains(searchQuery) ||
            medication.distributor.lowercase().contains(searchQuery) ||
            medication.therapeuticClass.displayName.lowercase().contains(searchQuery)
        }
    }
    
    /**
     * Get medication by ID
     */
    fun getMedicationById(id: String): Medication? {
        return getAllMedications().find { it.id == id }
    }
    
    /**
     * Get medications by therapeutic class
     */
    fun getMedicationsByClass(therapeuticClass: TherapeuticClass): List<Medication> {
        return getAllMedications().filter { it.therapeuticClass == therapeuticClass }
    }
    
    /**
     * Get new medications
     */
    fun getNewMedications(): List<Medication> {
        return getAllMedications().filter { it.isNew }
    }
}
