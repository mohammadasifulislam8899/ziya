package com.droidnest.tech.ziya.presentation.screens.surah.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.droidnest.tech.ziya.presentation.ui.theme.*
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.graphics.graphicsLayer


@Composable
 fun IslamicSurahListItem(
    number: Int,
    nameBangla: String,
    nameArabic: String,
    ayahCount: Int,
    onSurahClick: (Int) -> Unit,
    islamicColors: IslamicColors,
    dimensions: Dimensions,
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
