package com.droidnest.tech.ziya.presentation.screens.welcome

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.droidnest.tech.ziya.presentation.screens.home.DivisionDistrictBottomSheet
import com.droidnest.tech.ziya.presentation.ui.theme.NotoSerifBengali
import com.droidnest.tech.ziya.presentation.ui.theme.LocalIslamicColors
import com.droidnest.tech.ziya.presentation.ui.theme.LocalDimensions
import com.droidnest.tech.ziya.R
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun WelcomeScreen(
    viewModel: WelcomeViewModel = hiltViewModel(),
    onContinue: () -> Unit,
) {
    val colors = LocalIslamicColors.current
    val dimensions = LocalDimensions.current

    val userName by viewModel.userName.collectAsState()
    val selectedLocation by viewModel.selectedDistrict.collectAsState()
    val configuration = LocalConfiguration.current

    var name by remember { mutableStateOf(userName ?: "") }
    var showBottomSheet by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf(selectedLocation) }

    // Animation states
    val infiniteTransition = rememberInfiniteTransition(label = "background_animation")
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset_animation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        colors.primaryGreen,
                        colors.darkGreen,
                        colors.background
                    ),
                    center = Offset(
                        configuration.screenWidthDp * 0.3f + cos(Math.toRadians(animatedOffset.toDouble())).toFloat() * 50,
                        configuration.screenHeightDp * 0.2f + sin(Math.toRadians(animatedOffset.toDouble())).toFloat() * 30
                    ),
                    radius = configuration.screenWidthDp * 1.2f
                )
            )
    ) {
        // Animated floating elements using theme dimensions
        repeat(6) { index ->
            AnimatedFloatingElement(
                modifier = Modifier
                    .offset(
                        x = (30 + index * (configuration.screenWidthDp / 6)).dp,
                        y = (80 + index * (configuration.screenHeightDp / 8)).dp
                    )
                    .size(dimensions.largeIconSize + (index * 4).dp),
                animationDelay = index * 300,
                colors = colors
            )
        }

        // Geometric patterns
        GeometricBackground(
            modifier = Modifier.fillMaxSize(),
            animatedOffset = animatedOffset,
            colors = colors,
            dimensions = dimensions
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = dimensions.extraLargePadding, vertical = dimensions.mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // Hero Section
            FuturisticWelcomeHero(
                colors = colors,
                dimensions = dimensions
            )

            Spacer(modifier = Modifier.height(dimensions.largePadding))

            // Input Cards Section
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensions.mediumPadding),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Name Input Card
                FuturisticInputCard(
                    icon = Icons.Default.Person,
                    label = stringResource(R.string.your_name),
                    value = name,
                    onValueChange = { name = it },
                    placeholder = stringResource(R.string.enter_your_name),
                    colors = colors,
                    dimensions = dimensions
                )

                // Location Selection Card
                FuturisticLocationCard(
                    selectedLocation = location,
                    onClick = { showBottomSheet = true },
                    colors = colors,
                    dimensions = dimensions
                )
            }

            Spacer(modifier = Modifier.height(dimensions.extraLargePadding))

            // CTA Button
            FuturisticButton(
                text = stringResource(R.string.begin_journey),
                enabled = name.isNotBlank() && location != null,
                onClick = {
                    if (name.isNotBlank() && location != null) {
                        viewModel.saveUserName(name)
                        viewModel.saveSelectedDistrict(location!!)
                        onContinue()
                    }
                },
                colors = colors,
                dimensions = dimensions
            )

            Spacer(modifier = Modifier.height(dimensions.mediumPadding))

            // Footer
            FuturisticFooterSection(
                colors = colors,
                dimensions = dimensions
            )

            // Bottom spacing
            Spacer(modifier = Modifier.height(dimensions.largePadding))
        }
    }

    if (showBottomSheet) {
        DivisionDistrictBottomSheet(
            onDismiss = { showBottomSheet = false },
            onDistrictSelected = { selectedLoc ->
                location = selectedLoc
                showBottomSheet = false
            }
        )
    }
}

