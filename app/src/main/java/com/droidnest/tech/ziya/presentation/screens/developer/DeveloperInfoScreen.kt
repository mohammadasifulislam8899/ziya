package com.droidnest.tech.ziya.presentation.screens.developer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.droidnest.tech.ziya.R

// Theme-aware color scheme
@Composable
private fun getThemeColors(): ThemeColors {
    val isDark = isSystemInDarkTheme()
    return if (isDark) {
        ThemeColors(
            background = Color(0xFF0A0B0E),
            surface = Color.White.copy(alpha = 0.02f),
            surfaceVariant = Color.White.copy(alpha = 0.05f),
            onBackground = Color.White,
            onSurface = Color.White,
            onSurfaceVariant = Color.White.copy(alpha = 0.6f),
            primary = Color(0xFF00D4FF),
            secondary = Color.White.copy(alpha = 0.5f),
            accent = Color(0xFF00D4FF),
            glowStart = Color(0xFF00D4FF).copy(alpha = 0.3f),
            glowEnd = Color.Transparent,
            skillGradientStart = Color(0xFF00D4FF).copy(alpha = 0.1f),
            skillGradientEnd = Color(0xFF0099CC).copy(alpha = 0.05f)
        )
    } else {
        ThemeColors(
            background = Color(0xFFF8F9FA),
            surface = Color(0xFF000000).copy(alpha = 0.03f),
            surfaceVariant = Color(0xFF000000).copy(alpha = 0.08f),
            onBackground = Color(0xFF1A1B1E),
            onSurface = Color(0xFF1A1B1E),
            onSurfaceVariant = Color(0xFF1A1B1E).copy(alpha = 0.7f),
            primary = Color(0xFF0066CC),
            secondary = Color(0xFF1A1B1E).copy(alpha = 0.6f),
            accent = Color(0xFF0066CC),
            glowStart = Color(0xFF0066CC).copy(alpha = 0.2f),
            glowEnd = Color.Transparent,
            skillGradientStart = Color(0xFF0066CC).copy(alpha = 0.08f),
            skillGradientEnd = Color(0xFF004499).copy(alpha = 0.04f)
        )
    }
}

private data class ThemeColors(
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val onBackground: Color,
    val onSurface: Color,
    val onSurfaceVariant: Color,
    val primary: Color,
    val secondary: Color,
    val accent: Color,
    val glowStart: Color,
    val glowEnd: Color,
    val skillGradientStart: Color,
    val skillGradientEnd: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperInfoScreen(
    onBack: () -> Unit
) {
    val colors = getThemeColors()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .statusBarsPadding()
    ) {
        // Minimal Top Bar
        MinimalTopBar(
            onBack = onBack,
            colors = colors
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Profile Section
            ProfileSection(colors = colors)

            Spacer(modifier = Modifier.height(48.dp))

            // Info Cards
            InfoSection(colors = colors)

            Spacer(modifier = Modifier.height(48.dp))

            // Skills
            SkillsGrid(colors = colors)

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun MinimalTopBar(
    onBack: () -> Unit,
    colors: ThemeColors
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .size(44.dp)
                .background(
                    colors.surfaceVariant,
                    CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = colors.onBackground,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = stringResource(R.string.developer_info),
            style = MaterialTheme.typography.titleLarge.copy(
                color = colors.onBackground,
                fontWeight = FontWeight.Light,
                letterSpacing = 1.sp
            )
        )
    }
}

@Composable
private fun ProfileSection(colors: ThemeColors) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Image
        Box(
            modifier = Modifier.size(120.dp),
            contentAlignment = Alignment.Center
        ) {
            // Glow effect
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                colors.glowStart,
                                colors.glowEnd
                            )
                        ),
                        CircleShape
                    )
            )

            Image(
                painter = painterResource(id = R.drawable.dev_photo),
                contentDescription = "Developer Photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Name and title
        Text(
            text = "Mohammad Asiful Islam",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = colors.onBackground,
                fontWeight = FontWeight.Light,
                fontSize = 28.sp,
                letterSpacing = (-0.5).sp
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Chief Executive Officer",
            style = MaterialTheme.typography.titleMedium.copy(
                color = colors.accent,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                letterSpacing = 0.5.sp
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "DroidNest Tech",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = colors.onSurfaceVariant,
                fontWeight = FontWeight.Light,
                fontSize = 14.sp
            )
        )
    }
}

@Composable
private fun InfoSection(colors: ThemeColors) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        InfoCard(
            icon = Icons.Default.Email,
            title = "Email",
            value = "droidnesttech@gmail.com",
            colors = colors
        )

        InfoCard(
            icon = Icons.Default.Public,
            title = "Website",
            value = "www.droidnest.tech",
            colors = colors
        )

        InfoCard(
            icon = Icons.Default.LocationOn,
            title = "Location",
            value = "Chittagong, Bangladesh",
            colors = colors
        )
    }
}

@Composable
private fun InfoCard(
    icon: ImageVector,
    title: String,
    value: String,
    colors: ThemeColors
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                colors.surface,
                RoundedCornerShape(8.dp)
            )
            .clickable { }
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colors.accent,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = colors.secondary,
                    fontSize = 12.sp,
                    letterSpacing = 0.5.sp
                )
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = colors.onSurface,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp
                )
            )
        }
    }
}

@Composable
private fun SkillsGrid(colors: ThemeColors) {
    Column {
        Text(
            text = stringResource(R.string.expertise),
            style = MaterialTheme.typography.headlineSmall.copy(
                color = colors.onBackground,
                fontWeight = FontWeight.Light,
                fontSize = 24.sp,
                letterSpacing = 0.5.sp
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        val skills = listOf(
            "Android Development",
            "Kotlin & Java",
            "Jetpack Compose",
            "Firebase & Backend",
            "UI/UX Design",
            "Problem Solving"
        )

        // Two column grid
        for (i in skills.indices step 2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SkillChip(
                    skill = skills[i],
                    colors = colors,
                    modifier = Modifier.weight(1f)
                )

                if (i + 1 < skills.size) {
                    SkillChip(
                        skill = skills[i + 1],
                        colors = colors,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            if (i + 2 < skills.size) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun SkillChip(
    skill: String,
    colors: ThemeColors,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        colors.skillGradientStart,
                        colors.skillGradientEnd
                    )
                ),
                RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = skill,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = colors.onSurface.copy(alpha = 0.9f),
                fontWeight = FontWeight.Light,
                fontSize = 13.sp,
                letterSpacing = 0.3.sp
            ),
            textAlign = TextAlign.Center
        )
    }
}