package com.droidnest.tech.ziya.domain.model


data class Surah(
    val number: Int=0,
    val nameArabic: String="",
    val nameBangla: String="",
    val ayahCount: Int=0,
    val ayahs: List<Ayah> = emptyList()
)

data class Ayah(
    val arabic: String="",
    val banglaPronunciation: String="",
    val banglaMeaning: String=""
)
