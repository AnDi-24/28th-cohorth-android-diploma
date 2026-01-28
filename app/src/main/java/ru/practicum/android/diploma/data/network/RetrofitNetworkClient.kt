package ru.practicum.android.diploma.data.network

import android.util.Log
import retrofit2.HttpException
import ru.practicum.android.diploma.data.network.api.FindJobApi
import ru.practicum.android.diploma.data.network.api.NetworkClient
import ru.practicum.android.diploma.data.network.models.Response
import ru.practicum.android.diploma.data.network.models.VacancyDetailsRequest
import ru.practicum.android.diploma.data.network.models.VacancyDetailsResponse
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.util.ResponseState

private const val TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVC" +
    "J9.eyJpc3MiOiJwcmFjdGljdW0ucnUiLCJhdWQiOiJwcmFjdGljdW0ucnUiLCJ1c" +
    "2VybmFtZSI6InBvcGthIn0.VsTHOxBepYX9fZCbzWNIieL3ypqTULOI_T5WV5Ed8wY"

class RetrofitNetworkClient(
    private val findJobApi: FindJobApi
) : NetworkClient {

    override suspend fun doRequestVacancyDetails(dto: Any): Response {
        // Проверка типа DTO
        if (dto !is VacancyDetailsRequest) {
            return Response().apply {
                resultCode = ResponseState.INVALID_DTO_TYPE
            }
        }

        return try {
            // Выполнение запроса
            val vacancy = findJobApi.getVacancyById(dto.expression, TOKEN)

            if (vacancy == null) {
                Response().apply {
                    resultCode = ResponseState.NULL_DATA
                }
            } else {
                VacancyDetailsResponse(
                    vacancyDto = Resource.Success(data = vacancy)
                ).apply {
                    resultCode = ResponseState.SUCCESS
                }
            }
        } catch (e: HttpException) {
            Response().apply {
                resultCode = ResponseState.HTTP_EXCEPTION
            }
        }
    }

//    private fun isConnected(): Boolean {
//
//    }
}
