package com.droidnest.tech.ziya.domain.repository

import com.droidnest.tech.ziya.domain.model.Hadith
import com.droidnest.tech.ziya.domain.model.PrayerTime

interface HomeRepository {
    suspend fun getPrayerTimes(district: String): PrayerTime?
    suspend fun getAllHadith(): List<Hadith>

}