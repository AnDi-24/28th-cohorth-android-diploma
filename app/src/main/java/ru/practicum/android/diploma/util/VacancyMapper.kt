package ru.practicum.android.diploma.util

import ru.practicum.android.diploma.data.local.VacancyEntity
import ru.practicum.android.diploma.domain.local.model.VacancyModel

object VacancyMapper {

    fun mapperToEntity(vacancyModel: VacancyModel): VacancyEntity {
        return VacancyEntity(
            vacancyId = vacancyModel.vacancyId
        )
    }

    fun mapperToModel(vacancyEntity: VacancyEntity): VacancyModel {
        return VacancyModel(
            vacancyId = vacancyEntity.vacancyId
        )
    }

}
