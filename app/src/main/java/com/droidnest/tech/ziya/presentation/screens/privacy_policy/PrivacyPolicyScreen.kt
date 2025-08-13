package com.droidnest.tech.ziya.presentation.screens.privacy_policy

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Privacy Policy",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF00796B)
                        )
                    }
                },
                modifier = Modifier.shadow(4.dp)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "Privacy Policy for Ziya – Light of Islam",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
                text = "Last updated: August 7, 2025",
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Ziya – Light of Islam is a spiritual companion app developed by DroidNest Tech to help users stay connected with Islamic practices, duas, and information. We value your privacy and want to ensure transparency about how the app operates.",
                fontSize = 16.sp,
                lineHeight = 24.sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            SectionTitle(title = "1. Personal Data Storage on Device")
            Text(
                text = "Ziya app collects some personal information such as your name and location to personalize your experience (e.g., showing prayer times based on location).",
                fontSize = 16.sp,
                lineHeight = 24.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "However, this data is stored only locally on your device using Android's DataStore system and is never uploaded, shared, or transmitted to any external server or third party.",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 24.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Your privacy is our priority, and your personal data remains private to your device only.",
                fontSize = 16.sp,
                lineHeight = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            SectionTitle(title = "2. Data Sources and Content")
            BulletPoint(text = "Public APIs: We use publicly available APIs to fetch prayer times and Quran content. These sources do not involve personal data and are used solely to enhance user experience.")
            BulletPoint(text = "AI-Generated Content: Some Islamic educational texts, including translations or summaries, may be generated using AI tools like ChatGPT. While we strive for accuracy, we encourage users to consult verified scholars for religious rulings.")
            BulletPoint(text = "Python Processing: Our team uses Python scripts to process public data and convert it into JSON format for use inside the app. No personal data is involved in this process.")
            Spacer(modifier = Modifier.height(20.dp))

            SectionTitle(title = "3. Analytics")
            Text(
                text = "We use Google Firebase Analytics to better understand how users interact with the app. This helps us improve the app’s features and performance.",
                fontSize = 16.sp,
                lineHeight = 24.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
                text = "Firebase Analytics collects anonymous usage data and device information. It does not collect personally identifiable information such as your name or location unless explicitly provided by you.",
                fontSize = 16.sp,
                lineHeight = 24.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            val privacyPolicyText = buildAnnotatedString {
                append("Data collected by Firebase Analytics is governed by Google's ")
                pushStringAnnotation(
                    tag = "URL",
                    annotation = "https://policies.google.com/privacy"
                )
                withStyle(
                    style = SpanStyle(
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append("Privacy Policy")
                }
                pop()
                append(".")
            }
            Text(
                text = privacyPolicyText,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = Color.Unspecified,
                modifier = Modifier.clickable {
                    uriHandler.openUri("https://policies.google.com/privacy")
                }
            )
            Text(
                text = "You can opt out of Firebase Analytics collection by adjusting your device settings or uninstalling the app.",
                fontSize = 16.sp,
                lineHeight = 24.sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            SectionTitle(title = "4. Icons and Media Attributions")
            Text(
                text = "The following third-party icons and images are used in this app under proper license:",
                fontSize = 16.sp,
                lineHeight = 24.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            BulletPointWithLink(
                text = "Home icons by Bharat Icons",
                url = "https://www.flaticon.com/free-icons/home-page"
            )
            BulletPointWithLink(
                text = "Quran icons by Marz Gallery",
                url = "https://www.flaticon.com/free-icons/quran"
            )
            BulletPointWithLink(
                text = "Pray icons by Mayor Icons",
                url = "https://www.flaticon.com/free-icons/pray"
            )
            BulletPointWithLink(
                text = "Menu icons by SeyfDesigner",
                url = "https://www.flaticon.com/free-icons/menu"
            )
            BulletPointWithLink(
                text = "Tasbih icons by denimao",
                url = "https://www.flaticon.com/free-icons/tasbih"
            )
            BulletPointWithLink(
                text = "Background photo by Jose Aragones",
                url = "https://www.pexels.com/photo/photo-of-islamic-window-3254036/"
            )

            Spacer(modifier = Modifier.height(20.dp))

            SectionTitle(title = "6. Changes to This Policy")
            Text(
                text = "We may update this Privacy Policy from time to time. Any changes will be posted on this page with a new \"Last updated\" date.",
                fontSize = 16.sp,
                lineHeight = 24.sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            SectionTitle(title = "7. Contact Us")
            Text(
                text = "If you have any questions about this Privacy Policy, please contact us at:",
                fontSize = 16.sp,
                lineHeight = 24.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
                text = AnnotatedString(
                    "📧 droidnesttech@gmail.com",
                    spanStyle = SpanStyle(
                        textDecoration = TextDecoration.Underline
                    )
                ),
                modifier = Modifier
                    .clickable {
                        uriHandler.openUri("mailto:droidnesttech@gmail.com")
                    }
                    .padding(bottom = 24.dp),
                style = TextStyle(fontSize = 16.sp, lineHeight = 24.sp)
            )
            Text(
                text = "By using the Ziya app, you agree to the terms of this Privacy Policy and consent to our data practices as described herein.",
                fontSize = 16.sp,
                lineHeight = 24.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Text(
                text = "Thank you for trusting Ziya.\n– DroidNest Tech Team",
                fontSize = 16.sp,
                lineHeight = 24.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        modifier = Modifier.padding(vertical = 10.dp)
    )
}

@Composable
fun BulletPoint(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "•",
            fontSize = 20.sp,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = text,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        )
    }
}

@Composable
fun BulletPointWithLink(text: String, url: String) {
    val uriHandler = LocalUriHandler.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
    ) {
        Text(
            text = "•",
            fontSize = 20.sp,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = AnnotatedString(
                text,
                spanStyle = SpanStyle(
                    textDecoration = TextDecoration.Underline,
                    fontSize = 16.sp
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    uriHandler.openUri(url)
                }
        )
    }
}
