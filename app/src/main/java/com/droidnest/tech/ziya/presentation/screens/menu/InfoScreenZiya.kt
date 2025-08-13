package com.droidnest.tech.ziya.presentation.screens.menu

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.text.font.FontFamily
import com.droidnest.tech.ziya.presentation.ui.theme.NotoSerifBengali
import com.droidnest.tech.ziya.presentation.ui.theme.LocalDimensions
import com.droidnest.tech.ziya.presentation.ui.theme.LocalIslamicColors
import com.droidnest.tech.ziya.R

@Composable
fun MenuScreen(
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToDeveloperInfo: () -> Unit
) {
    // Access Islamic theme
    val dimensions = LocalDimensions.current
    val islamicColors = LocalIslamicColors.current

    var selectedInfo by remember { mutableStateOf<MenuInfo?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        islamicColors.background,
                        islamicColors.surface,
                        islamicColors.softMint.copy(alpha = 0.3f)
                    ),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
    ) {
        // Islamic ambient background elements
        IslamicAmbientBackground(islamicColors = islamicColors)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensions.largePadding),
            verticalArrangement = Arrangement.spacedBy(dimensions.largePadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Hero Header
            item {
                IslamicHeroHeader(
                    islamicColors = islamicColors,
                    dimensions = dimensions
                )
            }

            // Main Menu Cards
            item {
                MenuCardsSection(
                    islamicColors = islamicColors,
                    dimensions = dimensions,
                    onItemClick = { menuItem ->
                        when (menuItem.action) {
                            MenuAction.SHOW_DIALOG -> selectedInfo = menuItem
                            MenuAction.PRIVACY_POLICY -> onNavigateToPrivacyPolicy()
                            MenuAction.DEVELOPER_INFO -> onNavigateToDeveloperInfo()
                        }
                    }
                )
            }

            // Islamic Footer
            item {
                IslamicElegantFooter(
                    islamicColors = islamicColors,
                    dimensions = dimensions
                )
            }
        }

        // Islamic themed Dialog
        selectedInfo?.let { menuInfo ->
            IslamicModernDialog(
                menuInfo = menuInfo,
                islamicColors = islamicColors,
                dimensions = dimensions,
                onDismiss = { selectedInfo = null }
            )
        }
    }
}

@Composable
private fun IslamicAmbientBackground(islamicColors: com.droidnest.tech.ziya.presentation.ui.theme.IslamicColors) {
    val infiniteTransition = rememberInfiniteTransition(label = "islamic_ambient")

    // Islamic geometric floating elements
    repeat(4) { index ->
        val offsetY by infiniteTransition.animateFloat(
            initialValue = (index * 120f),
            targetValue = (index * 120f + 50f),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 7000 + (index * 1200),
                    easing = FastOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "islamic_orb_y_$index"
        )

        val rotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 15000 + (index * 2000),
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "islamic_rotation_$index"
        )

        Box(
            modifier = Modifier
                .offset(
                    x = (30 + index * 80).dp,
                    y = offsetY.dp
                )
                .size((20 + index * 6).dp)
                .graphicsLayer(rotationZ = rotation)
                .background(
                    brush = Brush.radialGradient(
                        colors = when (index % 3) {
                            0 -> listOf(
                                islamicColors.primaryGreen.copy(alpha = 0.08f),
                                Color.Transparent
                            )
                            1 -> listOf(
                                islamicColors.accentGold.copy(alpha = 0.06f),
                                Color.Transparent
                            )
                            else -> listOf(
                                islamicColors.richMaroon.copy(alpha = 0.05f),
                                Color.Transparent
                            )
                        }
                    ),
                    shape = if (index % 2 == 0) CircleShape else RoundedCornerShape(50)
                )
                .blur(4.dp)
        )
    }
}

