package ru.practicum.android.diploma.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.room.api.FavoriteVacancyInteractor
import ru.practicum.android.diploma.domain.network.models.VacancyDetailsModel
import java.io.IOException

class FavoriteViewModel(
    private val favoriteInteractor: FavoriteVacancyInteractor
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavoriteUiState>(FavoriteUiState.Loading)
    val uiState: StateFlow<FavoriteUiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            try {
                favoriteInteractor.getAllFavoritesForList().collectLatest { vacancies ->
                    _uiState.value = if (vacancies.isEmpty()) {
                        FavoriteUiState.Empty
                    } else {
                        FavoriteUiState.Success(vacancies)
                    }
                }
            } catch (e: IOException) {
                logException("IO error loading favorites", e)
                _uiState.value = FavoriteUiState.Error
            } catch (e: IllegalStateException) {
                logException("Illegal state loading favorites", e)
                _uiState.value = FavoriteUiState.Error
            } catch (e: IllegalArgumentException) {
                logException("Illegal argument loading favorites", e)
                _uiState.value = FavoriteUiState.Error
            }
        }
    }

    fun refresh() {
        _uiState.value = FavoriteUiState.Loading
        loadFavorites()
    }
}

sealed interface FavoriteUiState {
    object Loading : FavoriteUiState
    object Empty : FavoriteUiState
    object Error : FavoriteUiState
    data class Success(val vacancies: List<VacancyDetailsModel>) : FavoriteUiState
}

private fun logException(message: String, exception: Exception) {
    Log.e("FavoriteViewModel", "$message: ${exception.message}")
}
