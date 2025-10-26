package com.pharmatech.morocco

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class PharmaTechApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        // Initialize Timber for logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Setup Crashlytics
        FirebaseCrashlytics.getInstance().apply {
            setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        }

        // Create notification channels
        createNotificationChannels()

        Timber.i("PharmaTech Morocco App initialized successfully")
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(
                if (BuildConfig.DEBUG) android.util.Log.DEBUG else android.util.Log.ERROR
            )
            .build()

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)

            // Medication Reminder Channel
            val medicationChannel = NotificationChannel(
                MEDICATION_CHANNEL_ID,
                "Medication Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminders to take your medications"
                enableVibration(true)
                enableLights(true)
            }

            // Pharmacy Updates Channel
            val pharmacyChannel = NotificationChannel(
                PHARMACY_CHANNEL_ID,
                "Pharmacy Updates",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Updates from your favorite pharmacies"
            }

            // Health Insights Channel
            val insightsChannel = NotificationChannel(
                INSIGHTS_CHANNEL_ID,
                "Health Insights",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "AI-powered health insights and tips"
            }

            notificationManager.createNotificationChannels(
                listOf(medicationChannel, pharmacyChannel, insightsChannel)
            )
        }
    }

    companion object {
        const val MEDICATION_CHANNEL_ID = "medication_reminders"
        const val PHARMACY_CHANNEL_ID = "pharmacy_updates"
        const val INSIGHTS_CHANNEL_ID = "health_insights"
    }

    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == android.util.Log.ERROR || priority == android.util.Log.WARN) {
                FirebaseCrashlytics.getInstance().log(message)
                t?.let { FirebaseCrashlytics.getInstance().recordException(it) }
            }
        }
    }
}

