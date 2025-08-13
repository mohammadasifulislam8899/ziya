package com.droidnest.tech.ziya.util

import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

val todayDate: String? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val now = LocalDate.now()
            "${now.year}-${now.monthValue}-${now.dayOfMonth}"
        } else {
            val sdf = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
            sdf.format(Date())
        }

