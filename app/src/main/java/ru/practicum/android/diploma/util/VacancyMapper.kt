package ru.practicum.android.diploma.util

import ru.practicum.android.diploma.data.local.FavoriteVacancyEntity
import ru.practicum.android.diploma.data.network.models.VacancyDto
import ru.practicum.android.diploma.domain.network.models.Address
import ru.practicum.android.diploma.domain.network.models.Area
import ru.practicum.android.diploma.domain.network.models.Contacts
import ru.practicum.android.diploma.domain.network.models.Employer
import ru.practicum.android.diploma.domain.network.models.Employment
import ru.practicum.android.diploma.domain.network.models.Experience
import ru.practicum.android.diploma.domain.network.models.Industry
import ru.practicum.android.diploma.domain.network.models.Phone
import ru.practicum.android.diploma.domain.network.models.Salary
import ru.practicum.android.diploma.domain.network.models.Schedule
import ru.practicum.android.diploma.domain.network.models.VacancyDetailsModel

object VacancyMapper {
    fun mapToFavoriteVacancyEntity(vacancy: VacancyDetailsModel): FavoriteVacancyEntity {
        return FavoriteVacancyMapper.mapToEntity(vacancy)
    }

    fun mapFromFavoriteVacancyEntityForList(entity: FavoriteVacancyEntity): VacancyDetailsModel {
        return FavoriteVacancyMapper.mapFromEntityForList(entity)
    }

    fun mapFromFavoriteVacancyEntityForDetails(entity: FavoriteVacancyEntity): VacancyDetailsModel {
        return FavoriteVacancyMapper.mapFromEntityForDetails(entity)
    }

    fun mapperFromDto(vacancyDto: VacancyDto): VacancyDetailsModel {
        return DtoToModelMapper.map(vacancyDto)
    }
}

private object FavoriteVacancyMapper {
    fun mapToEntity(vacancy: VacancyDetailsModel): FavoriteVacancyEntity {
        return FavoriteVacancyEntity(
            id = vacancy.id,
            name = vacancy.name,
            employerName = vacancy.employer?.name,
            salaryFrom = vacancy.salary?.from,
            salaryTo = vacancy.salary?.to,
            salaryCurrency = vacancy.salary?.currency,
            employerLogoUrl = vacancy.employer?.logo,
            areaName = vacancy.area?.name,
            experience = vacancy.experience?.name,
            employment = vacancy.employment?.name,
            schedule = vacancy.schedule?.name,
            description = vacancy.description,
            skills = vacancy.skills.joinToString(", "),
            vacancyUrl = vacancy.url,
            addressCity = vacancy.address?.city,
            addressStreet = vacancy.address?.street,
            addressBuilding = vacancy.address?.building,
            addressRaw = vacancy.address?.raw,
            contactName = vacancy.contacts?.name,
            contactEmail = vacancy.contacts?.email,
            contactPhones = vacancy.contacts?.phones?.joinToString(";") { it.formatted }
        )
    }

    fun mapFromEntityForList(entity: FavoriteVacancyEntity): VacancyDetailsModel {
        return VacancyDetailsModel(
            id = entity.id,
            name = entity.name,
            salary = SalaryMapper.fromEntity(entity),
            employer = EmployerMapper.fromEntity(entity),
            area = AreaMapper.fromEntity(entity)
        )
    }

    fun mapFromEntityForDetails(entity: FavoriteVacancyEntity): VacancyDetailsModel {
        return VacancyDetailsModel(
            id = entity.id,
            name = entity.name,
            salary = SalaryMapper.fromEntity(entity),
            address = AddressMapper.fromEntity(entity),
            experience = ExperienceMapper.fromEntity(entity),
            schedule = ScheduleMapper.fromEntity(entity),
            employment = EmploymentMapper.fromEntity(entity),
            contacts = ContactsMapper.fromEntity(entity),
            description = entity.description,
            employer = EmployerMapper.fromEntity(entity),
            area = AreaMapper.fromEntity(entity),
            skills = SkillsMapper.fromEntity(entity),
            url = entity.vacancyUrl,
            industry = null
        )
    }
}

