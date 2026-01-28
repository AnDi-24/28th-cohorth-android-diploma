package ru.practicum.android.diploma.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.local.api.FavoriteVacancyInteractor
import ru.practicum.android.diploma.domain.local.model.VacancyModel

class FavoriteViewModel(
    private val roomInteractor: FavoriteVacancyInteractor
) : ViewModel() {


    private val _vacanciesFlow = MutableStateFlow<List<VacancyModel>>(emptyList())
    val vacanciesFlow: StateFlow<List<VacancyModel>> = _vacanciesFlow.asStateFlow()

    private fun getAllFavoriteVacancy() {
        viewModelScope.launch {
            roomInteractor.getAllFavoriteVacancy()
                .collect { listVacancies ->
                    _vacanciesFlow.value = listVacancies
                }
        }
    }

    fun addVacancy(vacancy: VacancyModel) {
        viewModelScope.launch {
            roomInteractor.addVacancyToFavorite(vacancy = vacancy)
        }
    }

    fun deleteVacancy(id: Long) {
        viewModelScope.launch {
            roomInteractor.deleteVacancyFromFavorite(id = id)
        }
    }
}
