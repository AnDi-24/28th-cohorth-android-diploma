package ru.practicum.android.diploma.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.local.api.FavoriteVacancyInteractor
import ru.practicum.android.diploma.domain.local.model.VacancyModel
import ru.practicum.android.diploma.domain.network.api.FindVacancyInteractor

private const val MOK_VACANCY_ID = "0001266a-3da9-4af8-b384-2377f0ea5453"
class FavoriteViewModel(
    private val roomInteractor: FavoriteVacancyInteractor,
    private val retrofitInteractor: FindVacancyInteractor
) : ViewModel() {

    init {
        getAllFavoriteVacancy()
        searchVacancyDetails()
    }

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

    fun searchVacancyDetails() {
        viewModelScope.launch {
            retrofitInteractor
                .getVacancyDetails(MOK_VACANCY_ID)
                .collect { vacancyDto ->
                    Log.d("searchVacancyDetails in ViewModel", vacancyDto.name ?: " --- ")
                }
        }
    }

}