@Composable
private fun AnimatedFloatingElement(
    modifier: Modifier = Modifier,
    animationDelay: Int = 0,
    colors: com.droidnest.tech.ziya.presentation.ui.theme.IslamicColors
) {
    val infiniteTransition = rememberInfiniteTransition(label = "floating_element")

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000 + animationDelay, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale_animation"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000 + animationDelay, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha_animation"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .clip(CircleShape)
            .background(colors.accentGold.copy(alpha = alpha))
            .blur(12.dp)
            .drawBehind {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            colors.primaryGreen.copy(alpha = alpha),
                            Color.Transparent
                        )
                    ),
                    radius = size.minDimension / 2
                )
            }
    )
}

@Composable
private fun GeometricBackground(
    modifier: Modifier = Modifier,
    animatedOffset: Float,
    colors: com.droidnest.tech.ziya.presentation.ui.theme.IslamicColors,
    dimensions: com.droidnest.tech.ziya.presentation.ui.theme.Dimensions
) {
    val density = LocalDensity.current.density

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Responsive line count and thickness based on density
        val lineCount = if (density < 2.0f) 2 else if (density < 3.0f) 3 else 4
        val strokeWidth = if (density < 2.0f) 1.dp.toPx() else 2.dp.toPx()

        // Draw animated lines
        repeat(lineCount) { i ->
            val startX = (width * 0.1f + i * width * 0.3f)
            val startY = height * 0.2f + sin(Math.toRadians((animatedOffset + i * 120).toDouble())).toFloat() * 50
            val endX = startX + width * 0.6f
            val endY = startY + height * 0.4f + cos(Math.toRadians((animatedOffset + i * 120).toDouble())).toFloat() * 30

            drawLine(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colors.accentGold.copy(alpha = 0.1f),
                        Color.Transparent
                    )
                ),
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = strokeWidth,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 20f))
            )
        }

        // Draw responsive glowing dots
        val dotCount = if (density < 2.0f) 3 else 5
        val dotRadius = dimensions.smallPadding.toPx()

        repeat(dotCount) { i ->
            val x = width * (0.2f + i * (0.6f / dotCount))
            val y = height * 0.8f + sin(Math.toRadians((animatedOffset + i * 72).toDouble())).toFloat() * 20

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        colors.primaryGreen.copy(alpha = 0.4f),
                        Color.Transparent
                    )
                ),
                radius = dotRadius,
                center = Offset(x, y)
            )
        }
    }
}

@Composable
private fun FuturisticWelcomeHero(
    colors: com.droidnest.tech.ziya.presentation.ui.theme.IslamicColors,
    dimensions: com.droidnest.tech.ziya.presentation.ui.theme.Dimensions
) {
    val infiniteTransition = rememberInfiniteTransition(label = "hero_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Main Title with glow effect
        Text(
            text = "আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহি ওয়া বারাকাতুহ",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
                fontSize = dimensions.extraLargeText,
                shadow = Shadow(
                    color = colors.accentGold.copy(alpha = 0.5f),
                    offset = Offset(0f, 0f),
                    blurRadius = 10f
                )
            ),
            textAlign = TextAlign.Center,
            fontFamily = NotoSerifBengali,
            modifier = Modifier
                .padding(horizontal = dimensions.mediumPadding, vertical = dimensions.mediumPadding)
        )

        Spacer(modifier = Modifier.height(dimensions.largePadding))

        // Futuristic Logo Container
        Box(
            modifier = Modifier
                .size(dimensions.largeIconSize * 4)
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            colors.primaryGreen,
                            colors.accentGold,
                            colors.darkGreen
                        )
                    ),
                    shape = CircleShape
                )
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            colors.cardBackground,
                            colors.cardBackground.copy(alpha = 0.8f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(R.string.app_logo_description),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(dimensions.extraLargePadding))

        // Description with futuristic styling
        Text(
            text = "Ziya - Light of Islam আপনার দৈনন্দিন ইসলামিক জীবনযাত্রার সঙ্গী। এখানে আপনি নামাজ, দোয়া, সূরা, তাসবিহসহ ইসলামের বিভিন্ন জ্ঞান সহজ ও আকর্ষণীয় উপস্থাপনায় পাবে। অ্যাপটি ব্যবহার করে নিজের বিশ্বাসকে আরও শক্তিশালী করো এবং প্রতিদিনের ইবাদতকে সুন্দর করো।",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal,
                fontSize = dimensions.mediumText,
                lineHeight = dimensions.largeText,
                color = colors.textSecondary
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensions.mediumPadding),
            fontFamily = NotoSerifBengali
        )
    }
}

