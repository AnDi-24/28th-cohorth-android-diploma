package ru.practicum.android.diploma.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.network.api.FindVacancyInteractor
import ru.practicum.android.diploma.domain.network.models.VacancyDetailsModel
import ru.practicum.android.diploma.util.Resource

class SearchViewModel(val interactor: FindVacancyInteractor
) : ViewModel() {

    var query by mutableStateOf("")

    private val _uiState = mutableStateOf<VacancySearchUiState>(VacancySearchUiState.Idle)
    val uiState: State<VacancySearchUiState> get() = _uiState

    fun searchVacancies() {
        if (query.isBlank()) {
            _uiState.value = VacancySearchUiState.Error("Введите запрос")
            return
        }

        viewModelScope.launch {
            _uiState.value = VacancySearchUiState.Loading

            val result = interactor.getVacancies(query)
            _uiState.value = when (result) {
                is Resource.Success -> {
                    if (result.data == null) {
                        VacancySearchUiState.Empty
                    } else {
                        VacancySearchUiState.Success(result.data)
                    }
                }
                is Resource.Error -> VacancySearchUiState.Error(result.message ?: "Ошибка загрузки")
            }
        }
    }
}

sealed interface VacancySearchUiState {
    object Idle : VacancySearchUiState
    object Loading : VacancySearchUiState
    data class Success(val vacancies: List<VacancyDetailsModel>) : VacancySearchUiState
    object Empty : VacancySearchUiState
    data class Error(val message: String) : VacancySearchUiState
}
