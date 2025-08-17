package com.droidnest.tech.ziya.domain.use_cases.datastore

import com.droidnest.tech.ziya.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class SaveThemeUseCase(
    private val repository: PreferencesRepository
) {
    suspend operator fun invoke(mode: String) {
        return repository.saveThemeMode(mode = mode)
    }
}