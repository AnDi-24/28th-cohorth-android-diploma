package ru.practicum.android.diploma.data.network

import android.util.Log
import retrofit2.HttpException
import ru.practicum.android.diploma.data.network.api.FindJobApi
import ru.practicum.android.diploma.data.network.api.NetworkClient
import ru.practicum.android.diploma.data.network.models.Response
import ru.practicum.android.diploma.data.network.models.VacancyDetailsRequest
import ru.practicum.android.diploma.data.network.models.VacancyDetailsResponse

private const val TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVC" +
    "J9.eyJpc3MiOiJwcmFjdGljdW0ucnUiLCJhdWQiOiJwcmFjdGljdW0ucnUiLCJ1c" +
    "2VybmFtZSI6InBvcGthIn0.VsTHOxBepYX9fZCbzWNIieL3ypqTULOI_T5WV5Ed8wY"

private const val SUCCESS = 200
private const val ERROR = -1

class RetrofitNetworkClient(
    private val findJobApi: FindJobApi
) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        if (dto !is VacancyDetailsRequest) {
            return Response().apply { resultCode = -1 }
        }
        return try {
            val vacancy = findJobApi.getVacancyById(dto.expression, TOKEN)

            VacancyDetailsResponse(result = vacancy).apply {
                resultCode = SUCCESS
            }
        } catch (e: HttpException) {
            Log.e("NetworkClient", "ERROR: ${e.message}", e)
            VacancyDetailsResponse(result = null).apply {
                resultCode = ERROR
            }
        }
    }

//    private fun isConnected(): Boolean {
//
//    }
}
