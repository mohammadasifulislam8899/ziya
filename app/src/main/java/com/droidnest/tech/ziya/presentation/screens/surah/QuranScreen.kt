package com.droidnest.tech.ziya.presentation.screens.surah


import android.app.Activity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.droidnest.tech.ziya.presentation.ui.theme.*
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import com.droidnest.tech.ziya.presentation.screens.surah.components.IslamicLoadingSpinner
import com.droidnest.tech.ziya.presentation.screens.surah.components.IslamicStarShape
import com.droidnest.tech.ziya.presentation.screens.surah.components.IslamicSurahListItem
import com.droidnest.tech.ziya.util.AdConstants
import com.droidnest.tech.ziya.util.BannerAdView
import com.droidnest.tech.ziya.util.InterstitialAdHelper
import kotlinx.coroutines.launch

@Composable
fun QuranScreen(
    viewModel: QuranViewModel = hiltViewModel(),
    onSurahClick: (Int) -> Unit,
) {

    val context = LocalContext.current
    val activity = context as Activity

// Load ad initially
    LaunchedEffect(Unit) {
        InterstitialAdHelper.loadAd(activity, AdConstants.INTERSTITIAL_AD_UNIT)
    }

    val surahs by viewModel.surahInfoList.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoadingSurahList.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var showScrollToTop by remember { mutableStateOf(false) }

    // Use Islamic theme colors and dimensions
    val islamicColors = LocalIslamicColors.current
    val dimensions = LocalDimensions.current

    // Monitor scroll position for FAB
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex > 3 }
            .collect { showScrollToTop = it }
    }

    // Filter surahs based on search query
    val filteredSurahs = remember(surahs, searchQuery) {
        if (searchQuery.isBlank()) {
            surahs
        } else {
            surahs.filter { surah ->
                surah.nameBangla.contains(searchQuery, ignoreCase = true) ||
                        surah.nameArabic.contains(searchQuery, ignoreCase = true) ||
                        surah.number.toString().contains(searchQuery)
            }
        }
    }

    when {
        isLoading -> {
            // Islamic themed loading state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                islamicColors.background,
                                islamicColors.softMint.copy(alpha = 0.2f),
                                islamicColors.background
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimensions.mediumPadding)
                ) {
                    val infiniteTransition = rememberInfiniteTransition(label = "loading")
                    val rotation by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 360f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(3000, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart
                        ),
                        label = "rotation"
                    )

                    val scale by infiniteTransition.animateFloat(
                        initialValue = 0.9f,
                        targetValue = 1.1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(2000, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "scale"
                    )

                    Box(
                        modifier = Modifier
                            .size(dimensions.largeIconSize * 4)
                            .graphicsLayer {
                                rotationZ = rotation
                                scaleX = scale
                                scaleY = scale
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        IslamicLoadingSpinner(
                            modifier = Modifier.fillMaxSize(),
                            islamicColors = islamicColors
                        )
                    }

                    Text(
                        text = "পবিত্র কুরআন লোড হচ্ছে...",
                        fontSize = dimensions.largeText,
                        fontWeight = FontWeight.Medium,
                        color = islamicColors.textPrimary,
                        fontFamily = NotoSerifBengali,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "অনুগ্রহ করে অপেক্ষা করুন",
                        fontSize = dimensions.mediumText,
                        color = islamicColors.textSecondary,
                        fontFamily = NotoSerifBengali
                    )
                }
            }
        }

        else -> {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        Column(
                            modifier = Modifier.padding(dimensions.mediumPadding)
                        ) {
                            // Islamic search bar
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(
                                        elevation = dimensions.cardElevation,
                                        shape = RoundedCornerShape(dimensions.largeCornerRadius)
                                    ),
                                placeholder = {
                                    Text(
                                        text = "সূরা খুঁজুন... (নাম, নম্বর বা আরবি)",
                                        fontFamily = NotoSerifBengali,
                                        fontSize = dimensions.mediumText,
                                        color = islamicColors.textSecondary
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search",
                                        modifier = Modifier.size(dimensions.iconSize),
                                        tint = islamicColors.primaryGreen
                                    )
                                },
                                trailingIcon = {
                                    AnimatedVisibility(
                                        visible = searchQuery.isNotEmpty(),
                                        enter = fadeIn() + scaleIn(),
                                        exit = fadeOut() + scaleOut()
                                    ) {
                                        IconButton(
                                            onClick = { searchQuery = "" }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = "Clear search",
                                                modifier = Modifier.size(dimensions.iconSize),
                                                tint = islamicColors.textSecondary
                                            )
                                        }
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(dimensions.largeCornerRadius),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = islamicColors.primaryGreen,
                                    unfocusedBorderColor = islamicColors.primaryGreen.copy(alpha = 0.3f),
                                    focusedContainerColor = islamicColors.cardBackground,
                                    unfocusedContainerColor = islamicColors.cardBackground,
                                    cursorColor = islamicColors.accentGold
                                ),
                                textStyle = LocalTextStyle.current.copy(
                                    fontSize = dimensions.mediumText,
                                    fontFamily = NotoSerifBengali
                                )
                            )

                            Spacer(modifier = Modifier.height(dimensions.mediumPadding))

                            // Islamic themed title section
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "পবিত্র কুরআন",
                                        fontSize = dimensions.extraLargeText,
                                        fontWeight = FontWeight.Bold,
                                        color = islamicColors.textPrimary,
                                        fontFamily = NotoSerifBengali
                                    )

                                    Text(
                                        text = "সূরাসমূহ • ${surahs.size} টি সূরা",
                                        fontSize = dimensions.mediumText,
                                        color = islamicColors.textSecondary,
                                        fontFamily = NotoSerifBengali
                                    )
                                }

                                // Islamic decorative pattern
                                Box(
                                    modifier = Modifier.size(dimensions.largeIconSize * 2),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        drawCircle(
                                            brush = Brush.radialGradient(
                                                colors = listOf(
                                                    Color(islamicColors.primaryGreen.value).copy(
                                                        alpha = 0.15f
                                                    ),
                                                    Color(islamicColors.accentGold.value).copy(alpha = 0.05f),
                                                    Color.Transparent
                                                )
                                            )
                                        )
                                    }

                                    IslamicStarShape(
                                        modifier = Modifier.size(dimensions.largeIconSize * 1.5f),
                                        strokeColor = islamicColors.primaryGreen.copy(alpha = 0.6f),
                                        strokeWidth = 2f,
                                    )
                                }
                            }
                        }


                        // Search results info
                        AnimatedVisibility(
                            visible = searchQuery.isNotEmpty(),
                            enter = fadeIn() + slideInVertically(),
                            exit = fadeOut() + slideOutVertically()
                        ) {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = islamicColors.lightGold.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(
                                    bottomStart = dimensions.cornerRadius,
                                    bottomEnd = dimensions.cornerRadius
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(dimensions.mediumPadding),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        modifier = Modifier.size(dimensions.iconSize),
                                        tint = islamicColors.primaryGreen
                                    )
                                    Spacer(modifier = Modifier.width(dimensions.smallPadding))
                                    Text(
                                        text = "${filteredSurahs.size} টি সূরা পাওয়া গেছে",
                                        fontSize = dimensions.mediumText,
                                        fontWeight = FontWeight.Medium,
                                        color = islamicColors.textPrimary,
                                        fontFamily = NotoSerifBengali
                                    )
                                }
                            }
                        }

                        // Islamic themed Surah list
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = dimensions.mediumPadding,
                                end = dimensions.mediumPadding,
                                bottom = 100.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(dimensions.smallPadding)
                        ) {
                            items(
                                items = filteredSurahs,
                                key = { it.number }
                            ) { surah ->
                                IslamicSurahListItem(
                                    number = surah.number,
                                    nameBangla = surah.nameBangla,
                                    nameArabic = surah.nameArabic,
                                    ayahCount = surah.ayahCount,
                                    onSurahClick = {
                                        InterstitialAdHelper.showAd(
                                            activity = activity,
                                            adUnitId = AdConstants.INTERSTITIAL_AD_UNIT
                                        ) {
                                            onSurahClick(surah.number)
                                        }
                                    },
                                    islamicColors = islamicColors,
                                    dimensions = dimensions
                                )
                            }

                            // Islamic themed empty state
                            if (filteredSurahs.isEmpty() && searchQuery.isNotEmpty()) {
                                item {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = dimensions.largePadding),
                                        colors = CardDefaults.cardColors(
                                            containerColor = islamicColors.richMaroon.copy(alpha = 0.08f)
                                        ),
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = islamicColors.richMaroon.copy(alpha = 0.2f)
                                        ),
                                        shape = RoundedCornerShape(dimensions.largeCornerRadius)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .padding(dimensions.largePadding)
                                                .fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(dimensions.mediumPadding)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Search,
                                                contentDescription = null,
                                                modifier = Modifier.size(dimensions.largeIconSize),
                                                tint = islamicColors.richMaroon.copy(alpha = 0.6f)
                                            )

                                            Text(
                                                text = "কোনো সূরা পাওয়া যায়নি",
                                                fontSize = dimensions.largeText,
                                                fontWeight = FontWeight.Bold,
                                                color = islamicColors.textPrimary,
                                                fontFamily = NotoSerifBengali,
                                                textAlign = TextAlign.Center
                                            )

                                            Text(
                                                text = "অন্য নাম বা নম্বর দিয়ে চেষ্টা করুন",
                                                fontSize = dimensions.mediumText,
                                                color = islamicColors.textSecondary,
                                                fontFamily = NotoSerifBengali,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Islamic themed FAB
                    this@Column.AnimatedVisibility(
                        visible = showScrollToTop,
                        enter = scaleIn(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMedium
                            )
                        ) + fadeIn(animationSpec = tween(200)),
                        exit = scaleOut(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessHigh
                            )
                        ) + fadeOut(animationSpec = tween(150)),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(dimensions.extraLargePadding)
                    ) {
                        FloatingActionButton(
                            onClick = {
                                coroutineScope.launch {
                                    listState.animateScrollToItem(
                                        index = 0,
                                        scrollOffset = 0
                                    )
                                }
                            },
                            modifier = Modifier.size(dimensions.largeIconSize * 2.5f),
                            containerColor = islamicColors.primaryGreen,
                            contentColor = islamicColors.lightGold,
                            elevation = FloatingActionButtonDefaults.elevation(
                                defaultElevation = dimensions.cardElevation * 2,
                                pressedElevation = dimensions.cardElevation * 4,
                                hoveredElevation = dimensions.cardElevation * 3
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp,
                                contentDescription = "Scroll to top",
                                modifier = Modifier.size(dimensions.iconSize)
                            )
                        }
                    }
                }
                // Banner ad bottom e
                BannerAdView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

        }
    }
}



