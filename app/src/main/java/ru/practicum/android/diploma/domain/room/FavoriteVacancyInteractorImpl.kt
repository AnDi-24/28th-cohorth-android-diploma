package ru.practicum.android.diploma.domain.room

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.domain.network.models.VacancyDetailsModel
import ru.practicum.android.diploma.domain.room.api.FavoriteVacancyInteractor
import ru.practicum.android.diploma.domain.room.api.FavoriteVacancyRepository
import ru.practicum.android.diploma.util.VacancyMapper

class FavoriteVacancyInteractorImpl(
    val repository: FavoriteVacancyRepository,
    val mapper: VacancyMapper
) : FavoriteVacancyInteractor {

    override suspend fun toggleFavorite(vacancy: VacancyDetailsModel) {
        val isFavorite = repository.isFavorite(vacancy.id)

        if (isFavorite) {
            repository.deleteById(vacancy.id)
        } else {
            val entity = mapper.mapToFavoriteVacancyEntity(vacancy)
            repository.insert(entity)
        }
    }

    override suspend fun isFavorite(id: String): Boolean {
        return repository.isFavorite(id)
    }

    override fun getAllFavoritesForList(): Flow<List<VacancyDetailsModel>> {
        return repository.getAll().map { entities ->
            entities.map { entity ->
                mapper.mapFromFavoriteVacancyEntityForList(entity)
            }
        }
    }

    override suspend fun removeFromFavorites(id: String) {
        repository.deleteById(id)
    }

    override suspend fun getFavoriteVacancy(id: String): VacancyDetailsModel? {
        val entity = repository.getById(id)
        return entity?.let { mapper.mapFromFavoriteVacancyEntityForDetails(it) }
    }
}
