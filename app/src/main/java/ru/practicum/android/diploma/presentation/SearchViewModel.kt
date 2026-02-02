package ru.practicum.android.diploma.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.network.api.FindVacancyInteractor
import ru.practicum.android.diploma.domain.network.models.SearchParams
import ru.practicum.android.diploma.domain.network.models.VacancyDetailsModel
import ru.practicum.android.diploma.presentation.models.VacancySearchUiState
import ru.practicum.android.diploma.util.Resource
import java.io.IOException
import java.net.SocketTimeoutException

class SearchViewModel(
    val interactor: FindVacancyInteractor
) : ViewModel() {

    private var searchJob: Job? = null
    private val _uiState = mutableStateOf<VacancySearchUiState>(VacancySearchUiState.Idle)
    val uiState: State<VacancySearchUiState> get() = _uiState

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage.asSharedFlow()

    private var currentPage by mutableIntStateOf(0)
    private var maxPages by mutableIntStateOf(0)
    private var isLoadingMore by mutableStateOf(false)
    private val vacanciesList = mutableStateListOf<VacancyDetailsModel>()
    private var totalFound by mutableIntStateOf(0)
    private var currentQuery = ""

    fun searchVacancies(query: String, isLoadMore: Boolean = false) {
        searchJob?.cancel()

        loadLogic(query = query, isLoadMore = isLoadMore)

        val searchParams = SearchParams(
            area = null,
            industry = null,
            text = query,
            salary = null,
            page = currentPage,
            onlyWithSalary = false
        )

        searchJob = viewModelScope.launch {
            if (!isLoadMore) {
                _uiState.value = VacancySearchUiState.Loading
            } else {
                isLoadingMore = true
                if (vacanciesList.isNotEmpty()) {
                    _uiState.value = VacancySearchUiState.Success(
                        vacancies = vacanciesList.toList(),
                        totalFound = totalFound,
                        isLoadingMore = true,
                        isLastPage = isLastPage()
                    )
                }
            }

            try {
                interactor.getListVacancies(
                    searchParams
                ).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            isSuccess(resource)
                        }

                        is Resource.Error -> {
                            isError(resource, isLoadMore)
                        }
                    }
                }
            } catch (e: Exception) {
                isException(isLoadMore, e)
            }
        }
    }

    private fun isSuccess(resource: Resource<Triple<List<VacancyDetailsModel>, Int, Int>>) {
        val data = resource.data
        val newVacancies = data?.first ?: emptyList()
        totalFound = data?.second ?: 0
        maxPages = resource.data?.third ?: 0

        vacanciesList.addAll(newVacancies)

        currentPage++

        if (vacanciesList.isEmpty()) {
            _uiState.value = VacancySearchUiState.Empty
        } else {
            _uiState.value = VacancySearchUiState.Success(
                vacancies = vacanciesList.toList(),
                totalFound = totalFound,
                isLoadingMore = false,
                isLastPage = isLastPage()
            )
        }

        isLoadingMore = false
    }

    private fun isError(resource: Resource<Triple<List<VacancyDetailsModel>, Int, Int>>, isLoadMore: Boolean) {
        isLoadingMore = false
        if (isLoadMore) {
            showToastMessage(getErrorMessage(resource.message))
            if (vacanciesList.isNotEmpty()) {
                _uiState.value = VacancySearchUiState.Success(
                    vacancies = vacanciesList.toList(),
                    totalFound = totalFound,
                    isLoadingMore = false,
                    isLastPage = isLastPage()
                )
            }
        } else {
            _uiState.value = handleError(resource.message)
        }
    }

    private fun isException(isLoadMore: Boolean, e: Exception) {
        isLoadingMore = false
        if (isLoadMore) {
            showToastMessage(getExceptionMessage(e))
            if (vacanciesList.isNotEmpty()) {
                _uiState.value = VacancySearchUiState.Success(
                    vacancies = vacanciesList.toList(),
                    totalFound = totalFound,
                    isLoadingMore = false,
                    isLastPage = isLastPage()
                )
            }
        } else {
            _uiState.value = handleException(e)
        }
    }

    private fun showToastMessage(message: String) {
        viewModelScope.launch {
            _toastMessage.emit(message)
        }
    }

    private fun loadLogic(query: String, isLoadMore: Boolean = false) {

        if (!isLoadMore || currentQuery != query) {
            currentQuery = query
            currentPage = 0
            maxPages = 0
            vacanciesList.clear()
            isLoadingMore = false
        }

        if (isLoadMore && isLastPage()) {
            isLoadingMore = false
            return
        }
    }

    private fun isLastPage(): Boolean {
        return maxPages in 1..currentPage
    }

    fun loadMoreVacancies() {
        if (!isLoadingMore && !isLastPage() && currentQuery.isNotEmpty()) {
            searchVacancies(currentQuery, isLoadMore = true)
        }
    }

    fun clearSearch() {
        searchJob?.cancel()
        _uiState.value = VacancySearchUiState.Idle
        vacanciesList.clear()
        currentPage = 0
        maxPages = 0
        currentQuery = ""
        isLoadingMore = false
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }

    private fun handleException(e: Exception): VacancySearchUiState {
        return when (e) {
            is SocketTimeoutException -> {
                VacancySearchUiState.NetworkError
            }

            is java.net.UnknownHostException -> {
                VacancySearchUiState.NetworkError
            }

            is IOException -> {
                VacancySearchUiState.NetworkError
            }

            else -> {
                VacancySearchUiState.UnknownError
            }
        }
    }

    private fun handleError(errorMessage: String?): VacancySearchUiState {
        return when (errorMessage) {
            "Данные не найдены" -> VacancySearchUiState.Empty
            "Ошибка сети" -> VacancySearchUiState.NetworkError
            "Неверный тип запроса" -> VacancySearchUiState.UnknownError
            "Неизвестная ошибка" -> VacancySearchUiState.UnknownError
            else -> VacancySearchUiState.Empty
        }
    }

    private fun getExceptionMessage(e: Exception): String {
        return when (e) {
            is SocketTimeoutException,
            is java.net.UnknownHostException,
            is IOException -> "Проверьте подключение к интернету"

            else -> "Произошла ошибка"
        }
    }

    private fun getErrorMessage(errorMessage: String?): String {
        return when (errorMessage) {
            "Ошибка сети" -> "Проверьте подключение к интернету"
            else -> "Произошла ошибка"
        }
    }
}

