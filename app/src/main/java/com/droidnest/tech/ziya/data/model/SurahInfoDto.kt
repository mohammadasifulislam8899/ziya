// data/remote/dto/SurahDto.kt
package com.droidnest.tech.ziya.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SurahInfoDto(
    val number: Int,
    val nameArabic: String,
    val nameBangla: String,
    val ayahCount: Int
)

