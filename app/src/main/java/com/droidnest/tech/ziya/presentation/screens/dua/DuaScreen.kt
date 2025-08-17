package com.droidnest.tech.ziya.presentation.screens.dua

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Source
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.droidnest.tech.ziya.domain.model.Dua
import com.droidnest.tech.ziya.presentation.ui.theme.*
import com.droidnest.tech.ziya.R
import com.droidnest.tech.ziya.util.BannerAdView

@Composable
fun DuaScreen(
    viewModel: DuaViewModel = hiltViewModel()
) {
    val dailyPrayers by viewModel.dailyDuas.collectAsStateWithLifecycle()
    val namazDuas by viewModel.namazDuas.collectAsStateWithLifecycle()

    // Use Islamic theme colors and dimensions
    val islamicColors = LocalIslamicColors.current
    val dimensions = LocalDimensions.current

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf(
        stringResource(R.string.daily_prayers),
        stringResource(R.string.liturgical_prayers)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        islamicColors.background,
                        islamicColors.softMint.copy(alpha = 0.3f),
                        islamicColors.background
                    )
                )
            )
    ) {
        // Islamic pattern floating elements
        repeat(3) { index ->
            IslamicFloatingElement(
                modifier = Modifier
                    .offset(
                        x = (50 + index * 120).dp,
                        y = (80 + index * 200).dp
                    )
                    .size((16 + index * 8).dp),
                color = islamicColors.primaryGreen.copy(alpha = 0.04f)
            )
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Islamic themed header with improved tabs
                IslamicTabRow(
                    selectedTab = selectedTab,
                    tabTitles = tabTitles,
                    islamicColors = islamicColors,
                    dimensions = dimensions,
                    onTabSelected = { selectedTab = it }
                )

                // Content with Islamic theme
                when (selectedTab) {
                    0 -> DuaList(
                        duas = dailyPrayers,
                        islamicColors = islamicColors,
                        dimensions = dimensions
                    )

                    1 -> DuaList(
                        duas = namazDuas,
                        islamicColors = islamicColors,
                        dimensions = dimensions
                    )
                }
            }
            // Banner ad bottom e
            BannerAdView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun IslamicFloatingElement(
    modifier: Modifier = Modifier,
    color: Color
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        color,
                        color.copy(alpha = 0.2f),
                        Color.Transparent
                    )
                )
            )
    )
}

@Composable
private fun IslamicTabRow(
    selectedTab: Int,
    tabTitles: List<String>,
    islamicColors: IslamicColors,
    dimensions: Dimensions,
    onTabSelected: (Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensions.mediumPadding)
            .shadow(
                elevation = dimensions.cardElevation,
                shape = RoundedCornerShape(dimensions.largeCornerRadius)
            ),
        shape = RoundedCornerShape(dimensions.largeCornerRadius),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            islamicColors.primaryGreen.copy(alpha = 0.1f)
        )
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = islamicColors.primaryGreen,
            indicator = { tabPositions ->
                val indicatorWidth = 32.dp
                val currentTabPosition = tabPositions[selectedTab]

                Box(
                    Modifier
                        .tabIndicatorOffset(currentTabPosition)
                        .padding(bottom = 6.dp)
                        .width(indicatorWidth)
                        .height(3.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    islamicColors.accentGold,
                                    islamicColors.primaryGreen
                                )
                            ),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            },
            divider = {}
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { onTabSelected(index) },
                    modifier = Modifier.padding(vertical = dimensions.smallPadding)
                ) {
                    Text(
                        text = title,
                        fontSize = dimensions.mediumText,
                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                        color = if (selectedTab == index) islamicColors.primaryGreen else islamicColors.textSecondary,
                        fontFamily = NotoSerifBengali,
                        modifier = Modifier.padding(vertical = dimensions.smallPadding)
                    )
                }
            }
        }
    }
}