@Composable
private fun IslamicHeroHeader(
    islamicColors: com.droidnest.tech.ziya.presentation.ui.theme.IslamicColors,
    dimensions: com.droidnest.tech.ziya.presentation.ui.theme.Dimensions
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = dimensions.mediumPadding)
    ) {
        // Animated logo with Islamic styling
        val infiniteTransition = rememberInfiniteTransition(label = "islamic_logo")
        val pulseAlpha by infiniteTransition.animateFloat(
            initialValue = 0.4f,
            targetValue = 0.8f,
            animationSpec = infiniteRepeatable(
                animation = tween(2500, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "islamic_logo_pulse"
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size((dimensions.iconSize.value * 6).dp)
        ) {
            // Islamic geometric glow rings
            Box(
                modifier = Modifier
                    .size((dimensions.iconSize.value * 5.4f).dp)
                    .background(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                Color.Transparent,
                                islamicColors.primaryGreen.copy(alpha = pulseAlpha * 0.4f),
                                islamicColors.accentGold.copy(alpha = pulseAlpha * 0.3f),
                                Color.Transparent,
                                islamicColors.darkGreen.copy(alpha = pulseAlpha * 0.35f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
                    .blur((dimensions.extraSmallPadding.value * 2.5f).dp)
            )

            // Logo container with Islamic design
            Surface(
                modifier = Modifier.size((dimensions.iconSize.value * 4.2f).dp),
                shape = CircleShape,
                shadowElevation = dimensions.cardElevation,
                color = islamicColors.cardBackground.copy(alpha = 0.95f),
                border = BorderStroke(
                    1.5.dp,
                    islamicColors.primaryGreen.copy(alpha = 0.3f)
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Ziya Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensions.smallPadding)
                        .clip(CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensions.mediumPadding))

        // Islamic app title
        Text(
            text = "Ziya - Light of Islam",
            fontSize = (dimensions.extraLargeText.value + 2).sp,
            fontWeight = FontWeight.Bold,
            color = islamicColors.textPrimary,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = 1.2.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(dimensions.smallPadding))

        Text(
            text = "Ziya - Light of Islam",
            fontSize = dimensions.mediumText,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.SansSerif,
            color = islamicColors.primaryGreen,
            letterSpacing = 0.8.sp
        )

        Spacer(modifier = Modifier.height(dimensions.mediumPadding))

        // Islamic company badge
        Surface(
            shape = RoundedCornerShape(dimensions.largeCornerRadius),
            color = islamicColors.softMint.copy(alpha = 0.8f),
            shadowElevation = dimensions.cardElevation / 2,
            border = BorderStroke(
                1.dp,
                islamicColors.primaryGreen.copy(alpha = 0.3f)
            )
        ) {
            Text(
                text = "DroidNest Tech",
                fontSize = dimensions.smallText,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif,
                color = islamicColors.primaryGreen,
                modifier = Modifier.padding(
                    horizontal = dimensions.largePadding,
                    vertical = dimensions.smallPadding
                ),
                letterSpacing = 0.3.sp
            )
        }
    }
}

@Composable
private fun MenuCardsSection(
    islamicColors: com.droidnest.tech.ziya.presentation.ui.theme.IslamicColors,
    dimensions: com.droidnest.tech.ziya.presentation.ui.theme.Dimensions,
    onItemClick: (MenuInfo) -> Unit
) {
    val menuItems = getIslamicMenuItems(islamicColors)

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensions.mediumPadding)
    ) {
        menuItems.forEach { menuItem ->
            IslamicModernMenuCard(
                menuInfo = menuItem,
                modifier = Modifier.fillMaxWidth(),
                islamicColors = islamicColors,
                dimensions = dimensions,
                onClick = { onItemClick(menuItem) }
            )
        }
    }
}

@Composable
private fun IslamicModernMenuCard(
    menuInfo: MenuInfo,
    modifier: Modifier = Modifier,
    islamicColors: com.droidnest.tech.ziya.presentation.ui.theme.IslamicColors,
    dimensions: com.droidnest.tech.ziya.presentation.ui.theme.Dimensions,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy
        ),
        label = "islamic_card_scale"
    )

    val elevation by animateFloatAsState(
        targetValue = if (isPressed) dimensions.cardElevation.value / 2 else dimensions.cardElevation.value,
        animationSpec = tween(150),
        label = "islamic_card_elevation"
    )

    Surface(
        modifier = modifier
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        val released = tryAwaitRelease()
                        isPressed = false
                        if (released) onClick()
                    }
                )
            },
        shape = RoundedCornerShape(dimensions.largeCornerRadius),
        color = islamicColors.cardBackground.copy(alpha = 0.98f),
        shadowElevation = elevation.dp,
        border = BorderStroke(
            1.dp,
            menuInfo.color.copy(alpha = 0.2f)
        )
    ) {
        Box {
            // Islamic gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                menuInfo.color.copy(alpha = 0.04f),
                                islamicColors.softMint.copy(alpha = 0.02f),
                                Color.Transparent
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensions.largePadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensions.mediumPadding)
            ) {
                // Islamic styled icon container
                Surface(
                    modifier = Modifier.size((dimensions.largeIconSize.value * 2.4f).dp),
                    shape = RoundedCornerShape(dimensions.cornerRadius),
                    color = menuInfo.color.copy(alpha = 0.12f),
                    border = BorderStroke(
                        1.dp,
                        menuInfo.color.copy(alpha = 0.4f)
                    )
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = menuInfo.icon,
                            fontSize = (dimensions.extraLargeText.value + 4).sp
                        )
                    }
                }

                // Islamic text content
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = menuInfo.title,
                        fontSize = dimensions.largeText,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = NotoSerifBengali,
                        color = islamicColors.textPrimary,
                        lineHeight = (dimensions.largeText.value + 4).sp
                    )

                    if (menuInfo.subtitle.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(dimensions.extraSmallPadding))
                        Text(
                            text = menuInfo.subtitle,
                            fontSize = dimensions.smallText,
                            fontWeight = FontWeight.Light,
                            fontFamily = NotoSerifBengali,
                            color = islamicColors.textSecondary,
                            lineHeight = (dimensions.smallText.value + 2).sp
                        )
                    }
                }

                // Islamic action indicator
                Surface(
                    modifier = Modifier.size((dimensions.largeIconSize.value * 1.2f).dp),
                    shape = CircleShape,
                    color = menuInfo.color.copy(alpha = 0.15f),
                    border = BorderStroke(
                        0.5.dp,
                        menuInfo.color.copy(alpha = 0.3f)
                    )
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "→",
                            fontSize = dimensions.mediumText,
                            color = menuInfo.color,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun IslamicElegantFooter(
    islamicColors: com.droidnest.tech.ziya.presentation.ui.theme.IslamicColors,
    dimensions: com.droidnest.tech.ziya.presentation.ui.theme.Dimensions
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = dimensions.mediumPadding)
    ) {
        // Islamic decorative line
        Box(
            modifier = Modifier
                .width((dimensions.largePadding.value * 5).dp)
                .height(2.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            islamicColors.primaryGreen.copy(alpha = 0.5f),
                            islamicColors.accentGold.copy(alpha = 0.4f),
                            islamicColors.primaryGreen.copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(1.dp)
                )
        )

        Spacer(modifier = Modifier.height(dimensions.mediumPadding))

        Text(
            text = "মুসলিম উম্মাহর জন্য ভালোবাসা দিয়ে তৈরি",
            fontSize = dimensions.smallText,
            fontWeight = FontWeight.Light,
            fontFamily = NotoSerifBengali,
            color = islamicColors.textSecondary,
            textAlign = TextAlign.Center,
            letterSpacing = 0.2.sp
        )

        Spacer(modifier = Modifier.height(dimensions.mediumPadding))

        Surface(
            shape = RoundedCornerShape(dimensions.cornerRadius),
            color = islamicColors.softMint.copy(alpha = 0.6f),
            border = BorderStroke(
                1.dp,
                islamicColors.primaryGreen.copy(alpha = 0.3f)
            )
        ) {
            Text(
                text = "সংস্করণ ৪.১.২",
                fontSize = dimensions.extraSmallText,
                fontWeight = FontWeight.Medium,
                fontFamily = NotoSerifBengali,
                color = islamicColors.primaryGreen,
                modifier = Modifier.padding(
                    horizontal = dimensions.mediumPadding,
                    vertical = dimensions.smallPadding
                )
            )
        }
    }
}

