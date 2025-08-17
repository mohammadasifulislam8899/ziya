package com.droidnest.tech.ziya.presentation.screens.home

import android.app.Activity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.droidnest.tech.ziya.presentation.ui.theme.LocalIslamicColors
import com.droidnest.tech.ziya.presentation.ui.theme.LocalDimensions
import com.droidnest.tech.ziya.presentation.screens.home.components.HadithModernSection
import com.droidnest.tech.ziya.presentation.screens.home.components.ModernDateHeader
import com.droidnest.tech.ziya.presentation.screens.home.components.PrayerTimeSection
import com.droidnest.tech.ziya.presentation.screens.home.components.TasbihModernCard
import com.droidnest.tech.ziya.util.AdConstants
import com.droidnest.tech.ziya.util.BannerAdView
import com.droidnest.tech.ziya.util.InterstitialAdHelper

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onTasbihClick: () -> Unit,
) {
    val colors = LocalIslamicColors.current
    val dimensions = LocalDimensions.current

    // States
    val prayerTimeState = viewModel.todayPrayerTime.collectAsStateWithLifecycle()
    val selectedDistrict = viewModel.selectedDistrict.collectAsStateWithLifecycle()
    val hadithList = viewModel.hadithList.collectAsStateWithLifecycle()


    val context = LocalContext.current
    val activity = context as Activity

    LaunchedEffect(Unit) {
        InterstitialAdHelper.loadAd(activity, AdConstants.INTERSTITIAL_AD_UNIT)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(dimensions.extraSmallPadding),
            verticalArrangement = Arrangement.spacedBy(dimensions.mediumPadding)
        ) {
            item {
                TasbihModernCard(
                    onTasbihClick = {
                        InterstitialAdHelper.showAd(
                            activity = activity,
                            adUnitId = AdConstants.INTERSTITIAL_AD_UNIT
                        ) {
                            onTasbihClick()
                        }
                    }
                )
            }

            item {
                ModernDateHeader(selectedDistrict = selectedDistrict.value)
            }

            item {
                PrayerTimeSection(prayerTimeState = prayerTimeState.value)
            }

            item {
                HadithModernSection(
                    hadithList = hadithList.value,
                    onRefresh = { viewModel.refreshHadith() }
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