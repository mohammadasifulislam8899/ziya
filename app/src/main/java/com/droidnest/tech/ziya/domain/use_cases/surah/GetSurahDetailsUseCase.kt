package com.droidnest.tech.ziya.domain.use_cases.surah

import com.droidnest.tech.ziya.domain.model.Surah
import com.droidnest.tech.ziya.domain.repository.SurahRepository

class GetSurahDetailsUseCase(
    private val repository: SurahRepository
) {
    suspend operator fun invoke(number: Int): Surah? {
        return repository.getSurahByNumber(number = number)
    }
}