package ru.practicum.android.diploma.data.network

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.network.api.NetworkClient
import ru.practicum.android.diploma.data.network.models.VacancyDetailsRequest
import ru.practicum.android.diploma.data.network.models.VacancyDetailsResponse
import ru.practicum.android.diploma.data.network.models.VacancyDto
import ru.practicum.android.diploma.data.network.models.VacancyListRequest
import ru.practicum.android.diploma.data.network.models.VacancyListResponse
import ru.practicum.android.diploma.domain.network.api.FindVacancyRepository
import ru.practicum.android.diploma.domain.network.models.SearchParams
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.util.ResponseState
import java.io.IOException

class FindVacancyRepositoryImpl(
    private val retrofitClient: NetworkClient
) : FindVacancyRepository {

    override suspend fun getVacancyDetails(expression: String): Resource<VacancyDto> {
        return try {
            val response = retrofitClient.doRequestVacancyDetails(
                VacancyDetailsRequest(expression)
            )
            when (response.resultCode) {
                ResponseState.SUCCESS -> {
                    val vacancyResponse = response as? VacancyDetailsResponse
                    val vacancy = vacancyResponse?.vacancyDto?.data
                    if (vacancy != null) {
                        Resource.Success(vacancy)
                    } else {
                        Resource.Error(
                            ResponseState.NULL_DATA.errorMessage
                        )
                    }
                }

                ResponseState.HTTP_EXCEPTION -> {
                    val errorCode = response.errorCode
                    Resource.Error(
                        message = response.resultCode.errorMessage,
                        errorCode = errorCode
                    )
                }

                else -> {
                    Resource.Error(
                        message = response.resultCode.errorMessage,
                        errorCode = null
                    )
                }
            }
        } catch (e: IOException) {
            Resource.Error(e.message.toString())

        }
    }

    override fun getListVacancies(
        params: SearchParams
    ): Flow<Resource<Triple<List<VacancyDto>, Int, Int>>> = flow {
        val response = retrofitClient.getVacanciesList(
            VacancyListRequest(
                area = params.area,
                industry = params.industry,
                text = params.text,
                salary = params.salary,
                page = params.page,
                onlyWithSalary = params.onlyWithSalary
            )
        )

        when (response.resultCode) {
            ResponseState.SUCCESS -> {
                val found = (response as? VacancyListResponse)?.found ?: 0
                val vacancyList = (response as? VacancyListResponse)?.items ?: emptyList()
                val pages = (response as? VacancyListResponse)?.pages ?: 0
                Log.d("VacancyList - репозиторий", vacancyList.size.toString())
                emit(Resource.Success(Triple(vacancyList, found, pages)))
            }

            ResponseState.NULL_DATA -> emit(Resource.Error(ResponseState.NULL_DATA.errorMessage))
            ResponseState.HTTP_EXCEPTION -> emit(Resource.Error(ResponseState.HTTP_EXCEPTION.errorMessage))
            ResponseState.UNKNOWN -> emit(Resource.Error(ResponseState.UNKNOWN.errorMessage))
            ResponseState.INVALID_DTO_TYPE -> emit(Resource.Error(ResponseState.INVALID_DTO_TYPE.errorMessage))
        }
    }
}
