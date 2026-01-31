package ru.practicum.android.diploma.domain.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.domain.network.api.FindVacancyInteractor
import ru.practicum.android.diploma.domain.network.api.FindVacancyRepository
import ru.practicum.android.diploma.domain.network.models.SearchParams
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

    override suspend fun getListVacancies(
        params: SearchParams
    ): Flow<Resource<Pair<List<VacancyDetailsModel>, Int>>> {
        return repository.getListVacancies(
            area = params.area,
            industry = params.industry,
            text = params.text,
            salary = params.salary,
            page = params.page,
            onlyWithSalary = params.onlyWithSalary
        )
            .map { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val vacancyDtoList = resource.data?.first ?: emptyList()
                        val totalFound = resource.data?.second ?: 0
                        val vacancyModels = vacancyDtoList.map { dto ->
                            mapper.mapperFromDto(dto) // Используем маппер
                        }
                        Resource.Success(Pair(vacancyModels, totalFound))
                    }

                    is Resource.Error -> {
                        Resource.Error(resource.message ?: "")
                    }
                }
            }
    }
}
