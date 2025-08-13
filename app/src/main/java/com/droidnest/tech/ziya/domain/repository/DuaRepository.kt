package com.droidnest.tech.ziya.domain.repository

import com.droidnest.tech.ziya.domain.model.Dua

interface DuaRepository {
    suspend fun getDailyDuas(): List<Dua>
    suspend fun getNamazDuas(): List<Dua>
}