@Composable
private fun FuturisticInputCard(
    icon: ImageVector,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    colors: com.droidnest.tech.ziya.presentation.ui.theme.IslamicColors,
    dimensions: com.droidnest.tech.ziya.presentation.ui.theme.Dimensions
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        colors.accentGold.copy(alpha = 0.5f),
                        colors.primaryGreen.copy(alpha = 0.3f),
                        colors.accentGold.copy(alpha = 0.5f)
                    )
                ),
                shape = RoundedCornerShape(dimensions.largeCornerRadius)
            ),
        shape = RoundedCornerShape(dimensions.largeCornerRadius),
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensions.cardElevation)
    ) {
        Column(
            modifier = Modifier.padding(dimensions.mediumPadding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(dimensions.largeIconSize)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(colors.primaryGreen, colors.darkGreen)
                            )
                        )
                        .border(
                            width = 1.dp,
                            color = colors.accentGold.copy(alpha = 0.5f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(dimensions.iconSize)
                    )
                }

                Spacer(modifier = Modifier.width(dimensions.mediumPadding))

                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = colors.textPrimary,
                        fontFamily = NotoSerifBengali,
                        fontSize = dimensions.largeText
                    )
                )
            }

            Spacer(modifier = Modifier.height(dimensions.mediumPadding))

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        placeholder,
                        color = colors.textSecondary.copy(alpha = 0.6f),
                        fontFamily = NotoSerifBengali,
                        fontSize = dimensions.mediumText
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(dimensions.cornerRadius),
                singleLine = true,
                textStyle = TextStyle(
                    fontFamily = NotoSerifBengali,
                    color = colors.textPrimary,
                    fontSize = dimensions.mediumText
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.accentGold,
                    unfocusedBorderColor = colors.primaryGreen.copy(alpha = 0.5f),
                    cursorColor = colors.accentGold,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
private fun FuturisticLocationCard(
    selectedLocation: String?,
    onClick: () -> Unit,
    colors: com.droidnest.tech.ziya.presentation.ui.theme.IslamicColors,
    dimensions: com.droidnest.tech.ziya.presentation.ui.theme.Dimensions
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        colors.accentGold.copy(alpha = 0.5f),
                        colors.primaryGreen.copy(alpha = 0.3f),
                        colors.accentGold.copy(alpha = 0.5f)
                    )
                ),
                shape = RoundedCornerShape(dimensions.largeCornerRadius)
            ),
        shape = RoundedCornerShape(dimensions.largeCornerRadius),
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensions.cardElevation)
    ) {
        Row(
            modifier = Modifier.padding(dimensions.mediumPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(dimensions.largeIconSize)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(colors.primaryGreen, colors.darkGreen)
                        )
                    )
                    .border(
                        width = 1.dp,
                        color = colors.accentGold.copy(alpha = 0.5f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(dimensions.iconSize)
                )
            }

            Spacer(modifier = Modifier.width(dimensions.mediumPadding))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.select_location),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = colors.textPrimary,
                        fontFamily = NotoSerifBengali,
                        fontSize = dimensions.largeText
                    )
                )

                if (selectedLocation != null) {
                    Text(
                        text = selectedLocation.replaceFirstChar { it.uppercaseChar() },
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = colors.accentGold,
                            fontWeight = FontWeight.Medium,
                            fontFamily = NotoSerifBengali,
                            fontSize = dimensions.mediumText
                        )
                    )
                } else {
                    Text(
                        text = "আপনার জেলা সিলেক্ট করতে ক্লিক করুন",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = colors.textSecondary.copy(alpha = 0.6f),
                            fontFamily = NotoSerifBengali,
                            fontSize = dimensions.smallText
                        )
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = colors.accentGold.copy(alpha = 0.7f),
                modifier = Modifier.size(dimensions.iconSize)
            )
        }
    }
}

