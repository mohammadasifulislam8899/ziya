package com.droidnest.tech.ziya.presentation.screens.tasbih

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.droidnest.tech.ziya.analytics.AnalyticsLogger
import com.droidnest.tech.ziya.analytics.AnalyticsViewModel
import com.droidnest.tech.ziya.presentation.ui.theme.NotoSerifBengali
import com.droidnest.tech.ziya.presentation.ui.theme.LocalDimensions
import com.droidnest.tech.ziya.presentation.ui.theme.LocalIslamicColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasbihScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    analyticsLogger: AnalyticsLogger = hiltViewModel<AnalyticsViewModel>().analyticsLogger
) {
    // Access custom theme dimensions and colors
    val dimensions = LocalDimensions.current
    val islamicColors = LocalIslamicColors.current

    var count by rememberSaveable { mutableIntStateOf(0) }
    var selectedMaxCount by rememberSaveable { mutableIntStateOf(33) }
    var selectedPhraseIndex by rememberSaveable { mutableIntStateOf(-1) }
    val hapticFeedback = LocalHapticFeedback.current

    // Animation for counter ripple effect
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(100),
        label = "scale"
    )

    val angle = (count / selectedMaxCount.toFloat()) * 360f

    // Progress calculation
    val progress = count / selectedMaxCount.toFloat()
    val isComplete = count >= selectedMaxCount

    // Modern preset options
    val presetCounts = listOf(33, 99, 100, 500)

    // Islamic phrases with translations
    val phrases = listOf(
        PhraseItem("سُبْحَانَ اللّٰهِ", "সুবহানাল্লাহ", "আল্লাহ পবিত্র", 33),
        PhraseItem("الْحَمْدُ لِلّٰهِ", "আলহামদুলিল্লাহ", "সমস্ত প্রশংসা আল্লাহর", 33),
        PhraseItem("اللّٰهُ أَكْبَر", "আল্লাহু আকবার", "আল্লাহ সবচেয়ে মহান", 34),
        PhraseItem("لَا إِلٰهَ إِلَّا اللّٰهُ", "লা ইলাহা ইল্লাল্লাহ", "আল্লাহ ছাড়া কোন ইলাহ নেই", 100),
        PhraseItem("أَسْتَغْفِرُ اللّٰهَ", "আস্তাগফিরুল্লাহ", "আমি আল্লাহর নিকট ক্ষমা চাই", 100),
        PhraseItem("سُبْحَانَ اللّٰهِ وَبِحَمْدِهِ", "সুবহানাল্লাহি ওয়া বিহামদিহি", "আল্লাহ পবিত্র ও প্রশংসিত", 100)
    )

    // Analytics: Track tasbih completion
    LaunchedEffect(count) {
        if (count > 0 && count % 33 == 0) {
            val phraseType = if (selectedPhraseIndex >= 0) {
                phrases[selectedPhraseIndex].transliteration
            } else {
                "custom"
            }
            analyticsLogger.logTasbihCount(count, phraseType)
        }
    }

    // Analytics: Track when tasbih is completed
    LaunchedEffect(isComplete) {
        if (isComplete && count > 0) {
            val phraseType = if (selectedPhraseIndex >= 0) {
                phrases[selectedPhraseIndex].transliteration
            } else {
                "custom"
            }
            analyticsLogger.logUserEngagement("tasbih", "completed_session")
            analyticsLogger.logTasbihCount(selectedMaxCount, phraseType)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "ডিজিটাল তাসবিহ",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = dimensions.largeText,
                            color = islamicColors.textPrimary
                        ),
                        fontFamily = NotoSerifBengali
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            analyticsLogger.logUserEngagement("tasbih", "back_clicked")
                            onBackClick()
                        },
                        modifier = Modifier.padding(dimensions.extraSmallPadding)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = islamicColors.primaryGreen,
                            modifier = Modifier.size(dimensions.largeIconSize)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = islamicColors.surface
                )
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(islamicColors.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.largePadding),
            contentPadding = PaddingValues(dimensions.largePadding)
        ) {

            // Counter Circle Section
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimensions.mediumPadding)
                ) {
                    // Current Zikr Display
                    if (selectedPhraseIndex >= 0) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(vertical = dimensions.smallPadding),
                            colors = CardDefaults.cardColors(
                                containerColor = islamicColors.softMint
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                2.dp,
                                islamicColors.primaryGreen.copy(alpha = 0.8f)
                            ),
                            shape = RoundedCornerShape(dimensions.cornerRadius),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = dimensions.cardElevation
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(dimensions.mediumPadding),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(dimensions.extraSmallPadding)
                            ) {
                                Text(
                                    text = "বর্তমান জিকির:",
                                    fontSize = dimensions.smallText,
                                    color = islamicColors.primaryGreen,
                                    fontFamily = NotoSerifBengali,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = phrases[selectedPhraseIndex].arabic,
                                    fontSize = (dimensions.extraLargeText.value + 4).sp,
                                    fontWeight = FontWeight.Bold,
                                    color = islamicColors.textPrimary,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = phrases[selectedPhraseIndex].transliteration,
                                    fontSize = dimensions.mediumText,
                                    color = islamicColors.textSecondary,
                                    fontFamily = NotoSerifBengali,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    // Instruction text
                    Text(
                        text = "জিকির গণনা করতে ক্লিক করতে থাকুন",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = dimensions.mediumText,
                            color = islamicColors.textSecondary
                        ),
                        textAlign = TextAlign.Center,
                        fontFamily = NotoSerifBengali
                    )

                    // Main counter circle
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(200.dp)
                            .scale(scale)
                            .shadow(
                                elevation = if (isPressed) dimensions.cardElevation else dimensions.cardElevation + 4.dp,
                                shape = CircleShape,
                                ambientColor = islamicColors.primaryGreen.copy(alpha = 0.3f),
                                spotColor = islamicColors.accentGold.copy(alpha = 0.3f)
                            )
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        islamicColors.cardBackground,
                                        islamicColors.surface
                                    )
                                ),
                                shape = CircleShape
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                if (count < selectedMaxCount) {
                                    count++
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    isPressed = true

                                    if (count % 10 == 0) {
                                        analyticsLogger.logUserEngagement("tasbih", "milestone_$count")
                                    }
                                }
                            }
                    ) {
                        // Progress ring
                        Canvas(modifier = Modifier.size(180.dp)) {
                            // Background circle
                            drawArc(
                                color = islamicColors.textSecondary.copy(alpha = 0.2f),
                                startAngle = 0f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(width = 12.dp.toPx())
                            )

                            // Progress arc
                            drawArc(
                                brush = Brush.sweepGradient(
                                    colors = listOf(
                                        islamicColors.primaryGreen,
                                        islamicColors.accentGold,
                                        if (isComplete) islamicColors.darkGreen else islamicColors.primaryGreen
                                    )
                                ),
                                startAngle = -90f,
                                sweepAngle = angle,
                                useCenter = false,
                                style = Stroke(
                                    width = 12.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                            )
                        }

                        // Counter text
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$count",
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold,
                                color = islamicColors.textPrimary
                            )
                            Text(
                                text = "/ $selectedMaxCount",
                                fontSize = dimensions.mediumText,
                                color = islamicColors.textSecondary
                            )
                        }
                    }

                    // Progress bar
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .width(200.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(dimensions.extraSmallPadding)),
                        color = if (isComplete) islamicColors.darkGreen else islamicColors.primaryGreen,
                        trackColor = islamicColors.textSecondary.copy(alpha = 0.2f),
                    )

                    // Completion indicator
                    if (isComplete) {
                        Card(
                            modifier = Modifier.padding(dimensions.smallPadding),
                            colors = CardDefaults.cardColors(
                                containerColor = islamicColors.softMint
                            ),
                            shape = RoundedCornerShape(dimensions.largeCornerRadius)
                        ) {
                            Text(
                                text = "সম্পন্ন! ✓",
                                modifier = Modifier.padding(
                                    horizontal = dimensions.mediumPadding,
                                    vertical = dimensions.smallPadding
                                ),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontSize = dimensions.mediumText,
                                    color = islamicColors.darkGreen
                                ),
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = NotoSerifBengali
                            )
                        }
                    }
                }
            }

            // Preset Counter Options
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = islamicColors.cardBackground
                    ),
                    shape = RoundedCornerShape(dimensions.cornerRadius),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = dimensions.cardElevation
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(dimensions.largePadding),
                        verticalArrangement = Arrangement.spacedBy(dimensions.mediumPadding)
                    ) {
                        Text(
                            text = "কত বার জিকির করবেন ?",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = dimensions.largeText,
                                color = islamicColors.textPrimary
                            ),
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = NotoSerifBengali
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(
                                dimensions.extraSmallPadding,
                                alignment = Alignment.CenterHorizontally
                            )
                        ) {
                            presetCounts.forEach { preset ->
                                FilterChip(
                                    onClick = {
                                        analyticsLogger.logUserEngagement("tasbih", "preset_selected_$preset")
                                        selectedMaxCount = preset
                                        count = 0
                                    },
                                    label = {
                                        Text(
                                            text = "$preset",
                                            fontSize = dimensions.smallText,
                                            modifier = Modifier.fillMaxSize(),
                                            textAlign = TextAlign.Center,
                                            color = if (selectedMaxCount == preset)
                                                islamicColors.background else islamicColors.textPrimary
                                        )
                                    },
                                    selected = selectedMaxCount == preset,
                                    modifier = Modifier.weight(1f),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = islamicColors.primaryGreen,
                                        containerColor = islamicColors.surface,
                                        selectedLabelColor = islamicColors.background,
                                        labelColor = islamicColors.textPrimary
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Suggested Phrases
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = islamicColors.cardBackground
                    ),
                    shape = RoundedCornerShape(dimensions.cornerRadius),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = dimensions.cardElevation
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(dimensions.largePadding),
                        verticalArrangement = Arrangement.spacedBy(dimensions.mediumPadding)
                    ) {
                        Text(
                            text = "সাজেস্টেড জিকির",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = dimensions.largeText,
                                color = islamicColors.textPrimary
                            ),
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = NotoSerifBengali
                        )

                        phrases.forEachIndexed { index, phrase ->
                            PhraseCard(
                                phrase = phrase,
                                isSelected = selectedPhraseIndex == index,
                                islamicColors = islamicColors,
                                dimensions = dimensions,
                                onSelect = {
                                    analyticsLogger.logUserEngagement("tasbih", "phrase_selected")
                                    analyticsLogger.logUserPreference("selected_phrase", phrase.transliteration)

                                    selectedMaxCount = phrase.recommendedCount
                                    selectedPhraseIndex = index
                                    count = 0
                                }
                            )
                        }
                    }
                }
            }

            // Action Buttons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimensions.mediumPadding)
                ) {
                    OutlinedButton(
                        onClick = {
                            analyticsLogger.logUserEngagement("tasbih", "reset_clicked")
                            count = 0
                            selectedPhraseIndex = -1
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = islamicColors.primaryGreen
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            islamicColors.primaryGreen
                        ),
                        shape = RoundedCornerShape(dimensions.cornerRadius)
                    ) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(dimensions.iconSize),
                            tint = islamicColors.primaryGreen
                        )
                        Spacer(modifier = Modifier.width(dimensions.smallPadding))
                        Text(
                            "রিসেট করুন",
                            fontFamily = NotoSerifBengali,
                            fontSize = dimensions.mediumText
                        )
                    }
                }
            }
        }
    }

    // Reset pressed state
    LaunchedEffect(count) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}

