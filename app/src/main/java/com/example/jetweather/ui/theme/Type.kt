package com.example.jetweather.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.jetweather.R

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)

val Roboto = FontFamily(
    Font(R.font.roboto_light, weight = FontWeight.Light),
    Font(R.font.roboto_medium, weight = FontWeight.Medium),
    Font(R.font.roboto_regular),
    Font(R.font.roboto_thin, weight = FontWeight.Thin)
)

val QuickSand = FontFamily(
    Font(R.font.qs_bold, weight = FontWeight.Bold),
    Font(R.font.qs_light, weight = FontWeight.Light),
    Font(R.font.qs_med, weight = FontWeight.Medium),
    Font(R.font.qs_reg),
)
