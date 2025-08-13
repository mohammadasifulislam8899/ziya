package com.droidnest.tech.ziya.presentation.navigation


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.*
import com.droidnest.tech.ziya.analytics.AnalyticsLogger
import com.droidnest.tech.ziya.analytics.AnalyticsViewModel
import com.droidnest.tech.ziya.presentation.screens.dua.DuaScreen
import com.droidnest.tech.ziya.presentation.screens.home.HomeScreen
import com.droidnest.tech.ziya.presentation.screens.home.HomeViewModel
import com.droidnest.tech.ziya.presentation.screens.menu.MenuScreen
import com.droidnest.tech.ziya.presentation.screens.surah.QuranScreen
import com.droidnest.tech.ziya.presentation.ui.theme.NotoSerifBengali
import com.droidnest.tech.ziya.util.toScreenName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithBottomNav(
    onNavigateToTasbih: () -> Unit,
    onSurahClick: (Int) -> Unit,
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToDeveloperInfo: () -> Unit,
    analyticsLogger: AnalyticsLogger = hiltViewModel<AnalyticsViewModel>().analyticsLogger,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val userName by homeViewModel.name.collectAsState()

    LaunchedEffect(currentRoute) {
        currentRoute?.let { analyticsLogger.logScreenView(it.toScreenName()) }
    }

    val items = listOf(
        BottomNavItem.HomeObject,
        BottomNavItem.QuranObject,
        BottomNavItem.DuaObject,
        BottomNavItem.MenuObject
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "\uD83D\uDC4B আসসালামু আলাইকুম",
                            fontSize = 16.sp,
                            fontFamily = NotoSerifBengali
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null)
                            Text(
                                text = " $userName",
                                fontSize = 20.sp,
                                fontFamily = NotoSerifBengali,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                modifier = Modifier.shadow(10.dp)
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) + fadeOut()
            ) {
                PremiumNavigationBar(
                    items = items,
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        if (currentRoute != route) {
                            // Log bottom navigation clicks
                            val tabName = when(route) {
                                BottomNavItem.HomeObject.route -> "home_tab"
                                BottomNavItem.QuranObject.route -> "quran_tab"
                                BottomNavItem.DuaObject.route -> "dua_tab"
                                BottomNavItem.MenuObject.route -> "menu_tab"
                                else -> "unknown_tab"
                            }
                            analyticsLogger.logUserEngagement("bottom_navigation", tabName)

                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.HomeObject.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.HomeObject.route) {
                HomeScreen(onTasbihClick = onNavigateToTasbih)
            }
            composable(BottomNavItem.QuranObject.route) {
                QuranScreen(onSurahClick = onSurahClick)
            }
            composable(BottomNavItem.DuaObject.route) { DuaScreen() }
            composable(BottomNavItem.MenuObject.route) {
                MenuScreen(
                    onNavigateToPrivacyPolicy = onNavigateToPrivacyPolicy,
                    onNavigateToDeveloperInfo = onNavigateToDeveloperInfo,
                )
            }
        }
    }
}

@Composable
private fun PremiumNavigationBar(
    items: List<BottomNavItem>,
    currentRoute: String?,
    onNavigate: (String) -> Unit,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.shadow(10.dp),
        tonalElevation = 8.dp,
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier.size( 24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize =  12.sp,
                        fontFamily = NotoSerifBengali,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )
        }
    }
}
