package com.pharmatech.morocco.core.utils

import android.os.Build
import timber.log.Timber

/**
 * Utility to detect if the app is running on an Android emulator vs physical device.
 * This helps provide appropriate fallbacks and mock data for emulator testing.
 */
object EmulatorDetector {

    private var isEmulatorCached: Boolean? = null

    /**
     * Checks if the current device is an emulator.
     * Result is cached after first check for performance.
     *
     * @return true if running on an emulator, false if on a physical device
     */
    fun isEmulator(): Boolean {
        // Return cached value if available
        isEmulatorCached?.let { return it }

        val result = checkIsEmulator()
        isEmulatorCached = result

        if (result) {
            Timber.d("Running on EMULATOR: ${Build.MANUFACTURER} ${Build.MODEL}")
        } else {
            Timber.d("Running on PHYSICAL DEVICE: ${Build.MANUFACTURER} ${Build.MODEL}")
        }

        return result
    }

    private fun checkIsEmulator(): Boolean {
        return (
            // Check fingerprint
            Build.FINGERPRINT.startsWith("generic") ||
            Build.FINGERPRINT.startsWith("unknown") ||
            Build.FINGERPRINT.contains("sdk") ||
            Build.FINGERPRINT.contains("emulator") ||
            Build.FINGERPRINT.contains("test-keys") ||

            // Check model
            Build.MODEL.contains("google_sdk") ||
            Build.MODEL.contains("Emulator") ||
            Build.MODEL.contains("Android SDK built for") ||
            Build.MODEL.lowercase().contains("droid4x") ||
            Build.MODEL.contains("sdk_gphone") ||

            // Check manufacturer
            Build.MANUFACTURER.contains("Genymotion") ||
            Build.MANUFACTURER.contains("unknown") ||
            Build.MANUFACTURER.contains("Google") && (
                Build.MODEL.startsWith("sdk") ||
                Build.MODEL.startsWith("google_sdk") ||
                Build.MODEL.lowercase().contains("emulator")
            ) ||

            // Check hardware
            Build.HARDWARE.contains("goldfish") ||
            Build.HARDWARE.contains("ranchu") ||
            Build.HARDWARE.contains("vbox") ||
            Build.HARDWARE.lowercase().contains("nox") ||

            // Check product
            Build.PRODUCT.contains("sdk") ||
            Build.PRODUCT.contains("emulator") ||
            Build.PRODUCT.contains("simulator") ||
            Build.PRODUCT.contains("google_sdk") ||
            Build.PRODUCT.contains("sdk_gphone") ||
            Build.PRODUCT.contains("vbox86p") ||

            // Check board
            Build.BOARD.lowercase().contains("goldfish") ||
            Build.BOARD.lowercase().contains("ranchu") ||

            // Check brand
            Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic") ||

            // Additional checks for specific emulators
            "google_sdk" == Build.PRODUCT ||
            "sdk" == Build.PRODUCT ||
            "sdk_x86" == Build.PRODUCT ||
            "vbox86p" == Build.PRODUCT ||
            "emulator" == Build.PRODUCT ||
            "simulator" == Build.PRODUCT
        )
    }

    /**
     * Returns a human-readable device description
     */
    fun getDeviceInfo(): String {
        return buildString {
            append("Device Type: ${if (isEmulator()) "Emulator" else "Physical Device"}\n")
            append("Manufacturer: ${Build.MANUFACTURER}\n")
            append("Model: ${Build.MODEL}\n")
            append("Brand: ${Build.BRAND}\n")
            append("Product: ${Build.PRODUCT}\n")
            append("Hardware: ${Build.HARDWARE}\n")
            append("Board: ${Build.BOARD}\n")
            append("Fingerprint: ${Build.FINGERPRINT}")
        }
    }

    /**
     * Checks if running on Android Studio emulator specifically
     */
    fun isAndroidStudioEmulator(): Boolean {
        return isEmulator() && (
            Build.HARDWARE.contains("ranchu") ||
            Build.PRODUCT.contains("sdk_gphone") ||
            Build.MODEL.contains("sdk_gphone")
        )
    }

    /**
     * Checks if running on Genymotion emulator
     */
    fun isGenymotionEmulator(): Boolean {
        return isEmulator() && Build.MANUFACTURER.contains("Genymotion")
    }

    /**
     * Force refresh the emulator detection (for testing purposes)
     */
    fun refreshDetection(): Boolean {
        isEmulatorCached = null
        return isEmulator()
    }
}
