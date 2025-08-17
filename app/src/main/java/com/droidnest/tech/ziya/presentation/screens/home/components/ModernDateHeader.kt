package com.droidnest.tech.ziya.presentation.screens.home.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.droidnest.tech.ziya.presentation.ui.theme.NotoSerifBengali
import com.droidnest.tech.ziya.presentation.ui.theme.LocalIslamicColors
import com.droidnest.tech.ziya.presentation.ui.theme.LocalDimensions
import com.droidnest.tech.ziya.util.*
import com.droidnest.tech.ziya.R
import java.time.LocalDate



@Composable
 fun ModernDateHeader(selectedDistrict: String?) {
    val dimensions = LocalDimensions.current
    val today = LocalDate.now()
    val formattedDate = formatDateWithDay(today)

    Row(
        modifier = Modifier
            .padding(horizontal = dimensions.extraLargePadding)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DateSection(
            modifier = Modifier.weight(1f),
            formattedDate = formattedDate
        )

        ModernDistrictSelector(
            modifier = Modifier.weight(1f),
            selectedDistrict = selectedDistrict
        )
    }
}

@Composable
private fun DateSection(
    modifier: Modifier = Modifier,
    formattedDate: String
) {
    val colors = LocalIslamicColors.current
    val dimensions = LocalDimensions.current

    Column(modifier = modifier) {
        Text(
            text = formattedDate,
            fontSize = dimensions.mediumText,
            color = colors.textSecondary,
            fontFamily = NotoSerifBengali
        )

        Text(
            text = stringResource(R.string.namaz_time),
            fontSize = dimensions.extraLargeText,
            fontWeight = FontWeight.Bold,
            color = colors.primaryGreen,
            fontFamily = NotoSerifBengali
        )
    }
}

@Composable
fun ModernDistrictSelector(
    modifier: Modifier = Modifier,
    selectedDistrict: String?,
    onClick: () -> Unit = {}
) {
    val colors = LocalIslamicColors.current
    val dimensions = LocalDimensions.current

    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(dimensions.cornerRadius),
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensions.cardElevation)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensions.smallPadding,
                    vertical = dimensions.extraSmallPadding
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = colors.accentGold,
                modifier = Modifier.size(dimensions.iconSize)
            )

            Spacer(modifier = Modifier.width(dimensions.extraSmallPadding))

            Text(
                text = selectedDistrict?.takeIf { it.isNotBlank() }
                    ?.replaceFirstChar { it.uppercase() }
                    ?: stringResource(R.string.select_zella),
                fontSize = dimensions.smallText,
                fontWeight = FontWeight.SemiBold,
                color = colors.textPrimary,
                fontFamily = NotoSerifBengali
            )

            Spacer(modifier = Modifier.width(dimensions.extraSmallPadding))

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = colors.textSecondary,
                modifier = Modifier.size(dimensions.iconSize)
            )
        }
    }
}