package com.droidnest.tech.ziya.presentation.screens.surah

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.droidnest.tech.ziya.analytics.AnalyticsLogger
import com.droidnest.tech.ziya.analytics.AnalyticsViewModel
import com.droidnest.tech.ziya.domain.model.Surah
import com.droidnest.tech.ziya.presentation.ui.theme.NotoSerifBengali
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahDetailScreen(
    viewModel: QuranViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    surahNumber: Int,
    analyticsLogger: AnalyticsLogger = hiltViewModel<AnalyticsViewModel>().analyticsLogger
) {
    val surah by viewModel.surahDetails.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoadingSurahDetails.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    var showScrollToTop by remember { mutableStateOf(false) }

    // Track screen entry and surah details
    LaunchedEffect(surahNumber) {
        viewModel.loadSurahDetails(number = surahNumber)

        // Analytics: Log surah reading start
        analyticsLogger.logSurahRead(surahNumber, "Surah $surahNumber")
        analyticsLogger.logUserEngagement("surah_details", "surah_opened")
    }

    // Track reading time and scroll behavior
    LaunchedEffect(Unit) {
        var startTime = System.currentTimeMillis()

        // Log reading session duration every 30 seconds
        while (true) {
            delay(30000) // 30 seconds
            val readingTime = (System.currentTimeMillis() - startTime) / 1000
            if (readingTime > 0) {
                analyticsLogger.logUserEngagement(
                    "surah_reading",
                    "session_duration_${readingTime}s"
                )
            }
        }
    }

    // Show scroll to top button when scrolled down
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex > 5 }
            .collect {
                showScrollToTop = it
                // Analytics: Track deep scrolling
                if (it && listState.firstVisibleItemIndex > 10) {
                    analyticsLogger.logUserEngagement("surah_reading", "deep_scroll")
                }
            }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            EnhancedTopAppBar(
                surah = surah,
                onBackClick = {
                    // Analytics: Track back navigation with reading completion
                    val scrollProgress = if (surah.ayahs.isNotEmpty()) {
                        (listState.firstVisibleItemIndex.toFloat() / surah.ayahs.size * 100).toInt()
                    } else 0

                    analyticsLogger.logUserEngagement(
                        "surah_reading",
                        "back_clicked_at_${scrollProgress}%"
                    )
                    onBackClick()
                },
                isLoading = isLoading,
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showScrollToTop,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        // Analytics: Track scroll to top usage
                        analyticsLogger.logUserEngagement("surah_reading", "scroll_to_top")

                        kotlinx.coroutines.MainScope().launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    modifier = Modifier.size(48.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Scroll to top"
                    )
                }
            }
        }
    ) { innerPadding ->
        when {
            errorMessage != null -> {
                EnhancedErrorState(
                    errorMessage = errorMessage ?: "",
                    onRetry = {
                        // Analytics: Track retry attempts
                        analyticsLogger.logUserEngagement("surah_details", "retry_clicked")
                        viewModel.retrySurahDetails(surahNumber)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    analyticsLogger = analyticsLogger
                )
            }

            isLoading -> {
                EnhancedLoadingState(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            else -> {
                EnhancedSurahDetailContent(
                    surah = surah,
                    listState = listState,
                    modifier = Modifier.padding(innerPadding),
                    analyticsLogger = analyticsLogger
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedTopAppBar(
    surah: Surah,
    onBackClick: () -> Unit,
    isLoading: Boolean,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = if (isLoading) 0.dp else 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        TopAppBar(
            title = {
                AnimatedVisibility(
                    visible = !isLoading,
                    enter = fadeIn(animationSpec = tween(500)) + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "${surah.number}. ${surah.nameBangla}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = NotoSerifBengali,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = "${surah.nameArabic} • ${surah.ayahCount} আয়াত",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = NotoSerifBengali
                        )
                    }
                }
            },
            navigationIcon = {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )
    }
}

@Composable
fun EnhancedErrorState(
    errorMessage: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    analyticsLogger: AnalyticsLogger
) {
    // Analytics: Track error occurrence
    LaunchedEffect(errorMessage) {
        analyticsLogger.logUserEngagement("surah_details", "error_occurred")
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
            ),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.error.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                    )
                )
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                )

                Text(
                    text = "কিছু সমস্যা হয়েছে",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontFamily = NotoSerifBengali,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f),
                    fontFamily = NotoSerifBengali,
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "আবার চেষ্টা করুন",
                        fontFamily = NotoSerifBengali,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun EnhancedLoadingState(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp
            )

            Text(
                text = "সূরা লোড হচ্ছে...",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = alpha),
                fontFamily = NotoSerifBengali
            )
        }
    }
}

