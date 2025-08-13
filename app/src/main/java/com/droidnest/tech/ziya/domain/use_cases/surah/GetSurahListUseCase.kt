package com.droidnest.tech.ziya.domain.use_cases.surah

import com.droidnest.tech.ziya.domain.model.SurahInfo
import com.droidnest.tech.ziya.domain.repository.SurahRepository

class GetSurahListUseCase(
    private val repository: SurahRepository
) {
    suspend operator fun invoke(): List<SurahInfo> {
        return repository.getAllSurahs()
    }
}