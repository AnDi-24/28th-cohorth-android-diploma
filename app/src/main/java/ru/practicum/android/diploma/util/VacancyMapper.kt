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

    fun mapFromFavoriteVacancyEntityForList(entity: FavoriteVacancyEntity): VacancyDetailsModel {
        return VacancyDetailsModel(
            id = entity.id,
            name = entity.name,
            salary = entity.salaryCurrency?.let { currency ->
                Salary(
                    id = entity.id,
                    currency = currency,
                    from = entity.salaryFrom,
                    to = entity.salaryTo
                )
            },
            employer = entity.employerName?.let { employerName ->
                Employer(
                    id = entity.id,
                    name = employerName,
                    logo = entity.employerLogoUrl
                )
            },
            area = entity.areaName?.let { areaName ->
                Area(
                    id = entity.id,
                    name = areaName
                )
            }
        )
    }

    fun mapFromFavoriteVacancyEntityForDetails(entity: FavoriteVacancyEntity): VacancyDetailsModel {
        return VacancyDetailsModel(
            id = entity.id,
            name = entity.name,
            salary = createSalaryFromEntity(entity),
            address = createAddressFromEntity(entity),
            experience = createExperienceFromEntity(entity),
            schedule = createScheduleFromEntity(entity),
            employment = createEmploymentFromEntity(entity),
            contacts = createContactsFromEntity(entity),
            description = entity.description,
            employer = createEmployerFromEntity(entity),
            area = createAreaFromEntity(entity),
            skills = createSkillsFromEntity(entity),
            url = entity.vacancyUrl,
            industry = null
        )
    }

    private fun createSalaryFromEntity(entity: FavoriteVacancyEntity): Salary? {
        return entity.salaryCurrency?.let { currency ->
            Salary(
                id = entity.id,
                currency = currency,
                from = entity.salaryFrom,
                to = entity.salaryTo
            )
        }
    }

    private fun createAddressFromEntity(entity: FavoriteVacancyEntity): Address? {
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

    private fun createExperienceFromEntity(entity: FavoriteVacancyEntity): Experience? {
        return entity.experience?.let { experienceName ->
            Experience(
                id = entity.id,
                name = experienceName
            )
        }
    }

    private fun createScheduleFromEntity(entity: FavoriteVacancyEntity): Schedule? {
        return entity.schedule?.let { scheduleName ->
            Schedule(
                id = entity.id,
                name = scheduleName
            )
        }
    }

    private fun createEmploymentFromEntity(entity: FavoriteVacancyEntity): Employment? {
        return entity.employment?.let { employmentName ->
            Employment(
                id = entity.id,
                name = employmentName
            )
        }
    }

    private fun createContactsFromEntity(entity: FavoriteVacancyEntity): Contacts? {
        return if (entity.contactName != null || entity.contactEmail != null || entity.contactPhones != null) {
            Contacts(
                id = entity.id,
                name = entity.contactName ?: "",
                email = entity.contactEmail,
                phones = createPhonesFromEntity(entity)
            )
        } else {
            null
        }
    }

    private fun createPhonesFromEntity(entity: FavoriteVacancyEntity): List<Phone> {
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

    private fun createEmployerFromEntity(entity: FavoriteVacancyEntity): Employer? {
        return entity.employerName?.let { employerName ->
            Employer(
                id = entity.id,
                name = employerName,
                logo = entity.employerLogoUrl
            )
        }
    }

    private fun createAreaFromEntity(entity: FavoriteVacancyEntity): Area? {
        return entity.areaName?.let { areaName ->
            Area(
                id = entity.id,
                name = areaName
            )
        }
    }

    private fun createSkillsFromEntity(entity: FavoriteVacancyEntity): List<String> {
        return entity.skills?.split(", ")
            ?.filter { it.isNotBlank() }
            ?: emptyList()
    }

    fun mapperFromDto(vacancyDto: VacancyDto): VacancyDetailsModel {
        return VacancyDetailsModel(
            id = vacancyDto.id.orEmpty(),
            name = vacancyDto.name.orEmpty(),
            salary = mapSalary(vacancyDto.salary),
            address = mapAddress(vacancyDto.address),
            experience = mapExperience(vacancyDto.experience),
            schedule = mapSchedule(vacancyDto.schedule),
            employment = mapEmployment(vacancyDto.employment),
            contacts = mapContacts(vacancyDto.contacts),
            description = vacancyDto.description.orEmpty(),
            employer = mapEmployer(vacancyDto.employer),
            area = mapArea(vacancyDto.area),
            skills = vacancyDto.skills.orEmpty(),
            url = vacancyDto.url.orEmpty(),
            industry = mapIndustry(vacancyDto.industry)
        )
    }

    private fun mapSalary(dto: ru.practicum.android.diploma.data.network.models.Salary?): Salary {
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

    private fun mapAddress(dto: ru.practicum.android.diploma.data.network.models.Address?): Address {
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

    private fun mapExperience(dto: ru.practicum.android.diploma.data.network.models.Experience?): Experience {
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

    private fun mapSchedule(dto: ru.practicum.android.diploma.data.network.models.Schedule?): Schedule {
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

    private fun mapEmployment(dto: ru.practicum.android.diploma.data.network.models.Employment?): Employment {
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

    private fun mapContacts(dto: ru.practicum.android.diploma.data.network.models.Contacts?): Contacts {
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
                phones = mapPhones(dto.phones)
            )
        }
    }

    private fun mapPhones(dtos: List<ru.practicum.android.diploma.data.network.models.Phone>?): List<Phone> {
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

    private fun mapEmployer(dto: ru.practicum.android.diploma.data.network.models.Employer?): Employer {
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

    private fun mapArea(dto: ru.practicum.android.diploma.data.network.models.Area?): Area {
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

    private fun mapIndustry(dto: ru.practicum.android.diploma.data.network.models.Industry?): Industry {
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
