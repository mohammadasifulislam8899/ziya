package com.droidnest.tech.ziya.data.mappers

import com.droidnest.tech.ziya.data.model.PrayerTimes
import com.droidnest.tech.ziya.domain.model.PrayerTime


fun PrayerTimes.toPrayerTime(): PrayerTime {
    return PrayerTime(
        fajr = fajr,
        sunrise = sunrise,
        dhuhr = dhuhr,
        asr = asr,
        maghrib = maghrib,
        isha = isha
    )
}