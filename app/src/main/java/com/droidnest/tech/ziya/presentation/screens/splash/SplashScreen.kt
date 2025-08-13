package com.droidnest.tech.ziya.presentation.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.droidnest.tech.ziya.R
import com.droidnest.tech.ziya.presentation.navigation.Main
import com.droidnest.tech.ziya.presentation.navigation.Welcome
import com.droidnest.tech.ziya.presentation.screens.welcome.WelcomeViewModel
import kotlinx.coroutines.delay

@Composable
fun ZiyaSplashScreen(
    navController: NavHostController,
    viewmodel: WelcomeViewModel = hiltViewModel()
) {
    val userName by viewmodel.userName.collectAsState()
    val district by viewmodel.selectedDistrict.collectAsState()

    // No animations, all values fixed to fully visible/static
    val logoScale = 1f
    val logoRotation = 0f
    val logoAlpha = 1f
    val textAlpha = 1f
    val backgroundAlpha = 1f

    // Direct navigation after a short delay (if you want)
    LaunchedEffect(Unit) {
        delay(1000) // Optional: keep splash visible for 1 sec
        navController.popBackStack()
        if (userName != null && district != null) {
            navController.navigate(Main.route)
        } else {
            navController.navigate(Welcome.route)
        }
    }

    ModernSplash(
        logoScale = logoScale,
        logoRotation = logoRotation,
        logoAlpha = logoAlpha,
        textAlpha = textAlpha,
        backgroundAlpha = backgroundAlpha
    )
}


@Composable
fun ModernSplash(
    logoScale: Float,
    logoRotation: Float,
    logoAlpha: Float,
    textAlpha: Float,
    backgroundAlpha: Float
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1a1a2e).copy(alpha = backgroundAlpha),
                        Color(0xFF16213e).copy(alpha = backgroundAlpha),
                        Color(0xFF0f3460).copy(alpha = backgroundAlpha)
                    ),
                    radius = 800f
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        // Background decorative elements
        repeat(3) { index ->
            Box(
                modifier = Modifier
                    .size((200 + index * 100).dp)
                    .alpha(0.1f * backgroundAlpha)
                    .rotate(logoRotation * (0.5f + index * 0.3f))
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.1f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Logo container with modern shadow effect
            Box(
                contentAlignment = Alignment.Center
            ) {
                // Shadow/Glow effect
                Box(
                    modifier = Modifier
                        .size(280.dp)
                        .scale(logoScale * 0.9f)
                        .alpha(logoAlpha * 0.3f)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF4FC3F7).copy(alpha = 0.4f),
                                    Color.Transparent
                                ),
                                radius = 140f
                            ),
                            shape = CircleShape
                        )
                        .blur(20.dp)
                )

                // Main logo
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(240.dp)
                        .scale(logoScale)
                        .alpha(logoAlpha)
                        .rotate(logoRotation)
                        .clip(CircleShape)
                        .background(
                            Color.White.copy(alpha = 0.1f),
                            CircleShape
                        ),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            // App name with modern typography
            Text(
                text = "Ziya",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 4.sp,
                modifier = Modifier.alpha(textAlpha)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // App tagline
            Text(
                text = "Islamic Companion App",
                color = Color(0xFFB0BEC5),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(textAlpha)
            )
        }

        // Bottom brand section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
                .alpha(textAlpha)
        ) {

            // Loading indicator
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(3.dp)
                    .background(
                        Color.White.copy(alpha = 0.2f),
                        RoundedCornerShape(2.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width((120 * logoScale).dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF4FC3F7),
                                    Color(0xFF29B6F6)
                                )
                            ),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Developer credit with modern styling
            Text(
                text = "Crafted with ❤️",
                color = Color(0xFF78909C),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.5.sp
            )

            Text(
                text = "by ASIF",
                color = Color(0xFFB0BEC5),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 2.sp
            )
        }
    }
}