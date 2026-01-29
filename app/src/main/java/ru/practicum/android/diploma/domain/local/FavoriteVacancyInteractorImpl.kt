package ru.practicum.android.diploma.domain.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.domain.local.api.FavoriteVacancyInteractor
import ru.practicum.android.diploma.domain.local.api.FavoriteVacancyRepository
import ru.practicum.android.diploma.domain.local.model.VacancyModel
import ru.practicum.android.diploma.util.VacancyMapper

class FavoriteVacancyInteractorImpl(
    val repository: FavoriteVacancyRepository,
    val mapper: VacancyMapper
) : FavoriteVacancyInteractor {

    override suspend fun addVacancyToFavorite(vacancy: VacancyModel) {
        repository.addVacancyToFavorite(
            mapper.mapperToEntity(vacancyModel = vacancy)
        )
    }

    override suspend fun deleteVacancyFromFavorite(id: Long) {
        repository.deleteVacancyFromFavorite(id = id)
    }

    override fun getAllFavoriteVacancy(): Flow<List<VacancyModel>> {
        return repository.getAllFavoriteVacancy().map { listEntity ->
            listEntity.map { vacancyEntity ->
                mapper.mapperToRoomModel(vacancyEntity = vacancyEntity)
            }
        }
    }

}
