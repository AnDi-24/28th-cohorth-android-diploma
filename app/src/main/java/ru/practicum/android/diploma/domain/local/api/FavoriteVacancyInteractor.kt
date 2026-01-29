package ru.practicum.android.diploma.domain.local.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.local.model.VacancyModel

interface FavoriteVacancyInteractor {
    suspend fun addVacancyToFavorite(vacancy: VacancyModel)
    suspend fun deleteVacancyFromFavorite(id: Long)
    fun getAllFavoriteVacancy(): Flow<List<VacancyModel>>
}
