package ru.practicum.android.diploma.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

const val BlackColor = 0xFF1A1B22
const val WhiteColor = 0xFFFDFDFD
const val BlueColor = 0xFF3772E7
const val RedColor = 0xFFF56B6C
const val GrayColor = 0xFFAEAFB4
const val LightGrayColor = 0xFFE6E8EB

val Black = Color(BlackColor)
val White = Color(WhiteColor)
val Blue = Color(BlueColor)
val Red = Color(RedColor)
val Gray = Color(GrayColor)
val LightGray = Color(LightGrayColor)

val LightColorScheme = lightColorScheme(
    primary = Blue,
    onPrimary = White,

    tertiary = Red,
    onTertiary = White,

    error = Red,
    onError = White,

    background = White,
    onBackground = Black,

    surface = LightGray,
    onSurface = Black,
    onSurfaceVariant = Gray
)
