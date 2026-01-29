package ru.practicum.android.diploma.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import ru.practicum.android.diploma.R

val YsDisplayBold = FontFamily(Font(R.font.ys_display_bold))
val YsDisplayMedium = FontFamily(Font(R.font.ys_display_medium))
val YsDisplayRegular = FontFamily(Font(R.font.ys_display_regular))

val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = YsDisplayBold,
        fontSize = 32.sp,
        lineHeight = 38.sp
    ),
    titleMedium = TextStyle(
        fontFamily = YsDisplayMedium,
        fontSize = 22.sp,
        lineHeight = 26.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = YsDisplayMedium,
        fontSize = 16.sp,
        lineHeight = 19.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = YsDisplayRegular,
        fontSize = 16.sp,
        lineHeight = 19.sp
    ),
    labelSmall = TextStyle(
        fontFamily = YsDisplayRegular,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
)
