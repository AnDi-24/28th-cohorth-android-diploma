package ru.practicum.android.diploma.presentation

import android.util.Log
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.network.api.FindVacancyInteractor
import ru.practicum.android.diploma.domain.network.api.industries.IndustriesInteractor
import ru.practicum.android.diploma.domain.network.models.SearchParams
import ru.practicum.android.diploma.domain.network.models.VacancyDetailsModel
import ru.practicum.android.diploma.domain.network.models.industries.IndustryModel
import ru.practicum.android.diploma.presentation.models.IndustryUiState
import ru.practicum.android.diploma.presentation.models.VacancySearchUiState
import ru.practicum.android.diploma.util.Resource
import java.io.IOException

const val NETWORK_ERROR = "Ошибка сети"

class SearchViewModel(
    val searchInteractor: FindVacancyInteractor,
    val industryInteractor: IndustriesInteractor,
) : ViewModel() {

    private var searchJob: Job? = null
    private val _uiState = mutableStateOf<VacancySearchUiState>(VacancySearchUiState.Idle)
    val uiState: State<VacancySearchUiState> get() = _uiState
    private val _toastMessage = MutableSharedFlow<String>()

    private val _filterUiState = mutableStateOf<IndustryUiState>(IndustryUiState.Selected(emptyList(), false))
    val filterUiState: State<IndustryUiState> get() = _filterUiState
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    val toastMessage: SharedFlow<String> = _toastMessage.asSharedFlow()
    private var currentPage by mutableIntStateOf(0)
    private var maxPages by mutableIntStateOf(0)
    private var isLoadingMore by mutableStateOf(false)
    private val vacanciesList = mutableStateListOf<VacancyDetailsModel>()
    private var totalFound by mutableIntStateOf(0)
    private var lastShownToastMessage: String? = null
    private var currentQuery = ""

    init {
        searchIndustries("")
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

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
                loadMore()
            }

            try {
                searchInteractor.getListVacancies(
                    searchParams
                ).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            isSuccess(resource)
                        }

                        is Resource.Error -> {
                            isError(resource.message ?: "", isLoadMore)
                        }
                    }
                }
            } catch (e: IOException) {
                Log.d("Exception Message", "Exception $e")
                isError(NETWORK_ERROR, isLoadMore)
            }
        }
    }

    fun searchIndustries(query: String) {
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            try {
                when (val resource = industryInteractor.getIndustries()) {
                    is Resource.Success -> {
                        _filterUiState.value = IndustryUiState.Selected(resource.data?.filter { industry ->
                            industry.name.contains(query, ignoreCase = true)
                        } ?: emptyList(), false)
                    }

                    is Resource.Error -> {
                        handleError(resource.message ?: "")
                    }
                }

            } catch (e: IOException) {
                Log.d("Exception Message", "Exception $e")
                handleError(NETWORK_ERROR)
            }
        }
    }

    fun queryFiller(choose: IndustryModel) {
        _searchQuery.value = choose.name
        val chosenOne = listOf(
            choose
        )
        _filterUiState.value = IndustryUiState.Selected(chosenOne, true)

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
        lastShownToastMessage = null
    }

    private fun isError(resourceMessage: String, isLoadMore: Boolean) {
        isLoadingMore = false
        if (isLoadMore) {
            showToastMessage(getErrorMessage(resourceMessage))
            if (vacanciesList.isNotEmpty()) {
                _uiState.value = VacancySearchUiState.Success(
                    vacancies = vacanciesList.toList(),
                    totalFound = totalFound,
                    isLoadingMore = false,
                    isLastPage = isLastPage()
                )
            }
        } else {
            _uiState.value = handleError(resourceMessage)
        }
    }

    private fun loadMore() {
        if (vacanciesList.isNotEmpty()) {
            _uiState.value = VacancySearchUiState.Success(
                vacancies = vacanciesList.toList(),
                totalFound = totalFound,
                isLoadingMore = true,
                isLastPage = isLastPage()
            )
        }
    }

    private fun showToastMessage(message: String) {
        if (lastShownToastMessage != message) {
            lastShownToastMessage = message
            viewModelScope.launch {
                _toastMessage.emit(message)
            }
        }
    }

    private fun loadLogic(query: String, isLoadMore: Boolean = false) {
        if (query.isEmpty()) {
            clearSearch()
        }

        if (!isLoadMore || currentQuery != query) {
            currentQuery = query
            currentPage = 1
            maxPages = 0
            vacanciesList.clear()
            isLoadingMore = false
            lastShownToastMessage = null
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
        searchIndustries("")
        _searchQuery.value = ""
        vacanciesList.clear()
        currentPage = 0
        maxPages = 0
        currentQuery = ""
        isLoadingMore = false
        lastShownToastMessage = null
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }

    private fun handleError(errorMessage: String?): VacancySearchUiState {
        return when (errorMessage) {
            "Данные не найдены" -> VacancySearchUiState.Empty
            NETWORK_ERROR -> VacancySearchUiState.NetworkError
            "Неверный тип запроса" -> VacancySearchUiState.UnknownError
            "Неизвестная ошибка" -> VacancySearchUiState.UnknownError
            else -> VacancySearchUiState.NetworkError
        }
    }

    private fun getErrorMessage(errorMessage: String?): String {
        return when (errorMessage) {
            NETWORK_ERROR -> "Проверьте подключение к интернету"
            else -> "Произошла ошибка"
        }
    }
}
