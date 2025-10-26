package com.pharmatech.morocco.core.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private val apiDateFormat = SimpleDateFormat(Constants.DATE_FORMAT_API, Locale.getDefault())
    private val displayDateFormat = SimpleDateFormat(Constants.DATE_FORMAT_DISPLAY, Locale.getDefault())
    private val displayTimeFormat = SimpleDateFormat(Constants.TIME_FORMAT_DISPLAY, Locale.getDefault())

    fun formatDateForDisplay(date: Date): String {
        return displayDateFormat.format(date)
    }

    fun formatTimeForDisplay(date: Date): String {
        return displayTimeFormat.format(date)
    }

    fun formatDateTimeForDisplay(date: Date): String {
        return "${formatDateForDisplay(date)} ${formatTimeForDisplay(date)}"
    }

    fun parseApiDate(dateString: String): Date? {
        return try {
            apiDateFormat.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }

    fun formatDateForApi(date: Date): String {
        return apiDateFormat.format(date)
    }

    fun isToday(date: Date): Boolean {
        val today = Calendar.getInstance()
        val checkDate = Calendar.getInstance().apply { time = date }
        return today.get(Calendar.YEAR) == checkDate.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == checkDate.get(Calendar.DAY_OF_YEAR)
    }

    fun isTomorrow(date: Date): Boolean {
        val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
        val checkDate = Calendar.getInstance().apply { time = date }
        return tomorrow.get(Calendar.YEAR) == checkDate.get(Calendar.YEAR) &&
                tomorrow.get(Calendar.DAY_OF_YEAR) == checkDate.get(Calendar.DAY_OF_YEAR)
    }

    fun getRelativeDateString(date: Date): String {
        return when {
            isToday(date) -> "Today"
            isTomorrow(date) -> "Tomorrow"
            else -> formatDateForDisplay(date)
        }
    }

    fun getDaysBetween(startDate: Date, endDate: Date): Int {
        val diff = endDate.time - startDate.time
        return (diff / (1000 * 60 * 60 * 24)).toInt()
    }
}

