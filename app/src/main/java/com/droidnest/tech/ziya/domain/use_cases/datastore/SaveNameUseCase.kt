package com.droidnest.tech.ziya.domain.use_cases.datastore

import com.droidnest.tech.ziya.domain.repository.PreferencesRepository

class SaveNameUseCase(
    private val repository: PreferencesRepository
) {
    suspend operator fun invoke(name: String) {
        repository.saveUserName(name = name)
    }
}
