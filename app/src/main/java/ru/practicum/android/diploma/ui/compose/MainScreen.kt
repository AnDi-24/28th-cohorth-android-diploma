package ru.practicum.android.diploma.ui.compose

import android.content.Context
import android.content.res.Resources
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.network.models.VacancyDetailsModel
import ru.practicum.android.diploma.presentation.SearchViewModel
import ru.practicum.android.diploma.presentation.models.VacancySearchUiState
import ru.practicum.android.diploma.ui.compose.components.NoInternetEmptyState
import ru.practicum.android.diploma.ui.compose.components.SearchField
import ru.practicum.android.diploma.ui.compose.components.SearchScreenEmptyState
import ru.practicum.android.diploma.ui.compose.components.VacancyFailedState
import ru.practicum.android.diploma.ui.compose.components.VacancyListItem
import ru.practicum.android.diploma.ui.theme.BorderWidthThin
import ru.practicum.android.diploma.ui.theme.BoxHeight100
import ru.practicum.android.diploma.ui.theme.BoxHeight27
import ru.practicum.android.diploma.ui.theme.BoxHeight40
import ru.practicum.android.diploma.ui.theme.IconSize
import ru.practicum.android.diploma.ui.theme.Spacing12
import ru.practicum.android.diploma.ui.theme.Spacing16
import ru.practicum.android.diploma.ui.theme.Spacing40
import ru.practicum.android.diploma.ui.theme.Typography

@Composable
fun MainScreen(
    viewModel: SearchViewModel,
    navController: NavController
) {
    val colors = MaterialTheme.colorScheme
    val uiState by viewModel.uiState
    val lazyListState = rememberLazyListState()
    val context = LocalContext.current
    val searchPerformed = remember { mutableStateOf(false) }

    val vacancySearchQuery by viewModel.vacancySearchQuery.collectAsState()

    HandleToastMessages(viewModel, context)
    HandleScrollToTop(uiState, lazyListState, searchPerformed)
    HandleInfiniteScroll(lazyListState, viewModel)

    Column(modifier = Modifier.padding(Spacing16)) {
        SearchField(
            label = stringResource(R.string.request_placeholder),
            viewModel = viewModel,
            screenTag = MAIN,
            query = vacancySearchQuery,
            onQueryChange = { viewModel.setVacancySearchQuery(it) },
            onSearch = { query ->
                viewModel.searchVacancies(query)
            }
        )

        when (uiState) {
            VacancySearchUiState.Idle -> IdleState()
            VacancySearchUiState.Loading -> LoadingState(colors)
            is VacancySearchUiState.Success -> SuccessState(
                uiState as VacancySearchUiState.Success,
                navController,
                lazyListState,
                colors,
                context
            )

            is VacancySearchUiState.Empty -> FailedState()
            is VacancySearchUiState.UnknownError -> FailedState()
            is VacancySearchUiState.NetworkError -> NetworkErrorState()
        }
    }
}

@Composable
private fun HandleToastMessages(viewModel: SearchViewModel, context: Context) {
    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect { message ->
            withContext(Dispatchers.Main) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
private fun HandleScrollToTop(
    uiState: VacancySearchUiState,
    lazyListState: LazyListState,
    searchPerformed: MutableState<Boolean>
) {
    LaunchedEffect(uiState) {
        when {
            uiState is VacancySearchUiState.Success &&
                (uiState as VacancySearchUiState.Success).vacancies.isNotEmpty() &&
                searchPerformed.value -> {
                lazyListState.scrollToItem(0)
                searchPerformed.value = false
            }

            uiState is VacancySearchUiState.Loading -> {
                searchPerformed.value = true
            }

            else -> {
                searchPerformed.value = false
            }
        }
    }
}

@Composable
private fun HandleInfiniteScroll(lazyListState: LazyListState, viewModel: SearchViewModel) {
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                if (visibleItems.isNotEmpty()) {
                    val lastVisibleItem = visibleItems.last()
                    val totalItems = lazyListState.layoutInfo.totalItemsCount

                    if (lastVisibleItem.index >= totalItems - 2) {
                        viewModel.loadMoreVacancies()
                    }
                }
            }
    }
}

@Composable
private fun IdleState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SearchScreenEmptyState()
    }
}

@Composable
private fun LoadingState(colors: ColorScheme) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .height(BoxHeight100),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(IconSize),
            color = colors.primary
        )
    }
}

@Composable
private fun SuccessState(
    successState: VacancySearchUiState.Success,
    navController: NavController,
    lazyListState: LazyListState,
    colors: ColorScheme,
    context: Context
) {
    val vacancies = successState.vacancies
    val totalFound = successState.totalFound
    val isLoadingMore = successState.isLoadingMore
    val isLastPage = successState.isLastPage
    val resources = LocalResources.current

    Box(modifier = Modifier.fillMaxSize()) {
        VacancyList(
            vacancies = vacancies,
            navController = navController,
            lazyListState = lazyListState,
            isLoadingMore = isLoadingMore,
            colors = colors
        )

        if (isLastPage && vacancies.isNotEmpty()) {
            LaunchedEffect(isLastPage) {
                Toast.makeText(context, "Все вакансии загружены", Toast.LENGTH_SHORT).show()
            }
        }

        if (vacancies.isNotEmpty()) {
            ResultsCounter(totalFound, colors, resources)
        }
    }
}

@Composable
private fun VacancyList(
    vacancies: List<VacancyDetailsModel>,
    navController: NavController,
    lazyListState: LazyListState,
    isLoadingMore: Boolean,
    colors: ColorScheme
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = Spacing40),
        state = lazyListState
    ) {
        items(vacancies) { vacancy ->
            VacancyListItem(
                vacancy,
                onClick = { navController.navigate("vacancy/${vacancy.id}") }
            )
        }

        if (isLoadingMore) {
            item {
                LoadingMoreIndicator(colors)
            }
        }
    }
}

@Composable
private fun LoadingMoreIndicator(colors: ColorScheme) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing16),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(IconSize),
            color = colors.primary
        )
    }
}

@Composable
private fun ResultsCounter(totalFound: Int, colors: ColorScheme, resources: Resources) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(BoxHeight40),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .height(BoxHeight27)
                .wrapContentWidth()
                .clip(MaterialTheme.shapes.extraLarge)
                .border(
                    width = BorderWidthThin,
                    color = colors.primary,
                    shape = MaterialTheme.shapes.extraLarge
                )
                .background(colors.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Найдено ${
                    resources.getQuantityString(
                        R.plurals.vacancies_count,
                        totalFound,
                        totalFound
                    )
                }",
                style = Typography.bodyMedium,
                color = colors.onPrimary,
                modifier = Modifier.padding(horizontal = Spacing12)
            )
        }
    }
}

@Composable
private fun FailedState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        VacancyFailedState()
    }
}

@Composable
private fun NetworkErrorState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        NoInternetEmptyState()
    }
}
