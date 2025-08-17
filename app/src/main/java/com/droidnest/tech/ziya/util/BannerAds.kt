package com.droidnest.tech.ziya.util

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
// AdConstants.kt
object AdConstants {

    const val INTERSTITIAL_AD_UNIT = "ca-app-pub-3940256099942544/1033173712"
    const val BANNER_AD_UNIT = "ca-app-pub-3940256099942544/6300978111"
}

private const val TAG = "BannerAdComposable"


// BannerAdView.kt
@Composable
fun BannerAdView(
    modifier: Modifier = Modifier,
    adUnitId: String=AdConstants.BANNER_AD_UNIT,
    adWidth: Int = 360
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }

    // Preview mode
    if (LocalInspectionMode.current) {
        Box(modifier = modifier.wrapContentSize()) {
            Text(
                text = "Google Mobile Ads preview banner.",
                modifier = Modifier.align(Alignment.Center)
            )
        }
        return
    }

    val adView = remember {
        AdView(context).apply {
            this.adUnitId = adUnitId
            val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
            setAdSize(adSize)

            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    isLoading = false
                    hasError = false
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    isLoading = false
                    hasError = true
                }
            }
        }
    }

    // Lifecycle aware loading and cleanup
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    adView.resume()
                    // Retry loading if there was an error
                    if (hasError) {
                        adView.loadAd(AdRequest.Builder().build())
                        isLoading = true
                        hasError = false
                    }
                }
                Lifecycle.Event.ON_PAUSE -> adView.pause()
                Lifecycle.Event.ON_DESTROY -> adView.destroy()
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        // Initial ad load
        adView.loadAd(AdRequest.Builder().build())

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            try {
                adView.destroy()
            } catch (e: Exception) {
            }
        }
    }

    // Show loading or error state if needed
    Box(modifier = modifier.wrapContentSize()) {
        if (isLoading) {
            // Optional: Show loading indicator
             CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (hasError) {
            // Optional: Show error state
             Text("Ad failed to load", modifier = Modifier.align(Alignment.Center))
        }

        // Show the AdView
        AndroidView(
            modifier = Modifier.wrapContentSize(),
            factory = { adView }
        )
    }
}

