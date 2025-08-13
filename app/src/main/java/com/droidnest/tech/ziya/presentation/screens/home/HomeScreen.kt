package com.droidnest.tech.ziya.presentation.screens.home

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.droidnest.tech.ziya.domain.model.Hadith
import com.droidnest.tech.ziya.domain.model.PrayerTime
import com.droidnest.tech.ziya.presentation.ui.theme.NotoSerifBengali
import com.droidnest.tech.ziya.presentation.ui.theme.LocalIslamicColors
import com.droidnest.tech.ziya.presentation.ui.theme.LocalDimensions
import com.droidnest.tech.ziya.util.*
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.*
import com.droidnest.tech.ziya.R

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onTasbihClick: () -> Unit
) {
    val colors = LocalIslamicColors.current
    val dimensions = LocalDimensions.current
    val prayerTimeState = viewModel.todayPrayerTime.collectAsStateWithLifecycle()
    val selectedDistrict = viewModel.selectedDistrict.collectAsStateWithLifecycle()
    val hadithList = viewModel.hadithList.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = showBottomSheet,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        DivisionDistrictBottomSheet(
            onDismiss = { showBottomSheet = false },
            onDistrictSelected = { selected ->
                viewModel.saveDistrict(district = selected)
            },
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(dimensions.extraSmallPadding),
            verticalArrangement = Arrangement.spacedBy(dimensions.mediumPadding)
        ) {
            item {
                TasbihModernCard(onTasbihClick = onTasbihClick)
            }
            item {
                // Current Date with Islamic Calendar
                ModernDateHeader(
                    selectedDistrict = selectedDistrict.value,
                    onDistrictClick = { showBottomSheet = true }
                )
            }

            item {
                val prayerTime = prayerTimeState.value
                if (prayerTime != null) {
                    PrayerTimesModernCard(prayerTime = prayerTime)
                } else {
                    ShimmerPrayerCard()
                }
            }



            item {
                HadithModernSection(
                    hadithList = hadithList.value,
                    onRefresh = { viewModel.refreshHadith() }
                )
            }
        }
    }
}

@Composable
fun ModernDistrictSelector(
    modifier: Modifier = Modifier,
    selectedDistrict: String?,
    onClick: () -> Unit
) {
    val colors = LocalIslamicColors.current
    val dimensions = LocalDimensions.current

    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(dimensions.cornerRadius),
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensions.cardElevation)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensions.smallPadding,
                    vertical = dimensions.extraSmallPadding
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = colors.accentGold,
                modifier = Modifier.size(dimensions.iconSize)
            )

            Spacer(modifier = Modifier.width(dimensions.extraSmallPadding))

            Text(
                text = selectedDistrict?.takeIf { it.isNotBlank() }
                    ?.replaceFirstChar { it.uppercase() }
                    ?: stringResource(R.string.select_zella),
                fontSize = dimensions.smallText,
                fontWeight = FontWeight.SemiBold,
                color = colors.textPrimary,
                fontFamily = NotoSerifBengali
            )

            Spacer(modifier = Modifier.width(dimensions.extraSmallPadding))

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = colors.textSecondary,
                modifier = Modifier.size(dimensions.iconSize)
            )
        }
    }
}