@Composable
private fun IslamicModernDialog(
    menuInfo: MenuInfo,
    islamicColors: com.droidnest.tech.ziya.presentation.ui.theme.IslamicColors,
    dimensions: com.droidnest.tech.ziya.presentation.ui.theme.Dimensions,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .fillMaxWidth(0.92f)
            .wrapContentHeight(),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = dimensions.mediumPadding)
            ) {
                Surface(
                    modifier = Modifier.size((dimensions.largeIconSize.value * 2.2f).dp),
                    shape = RoundedCornerShape(dimensions.cornerRadius),
                    color = menuInfo.color.copy(alpha = 0.15f),
                    border = BorderStroke(
                        1.dp,
                        menuInfo.color.copy(alpha = 0.4f)
                    )
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = menuInfo.icon,
                            fontSize = (dimensions.extraLargeText.value + 2).sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(dimensions.mediumPadding))

                Text(
                    text = menuInfo.title,
                    fontFamily = NotoSerifBengali,
                    fontWeight = FontWeight.SemiBold,
                    color = islamicColors.textPrimary,
                    fontSize = dimensions.extraLargeText,
                    lineHeight = (dimensions.extraLargeText.value + 6).sp
                )
            }
        },
        text = {
            Text(
                text = menuInfo.description,
                fontFamily = NotoSerifBengali,
                fontWeight = FontWeight.Light,
                lineHeight = (dimensions.mediumText.value + 6).sp,
                color = islamicColors.textSecondary,
                fontSize = dimensions.mediumText,
                letterSpacing = 0.1.sp
            )
        },
        confirmButton = {
            Surface(
                onClick = onDismiss,
                shape = RoundedCornerShape(dimensions.cornerRadius),
                color = islamicColors.softMint,
                border = BorderStroke(
                    1.dp,
                    islamicColors.primaryGreen.copy(alpha = 0.4f)
                )
            ) {
                Text(
                    text = "বুঝেছি",
                    fontFamily = NotoSerifBengali,
                    fontWeight = FontWeight.Medium,
                    color = islamicColors.primaryGreen,
                    fontSize = dimensions.mediumText,
                    modifier = Modifier.padding(
                        horizontal = dimensions.extraLargePadding,
                        vertical = dimensions.mediumPadding
                    )
                )
            }
        },
        shape = RoundedCornerShape(dimensions.largeCornerRadius),
        containerColor = islamicColors.cardBackground,
        tonalElevation = 0.dp
    )
}

