package com.droidnest.tech.ziya.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    suspend fun saveSelectedDistrict(district: String)
    fun getSelectedDistrict(): Flow<String?>

    // নতুন: name সংরক্ষণের জন্য
    suspend fun saveUserName(name: String)
    fun getUserName(): Flow<String?>
}
