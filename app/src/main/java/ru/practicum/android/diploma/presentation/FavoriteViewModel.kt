package ru.practicum.android.diploma.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.local.api.FavoriteVacancyInteractor
import ru.practicum.android.diploma.domain.network.models.VacancyDetailsModel

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
            favoriteInteractor.getAllFavoritesForList().collectLatest { vacancies ->
                _uiState.value = if (vacancies.isEmpty()) {
                    FavoriteUiState.Empty
                } else {
                    FavoriteUiState.Success(vacancies)
                }
            }
        }
    }

    fun removeFromFavorites(id: String) {
        viewModelScope.launch {
            favoriteInteractor.removeFromFavorites(id)
        }
    }

    fun refresh() {
        loadFavorites()
    }
}

sealed interface FavoriteUiState {
    object Loading : FavoriteUiState
    object Empty : FavoriteUiState
    data class Success(val vacancies: List<VacancyDetailsModel>) : FavoriteUiState
}