@Composable
private fun DuaList(
    duas: List<Dua>,
    islamicColors: IslamicColors,
    dimensions: Dimensions
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = dimensions.mediumPadding,
            vertical = dimensions.smallPadding
        ),
        verticalArrangement = Arrangement.spacedBy(dimensions.smallPadding)
    ) {
        itemsIndexed(duas) { index, dua ->
            IslamicDuaCard(
                dua = dua,
                number = index + 1,
                islamicColors = islamicColors,
                dimensions = dimensions
            )
        }
    }
}

@Composable
private fun IslamicDuaCard(
    dua: Dua,
    number: Int,
    islamicColors: IslamicColors,
    dimensions: Dimensions
) {
    var isBookmarked by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = dimensions.cardElevation,
                shape = RoundedCornerShape(dimensions.cornerRadius)
            ),
        colors = CardDefaults.cardColors(
            containerColor = islamicColors.cardBackground
        ),
        shape = RoundedCornerShape(dimensions.cornerRadius),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            islamicColors.primaryGreen.copy(alpha = 0.08f)
        )
    ) {
        Column(
            modifier = Modifier.padding(dimensions.mediumPadding)
        ) {
            // Header with number and bookmark (Islamic styling)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(dimensions.cornerRadius),
                    color = islamicColors.primaryGreen.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        islamicColors.accentGold.copy(alpha = 0.3f)
                    )
                ) {
                    Text(
                        text = "দুয়া #$number",
                        fontSize = dimensions.smallText,
                        fontWeight = FontWeight.Bold,
                        color = islamicColors.primaryGreen,
                        modifier = Modifier.padding(
                            horizontal = dimensions.smallPadding * 1.5f,
                            vertical = dimensions.extraSmallPadding
                        ),
                        fontFamily = NotoSerifBengali
                    )
                }

                IconButton(
                    onClick = { isBookmarked = !isBookmarked },
                    modifier = Modifier.size(dimensions.largeIconSize + 12.dp)
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) islamicColors.accentGold else islamicColors.textSecondary,
                        modifier = Modifier.size(dimensions.iconSize)
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensions.smallPadding))

            // Arabic text with Islamic styling
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(dimensions.cornerRadius),
                colors = CardDefaults.cardColors(
                    containerColor = islamicColors.cardBackground
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    islamicColors.primaryGreen.copy(alpha = 0.15f)
                )
            ) {
                Text(
                    text = dua.dua,
                    fontSize = dimensions.largeText,
                    fontWeight = FontWeight.Medium,
                    color = islamicColors.textPrimary,
                    textAlign = TextAlign.End,
                    lineHeight = dimensions.largeText * 1.5f,
                    modifier = Modifier.padding(dimensions.mediumPadding)
                )
            }

            Spacer(modifier = Modifier.height(dimensions.smallPadding))

            // Pronunciation section with Islamic theme
            IslamicInfoSection(
                title = stringResource(R.string.pronunciation),
                content = dua.banglaPronunciation,
                islamicColors = islamicColors,
                dimensions = dimensions,
                backgroundColor = islamicColors.lightGold.copy(alpha = 0.4f),
                borderColor = islamicColors.accentGold.copy(alpha = 0.2f)
            )

            if (isExpanded) {
                Spacer(modifier = Modifier.height(dimensions.extraSmallPadding))

                // Translation section
                IslamicInfoSection(
                    title = stringResource(R.string.translation),
                    content = dua.banglaMeaning,
                    islamicColors = islamicColors,
                    dimensions = dimensions,
                    backgroundColor = islamicColors.richMaroon.copy(alpha = 0.08f),
                    borderColor = islamicColors.richMaroon.copy(alpha = 0.15f)
                )

                Spacer(modifier = Modifier.height(dimensions.extraSmallPadding))

                // Additional info with Islamic styling
                IslamicAdditionalInfo(
                    whenToRecite = dua.whenToRecite,
                    reference = dua.reference,
                    islamicColors = islamicColors,
                    dimensions = dimensions
                )
            }

            // Islamic expand/collapse button
            OutlinedButton(
                onClick = { isExpanded = !isExpanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensions.extraSmallPadding),
                shape = RoundedCornerShape(dimensions.cornerRadius),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = islamicColors.primaryGreen
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    Brush.horizontalGradient(
                        colors = listOf(
                            islamicColors.primaryGreen.copy(alpha = 0.4f),
                            islamicColors.accentGold.copy(alpha = 0.4f)
                        )
                    )
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isExpanded) "কম দেখুন" else "বিস্তারিত দেখুন",
                        fontSize = dimensions.smallText,
                        fontWeight = FontWeight.Medium,
                        fontFamily = NotoSerifBengali
                    )
                    Spacer(modifier = Modifier.width(dimensions.extraSmallPadding))
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        modifier = Modifier.size(dimensions.iconSize)
                    )
                }
            }
        }
    }
}

