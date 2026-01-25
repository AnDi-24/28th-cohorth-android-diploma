package ru.practicum.android.diploma.domain.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.local.VacancyEntity
import ru.practicum.android.diploma.domain.VacancyModel
import ru.practicum.android.diploma.domain.local.api.FavoriteVacancyInteractor
import ru.practicum.android.diploma.domain.local.api.FavoriteVacancyRepository

class FavoriteVacancyInteractorImpl(
    val repository: FavoriteVacancyRepository
): FavoriteVacancyInteractor {

    override suspend fun addVacancyToFavorite(vacancy: VacancyModel) {
        repository.addVacancyToFavorite(
            mapperToEntity(vacancyModel = vacancy)
        )
    }

    override suspend fun deleteVacancyFromFavorite(id: Long) {
        repository.deleteVacancyFromFavorite(id = id)
    }

    override fun getAllFavoriteVacancy(): Flow<List<VacancyModel>> {
       return repository.getAllFavoriteVacancy().map { listEntity ->
           listEntity.map { vacancyEntity ->
               mapperToModel(vacancyEntity = vacancyEntity)
           }
       }
    }


    // функции - мапперы
    private fun mapperToEntity(vacancyModel: VacancyModel): VacancyEntity {
        return VacancyEntity(
            vacancyId = vacancyModel.vacancyId
        )
    }

    private fun mapperToModel(vacancyEntity: VacancyEntity): VacancyModel {
        return VacancyModel(
            vacancyId = vacancyEntity.vacancyId
        )
    }

}
