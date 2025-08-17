package com.droidnest.tech.ziya.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.droidnest.tech.ziya.presentation.navigation.AppNavigation
import com.droidnest.tech.ziya.presentation.ui.theme.SharedViewModel
import com.droidnest.tech.ziya.presentation.ui.theme.ZiyaLightOfIslamTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            val sharedViewModel: SharedViewModel = hiltViewModel()
            val themeMode by sharedViewModel.themeMode.collectAsState()
            val isDark = when (themeMode) {
                "LIGHT" -> false
                "DARK" -> true
                else -> isSystemInDarkTheme()
            }

            ZiyaLightOfIslamTheme(
                darkTheme = isDark
            ) {
                AppNavigation()
            }

        }
    }
}