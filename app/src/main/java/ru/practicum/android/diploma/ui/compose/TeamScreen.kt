package ru.practicum.android.diploma.ui.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.compose.components.PositiveButton
import ru.practicum.android.diploma.ui.theme.PulseButtonSize
import ru.practicum.android.diploma.ui.theme.Spacing150
import ru.practicum.android.diploma.ui.theme.Spacing50

private const val APPEAR_DELAY = 500L
private const val ANIMATION_TWEEN1 = 1000
private const val ANIMATION_TWEEN2 = 2000
private const val ANIMATION_TWEEN3 = 3000
private const val BUTTON_TWEEN = 1500

@Composable
fun TeamScreen() {
    var showCelebration by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (!showCelebration) {
            CelebrationButton(
                onClick = { showCelebration = true }
            )
        } else {
            FullScreenCelebration()
        }
    }
}

@Composable
fun CelebrationButton(
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 2f,
        targetValue = 2.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(BUTTON_TWEEN, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        PositiveButton(
            text = R.string.team_screen,
            onClick = onClick,
            isEnabled = true,
            modifier = Modifier
                .clip(CircleShape)
                .size(PulseButtonSize)
                .graphicsLayer {
                    scaleX = pulseScale
                    scaleY = pulseScale
                }
        )
    }
}

@Composable
fun FullScreenCelebration() {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.celebrations)
    )
    val colors = MaterialTheme.colorScheme

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(APPEAR_DELAY)
        visible = true
    }

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 99,
        isPlaying = true,
        speed = 1f,
        restartOnPlay = false
    )

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Над приложением работали:",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.primary,
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                ),
                modifier = Modifier
                    .padding(top = Spacing50)
            )
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(ANIMATION_TWEEN3)) +
                    scaleIn(
                        initialScale = 2f,
                        animationSpec = tween(ANIMATION_TWEEN3, easing = FastOutSlowInEasing)
                    ),
                exit = fadeOut() + scaleOut()
            ) {
                Text(
                    text = "Бянкин Андрей",
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = colors.primary,
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(2f, 2f),
                            blurRadius = 4f
                        )
                    ),
                    modifier = Modifier
                        .padding(top = Spacing150)
                )
            }
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(ANIMATION_TWEEN2)) +
                    scaleIn(
                        initialScale = 2f,
                        animationSpec = tween(ANIMATION_TWEEN2, easing = FastOutSlowInEasing)
                    ),
                exit = fadeOut() + scaleOut()
            ) {
                Text(
                    text = "Кузнецов Максим",
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = colors.primary,
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(2f, 2f),
                            blurRadius = 4f
                        )
                    ),
                    modifier = Modifier
                        .padding(top = Spacing50)
                )
            }
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(ANIMATION_TWEEN1)) +
                    scaleIn(
                        initialScale = 2f,
                        animationSpec = tween(ANIMATION_TWEEN1, easing = FastOutSlowInEasing)
                    ),
                exit = fadeOut() + scaleOut()
            ) {
                Text(
                    text = "Лосев Денис",
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = colors.primary,
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(2f, 2f),
                            blurRadius = 4f
                        )
                    ),
                    modifier = Modifier
                        .padding(top = Spacing50)
                )
            }
        }

    }
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier.fillMaxSize()
    )
}
