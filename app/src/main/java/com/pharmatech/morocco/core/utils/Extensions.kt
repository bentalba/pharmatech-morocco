package com.pharmatech.morocco.core.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.text.DecimalFormat
import java.util.*

// Context Extensions
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

// String Extensions
fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPhoneNumber(): Boolean {
    // Moroccan phone number validation
    val moroccanPhonePattern = "^(\\+212|0)[5-7][0-9]{8}$"
    return this.matches(Regex(moroccanPhonePattern))
}

fun String.capitalizeWords(): String {
    return split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}

// Double Extensions
fun Double.formatPrice(): String {
    val format = DecimalFormat("#,##0.00")
    return "${format.format(this)} MAD"
}

fun Double.formatDistance(): String {
    return when {
        this < 1000 -> "${this.toInt()} m"
        else -> {
            val km = this / 1000
            String.format(Locale.getDefault(), "%.1f km", km)
        }
    }
}

// Date Extensions
fun Date.isToday(): Boolean {
    val today = Calendar.getInstance()
    val checkDate = Calendar.getInstance().apply { time = this@isToday }
    return today.get(Calendar.YEAR) == checkDate.get(Calendar.YEAR) &&
            today.get(Calendar.DAY_OF_YEAR) == checkDate.get(Calendar.DAY_OF_YEAR)
}

fun Date.isTomorrow(): Boolean {
    val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
    val checkDate = Calendar.getInstance().apply { time = this@isTomorrow }
    return tomorrow.get(Calendar.YEAR) == checkDate.get(Calendar.YEAR) &&
            tomorrow.get(Calendar.DAY_OF_YEAR) == checkDate.get(Calendar.DAY_OF_YEAR)
}

fun Date.isYesterday(): Boolean {
    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
    val checkDate = Calendar.getInstance().apply { time = this@isYesterday }
    return yesterday.get(Calendar.YEAR) == checkDate.get(Calendar.YEAR) &&
            yesterday.get(Calendar.DAY_OF_YEAR) == checkDate.get(Calendar.DAY_OF_YEAR)
}

fun Date.getTimeAgo(): String {
    val now = System.currentTimeMillis()
    val diff = now - this.time

    return when {
        diff < 60000 -> "Just now"
        diff < 3600000 -> "${diff / 60000} minutes ago"
        diff < 86400000 -> "${diff / 3600000} hours ago"
        diff < 604800000 -> "${diff / 86400000} days ago"
        else -> DateUtils.formatDateForDisplay(this)
    }
}

// List Extensions
fun <T> List<T>.isNotNullOrEmpty(): Boolean {
    return this.isNotEmpty()
}

// Composable Extensions
@Composable
fun showToast(message: String) {
    val context = LocalContext.current
    context.showToast(message)
}

// Validation Extensions
object Validators {
    fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "Email is required"
            !email.isValidEmail() -> "Invalid email format"
            else -> null
        }
    }

    fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "Password is required"
            password.length < 6 -> "Password must be at least 6 characters"
            else -> null
        }
    }

    fun validatePhoneNumber(phone: String): String? {
        if (phone.isBlank()) return null // Optional field
        return if (!phone.isValidPhoneNumber()) {
            "Invalid Moroccan phone number"
        } else null
    }

    fun validateRequired(value: String, fieldName: String): String? {
        return if (value.isBlank()) {
            "$fieldName is required"
        } else null
    }
}

// Network Extensions
fun <T> retrofit2.Response<T>.isSuccessful(): Boolean {
    return this.isSuccessful && this.body() != null
}

fun <T> retrofit2.Response<T>.getErrorMessage(): String {
    return try {
        this.errorBody()?.string() ?: "Unknown error occurred"
    } catch (e: Exception) {
        "Failed to parse error"
    }
}

