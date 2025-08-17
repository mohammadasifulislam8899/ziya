package com.droidnest.tech.ziya.presentation.screens.menu

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.text.font.FontFamily
import androidx.hilt.navigation.compose.hiltViewModel
import com.droidnest.tech.ziya.presentation.ui.theme.NotoSerifBengali
import com.droidnest.tech.ziya.presentation.ui.theme.LocalIslamicColors
import com.droidnest.tech.ziya.R
import com.droidnest.tech.ziya.presentation.screens.home.DivisionDistrictBottomSheet
import com.droidnest.tech.ziya.presentation.ui.theme.Dimensions
import com.droidnest.tech.ziya.presentation.ui.theme.IslamicColors
import com.droidnest.tech.ziya.presentation.ui.theme.SharedViewModel
import com.droidnest.tech.ziya.presentation.ui.theme.getDimensions


@Composable
fun MenuScreen(
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToDeveloperInfo: () -> Unit,
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    // Access Islamic theme
    val dimensions = getDimensions()
    val islamicColors = LocalIslamicColors.current

    var selectedInfo by remember { mutableStateOf<MenuInfo?>(null) }
    var showSettingsDialog by remember {
        mutableStateOf(false)
    }
    var showDistrictDialog by remember {
        mutableStateOf(false)
    }

    // শুধু SharedViewModel থেকে userName নিন, local state নয়
    val userName by sharedViewModel.name.collectAsState()

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
                            MenuAction.SHOW_SETTINGS -> showSettingsDialog = true
                            MenuAction.SHOW_DISTRICT -> showDistrictDialog = true
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

        if (showSettingsDialog) {
            ModernSettingsDialog(
                name = userName, // Direct userName ব্যবহার করুন
                onNameChange = { newName ->
                    // Directly save to SharedViewModel
                    sharedViewModel.saveName(newName)
                },
                onDismiss = { showSettingsDialog = false },
                onSave = {
                    showSettingsDialog = false
                },
                sharedViewModel = sharedViewModel,
            )
        }
        if(showDistrictDialog){
            DivisionDistrictBottomSheet(
                onDismiss = { showDistrictDialog = false },
                onDistrictSelected = {
                    sharedViewModel.saveDistrict(it)
                },
            )
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
private fun IslamicAmbientBackground(islamicColors: IslamicColors) {
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
    islamicColors: IslamicColors,
    dimensions: Dimensions
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
    islamicColors: IslamicColors,
    dimensions: Dimensions,
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
    islamicColors: IslamicColors,
    dimensions: Dimensions,
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
    islamicColors: IslamicColors,
    dimensions: Dimensions
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
    islamicColors: IslamicColors,
    dimensions: Dimensions,
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
    DEVELOPER_INFO,
    SHOW_SETTINGS,
    SHOW_DISTRICT
}

private fun getIslamicMenuItems(islamicColors: IslamicColors) = listOf(
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
        title = "সেটিংস",
        subtitle = "সেটিংস্‌ পরিবর্তন করতে ক্লিক করুন",
        description = "অ্যাপের সেটিংস",
        icon = "⚙️️",
        color = islamicColors.accentGold,
        action = MenuAction.SHOW_SETTINGS
    ),
    MenuInfo(
        title = "জেলা",
        subtitle = "জেলা পরিবর্তন করতে ক্লিক করুন",
        description = "আপনার জেলা",
        icon = "🗺️",
        color = islamicColors.accentGold,
        action = MenuAction.SHOW_DISTRICT
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

@Composable
fun ModernSettingsDialog(
    name: String,
    onNameChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    sharedViewModel: SharedViewModel,
) {

    // Local state for name editing
    var localName by remember(name) { mutableStateOf(name) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        ),
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .fillMaxHeight(0.85f)
            .padding(8.dp),
        title = {
            Column {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Customize your app experience",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 4.dp)
            ) {
                // Profile Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "Profile",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = localName,
                            onValueChange = { localName = it },
                            label = {
                                Text(
                                    "Display Name",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            placeholder = {
                                Text(
                                    "Enter your name",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            ),
                            textStyle = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                Spacer(modifier = Modifier.height(28.dp))

                // Theme Section
                EnhancedThemeSelector(sharedViewModel = sharedViewModel)

                Spacer(modifier = Modifier.height(36.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            "Cancel",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Button(
                        onClick = {
                            // Save the name and close dialog
                            onNameChange(localName)
                            onSave()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            Icons.Default.Save,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "Save",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun EnhancedThemeSelector(
    sharedViewModel: SharedViewModel
) {
    val themeMode by sharedViewModel.themeMode.collectAsState()

    val themeOptions = listOf(
        ThemeOption("LIGHT", "Light", Icons.Default.LightMode, "Bright and clean"),
        ThemeOption("DARK", "Dark", Icons.Default.DarkMode, "Easy on the eyes"),
        ThemeOption("SYSTEM", "System", Icons.Default.PhoneAndroid, "Follow device setting")
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(20.dp) // Increased corner radius
    ) {
        Column(
            modifier = Modifier.padding(24.dp) // Increased padding
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp) // Increased icon size
                )
                Spacer(modifier = Modifier.width(16.dp)) // Increased spacing
                Text(
                    text = "Theme",
                    style = MaterialTheme.typography.titleLarge, // Increased text size
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(20.dp)) // Increased spacing

            themeOptions.forEach { option ->
                ThemeOptionItem(
                    option = option,
                    isSelected = themeMode == option.key,
                    onClick = { sharedViewModel.setTheme(option.key) }
                )

                if (option != themeOptions.last()) {
                    Spacer(modifier = Modifier.height(12.dp)) // Increased spacing
                }
            }
        }
    }
}

@Composable
fun ThemeOptionItem(
    option: ThemeOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = if (isSelected) {
            CardDefaults.outlinedCardBorder().copy(
                width = 2.dp,
                brush = SolidColor(MaterialTheme.colorScheme.primary)
            )
        } else null,
        shape = RoundedCornerShape(16.dp), // Increased corner radius
        elevation = if (isSelected) CardDefaults.cardElevation(4.dp) else CardDefaults.cardElevation(
            0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp), // Increased padding
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = option.icon,
                contentDescription = null,
                tint = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(28.dp) // Increased icon size
            )

            Spacer(modifier = Modifier.width(20.dp)) // Increased spacing

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = option.title,
                    style = MaterialTheme.typography.bodyLarge, // Increased text size
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )

                Text(
                    text = option.description,
                    style = MaterialTheme.typography.bodyMedium, // Increased text size
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp) // Increased spacing
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp) // Increased icon size
                )
            }
        }
    }
}
data class ThemeOption(
    val key: String,
    val title: String,
    val icon: ImageVector,
    val description: String
)