private object DtoToModelMapper {
    fun map(vacancyDto: VacancyDto): VacancyDetailsModel {
        return VacancyDetailsModel(
            id = vacancyDto.id.orEmpty(),
            name = vacancyDto.name.orEmpty(),
            salary = SalaryMapper.fromDto(vacancyDto.salary),
            address = AddressMapper.fromDto(vacancyDto.address),
            experience = ExperienceMapper.fromDto(vacancyDto.experience),
            schedule = ScheduleMapper.fromDto(vacancyDto.schedule),
            employment = EmploymentMapper.fromDto(vacancyDto.employment),
            contacts = ContactsMapper.fromDto(vacancyDto.contacts),
            description = vacancyDto.description.orEmpty(),
            employer = EmployerMapper.fromDto(vacancyDto.employer),
            area = AreaMapper.fromDto(vacancyDto.area),
            skills = vacancyDto.skills.orEmpty(),
            url = vacancyDto.url.orEmpty(),
            industry = IndustryMapper.fromDto(vacancyDto.industry)
        )
    }
}

private object SalaryMapper {
    fun fromEntity(entity: FavoriteVacancyEntity): Salary? {
        return entity.salaryCurrency?.let { currency ->
            Salary(
                id = entity.id,
                currency = currency,
                from = entity.salaryFrom,
                to = entity.salaryTo
            )
        }
    }

    fun fromDto(dto: ru.practicum.android.diploma.data.network.models.Salary?): Salary {
        return if (dto == null) {
            Salary(
                id = "",
                currency = "",
                from = null,
                to = null
            )
        } else {
            Salary(
                id = dto.id ?: "",
                currency = dto.currency ?: "",
                from = dto.from,
                to = dto.to
            )
        }
    }
}

private object AddressMapper {
    fun fromEntity(entity: FavoriteVacancyEntity): Address? {
        return if (entity.addressCity != null || entity.addressRaw != null) {
            Address(
                id = entity.id,
                city = entity.addressCity ?: "",
                street = entity.addressStreet,
                building = entity.addressBuilding,
                raw = entity.addressRaw
            )
        } else {
            null
        }
    }

    fun fromDto(dto: ru.practicum.android.diploma.data.network.models.Address?): Address {
        return if (dto == null) {
            Address(
                city = "",
                street = null,
                building = null,
                raw = null,
                id = ""
            )
        } else {
            Address(
                city = dto.city ?: "",
                street = dto.street,
                building = dto.building,
                raw = dto.raw,
                id = ""
            )
        }
    }
}

private object ExperienceMapper {
    fun fromEntity(entity: FavoriteVacancyEntity): Experience? {
        return entity.experience?.let { experienceName ->
            Experience(
                id = entity.id,
                name = experienceName
            )
        }
    }

    fun fromDto(dto: ru.practicum.android.diploma.data.network.models.Experience?): Experience {
        return if (dto == null) {
            Experience(
                name = "",
                id = ""
            )
        } else {
            Experience(
                name = dto.name ?: "",
                id = ""
            )
        }
    }
}

private object ScheduleMapper {
    fun fromEntity(entity: FavoriteVacancyEntity): Schedule? {
        return entity.schedule?.let { scheduleName ->
            Schedule(
                id = entity.id,
                name = scheduleName
            )
        }
    }

    fun fromDto(dto: ru.practicum.android.diploma.data.network.models.Schedule?): Schedule {
        return if (dto == null) {
            Schedule(
                id = "",
                name = ""
            )
        } else {
            Schedule(
                id = dto.id ?: "",
                name = dto.name ?: ""
            )
        }
    }
}

