package com.droidnest.tech.ziya.data.dataStore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class PreferencesManager(private val context: Context) {

    companion object {
        val SELECTED_DISTRICT = stringPreferencesKey("selected_district")
        val USER_NAME = stringPreferencesKey("user_name")
        val THEME_MODE = stringPreferencesKey("theme_mode") // "LIGHT", "DARK", "SYSTEM"
    }

    // Save selected district
    suspend fun saveSelectedDistrict(district: String) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_DISTRICT] = district
        }
    }

    // Save user name
    suspend fun saveUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = name
        }
    }

    // Save Theme Mode
    suspend fun saveThemeMode(mode: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE] = mode
        }
    }

    // Get selected district as Flow
    fun getSelectedDistrict(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[SELECTED_DISTRICT]
        }
    }

    // Get user name as Flow
    fun getUserName(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_NAME]
        }
    }

    // Get Theme Mode
    fun getThemeMode(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[THEME_MODE] ?: "SYSTEM"
        }
    }
}