@Composable
private fun FuturisticButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    colors: com.droidnest.tech.ziya.presentation.ui.theme.IslamicColors,
    dimensions: com.droidnest.tech.ziya.presentation.ui.theme.Dimensions
) {
    val infiniteTransition = rememberInfiniteTransition(label = "button_animation")
    val glowIntensity by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_intensity"
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .drawBehind {
                if (enabled) {
                    drawRoundRect(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                colors.accentGold.copy(alpha = glowIntensity * 0.4f),
                                Color.Transparent
                            ),
                            radius = size.width * 0.6f
                        ),
                        cornerRadius = CornerRadius(dimensions.largeCornerRadius.toPx())
                    )
                }
            }
            .border(
                width = if (enabled) 1.dp else 0.5.dp,
                brush = if (enabled) {
                    Brush.horizontalGradient(
                        listOf(colors.primaryGreen, colors.accentGold, colors.darkGreen)
                    )
                } else {
                    Brush.horizontalGradient(
                        listOf(
                            colors.textSecondary.copy(alpha = 0.3f),
                            colors.textSecondary.copy(alpha = 0.3f)
                        )
                    )
                },
                shape = RoundedCornerShape(dimensions.largeCornerRadius)
            ),
        shape = RoundedCornerShape(dimensions.largeCornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = if (enabled) {
                        Brush.horizontalGradient(
                            listOf(colors.primaryGreen, colors.darkGreen)
                        )
                    } else {
                        Brush.horizontalGradient(
                            colors = listOf(
                                colors.textSecondary.copy(alpha = 0.2f),
                                colors.textSecondary.copy(alpha = 0.2f)
                            )
                        )
                    },
                    shape = RoundedCornerShape(dimensions.largeCornerRadius)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (enabled) Color.White else colors.textSecondary.copy(alpha = 0.6f),
                    fontSize = dimensions.largeText,
                    fontFamily = NotoSerifBengali,
                    shadow = if (enabled) Shadow(
                        color = colors.accentGold.copy(alpha = 0.8f),
                        offset = Offset(0f, 0f),
                        blurRadius = 8f
                    ) else null
                )
            )
        }
    }
}

@Composable
private fun FuturisticFooterSection(
    colors: com.droidnest.tech.ziya.presentation.ui.theme.IslamicColors,
    dimensions: com.droidnest.tech.ziya.presentation.ui.theme.Dimensions
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.extraSmallPadding)
    ) {
        Text(
            text = stringResource(R.string.developed_by),
            style = MaterialTheme.typography.bodySmall.copy(
                color = colors.textSecondary.copy(alpha = 0.6f),
                fontSize = dimensions.extraSmallText
            )
        )
        Text(
            text = "Mohammad Asiful Islam",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = colors.textSecondary.copy(alpha = 0.9f),
                fontSize = dimensions.smallText
            )
        )
        Text(
            text = "DroidNest Tech",
            style = MaterialTheme.typography.bodySmall.copy(
                color = colors.accentGold.copy(alpha = 0.8f),
                fontSize = dimensions.extraSmallText,
                fontWeight = FontWeight.Medium
            )
        )
    }
}