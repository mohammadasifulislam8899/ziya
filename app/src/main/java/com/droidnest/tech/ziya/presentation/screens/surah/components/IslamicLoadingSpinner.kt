package com.droidnest.tech.ziya.presentation.screens.surah.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.droidnest.tech.ziya.presentation.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin


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
                cap = StrokeCap.Round
            )
        )
    }
}

@Composable
fun IslamicStarShape(
    modifier: Modifier = Modifier,
    strokeColor: Color,
    strokeWidth: Float = 4f,
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
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}