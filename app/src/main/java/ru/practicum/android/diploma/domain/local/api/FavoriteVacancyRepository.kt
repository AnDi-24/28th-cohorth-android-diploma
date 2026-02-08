package ru.practicum.android.diploma.domain.local.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.local.FavoriteVacancyEntity

interface FavoriteVacancyRepository {
    suspend fun insert(vacancy: FavoriteVacancyEntity)
    suspend fun deleteById(id: String)
    fun getAll(): Flow<List<FavoriteVacancyEntity>>
    suspend fun isFavorite(id: String): Boolean
    suspend fun getById(id: String): FavoriteVacancyEntity?
}
