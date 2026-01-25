package ru.practicum.android.diploma.data.network

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.network.api.NetworkClient
import ru.practicum.android.diploma.data.network.models.VacancyDetailsRequest
import ru.practicum.android.diploma.data.network.models.VacancyDetailsResponse
import ru.practicum.android.diploma.data.network.models.VacancyDto
import ru.practicum.android.diploma.domain.network.api.FindVacancyRepository

private const val SUCCESS = 200
private const val NULL = "Vacancy data is null"
private const val INVALID_RESP_TYPE = "Invalid response type"
private const val EXCEPTION = "Exception"

class FindVacancyRepositoryImpl(
    private val retrofitClient: NetworkClient
) : FindVacancyRepository {

    override fun getVacancyDetails(expression: String): Flow<VacancyDto> = flow {
        val response = retrofitClient.doRequest(VacancyDetailsRequest(expression))

        when (response.resultCode) {
            SUCCESS -> {
                if (response is VacancyDetailsResponse) {
                    val result = response.result
                    if (result != null) {
                        Log.d("Repository", "Emitting vacancy: ${result.name}")
                        emit(result)
                    } else {
                        Log.d(EXCEPTION, NULL)

                    }
                } else {
                    Log.d(EXCEPTION, INVALID_RESP_TYPE)
                }
            }
            else -> {
                Log.d(EXCEPTION, "Request failed: ${response.resultCode}")
            }
        }
    }
}
