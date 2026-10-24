package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

val InterFontFamily = FontFamily(
    Font(R.font.inter, FontWeight.Normal),
    Font(R.font.inter, FontWeight.Medium),
    Font(R.font.inter, FontWeight.SemiBold),
    Font(R.font.inter, FontWeight.Bold)
)

// Exact 1:1 Typography Tokens from common.css
val VryvDisplayStyle = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 34.sp,
    lineHeight = 36.sp,
    letterSpacing = (-1.19).sp,
    color = VryvTextPrimary
)

val VryvTitleStyle = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 26.sp,
    lineHeight = 29.sp,
    letterSpacing = (-0.78).sp,
    color = VryvTextPrimary
)

val VryvH2Style = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 21.sp,
    lineHeight = 25.sp,
    letterSpacing = (-0.52).sp,
    color = VryvTextPrimary
)

val VryvH3Style = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 17.sp,
    lineHeight = 22.sp,
    letterSpacing = (-0.25).sp,
    color = VryvTextPrimary
)

val VryvBodyStyle = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 15.sp,
    lineHeight = 22.sp,
    color = VryvTextSecondary
)

val VryvCapStyle = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 13.sp,
    lineHeight = 18.sp,
    color = VryvTextSecondary
)

val VryvMicroStyle = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 11.sp,
    lineHeight = 14.sp,
    color = VryvTextSecondary
)

val Typography = Typography(
    displayLarge = VryvDisplayStyle,
    headlineLarge = VryvTitleStyle,
    headlineMedium = VryvH2Style,
    titleLarge = VryvH2Style,
    titleMedium = VryvH3Style,
    bodyLarge = VryvBodyStyle.copy(color = VryvTextPrimary),
    bodyMedium = VryvBodyStyle,
    bodySmall = VryvCapStyle,
    labelLarge = VryvH3Style,
    labelMedium = VryvCapStyle,
    labelSmall = VryvMicroStyle
)
