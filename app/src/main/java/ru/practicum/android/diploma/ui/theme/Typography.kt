package ru.practicum.android.diploma.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import ru.practicum.android.diploma.R

val YsDisplayBold = FontFamily(Font(R.font.ys_display_bold))
val YsDisplayMedium = FontFamily(Font(R.font.ys_display_medium))
val YsDisplayRegular = FontFamily(Font(R.font.ys_display_regular))

val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = YsDisplayBold,
        fontSize = fontSize32,
        lineHeight = fontLineHeight38
    ),
    titleMedium = TextStyle(
        fontFamily = YsDisplayMedium,
        fontSize = fontSize22,
        lineHeight = fontLineHeight26
    ),
    bodyLarge = TextStyle(
        fontFamily = YsDisplayMedium,
        fontSize = fontSize16,
        lineHeight = fontLineHeight19
    ),
    bodyMedium = TextStyle(
        fontFamily = YsDisplayRegular,
        fontSize = fontSize16,
        lineHeight = fontLineHeight19
    ),
    labelSmall = TextStyle(
        fontFamily = YsDisplayRegular,
        fontSize = fontSize12,
        lineHeight = fontLineHeight16
    )
)
