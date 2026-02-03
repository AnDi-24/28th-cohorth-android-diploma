package ru.practicum.android.diploma.data.local

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.local.api.FavoriteVacancyRepository

class FavoriteVacancyRepositoryImpl(
    val database: AppDatabase
) : FavoriteVacancyRepository {

    override suspend fun insert(vacancy: FavoriteVacancyEntity) {
        database.favoriteVacancyDao().insert(vacancy)
    }

    override suspend fun deleteById(id: String) {
        database.favoriteVacancyDao().deleteById(id)
    }

    override fun getAll(): Flow<List<FavoriteVacancyEntity>> {
        return database.favoriteVacancyDao().getAll()
    }

    override suspend fun isFavorite(id: String): Boolean {
        return database.favoriteVacancyDao().isFavorite(id)
    }

    override suspend fun getById(id: String): FavoriteVacancyEntity? {
        return database.favoriteVacancyDao().getById(id)
    }
}
