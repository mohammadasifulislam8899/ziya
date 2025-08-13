package com.droidnest.tech.ziya.data.repository

import android.util.Log
import com.droidnest.tech.ziya.data.local.AssetsLoader
import com.droidnest.tech.ziya.data.mappers.toDomain
import com.droidnest.tech.ziya.data.model.SurahDto
import com.droidnest.tech.ziya.data.model.SurahInfoDto
import com.droidnest.tech.ziya.domain.model.Surah
import com.droidnest.tech.ziya.domain.model.SurahInfo
import com.droidnest.tech.ziya.domain.repository.SurahRepository
import com.droidnest.tech.ziya.util.jsonParser
import kotlinx.serialization.SerializationException
import java.io.IOException
import javax.inject.Inject

class SurahRepositoryImpl @Inject constructor(
    private val assetsLoader: AssetsLoader
) : SurahRepository {

    override suspend fun getAllSurahs(): List<SurahInfo> {
        return try {
            val jsonString = assetsLoader.loadJsonFromAssets("surah_list.json")
            val surahInfoDtoList = jsonParser
                .decodeFromString<List<SurahInfoDto>>(jsonString)

            surahInfoDtoList.map { it.toDomain() }
        } catch (e: IOException) {
            emptyList()
        } catch (e: SerializationException) {
            emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getSurahByNumber(number: Int): Surah? {
        return try {
            val jsonString = assetsLoader.loadJsonFromAssets("surah_details.json")
            val surahList = jsonParser
                .decodeFromString<List<SurahDto>>(jsonString)

            surahList.find { it.number == number }?.toDomain()
        } catch (e: IOException) {
            null
        } catch (e: SerializationException) {
            null
        } catch (e: Exception) {
            null
        }
    }
}
