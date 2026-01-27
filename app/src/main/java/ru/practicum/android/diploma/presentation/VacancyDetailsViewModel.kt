package ru.practicum.android.diploma.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.network.api.FindVacancyInteractor
import ru.practicum.android.diploma.domain.network.models.VacancyDetailsModel
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.util.ResponseState

private const val MOK_VACANCY_ID = "0001266a-3da9-4af8-b384-2377f0ea5453"

class VacancyDetailsViewModel(
    val retrofitInteractor: FindVacancyInteractor
): ViewModel() {

    private val _uiState = MutableStateFlow<VacancyUiState>(VacancyUiState.Idle)
    val uiState: StateFlow<VacancyUiState> = _uiState

    init {
        searchVacancyDetails()
    }

    fun searchVacancyDetails() {
        viewModelScope.launch {
            _uiState.value = VacancyUiState.Loading

            try {
                val result = retrofitInteractor.getVacancyDetails(MOK_VACANCY_ID)

                _uiState.value = when (result) {
                    is Resource.Success -> {
                        val vacancyModel = result.data
                        if (vacancyModel != null) {
                            VacancyUiState.Success(vacancy = vacancyModel)
                        } else {
                            VacancyUiState.Error(ResponseState.NULL_DATA.errorMessage)
                        }
                    }

                    is Resource.Error -> {
                        VacancyUiState.Error(ResponseState.UNKNOWN.errorMessage)
                    }
                }

            } catch (e: Exception) {
                _uiState.value = VacancyUiState.Error(
                    "{${ResponseState.UNKNOWN.errorMessage}}: ${e.message}"
                )
            }
        }
    }
    sealed class VacancyUiState {
        object Idle : VacancyUiState()
        object Loading : VacancyUiState()
        data class Success(
            val vacancy: VacancyDetailsModel,
        ) : VacancyUiState()

        data class Error(val message: String) : VacancyUiState()
    }

}
