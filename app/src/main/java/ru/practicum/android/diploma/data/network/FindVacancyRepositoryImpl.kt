package ru.practicum.android.diploma.data.network

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.network.api.NetworkClient
import ru.practicum.android.diploma.data.network.models.VacancyDetailsRequest
import ru.practicum.android.diploma.data.network.models.VacancyDetailsResponse
import ru.practicum.android.diploma.data.network.models.VacancyDto
import ru.practicum.android.diploma.domain.network.api.FindVacancyRepository

class FindVacancyRepositoryImpl(
    private val retrofitClient: NetworkClient
) : FindVacancyRepository {

    override fun getVacancyDetails(expression: String): Flow<VacancyDto> = flow {
        val response = retrofitClient.doRequest(VacancyDetailsRequest(expression))
        Log.d("Repository", "Response code: ${response.resultCode}")
        Log.d("Repository", "Response type: ${response::class.simpleName}")

        when (response.resultCode) {
            200 -> {
                if (response is VacancyDetailsResponse) {
                    val result = response.result
                    if (result != null) {
                        Log.d("Repository", "Emitting vacancy: ${result.name}")
                        emit(result)
                    } else {
                        throw Exception("Vacancy data is null")
                    }
                } else {
                    throw Exception("Invalid response type")
                }
            }
            -1 -> {
                throw Exception("Network error")
            }
            else -> {
                throw Exception("Request failed: ${response.resultCode}")
            }
        }
    }
}
