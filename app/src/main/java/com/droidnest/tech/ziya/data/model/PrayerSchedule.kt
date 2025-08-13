package com.droidnest.tech.ziya.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PrayerSchedule(
    val date: String,
    val prayer_times: PrayerTimes
)