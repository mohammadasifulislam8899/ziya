package com.droidnest.tech.ziya.presentation.navigation

import com.droidnest.tech.ziya.R

sealed class BottomNavItem(val route: String, val title: String, val icon: Int) {
    object HomeObject : BottomNavItem("home", "Home", R.drawable.home)
    object QuranObject : BottomNavItem("quran", "Quran", R.drawable.surah)
    object DuaObject : BottomNavItem("dua", "Dua", R.drawable.dua)
    object MenuObject : BottomNavItem("menu", "Menu", R.drawable.menu)
}