private object EmploymentMapper {
    fun fromEntity(entity: FavoriteVacancyEntity): Employment? {
        return entity.employment?.let { employmentName ->
            Employment(
                id = entity.id,
                name = employmentName
            )
        }
    }

    fun fromDto(dto: ru.practicum.android.diploma.data.network.models.Employment?): Employment {
        return if (dto == null) {
            Employment(
                id = "",
                name = ""
            )
        } else {
            Employment(
                id = dto.id ?: "",
                name = dto.name ?: ""
            )
        }
    }
}

private object ContactsMapper {
    fun fromEntity(entity: FavoriteVacancyEntity): Contacts? {
        return if (entity.contactName != null || entity.contactEmail != null || entity.contactPhones != null) {
            Contacts(
                id = entity.id,
                name = entity.contactName ?: "",
                email = entity.contactEmail,
                phones = PhoneMapper.fromEntity(entity)
            )
        } else {
            null
        }
    }

    fun fromDto(dto: ru.practicum.android.diploma.data.network.models.Contacts?): Contacts {
        return if (dto == null) {
            Contacts(
                id = "",
                name = "",
                email = null,
                phones = emptyList()
            )
        } else {
            Contacts(
                id = dto.id ?: "",
                name = dto.name ?: "",
                email = dto.email,
                phones = PhoneMapper.fromDto(dto.phones)
            )
        }
    }
}

private object PhoneMapper {
    fun fromEntity(entity: FavoriteVacancyEntity): List<Phone> {
        return entity.contactPhones?.split(";")
            ?.mapNotNull { phone ->
                phone.trim().takeIf { it.isNotBlank() }?.let { formatted ->
                    Phone(
                        comment = null,
                        formatted = formatted
                    )
                }
            } ?: emptyList()
    }

    fun fromDto(dtos: List<ru.practicum.android.diploma.data.network.models.Phone>?): List<Phone> {
        if (dtos == null) return emptyList()

        return dtos.mapNotNull { phoneDto ->
            phoneDto.formatted?.let { formatted ->
                Phone(
                    comment = null,
                    formatted = formatted
                )
            }
        }
    }
}

private object EmployerMapper {
    fun fromEntity(entity: FavoriteVacancyEntity): Employer? {
        return entity.employerName?.let { employerName ->
            Employer(
                id = entity.id,
                name = employerName,
                logo = entity.employerLogoUrl
            )
        }
    }

    fun fromDto(dto: ru.practicum.android.diploma.data.network.models.Employer?): Employer {
        return if (dto == null) {
            Employer(
                id = "",
                name = "",
                logo = null
            )
        } else {
            Employer(
                id = dto.name ?: "",
                name = dto.name ?: "",
                logo = dto.logo
            )
        }
    }
}

private object AreaMapper {
    fun fromEntity(entity: FavoriteVacancyEntity): Area? {
        return entity.areaName?.let { areaName ->
            Area(
                id = entity.id,
                name = areaName
            )
        }
    }

    fun fromDto(dto: ru.practicum.android.diploma.data.network.models.Area?): Area {
        return if (dto == null) {
            Area(
                id = "",
                parentId = null,
                name = "",
                areas = emptyList()
            )
        } else {
            Area(
                id = dto.id ?: "",
                parentId = dto.parentId,
                name = dto.name ?: "",
                areas = emptyList()
            )
        }
    }
}

private object SkillsMapper {
    fun fromEntity(entity: FavoriteVacancyEntity): List<String> {
        return entity.skills?.split(", ")
            ?.filter { it.isNotBlank() }
            ?: emptyList()
    }
}

private object IndustryMapper {
    fun fromDto(dto: ru.practicum.android.diploma.data.network.models.Industry?): Industry {
        return if (dto == null) {
            Industry(
                id = "",
                name = ""
            )
        } else {
            Industry(
                id = dto.id ?: "",
                name = dto.name ?: ""
            )
        }
    }
}
