package ru.practicum.android.diploma.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.network.api.FindVacancyInteractor
import ru.practicum.android.diploma.domain.network.models.VacancyDetailsModel
import ru.practicum.android.diploma.util.Resource
import kotlin.coroutines.cancellation.CancellationException

class SearchViewModel(
    val interactor: FindVacancyInteractor
) : ViewModel() {

    private var searchJob: Job? = null

    private val _uiState = mutableStateOf<VacancySearchUiState>(VacancySearchUiState.Idle)
    val uiState: State<VacancySearchUiState> get() = _uiState
    var totalFound by mutableStateOf(0)

    fun searchVacancies(query: String) {
        searchJob?.cancel()

        if (query.isEmpty()) {
            _uiState.value = VacancySearchUiState.Idle
            return
        }

        searchJob = viewModelScope.launch {
            _uiState.value = VacancySearchUiState.Loading

            try {
                interactor.getListVacancies(
                    area = null,
                    industry = null,
                    text = query,
                    salary = null,
                    page = 0,
                    onlyWithSalary = false
                ).collect { resource ->
                    _uiState.value = when (resource) {
                        is Resource.Success -> {
                            val data = resource.data
                            val vacancyList = data?.first ?: emptyList()
                            totalFound = data?.second ?: 0
                            if (vacancyList.isEmpty()) {
                                VacancySearchUiState.Empty
                            } else {
                                val vacancies = vacancyList.map { dto ->
                                    VacancyDetailsModel(
                                        id = dto.id!!,
                                        name = dto.name!!,
                                    )
                                }
                                VacancySearchUiState.Success(vacancies, totalFound)
                            }
                        }

                        is Resource.Error -> {
                            when (resource.message) {
                                "Данные не найдены" -> VacancySearchUiState.Empty
                                "Ошибка сети" -> VacancySearchUiState.NetworkError
                                "Неверный тип запроса" -> VacancySearchUiState.UnknownError
                                "Неизвестная ошибка" -> VacancySearchUiState.UnknownError
                                else -> {
                                    return@collect
                                }
                            }
                        }
                    }
                }
            } catch (e: CancellationException) {
            } catch (e: Exception) {
                _uiState.value = VacancySearchUiState.NetworkError
            }
        }
    }

    fun clearSearch() {
        searchJob?.cancel()
        _uiState.value = VacancySearchUiState.Idle
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }
}

sealed interface VacancySearchUiState {
    object Idle : VacancySearchUiState
    object Loading : VacancySearchUiState
    data class Success(val vacancies: List<VacancyDetailsModel>, val totalFound: Int) : VacancySearchUiState
    object Empty : VacancySearchUiState
    object NetworkError : VacancySearchUiState
    object UnknownError : VacancySearchUiState
}