@Composable
fun TasbihModernCard(onTasbihClick: () -> Unit) {
    val colors = LocalIslamicColors.current
    val dimensions = LocalDimensions.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.mediumPadding, vertical = dimensions.smallPadding)
            .clickable { onTasbihClick() },
        shape = RoundedCornerShape(dimensions.largeCornerRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensions.cardElevation)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp, max = 180.dp)
        ) {
            // Gradient Background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                colors.primaryGreen,
                                colors.darkGreen,
                                colors.accentGold.copy(alpha = 0.8f)
                            ),
                            start = Offset.Zero,
                            end = Offset.Infinite
                        )
                    )
            )

            // Islamic Pattern Overlay
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawIslamicGeometry(size, Color.White.copy(alpha = 0.1f))
            }

            // Content
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensions.mediumPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(2f),
                    verticalArrangement = Arrangement.spacedBy(dimensions.extraSmallPadding)
                ) {
                    Text(
                        text = " وَاذْكُرُوا اللَّهَ كَثِيرًا ",
                        fontSize = dimensions.smallText,
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = stringResource(R.string.start_tasbih_counter),
                        fontSize = dimensions.largeText,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = NotoSerifBengali
                    )

                    Text(
                        text = stringResource(R.string.remember_allah),
                        fontSize = dimensions.smallText,
                        color = Color.White.copy(alpha = 0.8f),
                        fontFamily = NotoSerifBengali
                    )

                    Button(
                        onClick = onTasbihClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White.copy(alpha = 0.2f)
                        ),
                        shape = RoundedCornerShape(dimensions.cornerRadius),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .heightIn(min = 32.dp, max = 40.dp)
                            .widthIn(max = 140.dp),
                        contentPadding = PaddingValues(
                            horizontal = dimensions.smallPadding,
                            vertical = dimensions.extraSmallPadding
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.get_start_now),
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = NotoSerifBengali,
                            fontSize = dimensions.smallText
                        )
                    }
                }

                // Floating Tasbih Icon
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📿",
                        fontSize = when {
                            dimensions.largeIconSize < 20.dp -> 40.sp
                            dimensions.largeIconSize < 25.dp -> 50.sp
                            else -> 60.sp
                        }
                    )
                }
            }
        }
    }
}

fun DrawScope.drawIslamicGeometry(size: Size, color: Color) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val radius = minOf(size.width, size.height) / 4

    // Draw geometric pattern
    for (i in 0..7) {
        val angle = (i * 45f) * (PI / 180f)
        val startX = centerX + (radius * cos(angle)).toFloat()
        val startY = centerY + (radius * sin(angle)).toFloat()
        val endX = centerX - (radius * cos(angle)).toFloat()
        val endY = centerY - (radius * sin(angle)).toFloat()

        drawLine(
            color = color,
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = 1.dp.toPx()
        )
    }
}

@Composable
fun PrayerTimesModernCard(prayerTime: PrayerTime) {
    val colors = LocalIslamicColors.current
    val dimensions = LocalDimensions.current
    val currentTime by currentTimeTicker()
    val currentWaqtName = getCurrentWaqtName(prayerTime, currentTime)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.mediumPadding),
        shape = RoundedCornerShape(dimensions.largeCornerRadius),
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensions.cardElevation)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensions.mediumPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularPrayerTimer(
                modifier = Modifier.weight(1f),
                prayerTime = prayerTime
            )
            PrayerTimesList(
                modifier = Modifier.weight(1f),
                prayerTime = prayerTime,
                currentWaqt = currentWaqtName
            )
        }
    }
}

