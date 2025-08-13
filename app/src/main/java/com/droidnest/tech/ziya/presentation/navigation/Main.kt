package com.droidnest.tech.ziya.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object Welcome {
    const val route = "welcome"
}
@Serializable
object Splash {
    const val route = "splash"
}
@Serializable
object Main {
    const val route = "main"
}

@Serializable
object Tasbih {
    const val route = "tasbih"
}

@Serializable
object PrivacyPolicy {
    const val route = "privacyPolicy"
}

@Serializable
object DeveloperScreen {
    const val route = "developerScreen"
}

@Serializable
data class SurahDetails(val surahNumber: Int) {
    companion object {
        const val route = "surahDetails"
    }
}
