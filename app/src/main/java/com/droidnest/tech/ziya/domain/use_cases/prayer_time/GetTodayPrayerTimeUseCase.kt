package com.droidnest.tech.ziya.domain.use_cases.prayer_time

import com.droidnest.tech.ziya.domain.model.PrayerTime
import com.droidnest.tech.ziya.domain.repository.HomeRepository

class GetTodayPrayerTimeUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(district: String): PrayerTime? {
        return repository.getPrayerTimes(district)
    }
}
