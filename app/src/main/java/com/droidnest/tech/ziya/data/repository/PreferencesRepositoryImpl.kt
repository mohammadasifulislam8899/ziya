package com.droidnest.tech.ziya.data.repository


import com.droidnest.tech.ziya.data.dataStore.PreferencesManager
import com.droidnest.tech.ziya.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class PreferencesRepositoryImpl(
    private val preferencesManager: PreferencesManager
) : PreferencesRepository {
    override suspend fun saveSelectedDistrict(district: String) {
        preferencesManager.saveSelectedDistrict(district)
    }

    override fun getSelectedDistrict(): Flow<String?> {
        return preferencesManager.getSelectedDistrict()
    }

    // নতুন যোগ করো:
    override suspend fun saveUserName(name: String) {
         preferencesManager.saveUserName(name)
    }

    override fun getUserName(): Flow<String?> {
        return preferencesManager.getUserName()
    }

    override suspend fun saveThemeMode(mode: String) {
        return preferencesManager.saveThemeMode(mode)
    }

    override fun getThemeMode(): Flow<String?> {
        return preferencesManager.getThemeMode()
    }
}
