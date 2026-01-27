package ru.practicum.android.diploma.util

import ru.practicum.android.diploma.data.local.VacancyEntity
import ru.practicum.android.diploma.data.network.models.VacancyDto
import ru.practicum.android.diploma.domain.local.model.VacancyModel
import ru.practicum.android.diploma.domain.network.models.VacancyDetailsModel

object VacancyMapper {

    fun mapperToEntity(vacancyModel: VacancyModel): VacancyEntity {
        return VacancyEntity(
            vacancyId = vacancyModel.vacancyId
        )
    }

    fun mapperToRoomModel(vacancyEntity: VacancyEntity): VacancyModel {
        return VacancyModel(
            vacancyId = vacancyEntity.vacancyId
        )
    }

    fun mapperFromDto(vacancyDto: VacancyDto): VacancyDetailsModel {
        return VacancyDetailsModel(
            id = vacancyDto.id ?: "",
            name = vacancyDto.name ?: "",
            salary = vacancyDto.salary?.currency ?: "",
            address = vacancyDto.address?.city ?: "",
            experience = vacancyDto.experience?.name ?: "",
            schedule = vacancyDto.schedule?.name ?: "",
            employment = vacancyDto.employment?.name ?: "",
            description = vacancyDto.description ?: "",
            employer = vacancyDto.employer?.name ?: "",
            skills = vacancyDto.skills ?: emptyList(),
            logo = vacancyDto.employer?.logo ?: ""
        )
    }

}
