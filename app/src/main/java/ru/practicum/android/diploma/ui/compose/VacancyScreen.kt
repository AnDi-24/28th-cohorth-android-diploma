package ru.practicum.android.diploma.ui.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.domain.network.models.VacancyDetailsModel
import ru.practicum.android.diploma.presentation.VacancyDetailsViewModel

@Composable
fun VacancyScreen(vacancyId: String?) {
    val viewModel: VacancyDetailsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentState = uiState

    LaunchedEffect(vacancyId) {
        if (vacancyId != null) {
            viewModel.searchVacancyDetails(vacancyId)
        }
    }

    when (currentState) {
        VacancyDetailsViewModel.VacancyUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        VacancyDetailsViewModel.VacancyUiState.Idle -> {
        }

        is VacancyDetailsViewModel.VacancyUiState.Success -> {
            val vacancy = currentState.vacancy
            VacancyContent(vacancy)
        }

        is VacancyDetailsViewModel.VacancyUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentState.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun VacancyContent(vacancy: VacancyDetailsModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        item {
            vacancy.url?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        item {
            Text(
                text = vacancy.name,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                }
            }
        }
    }
}

// @Composable
// fun VacancyScreen(navController: NavController) {
//    val viewModel: VacancyDetailsViewModel = koinViewModel()
//    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
//    // Создаем локальную копию для smart cast
//    val currentState = uiState
//
//    val vacancyData: VacancyDetailsModel = when (currentState) {
//        is VacancyDetailsViewModel.VacancyUiState.Success -> {
//            val vacancy = currentState.vacancy
//            VacancyDetailsModel(
//                id = vacancy.id,
//                name = vacancy.name,
//                salary = vacancy.salary,
//                address = vacancy.address,
//                experience = vacancy.experience,
//                schedule = vacancy.schedule,
//                employment = vacancy.employment,
//                description = vacancy.description,
//                employer = vacancy.employer,
//                skills = vacancy.skills,
//                url = vacancy.url
//            )
//        }
//
//        else -> {
//        }
//    } as VacancyDetailsModel
//
//    LazyColumn(
//        modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(16.dp),
//        verticalArrangement = Arrangement.spacedBy(3.dp)
//    ) {
//        item {
//            vacancyData.url?.let {
//                Text(
//                    text = it,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
//        }
//
//        item {
// //            Text(
// //                text = vacancyData.salary,
// //                fontWeight = FontWeight.Bold,
// //                modifier = Modifier.fillMaxWidth()
// //            )
//        }
//
//        item {
//            Surface(
//                modifier = Modifier.fillMaxWidth()
//                    .height(100.dp),
//                shape = MaterialTheme.shapes.medium,
//                color = MaterialTheme.colorScheme.surfaceVariant,
//                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(16.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    EmployerLogoGlide(
//                        logoUrl = vacancyData.url, // Используем URL логотипа
//                        modifier = Modifier
//                            .size(80.dp)
//                            .clip(RoundedCornerShape(8.dp))
//                    )
//                }
//            }
//        }
//    }
// }
// @Composable
// fun EmployerLogoGlide(
//    logoUrl: String?,
//    modifier: Modifier = Modifier
// ) {
//    AndroidView(
//        factory = { context ->
//            ImageView(context).apply {
//                scaleType = ImageView.ScaleType.CENTER_CROP
//                adjustViewBounds = true
//            }
//        },
//        update = { imageView ->
//            // Определяем что загружать
//            logoUrl?.let { url ->
//                Glide.with(imageView.context)
//                    .load(url)
//                    .placeholder(R.drawable.team)
//                    .into(imageView)
//            }
//        },
//        modifier = modifier
//    )
// }
