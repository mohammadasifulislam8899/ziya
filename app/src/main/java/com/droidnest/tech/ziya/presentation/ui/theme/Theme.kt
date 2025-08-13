package com.droidnest.tech.ziya.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Responsive Dimensions based on Density
data class Dimensions(
    val extraSmallText: TextUnit,
    val smallText: TextUnit,
    val mediumText: TextUnit,
    val largeText: TextUnit,
    val extraLargeText: TextUnit,
    val extraSmallPadding: Dp,
    val smallPadding: Dp,
    val mediumPadding: Dp,
    val largePadding: Dp,
    val extraLargePadding: Dp,
    val cardElevation: Dp,
    val cornerRadius: Dp,
    val largeCornerRadius: Dp,
    val iconSize: Dp,
    val largeIconSize: Dp
)

@Composable
fun getDimensions(): Dimensions {
    val density = LocalDensity.current.density

    return when {
        density < 2.0f -> Dimensions(
            extraSmallText = 8.sp,
            smallText = 10.sp,
            mediumText = 12.sp,
            largeText = 14.sp,
            extraLargeText = 16.sp,
            extraSmallPadding = 2.dp,
            smallPadding = 4.dp,
            mediumPadding = 8.dp,
            largePadding = 12.dp,
            extraLargePadding = 16.dp,
            cardElevation = 6.dp,
            cornerRadius = 8.dp,
            largeCornerRadius = 16.dp,
            iconSize = 16.dp,
            largeIconSize = 20.dp
        )
        density < 3.0f -> Dimensions(
            extraSmallText = 10.sp,
            smallText = 12.sp,
            mediumText = 14.sp,
            largeText = 16.sp,
            extraLargeText = 18.sp,
            extraSmallPadding = 4.dp,
            smallPadding = 8.dp,
            mediumPadding = 12.dp,
            largePadding = 16.dp,
            extraLargePadding = 20.dp,
            cardElevation = 8.dp,
            cornerRadius = 12.dp,
            largeCornerRadius = 20.dp,
            iconSize = 18.dp,
            largeIconSize = 24.dp
        )
        else -> Dimensions(
            extraSmallText = 12.sp,
            smallText = 14.sp,
            mediumText = 16.sp,
            largeText = 18.sp,
            extraLargeText = 20.sp,
            extraSmallPadding = 6.dp,
            smallPadding = 12.dp,
            mediumPadding = 16.dp,
            largePadding = 20.dp,
            extraLargePadding = 24.dp,
            cardElevation = 12.dp,
            cornerRadius = 16.dp,
            largeCornerRadius = 24.dp,
            iconSize = 20.dp,
            largeIconSize = 28.dp
        )
    }
}

val LocalDimensions = compositionLocalOf {
    Dimensions(
        extraSmallText = 10.sp,
        smallText = 12.sp,
        mediumText = 14.sp,
        largeText = 16.sp,
        extraLargeText = 18.sp,
        extraSmallPadding = 4.dp,
        smallPadding = 8.dp,
        mediumPadding = 12.dp,
        largePadding = 16.dp,
        extraLargePadding = 20.dp,
        cardElevation = 8.dp,
        cornerRadius = 12.dp,
        largeCornerRadius = 20.dp,
        iconSize = 18.dp,
        largeIconSize = 24.dp
    )
}

// Enhanced Islamic Theme Colors
data class IslamicColors(
    val primaryGreen: Color,
    val accentGold: Color,
    val softMint: Color,
    val richMaroon: Color,
    val lightGold: Color,
    val darkGreen: Color,
    val cardBackground: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val background: Color,
    val surface: Color
)

object IslamicTheme {
    // Light Mode Colors
    val Light = IslamicColors(
        primaryGreen = Color(0xFF1B5E20),
        accentGold = Color(0xFFFFD700),
        softMint = Color(0xFFE8F5E8),
        richMaroon = Color(0xFF8E2A2A),
        lightGold = Color(0xFFFFF8DC),
        darkGreen = Color(0xFF2E7D32),
        cardBackground = Color(0xFFFAFAFA),
        textPrimary = Color(0xFF2C2C2C),
        textSecondary = Color(0xFF757575),
        background = Color(0xFFFFFFFF),
        surface = Color(0xFFF5F5F5)
    )

    // Dark Mode Colors
    val Dark = IslamicColors(
        primaryGreen = Color(0xFF4CAF50),
        accentGold = Color(0xFFFFC107),
        softMint = Color(0xFF2D4A2D),
        richMaroon = Color(0xFFE57373),
        lightGold = Color(0xFF3E3B2F),
        darkGreen = Color(0xFF81C784),
        cardBackground = Color(0xFF2C2C2C),
        textPrimary = Color(0xFFE0E0E0),
        textSecondary = Color(0xFFBDBDBD),
        background = Color(0xFF121212),
        surface = Color(0xFF1E1E1E)
    )
}

val LocalIslamicColors = compositionLocalOf { IslamicTheme.Light }

// Updated Islamic-themed Material3 Color Schemes
private val IslamicDarkColorScheme = darkColorScheme(
    primary = Color(0xFF4CAF50),        // Islamic Green
    onPrimary = Color(0xFF1B5E20),
    secondary = Color(0xFFFFC107),      // Gold Accent
    onSecondary = Color(0xFF3E3B2F),
    tertiary = Color(0xFFE57373),       // Rich Maroon
    onTertiary = Color(0xFF8E2A2A),
    background = Color(0xFF121212),
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFBDBDBD)
)

private val IslamicLightColorScheme = lightColorScheme(
    primary = Color(0xFF1B5E20),        // Islamic Green
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFFFFD700),      // Gold Accent
    onSecondary = Color(0xFF2C2C2C),
    tertiary = Color(0xFF8E2A2A),       // Rich Maroon
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF2C2C2C),
    surface = Color(0xFFF5F5F5),
    onSurface = Color(0xFF2C2C2C),
    surfaceVariant = Color(0xFFFAFAFA),
    onSurfaceVariant = Color(0xFF757575)
)

@Composable
fun ZiyaLightOfIslamTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Set to false to use Islamic colors by default
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> IslamicDarkColorScheme
        else -> IslamicLightColorScheme
    }

    val islamicColors = if (darkTheme) IslamicTheme.Dark else IslamicTheme.Light
    val dimensions = getDimensions()

    CompositionLocalProvider(
        LocalIslamicColors provides islamicColors,
        LocalDimensions provides dimensions
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}