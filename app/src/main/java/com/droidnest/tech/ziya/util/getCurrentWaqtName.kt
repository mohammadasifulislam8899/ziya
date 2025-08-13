// Add these functions to your util package or wherever you keep utility functions
package com.droidnest.tech.ziya.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import androidx.compose.runtime.*
import com.droidnest.tech.ziya.domain.model.PrayerTime
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Calculate seconds between two LocalDateTime objects
 */
@RequiresApi(Build.VERSION_CODES.O)
fun secondsBetween(start: LocalDateTime, end: LocalDateTime): Long {
    return ChronoUnit.SECONDS.between(start, end)
}

/**
 * Current time ticker that updates every second
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun currentTimeTicker(): State<LocalDateTime> {
    val currentTime = remember { mutableStateOf(LocalDateTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime.value = LocalDateTime.now()
            delay(1000) // Update every second
        }
    }

    return currentTime
}

/**
 * Get current waqt name based on prayer time and current time
 */
@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentWaqtName(prayerTime: PrayerTime, currentTime: LocalDateTime): String {
    val today = LocalDate.now()

    // Parse all prayer times
    val fajr = parseTimeSafe(prayerTime.fajr)?.let { LocalDateTime.of(today, it) }
    val sunrise = parseTimeSafe(prayerTime.sunrise)?.let { LocalDateTime.of(today, it) }
    val dhuhr = parseTimeSafe(prayerTime.dhuhr)?.let { LocalDateTime.of(today, it) }
    val asr = parseTimeSafe(prayerTime.asr)?.let { LocalDateTime.of(today, it) }
    val maghrib = parseTimeSafe(prayerTime.maghrib)?.let { LocalDateTime.of(today, it) }
    val isha = parseTimeSafe(prayerTime.isha)?.let { LocalDateTime.of(today, it) }

    return when {
        fajr != null && currentTime.isBefore(fajr) -> "ইশা"
        sunrise != null && currentTime.isBefore(sunrise) -> "ফজর"
        dhuhr != null && currentTime.isBefore(dhuhr) -> "দোহা"
        asr != null && currentTime.isBefore(asr) -> "যোহর"
        maghrib != null && currentTime.isBefore(maghrib) -> "আসর"
        isha != null && currentTime.isBefore(isha) -> "মাগরিব"
        else -> "ইশা"
    }
}

/**
 * Format date with Bengali day names
 */
@RequiresApi(Build.VERSION_CODES.O)
fun formatDateWithDay(date: LocalDate): String {
    val dayOfWeek = when (date.dayOfWeek.value) {
        1 -> "সোমবার"
        2 -> "মঙ্গলবার"
        3 -> "বুধবার"
        4 -> "বৃহস্পতিবার"
        5 -> "শুক্রবার"
        6 -> "শনিবার"
        7 -> "রবিবার"
        else -> ""
    }

    val monthName = when (date.monthValue) {
        1 -> "জানুয়ারি"
        2 -> "ফেব্রুয়ারি"
        3 -> "মার্চ"
        4 -> "এপ্রিল"
        5 -> "মে"
        6 -> "জুন"
        7 -> "জুলাই"
        8 -> "আগস্ট"
        9 -> "সেপ্টেম্বর"
        10 -> "অক্টোবর"
        11 -> "নভেম্বর"
        12 -> "ডিসেম্বর"
        else -> ""
    }

    val day = date.dayOfMonth.toString().toBanglaDigits()
    val year = date.year.toString().toBanglaDigits()

    return "$dayOfWeek, $day $monthName $year"
}

/**
 * Convert English digits to Bengali digits
 */
fun String.toBanglaDigits(): String {
    return this.replace('0', '০')
        .replace('1', '১')
        .replace('2', '২')
        .replace('3', '৩')
        .replace('4', '৪')
        .replace('5', '৫')
        .replace('6', '৬')
        .replace('7', '৭')
        .replace('8', '৮')
        .replace('9', '৯')
}

/**
 * Convert Bengali digits to English digits
 */
fun String.toBanglaToEnglishDigits(): String {
    return this.replace('০', '0')
        .replace('১', '1')
        .replace('২', '2')
        .replace('৩', '3')
        .replace('৪', '4')
        .replace('৫', '5')
        .replace('৬', '6')
        .replace('৭', '7')
        .replace('৮', '8')
        .replace('৯', '9')
}

/**
 * Parse time string safely handling Bengali digits and AM/PM
 */
@RequiresApi(Build.VERSION_CODES.O)
fun parseTimeSafe(time: String): LocalTime? {
    val cleaned = time
        .toBanglaToEnglishDigits() // বাংলা সংখ্যা → ইংরেজি
        .replace("am", "AM", ignoreCase = true)
        .replace("pm", "PM", ignoreCase = true)
        .trim()

    val formats = listOf(
        DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH),
        DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH),
        DateTimeFormatter.ofPattern("H:mm", Locale.ENGLISH),
        DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)
    )

    for (f in formats) {
        try {
            return LocalTime.parse(cleaned, f)
        } catch (_: Exception) {
            // Continue to next format
        }
    }
    return null
}