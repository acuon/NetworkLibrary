package com.acuon.networklibrary.utils.extensions

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

object DateFormats {
    const val SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    const val PRETTY_DATE_FORMAT = "MMM. dd, yyyy"
    const val PRETTY_DATE_FORMAT2 = "MMM. yyyy"
    const val DATE_FORMAT = "yyyy-MM-dd"
}

fun String?.serverToPrettyDate(
    currentFormat: String,
    targetFormat: String = ""
): String {
    if (this.isNullOrEmpty()) {
        return ""
    }

    val inputFormat = SimpleDateFormat(currentFormat, Locale.getDefault())
    val outputFormat = SimpleDateFormat(targetFormat, Locale.getDefault())

    return try {
        val date = inputFormat.parse(this)
        outputFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun Long.toMinutes(): Long = TimeUnit.MILLISECONDS.toMinutes(this)