data class PhraseItem(
    val arabic: String,
    val transliteration: String,
    val translation: String,
    val recommendedCount: Int
)

@Composable
fun PhraseCard(
    phrase: PhraseItem,
    isSelected: Boolean,
    islamicColors: com.droidnest.tech.ziya.presentation.ui.theme.IslamicColors,
    dimensions: com.droidnest.tech.ziya.presentation.ui.theme.Dimensions,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onSelect,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                islamicColors.softMint
            else
                islamicColors.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) dimensions.cardElevation else dimensions.cardElevation / 2
        ),
        border = if (isSelected)
            androidx.compose.foundation.BorderStroke(
                2.dp,
                islamicColors.primaryGreen.copy(alpha = 0.8f)
            )
        else null,
        shape = RoundedCornerShape(dimensions.cornerRadius)
    ) {
        Row(
            modifier = Modifier.padding(dimensions.mediumPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimensions.extraSmallPadding)
            ) {
                Text(
                    text = phrase.arabic,
                    fontSize = dimensions.extraLargeText,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected)
                        islamicColors.primaryGreen
                    else
                        islamicColors.textPrimary
                )
                Text(
                    text = phrase.transliteration,
                    fontSize = dimensions.mediumText,
                    fontWeight = FontWeight.Medium,
                    color = islamicColors.textSecondary,
                    fontFamily = NotoSerifBengali
                )
                Text(
                    text = "${phrase.translation} (${phrase.recommendedCount}x)",
                    fontSize = dimensions.smallText,
                    color = islamicColors.textSecondary,
                    fontFamily = NotoSerifBengali
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = islamicColors.primaryGreen,
                    modifier = Modifier.size(dimensions.largeIconSize)
                )
            }
        }
    }
}