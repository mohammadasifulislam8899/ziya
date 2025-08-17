package com.droidnest.tech.ziya.presentation.screens.home.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
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


@Composable
fun CircularPrayerTimer(
    modifier: Modifier = Modifier,
    prayerTime: PrayerTime
) {
    val now by currentTimeTicker()

    // Prayer time calculations
    val prayerTimeData = remember (prayerTime, now) {
        calculatePrayerTimeData(prayerTime, now)
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PrayerTimerHeader(
            waqtName = prayerTimeData.waqtName,
        )

        PrayerTimerCircle(
            progress = prayerTimeData.progress,
            timeUntilNext = prayerTimeData.timeUntilNext
        )
    }
}

@Composable
private fun PrayerTimerHeader(
    waqtName: String
) {
    val colors = LocalIslamicColors.current
    val dimensions = LocalDimensions.current

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
}

@Composable
private fun PrayerTimerCircle(
    progress: Float,
    timeUntilNext: String
) {
    val colors = LocalIslamicColors.current
    val dimensions = LocalDimensions.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .aspectRatio(1f)
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
            drawPrayerTimerArcs(
                animatedProgress = animatedProgress,
                glowAlpha = glowAlpha
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

// ========================= PRAYER TIMES LIST =========================

@Composable
fun PrayerTimesList(
    modifier: Modifier = Modifier,
    prayerTime: PrayerTime,
    currentWaqt: String
) {
    val prayers = remember(prayerTime) {
        listOf(
            PrayerInfo("ফজর", prayerTime.fajr, "🌅"),
            PrayerInfo("সূর্যোদয়", prayerTime.sunrise, "🌞"),
            PrayerInfo("যোহর", prayerTime.dhuhr, "☀️"),
            PrayerInfo("আসর", prayerTime.asr, "🌤️"),
            PrayerInfo("মাগরিব", prayerTime.maghrib, "🌇"),
            PrayerInfo("ইশা", prayerTime.isha, "🌙")
        )
    }

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

    val borderColor = if (isActive) colors.accentGold else Color.Transparent

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

private fun calculatePrayerTimeData(prayerTime: PrayerTime, now: LocalDateTime): PrayerTimeData {
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

    return PrayerTimeData(waqtName, progress, timeUntilNext)
}

private fun DrawScope.drawPrayerTimerArcs(
    animatedProgress: Float,
    glowAlpha: Float
) {
    val stroke = (10.dp).toPx()

    // Background arc
    drawArc(
        color = Color(0xFFE8F5E8), // softMint equivalent
        startAngle = -90f,
        sweepAngle = 360f,
        useCenter = false,
        style = Stroke(stroke, cap = StrokeCap.Round)
    )

    // Progress arc with gradient
    val gradient = Brush.sweepGradient(
        colors = listOf(
            Color(0xFF2E7D32), // primaryGreen equivalent
            Color(0xFFFFB300), // accentGold equivalent
            Color(0xFF2E7D32)  // primaryGreen equivalent
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
        color = Color(0xFFFFB300).copy(alpha = glowAlpha), // accentGold with alpha
        startAngle = -90f,
        sweepAngle = 360f * animatedProgress,
        useCenter = false,
        style = Stroke(
            stroke + 2.dp.toPx(),
            cap = StrokeCap.Round
        )
    )
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
data class PrayerInfo(val name: String, val time: String, val icon: String)

data class PrayerTimeData(
    val waqtName: String,
    val progress: Float,
    val timeUntilNext: String
)


