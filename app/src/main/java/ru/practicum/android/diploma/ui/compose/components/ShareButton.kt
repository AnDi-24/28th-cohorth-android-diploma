package ru.practicum.android.diploma.ui.compose.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.VacancyDetailsViewModel

@Composable
fun ShareButton(viewModel: VacancyDetailsViewModel) {
    val context = LocalContext.current

    IconButton(
        onClick = { viewModel.shareVacancy(context) }
    )
    {
        Icon(
            painter = painterResource(id = R.drawable.ic_sharing),
            contentDescription = null
        )
    }
}
