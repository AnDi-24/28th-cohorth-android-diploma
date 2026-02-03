package ru.practicum.android.diploma.domain.local.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.network.models.VacancyDetailsModel

interface FavoriteVacancyInteractor {
    suspend fun toggleFavorite(vacancy: VacancyDetailsModel)
    suspend fun isFavorite(id: String): Boolean
    fun getAllFavoritesForList(): Flow<List<VacancyDetailsModel>>
    suspend fun removeFromFavorites(id: String)
    suspend fun getFavoriteVacancy(id: String): VacancyDetailsModel?
}
