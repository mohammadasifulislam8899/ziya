package com.droidnest.tech.ziya.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PrayerTimes(
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String
)