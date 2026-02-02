package ru.practicum.android.diploma.util

import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import ru.practicum.android.diploma.R

@Composable
fun EmployerLogoGlide(
    logoUrl: String?,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                adjustViewBounds = true
            }
        },
        update = { imageView ->
            logoUrl?.let { url ->
                Glide.with(imageView.context)
                    .load(url)
                    .placeholder(R.drawable.team)
                    .into(imageView)
            }
        },
        modifier = modifier
    )
}
