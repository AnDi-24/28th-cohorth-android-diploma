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
            id = vacancyDto.id.orEmpty(),
            name = vacancyDto.name.orEmpty(),
            salary = vacancyDto.salary?.currency.orEmpty(),
            address = vacancyDto.address?.city.orEmpty(),
            experience = vacancyDto.experience?.name.orEmpty(),
            schedule = vacancyDto.schedule?.name.orEmpty(),
            employment = vacancyDto.employment?.name.orEmpty(),
            description = vacancyDto.description.orEmpty(),
            employer = vacancyDto.employer?.name.orEmpty(),
            skills = vacancyDto.skills.orEmpty(),
            logo = vacancyDto.employer?.logo.orEmpty()
        )
    }

}
