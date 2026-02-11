package ru.practicum.android.diploma.domain.room.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.room.FavoriteVacancyEntity

interface FavoriteVacancyRepository {
    suspend fun insert(vacancy: FavoriteVacancyEntity)
    suspend fun deleteById(id: String)
    fun getAll(): Flow<List<FavoriteVacancyEntity>>
    suspend fun isFavorite(id: String): Boolean
    suspend fun getById(id: String): FavoriteVacancyEntity?
}
