package com.droidnest.tech.ziya.util

fun String.toScreenName(): String {
    return when {
        startsWith("splash") -> "Splash Screen"
        startsWith("welcome") -> "Welcome Screen"
        startsWith("main") -> "Main Screen"
        startsWith("home") -> "Home Screen"
        startsWith("quran") -> "Quran Screen"
        startsWith("dua") -> "Dua Screen"
        startsWith("menu") -> "Menu Screen"
        startsWith("tasbih") -> "Tasbih Screen"
        startsWith("surahDetails") -> "Surah Details Screen"
        startsWith("privacyPolicy") -> "Privacy Policy Screen"
        startsWith("developerScreen") -> "Developer Info Screen"
        else -> "Unknown Screen"
    }
}