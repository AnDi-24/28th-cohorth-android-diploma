package ru.practicum.android.diploma.domain.network

import android.util.Log
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.network.models.VacancyDto
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

    override fun getListVacancies(
        area: Int,
        industry: Int?,
        text: String,
        salary: Int?,
        page: Int,
        onlyWithSalary: Boolean
    ): Flow<Resource<List<VacancyDto>>> {

        Log.d("VacancyList - интерактор", "мы тут")
        return repository.getListVacancies(
            area = area,
            industry = industry,
            text = text,
            salary = salary,
            page = page,
            onlyWithSalary = onlyWithSalary
        )
    }
}
