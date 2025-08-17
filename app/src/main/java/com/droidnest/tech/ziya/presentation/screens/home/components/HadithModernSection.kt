package com.droidnest.tech.ziya.presentation.screens.home.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.droidnest.tech.ziya.domain.model.Hadith
import com.droidnest.tech.ziya.presentation.ui.theme.NotoSerifBengali
import com.droidnest.tech.ziya.presentation.ui.theme.LocalIslamicColors
import com.droidnest.tech.ziya.presentation.ui.theme.LocalDimensions
import com.droidnest.tech.ziya.R


@Composable
fun HadithModernSection(
    hadithList: List<Hadith>,
    onRefresh: () -> Unit
) {
    val dimensions = LocalDimensions.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.mediumPadding)
    ) {
        HadithSectionHeader(onRefresh = onRefresh)

        HadithContent(hadithList = hadithList)
    }
}

@Composable
private fun HadithSectionHeader(onRefresh: () -> Unit) {
    val colors = LocalIslamicColors.current
    val dimensions = LocalDimensions.current

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
}

@Composable
private fun HadithContent(hadithList: List<Hadith>) {
    val dimensions = LocalDimensions.current

    when {
        hadithList.isEmpty() -> ShimmerHadithCard()
        else -> {
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
            HadithIcon()

            Spacer(modifier = Modifier.height(dimensions.smallPadding))

            HadithTexts(hadith = hadith)

            Spacer(modifier = Modifier.height(dimensions.smallPadding))

            HadithDivider()

            HadithFooter(hadith = hadith)
        }
    }
}

@Composable
private fun HadithIcon() {
    val colors = LocalIslamicColors.current
    val dimensions = LocalDimensions.current

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
}

@Composable
private fun HadithTexts(hadith: Hadith) {
    val colors = LocalIslamicColors.current
    val dimensions = LocalDimensions.current

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
}

@Composable
private fun HadithDivider() {
    val colors = LocalIslamicColors.current

    HorizontalDivider(
        thickness = 1.dp,
        color = colors.accentGold.copy(alpha = 0.3f),
        modifier = Modifier.fillMaxWidth(0.7f)
    )
}

@Composable
private fun HadithFooter(hadith: Hadith) {
    val colors = LocalIslamicColors.current
    val dimensions = LocalDimensions.current

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
