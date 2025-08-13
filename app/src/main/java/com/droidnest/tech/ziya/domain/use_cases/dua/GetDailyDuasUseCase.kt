package com.droidnest.tech.ziya.domain.use_cases.dua

import com.droidnest.tech.ziya.domain.model.Dua
import com.droidnest.tech.ziya.domain.repository.DuaRepository

// UseCase for getting daily duas
class GetDailyDuasUseCase(
    private val duaRepository: DuaRepository
) {
    suspend operator fun invoke(): List<Dua> {
        return duaRepository.getDailyDuas()
    }
}

// UseCase for getting namaz duas
class GetNamazDuasUseCase (
    private val duaRepository: DuaRepository
) {
    suspend operator fun invoke(): List<Dua> {
        return duaRepository.getNamazDuas()
    }
}
