package com.droidnest.tech.ziya.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.droidnest.tech.ziya.analytics.AnalyticsLogger
import com.droidnest.tech.ziya.analytics.AnalyticsViewModel
import com.droidnest.tech.ziya.presentation.screens.developer.DeveloperInfoScreen
import com.droidnest.tech.ziya.presentation.screens.privacy_policy.PrivacyPolicyScreen
import com.droidnest.tech.ziya.presentation.screens.splash.ZiyaSplashScreen
import com.droidnest.tech.ziya.presentation.screens.surah.SurahDetailScreen
import com.droidnest.tech.ziya.presentation.screens.tasbih.TasbihScreen
import com.droidnest.tech.ziya.presentation.screens.welcome.WelcomeScreen
import com.droidnest.tech.ziya.util.toScreenName

@Composable
fun AppNavigation(
    analyticsLogger: AnalyticsLogger = hiltViewModel<AnalyticsViewModel>().analyticsLogger
) {
    val navController = rememberNavController()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    // Log app open when navigation starts
    LaunchedEffect(Unit) {
        analyticsLogger.logAppOpen()
    }

    LaunchedEffect(currentRoute) {
        currentRoute?.let { route ->
            val screenName = route.toScreenName()
            analyticsLogger.logScreenView(screenName)
        }
    }

    NavHost(
        navController = navController,
        startDestination = Splash.route
    ) {
        composable(Splash.route) {
            ZiyaSplashScreen(
                navController = navController
            )
        }

        composable(Welcome.route) {
            WelcomeScreen(
                onContinue = {
                    analyticsLogger.logUserEngagement("welcome", "continue_clicked")
                    navController.popBackStack()
                    navController.navigate(Main.route)
                }
            )
        }

        composable(Main.route) {
            MainScreen(
                onNavigateToTasbih = {
                    analyticsLogger.logUserEngagement("navigation", "tasbih_clicked")
                    navController.navigate(Tasbih.route)
                },
                onSurahClick = { surahNumber ->
                    analyticsLogger.logUserEngagement("navigation", "surah_clicked")
                    navController.navigate("${SurahDetails.route}/$surahNumber")
                },
                onNavigateToPrivacyPolicy = {
                    analyticsLogger.logUserEngagement("menu", "privacy_policy_clicked")
                    navController.navigate(PrivacyPolicy.route)
                },
                onNavigateToDeveloperInfo = {
                    analyticsLogger.logUserEngagement("menu", "developer_info_clicked")
                    navController.navigate(DeveloperScreen.route)
                }
            )
        }

        composable(Tasbih.route) {
            TasbihScreen(onBackClick = { navController.navigateUp() })
        }

        composable(
            route = "${SurahDetails.route}/{surahNumber}",
            arguments = listOf(navArgument("surahNumber") { type = NavType.IntType })
        ) { backStackEntry ->
            val surahNumber = backStackEntry.arguments?.getInt("surahNumber") ?: 0
            SurahDetailScreen(
                surahNumber = surahNumber,
                onBackClick = { navController.navigateUp() }
            )
        }

        composable(PrivacyPolicy.route) {
            PrivacyPolicyScreen(onBack = { navController.navigateUp() })
        }

        composable(DeveloperScreen.route) {
            DeveloperInfoScreen(onBack = { navController.navigateUp() })
        }
    }
}