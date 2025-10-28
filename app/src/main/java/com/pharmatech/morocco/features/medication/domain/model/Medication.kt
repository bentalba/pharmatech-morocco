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
     * DOLIPRANE 1000 MG - Paracetamol
     * Common pain reliever and fever reducer
     */
    private val doliprane1000 = Medication(
        id = "med_doliprane_1000",
        name = "DOLIPRANE 1000 MG",
        nameAr = "دوليبران 1000 مغ",
        dosage = "1000 mg",
        pharmaceuticalForm = "Comprimé",
        composition = "Paracétamol 1000 mg",
        therapeuticClass = TherapeuticClass.ANALGESICS,
        packSize = "8 comprimés",
        ppv = 15.50,
        priceHospital = 10.20,
        priceWholesaler = 12.40,
        manufacturer = "Sanofi",
        distributor = "SANOFI MAROC",
        indications = "Traitement symptomatique de la douleur et de la fièvre",
        prescriptionRequired = false,
        isGeneric = false
    )

    /**
     * DOLIPRANE 500 MG - Paracetamol
     */
    private val doliprane500 = Medication(
        id = "med_doliprane_500",
        name = "DOLIPRANE 500 MG",
        nameAr = "دوليبران 500 مغ",
        dosage = "500 mg",
        pharmaceuticalForm = "Comprimé",
        composition = "Paracétamol 500 mg",
        therapeuticClass = TherapeuticClass.ANALGESICS,
        packSize = "16 comprimés",
        ppv = 12.80,
        priceHospital = 8.50,
        priceWholesaler = 10.24,
        manufacturer = "Sanofi",
        distributor = "SANOFI MAROC",
        indications = "Traitement symptomatique de la douleur et de la fièvre",
        prescriptionRequired = false,
        isGeneric = false
    )

    /**
     * ASPEGIC 500 MG - Aspirin
     */
    private val aspegic500 = Medication(
        id = "med_aspegic_500",
        name = "ASPEGIC 500 MG",
        nameAr = "أسبجيك 500 مغ",
        dosage = "500 mg",
        pharmaceuticalForm = "Poudre pour solution buvable",
        composition = "Acétylsalicylate de lysine équivalent à 500 mg d'acide acétylsalicylique",
        therapeuticClass = TherapeuticClass.ANALGESICS,
        packSize = "20 sachets",
        ppv = 18.90,
        priceHospital = 12.50,
        priceWholesaler = 15.12,
        manufacturer = "Sanofi",
        distributor = "SANOFI MAROC",
        indications = "Traitement symptomatique des douleurs d'intensité légère à modérée et/ou des états fébriles",
        prescriptionRequired = false,
        isGeneric = false
    )

    /**
     * CORTISONE 10 MG
     */
    private val cortisone10 = Medication(
        id = "med_cortisone_10",
        name = "CORTANCYL 10 MG",
        nameAr = "كورتانسيل 10 مغ",
        dosage = "10 mg",
        pharmaceuticalForm = "Comprimé",
        composition = "Prednisone 10 mg",
        therapeuticClass = TherapeuticClass.ANTI_INFLAMMATORY,
        packSize = "30 comprimés",
        ppv = 28.50,
        priceHospital = 18.90,
        priceWholesaler = 22.80,
        manufacturer = "Sanofi",
        distributor = "SANOFI MAROC",
        indications = "Traitement anti-inflammatoire et immunosuppresseur",
        contraindications = "Infections non contrôlées, ulcère gastroduodénal évolutif",
        prescriptionRequired = true,
        isGeneric = false
    )

    /**
     * VITAMIN C - Not eligible for reimbursement
     */
    private val vitaminC = Medication(
        id = "med_vitamin_c",
        name = "VITAMINE C 500 MG",
        nameAr = "فيتامين سي 500 مغ",
        dosage = "500 mg",
        pharmaceuticalForm = "Comprimé effervescent",
        composition = "Acide ascorbique 500 mg",
        therapeuticClass = TherapeuticClass.VITAMINS,
        packSize = "20 comprimés",
        ppv = 22.00,
        priceHospital = 14.50,
        priceWholesaler = 17.60,
        manufacturer = "Laboratoire Général",
        distributor = "PHARMACIE CENTRALE",
        indications = "Prévention et traitement des carences en vitamine C",
        prescriptionRequired = false,
        isGeneric = false
    )

    /**
     * AMOXICILLIN 500 MG - Antibiotic
     */
    private val amoxicillin500 = Medication(
        id = "med_amoxicillin_500",
        name = "AMOXICILLINE 500 MG",
        nameAr = "أموكسيسيلين 500 مغ",
        dosage = "500 mg",
        pharmaceuticalForm = "Gélule",
        composition = "Amoxicilline 500 mg",
        therapeuticClass = TherapeuticClass.ANTIBIOTICS,
        packSize = "12 gélules",
        ppv = 32.50,
        priceHospital = 21.50,
        priceWholesaler = 26.00,
        manufacturer = "GlaxoSmithKline",
        distributor = "GSK MAROC",
        indications = "Infections bactériennes sensibles à l'amoxicilline",
        contraindications = "Allergie aux pénicillines",
        prescriptionRequired = true,
        isGeneric = true
    )

    /**
     * INSULIN 100UI - Essential diabetes medication
     */
    private val insulin100ui = Medication(
        id = "med_insulin_100ui",
        name = "INSULINE RAPIDE 100 UI/ML",
        nameAr = "أنسولين سريع 100 وحدة دولية/مل",
        dosage = "100 UI/mL",
        pharmaceuticalForm = "Solution injectable",
        composition = "Insuline humaine 100 UI/mL",
        therapeuticClass = TherapeuticClass.ENDOCRINE,
        packSize = "Flacon 10 mL",
        ppv = 85.00,
        priceHospital = 56.00,
        priceWholesaler = 68.00,
        manufacturer = "Novo Nordisk",
        distributor = "NOVO NORDISK MAROC",
        indications = "Traitement du diabète sucré",
        storageConditions = "À conserver au réfrigérateur entre 2°C et 8°C",
        prescriptionRequired = true,
        isGeneric = false
    )

    /**
     * Expensive cancer drug - Example of differential coverage
     */
    private val expensiveCancerDrug = Medication(
        id = "med_expensive_cancer_drug",
        name = "ONCOTHERAP 250 MG",
        nameAr = "أونكوثيراب 250 مغ",
        dosage = "250 mg",
        pharmaceuticalForm = "Comprimé pelliculé",
        composition = "Principe actif anticancéreux 250 mg",
        therapeuticClass = TherapeuticClass.OTHER,
        packSize = "30 comprimés",
        ppv = 4500.00,
        priceHospital = 3000.00,
        priceWholesaler = 3600.00,
        manufacturer = "Roche",
        distributor = "ROCHE MAROC",
        indications = "Traitement de certains cancers",
        contraindications = "Nombreuses contre-indications, consultation spécialisée requise",
        prescriptionRequired = true,
        isGeneric = false
    )

    /**
     * Get all medications
     * Returns list of all available medications
     */
    fun getAllMedications(): List<Medication> {
        return listOf(
            cotareg,
            doliprane1000,
            doliprane500,
            aspegic500,
            cortisone10,
            vitaminC,
            amoxicillin500,
            insulin100ui,
            expensiveCancerDrug
        )
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
