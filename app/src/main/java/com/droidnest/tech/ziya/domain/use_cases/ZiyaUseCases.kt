package com.droidnest.tech.ziya.domain.use_cases

import com.droidnest.tech.ziya.domain.use_cases.datastore.GetNameUseCase
import com.droidnest.tech.ziya.domain.use_cases.datastore.GetSelectedDistrictUseCase
import com.droidnest.tech.ziya.domain.use_cases.datastore.SaveNameUseCase
import com.droidnest.tech.ziya.domain.use_cases.datastore.SaveSelectedDistrictUseCase
import com.droidnest.tech.ziya.domain.use_cases.dua.GetDailyDuasUseCase
import com.droidnest.tech.ziya.domain.use_cases.dua.GetNamazDuasUseCase
import com.droidnest.tech.ziya.domain.use_cases.hadith.GetRandomHadithUseCase
import com.droidnest.tech.ziya.domain.use_cases.prayer_time.GetTodayPrayerTimeUseCase
import com.droidnest.tech.ziya.domain.use_cases.surah.GetSurahDetailsUseCase
import com.droidnest.tech.ziya.domain.use_cases.surah.GetSurahListUseCase

data class ZiyaUseCases(
    val todayPrayerTimeUseCase: GetTodayPrayerTimeUseCase,
    val saveDistrictUseCase: SaveSelectedDistrictUseCase,
    val readDistrictUseCase: GetSelectedDistrictUseCase,
    val saveNameUseCase: SaveNameUseCase,
    val readNameUseCase: GetNameUseCase,
    val getRandomHadithUseCase: GetRandomHadithUseCase,
    val getSurahListUseCase: GetSurahListUseCase,
    val getSurahDetailsUseCase: GetSurahDetailsUseCase,
    val getDailyDuasUseCase : GetDailyDuasUseCase,
    val getNamazDuasUseCase : GetNamazDuasUseCase,
    )