@Composable
fun EnhancedSurahDetailContent(
    surah: Surah,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    analyticsLogger: AnalyticsLogger
) {
    // Track reading progress
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                if (surah.ayahs.isNotEmpty() && index > 0) {
                    val progress = (index.toFloat() / surah.ayahs.size * 100).toInt()
                    // Log progress milestones
                    when (progress) {
                        in 25..29 -> analyticsLogger.logUserEngagement(
                            "surah_reading",
                            "25_percent_read"
                        )

                        in 50..54 -> analyticsLogger.logUserEngagement(
                            "surah_reading",
                            "50_percent_read"
                        )

                        in 75..79 -> analyticsLogger.logUserEngagement(
                            "surah_reading",
                            "75_percent_read"
                        )

                        in 95..100 -> analyticsLogger.logUserEngagement(
                            "surah_reading",
                            "completed"
                        )
                    }
                }
            }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                    )
                )
            )
    ) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 80.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Header Card with Bismillah
            item {
                if (surah.number != 1 && surah.number != 9) { // Except Al-Fatiha and At-Tawbah
                    BismillahCard(analyticsLogger)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            itemsIndexed(
                items = surah.ayahs,
                key = { index, _ -> index }
            ) { index, ayah ->
                EnhancedAyahItem(
                    ayahNumber = index + 1,
                    arabic = ayah.arabic,
                    meaning = ayah.banglaMeaning,
                    analyticsLogger = analyticsLogger,
                    surahNumber = surah.number
                )
            }
        }
    }
}

@Composable
fun BismillahCard(analyticsLogger: AnalyticsLogger) {
    LaunchedEffect(Unit) {
        // Track Bismillah viewing
        analyticsLogger.logUserEngagement("surah_reading", "bismillah_viewed")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                textDirection = TextDirection.Rtl
            ),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}

@Composable
fun EnhancedAyahItem(
    ayahNumber: Int,
    arabic: String,
    meaning: String,
    analyticsLogger: AnalyticsLogger,
    surahNumber: Int
) {
    val clipboardManager = LocalClipboardManager.current
    var showCopyFeedback by remember { mutableStateOf(false) }

    // Animation for the card entrance
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(ayahNumber * 50L) // Stagger animation
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(500)) + slideInVertically(
            initialOffsetY = { it / 2 },
            animationSpec = tween(500)
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 3.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Header with ayah number and actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Ayah number with enhanced star design
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(50))
                        ) {
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF4CAF50).copy(alpha = 0.15f),
                                        Color(0xFF2196F3).copy(alpha = 0.08f)
                                    )
                                )
                            )
                        }

//                        StarStrokeShape(
//                            modifier = Modifier.size(dimensions.starSize),
//                            strokeColor = MaterialTheme.colorScheme.primary,
//                            strokeWidth = 2.5f,
//                            dimensions = dimensions
//                        )
                        Text(
                            text = ayahNumber.toString(),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = NotoSerifBengali
                        )
                    }

                    // Action buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Copy button
                        IconButton(
                            onClick = {
                                clipboardManager.setText(
                                    AnnotatedString("$arabic\n\n$meaning")
                                )
                                showCopyFeedback = true

                                // Analytics: Track ayah copy
                                analyticsLogger.logUserEngagement("ayah_interaction", "copy_ayah")
                                analyticsLogger.logUserEngagement(
                                    "surah_$surahNumber",
                                    "ayah_${ayahNumber}_copied"
                                )
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy ayah",
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }


                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Arabic text with enhanced styling
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = arabic,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.sp,
                        lineHeight = 26.sp,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.End,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Divider
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Bengali meaning with better typography
                Text(
                    text = meaning,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        lineHeight = 26.sp,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 0.25.sp
                    ),
                    fontFamily = NotoSerifBengali,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    // Copy feedback
    LaunchedEffect(showCopyFeedback) {
        if (showCopyFeedback) {
            delay(2000)
            showCopyFeedback = false
        }
    }

    AnimatedVisibility(
        visible = showCopyFeedback,
        enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { -it })
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            color = MaterialTheme.colorScheme.inverseSurface,
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.inverseOnSurface,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "আয়াত কপি হয়েছে",
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = NotoSerifBengali
                )
            }
        }
    }
}