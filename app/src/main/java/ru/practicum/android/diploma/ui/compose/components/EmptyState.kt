package ru.practicum.android.diploma.ui.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.ErrorImageHeight
import ru.practicum.android.diploma.ui.theme.Spacing16
import ru.practicum.android.diploma.ui.theme.Spacing46

@Composable
fun EmptyState(
    imagePainter: Painter,
    imageHeight: Dp = ErrorImageHeight,
    titleText: String? = null,
    modifier: Modifier
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing16),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = imagePainter,
                contentDescription = titleText ?: stringResource(R.string.empty),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight),
                contentScale = ContentScale.Fit
            )

            titleText?.let { text ->
                Spacer(modifier = Modifier.height(Spacing16))
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing46)
                )
            }
        }
    }
}

@Composable
fun SearchScreenEmptyState(
    modifier: Modifier = Modifier
) {
    EmptyState(
        imagePainter = painterResource(R.drawable.searching_man_image),
        titleText = null,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun SearchScreenEmptyStatePreview() {
    MaterialTheme {
        SearchScreenEmptyState()
    }
}

@Composable
fun NoInternetEmptyState(
    modifier: Modifier = Modifier
) {
    EmptyState(
        imagePainter = painterResource(R.drawable.scull_image),
        titleText = stringResource(R.string.no_internet),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun NoInternetEmptyStatePreview() {
    MaterialTheme {
        NoInternetEmptyState()
    }
}

@Composable
fun NoRegionEmptyState(
    modifier: Modifier = Modifier
) {
    EmptyState(
        imagePainter = painterResource(R.drawable.angry_cat_image),
        titleText = stringResource(R.string.no_region),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun NoRegionEmptyStatePreview() {
    MaterialTheme {
        NoRegionEmptyState()
    }
}

@Composable
fun GetListFailedEmptyState(
    modifier: Modifier = Modifier
) {
    EmptyState(
        imagePainter = painterResource(R.drawable.magic_carpet_image),
        titleText = stringResource(R.string.get_list_failed),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GetListFailedEmptyStatePreview() {
    MaterialTheme {
        GetListFailedEmptyState()
    }
}

@Composable
fun VacancyNotFoundEmptyState(
    modifier: Modifier = Modifier
) {
    EmptyState(
        imagePainter = painterResource(R.drawable.fun_carpet_image),
        titleText = stringResource(R.string.vacancy_not_found),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun VacancyNotFoundEmptyStatePreview() {
    MaterialTheme {
        VacancyNotFoundEmptyState()
    }
}

@Composable
fun ServerErrorMainEmptyState(
    modifier: Modifier = Modifier
) {
    EmptyState(
        imagePainter = painterResource(R.drawable.server_error_image),
        titleText = stringResource(R.string.server_error),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun ServerErrorMainEmptyStatePreview() {
    MaterialTheme {
        ServerErrorMainEmptyState()
    }
}

@Composable
fun ServerErrorVacancyEmptyState(
    modifier: Modifier = Modifier
) {
    EmptyState(
        imagePainter = painterResource(R.drawable.server_error_cat_image),
        titleText = stringResource(R.string.server_error),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun ServerErrorVacancyEmptyStatePreview() {
    MaterialTheme {
        ServerErrorVacancyEmptyState()
    }
}

@Composable
fun FavoritesEmptyState(
    modifier: Modifier = Modifier
) {
    EmptyState(
        imagePainter = painterResource(R.drawable.empty_list_image),
        titleText = stringResource(R.string.empty_list),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun FavoritesEmptyStatePreview() {
    MaterialTheme {
        FavoritesEmptyState()
    }
}

@Composable
fun VacancyFailedState(
    modifier: Modifier = Modifier
) {
    EmptyState(
        imagePainter = painterResource(R.drawable.angry_cat_image),
        titleText = stringResource(R.string.get_vacancy_failed),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun EmptyFavoritesStatePreview() {
    MaterialTheme {
        VacancyFailedState()
    }
}
