package com.droidnest.tech.ziya.analytics

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    val analyticsLogger: AnalyticsLogger
) : ViewModel()
