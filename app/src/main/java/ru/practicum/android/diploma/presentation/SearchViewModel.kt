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
import ru.practicum.android.diploma.domain.prefs.FilterSettingsModel
import ru.practicum.android.diploma.domain.prefs.PrefsInteractor
import ru.practicum.android.diploma.presentation.models.IndustryUiState
import ru.practicum.android.diploma.presentation.models.VacancySearchUiState
import ru.practicum.android.diploma.util.Resource
import java.io.IOException

const val NETWORK_ERROR = "Ошибка сети"

class SearchViewModel(
    private val searchInteractor: FindVacancyInteractor,
//    private val industryInteractor: IndustriesInteractor,
    private val prefsInteractor: PrefsInteractor
) : ViewModel() {
    private var searchJob: Job? = null
    private val _uiState = mutableStateOf<VacancySearchUiState>(VacancySearchUiState.Idle)
    val uiState: State<VacancySearchUiState> get() = _uiState
    private val _toastMessage = MutableSharedFlow<String>()

    private var lastRequestString: String = ""

    private val _vacancySearchQuery = MutableStateFlow("")
    val vacancySearchQuery: StateFlow<String> = _vacancySearchQuery.asStateFlow()

    val toastMessage: SharedFlow<String> = _toastMessage.asSharedFlow()
    private var currentPage by mutableIntStateOf(0)
    private var maxPages by mutableIntStateOf(0)
    private var isLoadingMore by mutableStateOf(false)
    private val vacanciesList = mutableStateListOf<VacancyDetailsModel>()
    private var totalFound by mutableIntStateOf(0)
    private var lastShownToastMessage: String? = null
    private var currentQuery = ""

    fun setVacancySearchQuery(query: String) {
        _vacancySearchQuery.value = query
    }

    // Функция для формирования строки запроса
    private fun buildRequestString(
        query: String,
        industryId: Int?,
        salary: Int?,
        page: Int,
        showSalary: Boolean
    ): String {
        val params = mutableListOf<String>()

        if (query.isNotEmpty()) params.add("text=$query")
        industryId?.let { params.add("industry=$it") }
        salary?.takeIf { it > 0 }?.let { params.add("salary=$it") }
        params.add("page=$page")
        if (showSalary) params.add("only_with_salary=true")

        // Сортируем параметры для стабильного сравнения
        return params.sorted().joinToString("&")
    }

    // Проверяем, изменился ли запрос
    private fun hasRequestChanged(newRequestString: String): Boolean {
        return newRequestString != lastRequestString
    }

    private fun updateLastRequest(newRequestString: String) {
        lastRequestString = newRequestString
        Log.d("SearchViewModel", "Сохранен запрос: $newRequestString")
    }

    fun searchVacancies(query: String, isLoadMore: Boolean = false) {
        searchJob?.cancel()

        if (query.isEmpty() && !isLoadMore) {
            clearSearch()
            return
        }

        val filters = prefsInteractor.getFilterSettings()
        val industryId = filters?.industry?.takeIf { it.isNotEmpty() }?.toIntOrNull()
        val page = if (isLoadMore) currentPage else 0

        // Формируем строку текущего запроса
        val currentRequestString = buildRequestString(
            query = query,
            industryId = industryId,
            salary = filters?.salary,
            page = page,
            showSalary = filters?.showSalary ?: false
        )

        // Для дозагрузки всегда выполняем
        if (isLoadMore) {
            // Логируем запрос
            Log.d("API_DEBUG", "GET {{baseUrl}}/vacancies?$currentRequestString")

            // Выполняем поиск
            executeSearch(query, industryId, filters, page, isLoadMore)
            return
        }

        // Для нового поиска проверяем, изменился ли запрос
        val requestChanged = hasRequestChanged(currentRequestString)

        if (requestChanged) {
            // Логируем запрос
            Log.d("API_DEBUG", "GET {{baseUrl}}/vacancies?$currentRequestString")

            // Сохраняем новый запрос
            updateLastRequest(currentRequestString)

            // Сбрасываем состояние для нового поиска
            currentQuery = query
            currentPage = 0
            maxPages = 0
            vacanciesList.clear()
            isLoadingMore = false
            lastShownToastMessage = null

            // Выполняем поиск
            executeSearch(query, industryId, filters, page, isLoadMore)
        } else {
            Log.d("SearchViewModel", "Запрос не изменился: $currentRequestString")
            Log.d("SearchViewModel", "Предыдущий запрос: $lastRequestString")
        }
    }

    private fun executeSearch(
        query: String,
        industryId: Int?,
        filters: FilterSettingsModel?,
        page: Int,
        isLoadMore: Boolean
    ) {
        val searchParams = SearchParams(
            area = null,
            industry = industryId,
            text = query,
            salary = filters?.salary?.takeIf { it > 0 },
            page = page,
            onlyWithSalary = filters?.showSalary ?: false
        )

        searchJob = viewModelScope.launch {
            if (!isLoadMore) {
                _uiState.value = VacancySearchUiState.Loading
            } else {
                isLoadingMore = true
                loadMore()
            }

            try {
                searchInteractor.getListVacancies(searchParams).collect { resource ->
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
                isError(NETWORK_ERROR, isLoadMore)
            }
        }
    }

    fun searchWithFilters() {
        viewModelScope.launch {
            searchVacancies(currentQuery)
        }
    }

    private fun isSuccess(resource: Resource<Triple<List<VacancyDetailsModel>, Int, Int>>) {
        val data = resource.data
        val newVacancies = data?.first ?: emptyList()
        totalFound = data?.second ?: 0
        maxPages = data?.third ?: 0

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

    private fun isLastPage(): Boolean {
        return maxPages > 0 && currentPage >= maxPages
    }

    fun loadMoreVacancies() {
        if (!isLoadingMore && !isLastPage() && currentQuery.isNotEmpty()) {
            searchVacancies(currentQuery, isLoadMore = true)
        }
    }

    fun clearSearch() {
        searchJob?.cancel()
//        searchDebounceJob?.cancel()
        _uiState.value = VacancySearchUiState.Idle
        _vacancySearchQuery.value = ""
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
//        searchDebounceJob?.cancel()
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

//    private var lastAppliedFilters: FilterSettingsModel? = null
//    private var filtersHash: Int = 0
//
//
//
//    private val _filterUiState = mutableStateOf<IndustryUiState>(IndustryUiState.OnSelect(emptyList()))
//    val filterUiState: State<IndustryUiState> get() = _filterUiState
//
//
//
//    private val _industrySearchQuery = MutableStateFlow("")
//    val industrySearchQuery: StateFlow<String> = _industrySearchQuery.asStateFlow()



//    private var searchDebounceJob: Job? = null
//
//    init {
//        loadLastAppliedFilters()
//        searchIndustries("")
//    }
//
//    private fun loadLastAppliedFilters() {
//        lastAppliedFilters = prefsInteractor.getFilterSettings()
//        filtersHash = lastAppliedFilters.hashCode()
//    }
//
//
//
//    fun setIndustrySearchQuery(query: String) {
//        _industrySearchQuery.value = query
//    }

//
//
//
//
//    fun searchIndustries(query: String) {
//        searchJob?.cancel()
//
//        searchJob = viewModelScope.launch {
//            try {
//                when (val resource = industryInteractor.getIndustries()) {
//                    is Resource.Success -> {
//                        val allIndustries = resource.data ?: emptyList()
//                        val filteredIndustries = if (query.isNotEmpty()) {
//                            allIndustries.filter { industry ->
//                                industry.name.contains(query, ignoreCase = true)
//                            }
//                        } else {
//                            allIndustries
//                        }
//                        _filterUiState.value = IndustryUiState.OnSelect(filteredIndustries)
//                    }
//
//                    is Resource.Error -> {
//                        // Просто логируем ошибку для отраслей
//                        Log.d("SearchViewModel", "Error loading industries: ${resource.message}")
//                    }
//                }
//            } catch (e: IOException) {
//                Log.d("SearchViewModel", "Network error loading industries: $e")
//            }
//        }
//    }
//
//    fun selectedIndustry(industry: IndustryModel) {
//        _industrySearchQuery.value = industry.name
//        _filterUiState.value = IndustryUiState.Selected(industry)
//    }

















}
