package com.droidnest.tech.ziya.presentation.screens.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.droidnest.tech.ziya.domain.model.PrayerTime
import com.droidnest.tech.ziya.presentation.ui.theme.NotoSerifBengali
import com.droidnest.tech.ziya.presentation.ui.theme.LocalIslamicColors
import com.droidnest.tech.ziya.presentation.ui.theme.LocalDimensions
import com.droidnest.tech.ziya.util.*


@Composable
 fun PrayerTimeSection(prayerTimeState: PrayerTime?) {
    when (prayerTimeState) {
        null -> ShimmerPrayerCard()
        else -> PrayerTimesModernCard(prayerTime = prayerTimeState)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensions.mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = colors.primaryGreen,
                strokeWidth = 4.dp
            )

            Spacer(modifier = Modifier.height(dimensions.mediumPadding))

            Text(
                text = "নামাজের সময় লোড হচ্ছে...",
                fontSize = dimensions.mediumText,
                color = colors.textSecondary,
                fontFamily = NotoSerifBengali,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(dimensions.smallPadding))

            TextButton(
                onClick = { /* Add refresh logic */ },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = colors.primaryGreen
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "ডাটা পাওয়া যাচ্ছে না,\nদয়া করে আপডেট করুন😊",
                    fontSize = 22.sp,
                    color = colors.textPrimary,
                    fontFamily = NotoSerifBengali,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