@Composable
private fun IslamicInfoSection(
    title: String,
    content: String,
    islamicColors: IslamicColors,
    dimensions: Dimensions,
    backgroundColor: Color,
    borderColor: Color
) {
    Card(
        shape = RoundedCornerShape(dimensions.cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = islamicColors.cardBackground
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(dimensions.smallPadding * 1.2f)
        ) {
            Text(
                text = title,
                fontSize = dimensions.smallText,
                fontWeight = FontWeight.SemiBold,
                color = islamicColors.primaryGreen,
                fontFamily = NotoSerifBengali
            )

            Spacer(modifier = Modifier.height(dimensions.extraSmallPadding / 2))

            Text(
                text = content,
                fontSize = dimensions.mediumText,
                fontWeight = FontWeight.Normal,
                color = islamicColors.textPrimary,
                lineHeight = dimensions.mediumText,
                fontFamily = NotoSerifBengali
            )
        }
    }
}

@Composable
private fun IslamicAdditionalInfo(
    whenToRecite: String?,
    reference: String,
    islamicColors: IslamicColors,
    dimensions: Dimensions
) {
    Card(
        shape = RoundedCornerShape(dimensions.cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = islamicColors.surface.copy(alpha = 0.6f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            islamicColors.primaryGreen.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(dimensions.smallPadding * 1.2f),
            verticalArrangement = Arrangement.spacedBy(dimensions.extraSmallPadding)
        ) {
            if (!whenToRecite.isNullOrBlank()) {
                IslamicInfoRow(
                    icon = Icons.Default.Schedule,
                    label = stringResource(R.string.when_to_recite),
                    value = whenToRecite,
                    islamicColors = islamicColors,
                    dimensions = dimensions
                )
            }

            IslamicInfoRow(
                icon = Icons.Default.Source,
                label = stringResource(R.string.reference),
                value = reference,
                islamicColors = islamicColors,
                dimensions = dimensions
            )
        }
    }
}

@Composable
private fun IslamicInfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    islamicColors: IslamicColors,
    dimensions: Dimensions
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = CircleShape,
            color = islamicColors.accentGold.copy(alpha = 0.15f),
            modifier = Modifier.padding(top = dimensions.extraSmallPadding / 2)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = islamicColors.primaryGreen,
                modifier = Modifier
                    .size(dimensions.iconSize - 2.dp)
                    .padding(dimensions.extraSmallPadding / 2)
            )
        }

        Spacer(modifier = Modifier.width(dimensions.smallPadding))

        Column {
            Text(
                text = label,
                fontSize = dimensions.extraSmallText,
                fontWeight = FontWeight.SemiBold,
                color = islamicColors.textSecondary,
                fontFamily = NotoSerifBengali
            )
            Text(
                text = value,
                fontSize = dimensions.smallText,
                fontWeight = FontWeight.Normal,
                color = islamicColors.textPrimary,
                fontFamily = NotoSerifBengali,
                lineHeight = dimensions.mediumText
            )
        }
    }
}