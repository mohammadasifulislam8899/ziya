package com.droidnest.tech.ziya.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject

class AnalyticsLogger @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) {

    fun logScreenView(screenName: String, screenClass: String = "MainActivity") {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
        }
    }

    // Additional useful events for your Islamic app
    fun logSurahRead(surahNumber: Int, surahName: String) {
        firebaseAnalytics.logEvent("surah_read") {
            param("surah_number", surahNumber.toLong())
            param("surah_name", surahName)
        }
    }

    fun logDuaRead(duaName: String, category: String = "") {
        firebaseAnalytics.logEvent("dua_read") {
            param("dua_name", duaName)
            if (category.isNotEmpty()) {
                param("dua_category", category)
            }
        }
    }

    fun logTasbihCount(count: Int, tasbihType: String = "default") {
        firebaseAnalytics.logEvent("tasbih_completed") {
            param("count", count.toLong())
            param("tasbih_type", tasbihType)
        }
    }

    fun logUserEngagement(feature: String, action: String) {
        firebaseAnalytics.logEvent("user_engagement") {
            param("feature", feature)
            param("action", action)
        }
    }

    fun logAppOpen() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null)
    }

    // For tracking user preferences
    fun logUserPreference(preferenceName: String, preferenceValue: String) {
        firebaseAnalytics.logEvent("user_preference") {
            param("preference_name", preferenceName)
            param("preference_value", preferenceValue)
        }
    }
}