package com.pharmatech.morocco.core.utils

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import timber.log.Timber

/**
 * Helper class for managing exact alarm permissions on Android 12+ (API 31+).
 *
 * Starting from Android 12, apps need to explicitly request permission to schedule exact alarms.
 * This is critical for medication reminder features.
 */
object AlarmPermissionHelper {

    /**
     * Checks if the app can schedule exact alarms.
     *
     * On Android 12+ (API 31+), this checks the SCHEDULE_EXACT_ALARM permission.
     * On older versions, always returns true.
     *
     * @param context The application context
     * @return true if exact alarms can be scheduled, false otherwise
     */
    fun canScheduleExactAlarms(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            alarmManager?.canScheduleExactAlarms() ?: false
        } else {
            // On older Android versions, no special permission needed
            true
        }
    }

    /**
     * Opens the system settings page where users can grant exact alarm permission.
     * Only applicable on Android 12+ (API 31+).
     *
     * @param context The application context
     * @return true if the settings page was opened successfully, false otherwise
     */
    @RequiresApi(Build.VERSION_CODES.S)
    fun openExactAlarmSettings(context: Context): Boolean {
        return try {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.parse("package:${context.packageName}")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            Timber.d("Opened exact alarm permission settings")
            true
        } catch (e: Exception) {
            Timber.e(e, "Failed to open exact alarm settings")
            false
        }
    }

    /**
     * Gets a user-friendly explanation of why exact alarm permission is needed.
     *
     * @return Explanation text
     */
    fun getPermissionRationale(): String {
        return """
            PharmaTech needs permission to schedule exact alarms to ensure your medication reminders
            are delivered at the precise times you set. This helps you stay on track with your
            medication schedule.

            Without this permission, reminders may be delayed or delivered at approximate times,
            which could affect your medication adherence.
        """.trimIndent()
    }

    /**
     * Determines the best alarm scheduling strategy based on permission status.
     *
     * @param context The application context
     * @return Strategy: "EXACT" if exact alarms allowed, "INEXACT" if not
     */
    fun getRecommendedAlarmStrategy(context: Context): AlarmStrategy {
        return if (canScheduleExactAlarms(context)) {
            AlarmStrategy.EXACT
        } else {
            AlarmStrategy.INEXACT
        }
    }

    /**
     * Checks if we need to request exact alarm permission.
     * Returns true only on Android 12+ if permission is not granted.
     *
     * @param context The application context
     * @return true if permission request is needed
     */
    fun shouldRequestPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            !canScheduleExactAlarms(context)
        } else {
            false
        }
    }

    /**
     * Gets the Android version-specific permission status message
     *
     * @param context The application context
     * @return Human-readable status message
     */
    fun getPermissionStatus(context: Context): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (canScheduleExactAlarms(context)) {
                "Exact alarms: Granted ✓"
            } else {
                "Exact alarms: Not granted (reminders may be delayed)"
            }
        } else {
            "Exact alarms: Not required on Android ${Build.VERSION.SDK_INT}"
        }
    }

    enum class AlarmStrategy {
        /**
         * Use exact alarms (AlarmManager.setExact or setExactAndAllowWhileIdle)
         * Requires SCHEDULE_EXACT_ALARM permission on Android 12+
         */
        EXACT,

        /**
         * Use inexact alarms (AlarmManager.set)
         * No special permission required, but timing is approximate
         */
        INEXACT
    }
}
