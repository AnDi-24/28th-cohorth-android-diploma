package ru.practicum.android.diploma.data.network

import android.util.Log
import ru.practicum.android.diploma.data.network.api.FindJobApi
import ru.practicum.android.diploma.data.network.api.NetworkClient
import ru.practicum.android.diploma.data.network.models.Response
import ru.practicum.android.diploma.data.network.models.VacancyDetailsRequest
import ru.practicum.android.diploma.data.network.models.VacancyDetailsResponse

private const val TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwcmFjdGljdW0ucnUiLCJhdWQiOiJwcmFjdGljdW0ucnUiLCJ1c2VybmFtZSI6InBvcGthIn0.VsTHOxBepYX9fZCbzWNIieL3ypqTULOI_T5WV5Ed8wY"

class RetrofitNetworkClient(
    private val findJobApi: FindJobApi
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {

        if (dto !is VacancyDetailsRequest) {
            return Response().apply { resultCode = -1 }
        }
        return try {
            val vacancy = findJobApi.getVacancyById(dto.expression, TOKEN)
            Log.d("NetworkClient", "Received vacancy: ${vacancy.name}")
            Log.d("NetworkClient", "Vacancy ID: ${vacancy.id}")
            VacancyDetailsResponse(result = vacancy).apply {
                resultCode = 200
            }
        } catch (e: Exception) {
            Log.e("NetworkClient", "ERROR: ${e.message}", e)
            VacancyDetailsResponse(result = null).apply {
                resultCode = -2
            }
        }
    }

    private fun isConnected(): Boolean {
        TODO()
    }
}
