//package com.droidnest.tech.ziya.util
//
//import androidx.compose.runtime.State
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import kotlinx.coroutines.delay
//import java.time.LocalDateTime
//import android.os.Build
//import androidx.annotation.RequiresApi
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun currentTimeTicker(): State<LocalDateTime> {
//    val currentTime = remember { mutableStateOf(LocalDateTime.now()) }
//    LaunchedEffect(Unit) {
//        while (true) {
//            currentTime.value = LocalDateTime.now()
//            delay(1000L)
//        }
//    }
//    return currentTime
//}
//
