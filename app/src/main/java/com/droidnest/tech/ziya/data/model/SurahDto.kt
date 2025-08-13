package com.droidnest.tech.ziya.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SurahDto(
    val number: Int,
    val nameArabic: String,
    val nameBangla: String,
    val ayahCount: Int,
    val ayahs: List<AyahDto>
)

@Serializable
data class AyahDto(
    val arabic: String,
    val banglaPronunciation: String,
    val banglaMeaning: String
)