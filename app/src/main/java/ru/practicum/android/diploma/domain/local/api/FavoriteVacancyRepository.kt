package ru.practicum.android.diploma.domain.local.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.local.VacancyEntity

interface FavoriteVacancyRepository {

    suspend fun addVacancyToFavorite(vacancy: VacancyEntity)
    suspend fun deleteVacancyFromFavorite(id: Long)
    fun getAllFavoriteVacancy(): Flow<List<VacancyEntity>>

}
