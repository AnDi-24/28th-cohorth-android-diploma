package ru.practicum.android.diploma.domain.network

import ru.practicum.android.diploma.domain.network.api.FindVacancyInteractor
import ru.practicum.android.diploma.domain.network.api.FindVacancyRepository
import ru.practicum.android.diploma.domain.network.models.VacancyDetailsModel
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.util.ResponseState
import ru.practicum.android.diploma.util.VacancyMapper

class FindVacancyInteractorImpl(
    val repository: FindVacancyRepository,
    val mapper: VacancyMapper
) : FindVacancyInteractor {
    override suspend fun getVacancyDetails(expression: String): Resource<VacancyDetailsModel> {
        val resource = repository.getVacancyDetails(expression)
        return when (resource) {
            is Resource.Success -> {
                val vacancyDto = resource.data
                if (vacancyDto != null) {
                    val vacancyModel = mapper.mapperFromDto(vacancyDto)
                    Resource.Success(vacancyModel)
                } else {
                    Resource.Error(ResponseState.NULL_DATA.errorMessage)
                }
            }
            is Resource.Error -> {
                Resource.Error(resource.message ?: "")
            }
        }
    }

    override suspend fun getVacancies(expression: String): Resource<List<VacancyDetailsModel>> {
        val resource = repository.getVacancies(expression)
        return when (resource) {
            is Resource.Success -> {
                val vacancyDto = resource.data
                if (vacancyDto != null) {
                    val vacancyList = vacancyDto.map { dto ->
                        mapper.mapperFromDto(dto)
                    }
                    Resource.Success(vacancyList)
                } else {
                    Resource.Error(ResponseState.NULL_DATA.errorMessage)
                }
            }
            is Resource.Error -> {
                Resource.Error(resource.message ?: "")
            }
        }
    }
}
