package ru.practicum.android.diploma.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.local.api.FavoriteVacancyInteractor
import ru.practicum.android.diploma.domain.network.api.FindVacancyInteractor
import ru.practicum.android.diploma.domain.network.models.VacancyDetailsModel
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.util.ResponseState
import java.io.IOException

class VacancyDetailsViewModel(
    val retrofitInteractor: FindVacancyInteractor,
    private val favoriteInteractor: FavoriteVacancyInteractor
) : ViewModel() {

    private val _uiState = MutableStateFlow<VacancyUiState>(VacancyUiState.Idle)
    val uiState: StateFlow<VacancyUiState> = _uiState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private var currentVacancyId: String? = null
    private var currentVacancy: VacancyDetailsModel? = null

    fun setVacancyId(id: String) {
        if (currentVacancyId != id || currentVacancy?.id != id) {
            currentVacancyId = id
            currentVacancy = null
            _isFavorite.value = false
            _uiState.value = VacancyUiState.Idle
        }
        checkFavoriteStatus(id)
    }

    fun searchVacancyDetails(id: String) {
        setVacancyId(id)
        _uiState.value = VacancyUiState.Loading

        viewModelScope.launch {
            try {
                val result = retrofitInteractor.getVacancyDetails(id)

                _uiState.value = when (result) {
                    is Resource.Success -> {
                        val vacancyModel = result.data
                        if (vacancyModel != null) {
                            currentVacancy = vacancyModel
                            checkFavoriteStatus(vacancyModel.id)
                            VacancyUiState.Success(vacancy = vacancyModel)
                        } else {
                            VacancyUiState.Error(ResponseState.NULL_DATA.errorMessage)
                        }
                    }

                    is Resource.Error -> {
                        VacancyUiState.Error(ResponseState.UNKNOWN.errorMessage)
                    }
                }

            } catch (e: IOException) {
                _uiState.value = VacancyUiState.Error(
                    "{${ResponseState.UNKNOWN.errorMessage}}: ${e.message}"
                )
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val currentVacancyValue = currentVacancy
            if (currentVacancyValue != null) {
                favoriteInteractor.toggleFavorite(currentVacancyValue)
                val newFavoriteStatus = favoriteInteractor.isFavorite(currentVacancyValue.id)
                _isFavorite.value = newFavoriteStatus
            } else if (currentVacancyId != null) {
                val result = retrofitInteractor.getVacancyDetails(currentVacancyId!!)
                if (result is Resource.Success) {
                    val vacancy = result.data
                    if (vacancy != null) {
                        currentVacancy = vacancy
                        favoriteInteractor.toggleFavorite(vacancy)
                        val newFavoriteStatus = favoriteInteractor.isFavorite(vacancy.id)
                        _isFavorite.value = newFavoriteStatus
                    }
                }
            }
        }
    }

    fun checkFavoriteStatus(vacancyId: String) {
        viewModelScope.launch {
            val status = favoriteInteractor.isFavorite(vacancyId)
            _isFavorite.value = status
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
