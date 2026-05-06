package com.choiminjun.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.choiminjun.designsystem.R

val PretendardFontFamily = FontFamily(
    Font(R.font.pretendard_variable, FontWeight.Light),
    Font(R.font.pretendard_variable, FontWeight.Normal),
    Font(R.font.pretendard_variable, FontWeight.Medium),
    Font(R.font.pretendard_variable, FontWeight.SemiBold),
    Font(R.font.pretendard_variable, FontWeight.Bold),
    Font(R.font.pretendard_variable, FontWeight.ExtraBold),
)

@Immutable
data class SRTypography(
    val headingXLSB: TextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
    ),
    val headingLSB: TextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
    ),
    val headingMSB: TextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
    ),
    val bodyMSB: TextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
    ),
    val bodyLM: TextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
    ),
    val bodyLR: TextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
    ),
    val bodyXMM: TextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
    ),
    val bodyXMR: TextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
    ),
    val bodyMM: TextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
    ),
    val bodyMR: TextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    val bodySSB: TextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
    ),
    val bodySM: TextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
    ),
    val bodySR: TextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    ),
)
