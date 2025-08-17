package com.droidnest.tech.ziya.util

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object InterstitialAdHelper {

    private var interstitialAd: InterstitialAd? = null
    private var clickCount = 0
    private var firstClickShown = false

    fun loadAd(activity: Activity, adUnitId: String) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            activity,
            adUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    interstitialAd = null
                }
            }
        )
    }

    fun showAd(activity: Activity, adUnitId: String, onAdDismissed: () -> Unit = {}) {
        clickCount++

        // First click or every 4th click
        if (!firstClickShown || clickCount % 4 == 0) {
            if (interstitialAd != null) {
                interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        interstitialAd = null
                        firstClickShown = true
                        loadAd(activity, adUnitId) // Reload ad
                        onAdDismissed()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError) {
                        interstitialAd = null
                        loadAd(activity, adUnitId) // Reload ad
                        onAdDismissed()
                    }
                }
                interstitialAd?.show(activity)
            } else {
                onAdDismissed()
            }
        } else {
            onAdDismissed()
        }
    }
}
