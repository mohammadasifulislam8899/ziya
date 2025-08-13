package com.droidnest.tech.ziya.data.local

import android.content.Context

class AssetsLoader(
    private val context: Context
) {
    fun loadJsonFromAssets(filename: String): String {
        return context.assets.open(filename)
            .bufferedReader().use { it.readText() }
    }
}