package com.droidnest.tech.ziya.domain.use_cases.hadith

import com.droidnest.tech.ziya.domain.model.Hadith
import com.droidnest.tech.ziya.domain.repository.HomeRepository

class GetRandomHadithUseCase(
private val repository: HomeRepository
) {
    suspend operator fun invoke(): List<Hadith> {
        return repository.getAllHadith().shuffled().take(3)
    }
}
