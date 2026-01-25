package ru.practicum.android.diploma.data.local

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.local.api.FavoriteVacancyRepository

class FavoriteVacancyRepositoryImpl(
    val roomDataBase: AppDatabase
): FavoriteVacancyRepository {
    override suspend fun addVacancyToFavorite(vacancy: VacancyEntity) {
        roomDataBase.favoriteVacancyDao().addVacancyToFavorite(vacancy=vacancy)
    }

    override suspend fun deleteVacancyFromFavorite(id: Long) {
        roomDataBase.favoriteVacancyDao().deleteVacancyFromFavorite(id=id)
    }

    override fun getAllFavoriteVacancy(): Flow<List<VacancyEntity>> {
        return roomDataBase.favoriteVacancyDao()
            .getAllFavoriteVacancy()
    }
}
