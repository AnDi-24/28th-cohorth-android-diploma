package ru.practicum.android.diploma.ui.compose.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.VacancyDetailsViewModel

@Composable
fun FavoriteButton(
    viewModel: VacancyDetailsViewModel,
    modifier: Modifier = Modifier
) {
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()

    IconButton(
        onClick = {
            viewModel.toggleFavorite()
        },
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(
                id = if (isFavorite) {
                    R.drawable.favorites_on
                } else {
                    R.drawable.favorites_off
                }
            ),
            contentDescription = if (isFavorite) {
                stringResource(R.string.delete_from_favorites)
            } else {
                stringResource(R.string.add_to_favorites)
            },
            tint = if (isFavorite) Color.Red else Color.Gray
        )
    }
}
