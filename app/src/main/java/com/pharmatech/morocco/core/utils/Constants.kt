package com.pharmatech.morocco.core.utils

object Constants {
    // Database
    const val DATABASE_NAME = "pharmatech_database"
    const val DATABASE_VERSION = 1

    // Preferences
    const val PREF_NAME = "pharmatech_prefs"
    const val PREF_KEY_TOKEN = "auth_token"
    const val PREF_KEY_USER_ID = "user_id"
    const val PREF_KEY_LANGUAGE = "language"
    const val PREF_KEY_THEME = "theme"

    // API
    const val API_TIMEOUT = 30L
    const val API_CACHE_SIZE = 10 * 1024 * 1024L // 10 MB

    // Notification
    const val NOTIFICATION_ID_MEDICATION = 1001
    const val NOTIFICATION_ID_PHARMACY = 1002
    const val NOTIFICATION_ID_INSIGHTS = 1003

    // Location
    const val DEFAULT_LOCATION_RADIUS = 5000 // meters
    const val LOCATION_UPDATE_INTERVAL = 10000L // 10 seconds
    const val LOCATION_FASTEST_INTERVAL = 5000L // 5 seconds

    // Medication Reminder
    const val REMINDER_ADVANCE_TIME = 5 // minutes before

    // Date Formats
    const val DATE_FORMAT_API = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val DATE_FORMAT_DISPLAY = "dd/MM/yyyy"
    const val TIME_FORMAT_DISPLAY = "HH:mm"

    // Languages
    const val LANG_FRENCH = "fr"
    const val LANG_ARABIC = "ar"
    const val LANG_ENGLISH = "en"

    // Medication Categories
    val MEDICATION_CATEGORIES = listOf(
        "Analgesics",
        "Antibiotics",
        "Antihistamines",
        "Cardiovascular",
        "Gastrointestinal",
        "Respiratory",
        "Diabetes",
        "Vitamins & Supplements",
        "Other"
    )

    // Frequency Options
    val FREQUENCY_OPTIONS = listOf(
        "Once daily",
        "Twice daily",
        "Three times daily",
        "Four times daily",
        "Every 4 hours",
        "Every 6 hours",
        "Every 8 hours",
        "Every 12 hours",
        "As needed",
        "Weekly",
        "Monthly"
    )

    // Time of Day
    val TIME_OF_DAY = listOf(
        "Morning",
        "Afternoon",
        "Evening",
        "Night",
        "Before meals",
        "After meals",
        "With meals"
    )
}

