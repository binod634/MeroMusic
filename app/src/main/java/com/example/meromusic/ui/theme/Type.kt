package com.example.meromusic.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.meromusic.R
import com.example.meromusic.R.font.rubix
import com.example.meromusic.R.font.kenia

val rubixfont = FontFamily(
    Font(rubix)
)

val keniaFont = FontFamily(
    Font(kenia)
)

val monotoon = FontFamily(
    Font(R.font.monotoon)

)

val justanotherhand = FontFamily(
    Font(R.font.justanotherhand)
)

// Set of Material typography styles to start with
val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily  = rubixfont,
        fontSize = 20.sp,
    ),
    bodySmall = TextStyle(
        fontFamily  = rubixfont,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    labelMedium = TextStyle(
        fontFamily = keniaFont,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp
    ),
    labelLarge =  TextStyle(
        fontFamily = monotoon,
        fontWeight = FontWeight.Normal,
        fontSize = 40.sp
    ),
    headlineSmall =  TextStyle(
        fontFamily = justanotherhand,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    )
)