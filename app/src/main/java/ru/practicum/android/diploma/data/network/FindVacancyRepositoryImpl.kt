package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.network.api.NetworkClient
import ru.practicum.android.diploma.data.network.models.VacancyDetailsRequest
import ru.practicum.android.diploma.data.network.models.VacancyDetailsResponse
import ru.practicum.android.diploma.data.network.models.VacancyDto
import ru.practicum.android.diploma.domain.network.api.FindVacancyRepository
import ru.practicum.android.diploma.util.ResponseState
import ru.practicum.android.diploma.util.Resource
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
                else -> {
                    Resource.Error(message = response.resultCode.errorMessage)
                }
            }
        } catch (e: IOException) {
            Resource.Error(e.message.toString())

        }
    }
}