@Composable
fun CircularPrayerTimer(
    modifier: Modifier = Modifier,
    prayerTime: PrayerTime
) {
    val colors = LocalIslamicColors.current
    val dimensions = LocalDimensions.current
    val now by currentTimeTicker()

    fun parseTimeSafe(time: String): LocalTime? {
        val cleaned = time
            .toBanglaToEnglishDigits()
            .replace("am", "AM", ignoreCase = true)
            .replace("pm", "PM", ignoreCase = true)
            .trim()

        val formats = listOf(
            DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("H:mm", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)
        )

        for (f in formats) {
            try {
                return LocalTime.parse(cleaned, f)
            } catch (_: Exception) {
            }
        }
        return null
    }

    val today = LocalDate.now()
    val fajr = parseTimeSafe(prayerTime.fajr)
    val sunrise = parseTimeSafe(prayerTime.sunrise)
    val dhuhr = parseTimeSafe(prayerTime.dhuhr)
    val asr = parseTimeSafe(prayerTime.asr)
    val maghrib = parseTimeSafe(prayerTime.maghrib)
    val isha = parseTimeSafe(prayerTime.isha)

    val fajrDT = fajr?.let { LocalDateTime.of(today, it) } ?: now
    val sunriseDT = sunrise?.let { LocalDateTime.of(today, it) } ?: now
    val dhuhrDT = dhuhr?.let { LocalDateTime.of(today, it) } ?: now
    val asrDT = asr?.let { LocalDateTime.of(today, it) } ?: now
    val maghribDT = maghrib?.let { LocalDateTime.of(today, it) } ?: now
    val ishaDT = isha?.let { LocalDateTime.of(today, it) } ?: now
    val nextFajrDT = fajrDT.plusDays(1)

    val (waqtName, start, end) = when {
        now.isBefore(fajrDT) -> Triple("ইশা ও তাহাজ্জুদ", ishaDT.minusDays(1), fajrDT)
        now.isBefore(sunriseDT) -> Triple("ফজর", fajrDT, sunriseDT)
        now.isBefore(dhuhrDT) -> Triple("সালাতুদ দুহা", sunriseDT, dhuhrDT)
        now.isBefore(asrDT) -> Triple("যোহর", dhuhrDT, asrDT)
        now.isBefore(maghribDT) -> Triple("আসর", asrDT, maghribDT)
        now.isBefore(ishaDT) -> Triple("মাগরিব", maghribDT, ishaDT)
        else -> Triple("ইশা ও তাহাজ্জুদ", ishaDT, nextFajrDT)
    }

    val totalSeconds = Duration.between(start, end).seconds.coerceAtLeast(1)
    val remainingSeconds = Duration.between(now, end).seconds.coerceAtLeast(0)

    val progress = (remainingSeconds.toFloat() / totalSeconds).coerceIn(0f, 1f)

    val timeUntilNext = String.format(
        Locale.getDefault(),
        "%02d:%02d:%02d",
        remainingSeconds / 3600,
        (remainingSeconds % 3600) / 60,
        remainingSeconds % 60
    )

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = waqtName,
            fontSize = dimensions.largeText,
            fontWeight = FontWeight.Bold,
            color = colors.primaryGreen,
            fontFamily = NotoSerifBengali
        )
        Text(
            text = "ওয়াক্ত শেষ হতে বাকি",
            fontSize = dimensions.mediumText,
            color = colors.textSecondary,
            fontFamily = NotoSerifBengali
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .aspectRatio(1f / 1f)
                .padding(dimensions.extraLargePadding)
        ) {
            val animatedProgress by animateFloatAsState(
                targetValue = progress,
                animationSpec = tween(
                    durationMillis = 800,
                    easing = FastOutSlowInEasing
                ),
                label = "progress_animation"
            )

            val glowAlpha by animateFloatAsState(
                targetValue = if (progress < 0.2f) 0.6f else 0.3f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "glow_animation"
            )

            Canvas(modifier = Modifier.fillMaxSize()) {
                val stroke = (dimensions.smallPadding + 2.dp).toPx()

                // Background arc
                drawArc(
                    color = colors.softMint,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(stroke, cap = StrokeCap.Round)
                )

                // Progress arc with gradient
                val gradient = Brush.sweepGradient(
                    colors = listOf(
                        colors.primaryGreen,
                        colors.accentGold,
                        colors.primaryGreen
                    )
                )

                drawArc(
                    brush = gradient,
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress,
                    useCenter = false,
                    style = Stroke(stroke, cap = StrokeCap.Round)
                )

                // Glowing effect
                drawArc(
                    color = colors.accentGold.copy(alpha = glowAlpha),
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress,
                    useCenter = false,
                    style = Stroke(stroke + dimensions.extraSmallPadding.toPx(), cap = StrokeCap.Round)
                )
            }

            Text(
                text = timeUntilNext,
                fontSize = dimensions.mediumText,
                fontWeight = FontWeight.SemiBold,
                color = colors.primaryGreen,
                fontFamily = NotoSerifBengali
            )
        }
    }
}

