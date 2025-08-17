@file:OptIn(ExperimentalMaterial3Api::class)
package com.droidnest.tech.ziya.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.droidnest.tech.ziya.presentation.screens.menu.MenuScreen
import com.droidnest.tech.ziya.presentation.screens.surah.QuranScreen
import com.droidnest.tech.ziya.presentation.ui.theme.NotoSerifBengali
import com.droidnest.tech.ziya.presentation.ui.theme.SharedViewModel
import com.droidnest.tech.ziya.util.toScreenName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToTasbih: () -> Unit,
    onSurahClick: (Int) -> Unit,
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToDeveloperInfo: () -> Unit,
    analyticsLogger: AnalyticsLogger = hiltViewModel<AnalyticsViewModel>().analyticsLogger,
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val currentRoute by navController.currentBackStackEntryAsState()
    val userName by sharedViewModel.name.collectAsState()
    var name by remember { mutableStateOf(userName) }

    // Update name when userName changes
    LaunchedEffect(userName) {
        name = userName
    }

    // Analytics tracking
    LaunchedEffect(currentRoute) {
        currentRoute?.destination?.route?.let {
            analyticsLogger.logScreenView(it.toScreenName())
        }
    }

    val bottomNavItems = remember {
        listOf(
            BottomNavItem.HomeObject,
            BottomNavItem.QuranObject,
            BottomNavItem.DuaObject,
            BottomNavItem.MenuObject
        )
    }


    Scaffold(
        topBar = {
            ModernTopBar(
                userName = userName,
            )
        },
        bottomBar = {
            PremiumNavigationBar(
                items = bottomNavItems,
                currentRoute = currentRoute?.destination?.route,
                onNavigate = { route ->
                    if (currentRoute?.destination?.route != route) {
                        // Analytics tracking
                        val tabName = when (route) {
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
    ) { innerPadding ->

        // Navigation Content
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
            composable(BottomNavItem.DuaObject.route) {
                DuaScreen()
            }
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
fun ModernTopBar(
    userName: String,
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "👋 আসসালামু আলাইকুম",
                    fontSize = 16.sp,
                    fontFamily = NotoSerifBengali,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = userName.ifEmpty { "Guest" },
                        fontSize = 18.sp,
                        fontFamily = NotoSerifBengali,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.95f)
        ),
        modifier = Modifier.shadow(
            elevation = 4.dp,
            spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        )
    )
}

@Composable
private fun PremiumNavigationBar(
    items: List<BottomNavItem>,
    currentRoute: String?,
    onNavigate: (String) -> Unit,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .shadow(
                elevation = 8.dp,
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        tonalElevation = 0.dp,
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 11.sp,
                        fontFamily = NotoSerifBengali,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = if (currentRoute == item.route) FontWeight.Medium else FontWeight.Normal
                    )
                },
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )
        }
    }
}

