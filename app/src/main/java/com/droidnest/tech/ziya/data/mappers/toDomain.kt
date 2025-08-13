// data/mapper/SurahMapper.kt
package com.droidnest.tech.ziya.data.mappers

import com.droidnest.tech.ziya.data.model.AyahDto
import com.droidnest.tech.ziya.data.model.SurahDto
import com.droidnest.tech.ziya.data.model.SurahInfoDto
import com.droidnest.tech.ziya.domain.model.Ayah
import com.droidnest.tech.ziya.domain.model.Surah
import com.droidnest.tech.ziya.domain.model.SurahInfo


fun SurahInfoDto.toDomain(): SurahInfo {
    return SurahInfo(
        number = number,
        nameArabic = nameArabic,
        nameBangla = nameBangla,
        ayahCount = ayahCount,
    )
}
fun AyahDto.toDomain(): Ayah = Ayah(
    arabic = this.arabic,
    banglaPronunciation = this.banglaPronunciation,
    banglaMeaning = this.banglaMeaning
)

// SurahDto থেকে Surah তে রূপান্তর
fun SurahDto.toDomain(): Surah = Surah(
    number = this.number,
    nameArabic = this.nameArabic,
    nameBangla = this.nameBangla,
    ayahCount = this.ayahCount,
    ayahs = this.ayahs.map { it.toDomain() }
)