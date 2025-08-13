package com.droidnest.tech.ziya.data.repository

import com.droidnest.tech.ziya.data.local.AssetsLoader
import com.droidnest.tech.ziya.data.mappers.toPrayerTime
import com.droidnest.tech.ziya.data.model.PrayerSchedule
import com.droidnest.tech.ziya.domain.model.Hadith
import com.droidnest.tech.ziya.domain.model.PrayerTime
import com.droidnest.tech.ziya.domain.repository.HomeRepository
import com.droidnest.tech.ziya.util.todayDate
import kotlinx.serialization.json.Json
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val assetsLoader: AssetsLoader
) : HomeRepository {

    override suspend fun getPrayerTimes(district: String): PrayerTime? {
        val todayDate = todayDate

        // Helper function to load and parse JSON
        suspend fun loadAndFindPrayerTime(fileName: String): PrayerTime? {
            return try {
                val json = assetsLoader.loadJsonFromAssets(fileName)
                val list = Json.decodeFromString<List<PrayerSchedule>>(json)
                if (list.isEmpty()) {
                    return null
                }
                val todayPrayerTime = list.find { it.date == todayDate }
                todayPrayerTime?.prayer_times?.toPrayerTime()
            } catch (e: Exception) {
                null
            }
        }
        // Try with district-specific file
        val prayerTime = loadAndFindPrayerTime("$district.json")

        return prayerTime
    }

    override suspend fun getAllHadith(): List<Hadith> {
        return try {
            val jsonString = assetsLoader.loadJsonFromAssets("hadith.json")

            val json = Json { ignoreUnknownKeys = true }
            val hadithList = json.decodeFromString<List<Hadith>>(jsonString)
            hadithList
        } catch (e: Exception) {
            emptyList()
        }
    }


}