@Composable
fun PrayerTimesList(
    modifier: Modifier = Modifier,
    prayerTime: PrayerTime,
    currentWaqt: String
) {
    val prayers = listOf(
        PrayerInfo("ফজর", prayerTime.fajr, "🌅"),
        PrayerInfo("সূর্যোদয়", prayerTime.sunrise, "🌞"),
        PrayerInfo("যোহর", prayerTime.dhuhr, "☀️"),
        PrayerInfo("আসর", prayerTime.asr, "🌤️"),
        PrayerInfo("মাগরিব", prayerTime.maghrib, "🌇"),
        PrayerInfo("ইশা", prayerTime.isha, "🌙")
    )

    Column(modifier = modifier) {
        prayers.forEach { prayer ->
            ModernPrayerTimeRow(
                modifier = Modifier.fillMaxWidth(),
                prayer = prayer,
                isActive = prayer.name == currentWaqt
            )
        }
    }
}

data class PrayerInfo(val name: String, val time: String, val icon: String)

@Composable
fun ModernPrayerTimeRow(
    modifier: Modifier = Modifier,
    prayer: PrayerInfo,
    isActive: Boolean
) {
    val colors = LocalIslamicColors.current
    val dimensions = LocalDimensions.current

    val backgroundColor = if (isActive) {
        colors.primaryGreen.copy(alpha = 0.1f)
    } else {
        Color.Transparent
    }

    val borderColor = if (isActive) {
        colors.accentGold
    } else {
        Color.Transparent
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(dimensions.cornerRadius),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(
            width = if (isActive) 1.dp else 0.dp,
            color = borderColor
        )
    ) {
        Row(
            modifier = Modifier.padding(dimensions.smallPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = prayer.icon,
                fontSize = dimensions.mediumText
            )
            Spacer(modifier = Modifier.weight(.1f))
            Text(
                text = prayer.name,
                fontSize = dimensions.mediumText,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                color = if (isActive) colors.primaryGreen else colors.textPrimary,
                fontFamily = NotoSerifBengali,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = prayer.time,
                fontSize = dimensions.mediumText,
                fontWeight = FontWeight.SemiBold,
                color = colors.textSecondary,
            )
        }
    }
}

@Composable
fun ModernDateHeader(
    selectedDistrict: String?,
    onDistrictClick: () -> Unit
) {
    val colors = LocalIslamicColors.current
    val dimensions = LocalDimensions.current
    val today = LocalDate.now()
    val formattedDate = formatDateWithDay(today)

    Row(
        modifier = Modifier
            .padding(
                horizontal = dimensions.extraLargePadding,
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = formattedDate,
                fontSize = dimensions.mediumText,
                color = colors.textSecondary,
                fontFamily = NotoSerifBengali
            )

            Text(
                text = stringResource(R.string.namaz_time),
                fontSize = dimensions.extraLargeText,
                fontWeight = FontWeight.Bold,
                color = colors.primaryGreen,
                fontFamily = NotoSerifBengali
            )
        }

        ModernDistrictSelector(
            modifier = Modifier.weight(1f),
            selectedDistrict = selectedDistrict,
            onClick = onDistrictClick
        )
    }
}

@Composable
fun HadithModernSection(
    hadithList: List<Hadith>,
    onRefresh: () -> Unit
) {
    val colors = LocalIslamicColors.current
    val dimensions = LocalDimensions.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.mediumPadding)
    ) {
        // Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensions.smallPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.today_hadith),
                fontSize = dimensions.largeText,
                fontWeight = FontWeight.Bold,
                color = colors.primaryGreen,
                fontFamily = NotoSerifBengali
            )

            IconButton(
                onClick = onRefresh,
                modifier = Modifier
                    .padding(dimensions.mediumPadding)
                    .size(dimensions.iconSize)
                    .background(colors.softMint, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = stringResource(R.string.refresh_hadith),
                    tint = colors.primaryGreen
                )
            }
        }

        if (hadithList.isEmpty()) {
            ShimmerHadithCard()
        } else {
            hadithList.forEach { hadith ->
                ModernHadithCard(hadith = hadith)
                Spacer(modifier = Modifier.height(dimensions.smallPadding))
            }
        }
    }
}

