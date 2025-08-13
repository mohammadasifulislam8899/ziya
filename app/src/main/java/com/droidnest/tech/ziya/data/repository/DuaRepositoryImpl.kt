package com.droidnest.tech.ziya.data.repository

import com.droidnest.tech.ziya.data.local.AssetsLoader
import com.droidnest.tech.ziya.domain.model.Dua
import com.droidnest.tech.ziya.domain.model.DuaList
import com.droidnest.tech.ziya.domain.repository.DuaRepository
import com.droidnest.tech.ziya.util.jsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DuaRepositoryImpl @Inject constructor(
    private val assetsLoader: AssetsLoader
) : DuaRepository {

    suspend fun loadDuaListFromJson(): DuaList = withContext(Dispatchers.IO) {
        val jsonString = assetsLoader.loadJsonFromAssets("dua.json")
        jsonParser.decodeFromString<DuaList>(jsonString)
    }
    override suspend fun getDailyDuas(): List<Dua> {
        return loadDuaListFromJson().daily
    }

    override suspend fun getNamazDuas(): List<Dua> {
        return loadDuaListFromJson().namaz
    }
}
