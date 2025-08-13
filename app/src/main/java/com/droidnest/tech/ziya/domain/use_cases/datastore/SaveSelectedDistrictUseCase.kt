package com.droidnest.tech.ziya.domain.use_cases.datastore

import com.droidnest.tech.ziya.domain.repository.PreferencesRepository


class SaveSelectedDistrictUseCase(
    private val repository: PreferencesRepository
) {
    suspend operator fun invoke(district: String) {
        repository.saveSelectedDistrict(district)
    }
}