@Composable
fun ModernHadithCard(hadith: Hadith) {
    val colors = LocalIslamicColors.current
    val dimensions = LocalDimensions.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensions.largeCornerRadius),
        colors = CardDefaults.cardColors(containerColor = colors.lightGold),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensions.cardElevation)
    ) {
        Column(
            modifier = Modifier.padding(dimensions.mediumPadding)
        ) {
            // Hadith Icon
            Box(
                modifier = Modifier
                    .size(dimensions.largeIconSize)
                    .background(colors.accentGold.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📖",
                    fontSize = dimensions.mediumText
                )
            }

            Spacer(modifier = Modifier.height(dimensions.smallPadding))

            // Bangla Text
            Text(
                text = hadith.bangla,
                fontSize = dimensions.largeText,
                color = colors.textPrimary,
                fontFamily = NotoSerifBengali
            )

            // Arabic Text
            Card(
                shape = RoundedCornerShape(dimensions.cornerRadius),
                colors = CardDefaults.cardColors(
                    containerColor = colors.cardBackground.copy(alpha = 0.7f)
                )
            ) {
                Text(
                    text = hadith.arabic,
                    fontSize = dimensions.mediumText,
                    color = colors.richMaroon,
                    textAlign = TextAlign.End,
                    fontFamily = FontFamily.Cursive,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = dimensions.mediumPadding,
                            vertical = dimensions.extraSmallPadding
                        )
                )
            }

            Spacer(modifier = Modifier.height(dimensions.smallPadding))

            // Divider
            HorizontalDivider(
                thickness = 1.dp,
                color = colors.accentGold.copy(alpha = 0.3f),
                modifier = Modifier.fillMaxWidth(0.7f)
            )

            // Reference and Category
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.reference, hadith.reference),
                    fontSize = dimensions.extraSmallText,
                    color = colors.textSecondary,
                    fontFamily = NotoSerifBengali
                )

                Card(
                    shape = RoundedCornerShape(dimensions.smallPadding),
                    colors = CardDefaults.cardColors(
                        containerColor = colors.primaryGreen.copy(alpha = 0.1f)
                    )
                ) {
                    Text(
                        text = hadith.category,
                        fontSize = dimensions.extraSmallText,
                        color = colors.primaryGreen,
                        fontFamily = NotoSerifBengali,
                        modifier = Modifier.padding(
                            horizontal = dimensions.smallPadding,
                            vertical = dimensions.extraSmallPadding
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ShimmerPrayerCard() {
    val colors = LocalIslamicColors.current
    val dimensions = LocalDimensions.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.extraLargePadding)
            .height((250 + dimensions.extraLargePadding.value * 2).dp),
        shape = RoundedCornerShape(dimensions.largeCornerRadius),
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shimmerEffect()
        )
    }
}

@Composable
fun ShimmerHadithCard() {
    val dimensions = LocalDimensions.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height((150 + dimensions.mediumPadding.value * 2).dp),
        shape = RoundedCornerShape(dimensions.largeCornerRadius)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shimmerEffect()
        )
    }
}

@Composable
fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember { mutableStateOf(Size.Zero) }
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")

    val transition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_transition"
    )

    this
        .onGloballyPositioned { coordinates ->
            size = coordinates.size.toSize()
        }
        .background(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFB0B0B0),
                    Color(0xFFC0C0C0),
                    Color(0xFFB0B0B0)
                ),
                start = Offset(transition * size.width - size.width, 0f),
                end = Offset(transition * size.width, size.height)
            )
        )
}