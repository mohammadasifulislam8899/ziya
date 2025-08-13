package com.droidnest.tech.ziya.domain.repository

import com.droidnest.tech.ziya.domain.model.Surah
import com.droidnest.tech.ziya.domain.model.SurahInfo

interface SurahRepository {
    suspend fun getAllSurahs(): List<SurahInfo>
    suspend fun getSurahByNumber(number: Int): Surah?

}