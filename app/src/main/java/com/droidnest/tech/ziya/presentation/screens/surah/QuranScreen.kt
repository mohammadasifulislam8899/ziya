package com.droidnest.tech.ziya.presentation.screens.surah

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.droidnest.tech.ziya.presentation.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranScreen(
    viewModel: QuranViewModel = hiltViewModel(),
    onSurahClick: (Int) -> Unit
) {
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
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    // Islamic themed header
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
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
                                                    Color(islamicColors.primaryGreen.value).copy(alpha = 0.15f),
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
                                        islamicColors = islamicColors
                                    )
                                }
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
                                onSurahClick = onSurahClick,
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
                AnimatedVisibility(
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
        }
    }
}

@Composable
private fun IslamicSurahListItem(
    number: Int,
    nameBangla: String,
    nameArabic: String,
    ayahCount: Int,
    onSurahClick: (Int) -> Unit,
    islamicColors: IslamicColors,
    dimensions: Dimensions
) {
    val animatedVisibility by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMediumLow,
            visibilityThreshold = 0.01f
        ),
        label = "visibility"
    )

    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                alpha = animatedVisibility
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(
                    color = islamicColors.primaryGreen.copy(alpha = 0.15f),
                    bounded = true
                ),
                onClick = {
                    isPressed = true
                    onSurahClick(number)
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = islamicColors.cardBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensions.cardElevation,
            pressedElevation = dimensions.cardElevation * 3
        ),
        shape = RoundedCornerShape(dimensions.cornerRadius),
        border = BorderStroke(
            width = 1.dp,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    islamicColors.primaryGreen.copy(alpha = 0.1f),
                    islamicColors.accentGold.copy(alpha = 0.05f)
                )
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensions.mediumPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Islamic number display
            Box(
                modifier = Modifier.size(dimensions.largeIconSize * 2),
                contentAlignment = Alignment.Center
            ) {
                // Islamic gradient background
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                ) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(islamicColors.primaryGreen.value).copy(alpha = 0.12f),
                                Color(islamicColors.accentGold.value).copy(alpha = 0.08f),
                                Color.Transparent
                            )
                        )
                    )
                }

                // Islamic star shape
                IslamicStarShape(
                    modifier = Modifier.size(dimensions.largeIconSize * 1.5f),
                    strokeColor = islamicColors.primaryGreen,
                    strokeWidth = 2f,
                    islamicColors = islamicColors
                )

                Text(
                    text = number.toString(),
                    color = islamicColors.textPrimary,
                    fontSize = dimensions.mediumText,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontFamily = NotoSerifBengali
                )
            }

            Spacer(modifier = Modifier.width(dimensions.mediumPadding))

            // Text section with Islamic styling
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = nameBangla,
                    fontSize = dimensions.largeText,
                    fontWeight = FontWeight.SemiBold,
                    color = islamicColors.textPrimary,
                    fontFamily = NotoSerifBengali,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(dimensions.extraSmallPadding))

                Text(
                    text = nameArabic,
                    fontSize = dimensions.mediumText,
                    fontWeight = FontWeight.Normal,
                    color = islamicColors.textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(dimensions.smallPadding))

            // Islamic themed ayah count chip
            Surface(
                color = islamicColors.lightGold.copy(alpha = 0.6f),
                shape = RoundedCornerShape(dimensions.largeCornerRadius),
                border = BorderStroke(
                    1.dp,
                    islamicColors.accentGold.copy(alpha = 0.3f)
                ),
                modifier = Modifier.shadow(
                    dimensions.cardElevation / 2,
                    RoundedCornerShape(dimensions.largeCornerRadius)
                )
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = dimensions.smallPadding * 1.5f,
                        vertical = dimensions.extraSmallPadding * 1.5f
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensions.extraSmallPadding)
                ) {
                    Text(
                        text = ayahCount.toString(),
                        fontSize = dimensions.mediumText,
                        fontWeight = FontWeight.Bold,
                        color = islamicColors.primaryGreen,
                        fontFamily = NotoSerifBengali
                    )
                    Text(
                        text = "আয়াত",
                        fontSize = dimensions.smallText,
                        fontWeight = FontWeight.Medium,
                        color = islamicColors.primaryGreen,
                        fontFamily = NotoSerifBengali
                    )
                }
            }
        }
    }
}

@Composable
fun IslamicLoadingSpinner(
    modifier: Modifier = Modifier,
    islamicColors: IslamicColors
) {
    Canvas(modifier = modifier) {
        val path = Path()
        val midX = size.width / 2f
        val midY = size.height / 2f
        val outerRadius = size.minDimension / 2.5f
        val innerRadius = outerRadius / 2.8f
        val points = 8
        val angleStep = (Math.PI / points).toFloat()

        path.moveTo(midX, midY - outerRadius)

        for (i in 1 until points * 2) {
            val angle = i * angleStep
            val r = if (i % 2 == 0) outerRadius else innerRadius
            val x = midX + r * sin(angle)
            val y = midY - r * cos(angle)
            path.lineTo(x, y)
        }
        path.close()

        drawPath(
            path = path,
            brush = Brush.sweepGradient(
                colors = listOf(
                    Color.Transparent,
                    Color(islamicColors.primaryGreen.value).copy(alpha = 0.6f),
                    Color(islamicColors.accentGold.value).copy(alpha = 0.8f),
                    Color(islamicColors.richMaroon.value).copy(alpha = 0.4f),
                    Color.Transparent
                )
            ),
            style = Stroke(
                width = 3.dp.toPx(),
                cap = androidx.compose.ui.graphics.StrokeCap.Round
            )
        )
    }
}

@Composable
fun IslamicStarShape(
    modifier: Modifier = Modifier,
    strokeColor: Color,
    strokeWidth: Float = 4f,
    islamicColors: IslamicColors
) {
    Canvas(modifier = modifier) {
        val path = Path()
        val midX = size.width / 2f
        val midY = size.height / 2f
        val outerRadius = size.minDimension / 2.2f
        val innerRadius = outerRadius / 2.8f
        val points = 5
        val angleStep = (Math.PI / points).toFloat()

        // Start from top
        path.moveTo(midX, midY - outerRadius)

        for (i in 1 until points * 2) {
            val angle = i * angleStep
            val r = if (i % 2 == 0) outerRadius else innerRadius
            val x = midX + r * sin(angle)
            val y = midY - r * cos(angle)
            path.lineTo(x, y)
        }

        path.close()

        drawPath(
            path = path,
            color = strokeColor,
            style = Stroke(
                width = strokeWidth,
                cap = androidx.compose.ui.graphics.StrokeCap.Round,
                join = androidx.compose.ui.graphics.StrokeJoin.Round
            )
        )
    }
}