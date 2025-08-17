package com.droidnest.tech.ziya

import android.app.Application
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Ziya : Application(){
    override fun onCreate() {
        super.onCreate()
        // Initialize AdMob
        MobileAds.initialize(this) { }
    }
}