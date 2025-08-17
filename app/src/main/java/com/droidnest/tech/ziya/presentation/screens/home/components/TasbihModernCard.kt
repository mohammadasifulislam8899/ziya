package com.droidnest.tech.ziya.presentation.screens.home.components


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.droidnest.tech.ziya.presentation.ui.theme.NotoSerifBengali
import com.droidnest.tech.ziya.presentation.ui.theme.LocalIslamicColors
import com.droidnest.tech.ziya.presentation.ui.theme.LocalDimensions
import com.droidnest.tech.ziya.R


@Composable
fun TasbihModernCard(onTasbihClick: () -> Unit) {
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
            // Background with gradient and pattern
            TasbihCardBackground()

            // Content
            TasbihCardContent(onTasbihClick = onTasbihClick)
        }
    }
}

@Composable
private fun TasbihCardBackground() {
    val colors = LocalIslamicColors.current

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

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawIslamicGeometry(size, Color.White.copy(alpha = 0.1f))
    }
}

@Composable
private fun TasbihCardContent(onTasbihClick: () -> Unit) {
    val dimensions = LocalDimensions.current

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensions.mediumPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TasbihTextSection(
            modifier = Modifier.weight(2f),
            onTasbihClick = onTasbihClick
        )

        TasbihIconSection(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun TasbihTextSection(
    modifier: Modifier = Modifier,
    onTasbihClick: () -> Unit
) {
    val dimensions = LocalDimensions.current

    Column(
        modifier = modifier,
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

        TasbihStartButton(onClick = onTasbihClick)
    }
}

@Composable
private fun TasbihStartButton(onClick: () -> Unit) {
    val dimensions = LocalDimensions.current

    Button(
        onClick = onClick,
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

@Composable
private fun TasbihIconSection(modifier: Modifier = Modifier) {
    val dimensions = LocalDimensions.current

    Box(
        modifier = modifier.aspectRatio(1f),
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
