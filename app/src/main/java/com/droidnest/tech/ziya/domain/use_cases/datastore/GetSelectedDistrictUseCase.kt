package com.droidnest.tech.ziya.domain.use_cases.datastore

import com.droidnest.tech.ziya.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetSelectedDistrictUseCase(
    private val repository: PreferencesRepository
) {
    operator fun invoke(): Flow<String?> {
        return repository.getSelectedDistrict()
    }
}