// Data classes
data class MenuInfo(
    val title: String,
    val subtitle: String = "",
    val description: String,
    val icon: String,
    val color: Color,
    val action: MenuAction
)

enum class MenuAction {
    SHOW_DIALOG,
    PRIVACY_POLICY,
    DEVELOPER_INFO
}

private fun getIslamicMenuItems(islamicColors: com.droidnest.tech.ziya.presentation.ui.theme.IslamicColors) = listOf(
    MenuInfo(
        title = "আমাদের উদ্দেশ্য",
        subtitle = "অ্যাপের লক্ষ্য ও ভিশন",
        description = "জিয়া একটি আধুনিক ইসলামিক অ্যাপ যা মুসলিমদের দৈনন্দিন জীবনের জন্য প্রয়োজনীয় দোয়া, সময়ানুগ নামাজের রিমাইন্ডার, কিবলার দিক, ইসলামী জ্ঞান ও সেবা প্রদান করে।\n\nআমাদের উদ্দেশ্য হলো প্রযুক্তির আলোকে ইসলামের সঠিক দিকনির্দেশনা পৌঁছে দেওয়া এবং মুসলিম উম্মাহকে তাদের ধর্মীয় জীবনযাত্রায় সহায়তা করা।",
        icon = "🎯",
        color = islamicColors.primaryGreen,
        action = MenuAction.SHOW_DIALOG
    ),
    MenuInfo(
        title = "প্রাইভেসি নীতি",
        subtitle = "আপনার তথ্যের নিরাপত্তা",
        description = "",
        icon = "🔒",
        color = islamicColors.richMaroon,
        action = MenuAction.PRIVACY_POLICY
    ),
    MenuInfo(
        title = "ডেভেলপার পরিচিতি",
        subtitle = "আমাদের টিম সম্পর্কে জানুন",
        description = "",
        icon = "👨‍💻",
        color = islamicColors.darkGreen,
        action = MenuAction.DEVELOPER_INFO
    ),
    MenuInfo(
        title = "যোগাযোগ ও সহায়তা",
        subtitle = "আমাদের সাথে কথা বলুন",
        description = "আপনার কোনো প্রশ্ন, পরামর্শ বা সমস্যা থাকলে আমাদের সাথে যোগাযোগ করুন।\n\n📧 ইমেইল: droidnesttech@gmail.com\n🌐 ওয়েবসাইট: www.droidnest.tech\n📍 অবস্থান: চট্টগ্রাম, বাংলাদেশ\n\nআপনার মূল্যবান মতামত ও পরামর্শ আমাদের কাছে অত্যন্ত গুরুত্বপূর্ণ। আমরা ক্রমাগত আমাদের সেবার মান উন্নত করতে প্রতিশ্রুতিবদ্ধ।",
        icon = "📞",
        color = islamicColors.accentGold,
        action = MenuAction.SHOW_DIALOG
    )
)