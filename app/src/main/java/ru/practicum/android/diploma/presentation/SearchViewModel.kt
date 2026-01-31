package ru.practicum.android.diploma.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.network.api.FindVacancyInteractor
import ru.practicum.android.diploma.domain.network.models.VacancyDetailsModel
import ru.practicum.android.diploma.util.Resource
import kotlin.coroutines.cancellation.CancellationException

//class SearchViewModel(
//    val interactor: FindVacancyInteractor
//) : ViewModel() {
//
//    var query by mutableStateOf("")
//
//    private val _uiState = mutableStateOf<VacancySearchUiState>(VacancySearchUiState.Idle)
//    val uiState: State<VacancySearchUiState> get() = _uiState
//
//
//    fun searchVacancies() {
//
//        viewModelScope.launch {
//            _uiState.value = VacancySearchUiState.Loading
//
//            interactor.getListVacancies(
//                area = 1,
//                industry = null,
//                text = query,
//                salary = null,
//                page = 1,
//                onlyWithSalary = false
//            ).collect { resource ->
//                _uiState.value = when (resource) {
//                    is Resource.Success -> {
//                        val data = resource.data
//                        if (data == null || data.isEmpty()) {
//                            VacancySearchUiState.Empty
//                        } else {
//                            // Преобразуем DTO в модель для UI
//                            val vacancies = data.map { dto ->
//                                VacancyDetailsModel(
//                                    id = dto.id!!,
//                                    name = dto.name!!,
//                                    // другие поля по необходимости
//                                )
//                            }
//                            VacancySearchUiState.Success(vacancies)
//                        }
//                    }
//                    is Resource.Error -> {
//                        VacancySearchUiState.Error(resource.message ?: "Ошибка загрузки")
//                    }
//                }
//            }
//        }
//    }
//}

class SearchViewModel(
    val interactor: FindVacancyInteractor
) : ViewModel() {

    // Убираем публичную переменную query, так как теперь она управляется в Composable
    private var searchJob: Job? = null

    private val _uiState = mutableStateOf<VacancySearchUiState>(VacancySearchUiState.Idle)
    val uiState: State<VacancySearchUiState> get() = _uiState

    // Функция для выполнения поиска по запросу
    fun performSearch(query: String) {
        // Отменяем предыдущий поиск
        searchJob?.cancel()

        // Если запрос пустой - показываем Idle состояние
        if (query.isEmpty()) {
            _uiState.value = VacancySearchUiState.Idle
            return
        }

        searchJob = viewModelScope.launch {
            _uiState.value = VacancySearchUiState.Loading

            try {
                interactor.getListVacancies(
                    area = 1,
                    industry = null,
                    text = query,
                    salary = null,
                    page = 1,
                    onlyWithSalary = false
                ).collect { resource ->
                    _uiState.value = when (resource) {
                        is Resource.Success -> {
                            val data = resource.data
                            if (data == null || data.isEmpty()) {
                                VacancySearchUiState.Empty
                            } else {
                                val vacancies = data.map { dto ->
                                    VacancyDetailsModel(
                                        id = dto.id!!,
                                        name = dto.name!!,
                                        // другие поля по необходимости
                                    )
                                }
                                VacancySearchUiState.Success(vacancies)
                            }
                        }

                        is Resource.Error -> {
                            VacancySearchUiState.Error(resource.message ?: "Ошибка загрузки")
                        }
                    }
                }
            } catch (e: CancellationException) {
                // Поиск был отменен - ничего не делаем
            } catch (e: Exception) {
                _uiState.value = VacancySearchUiState.Error("Ошибка: ${e.message}")
            }
        }
    }

    // Функция для очистки поиска
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
    data class Success(val vacancies: List<VacancyDetailsModel>) : VacancySearchUiState
    object Empty : VacancySearchUiState
    data class Error(val message: String) : VacancySearchUiState
}
