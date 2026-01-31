package ru.practicum.android.diploma.data.network

import retrofit2.HttpException
import ru.practicum.android.diploma.data.network.api.FindJobApi
import ru.practicum.android.diploma.data.network.api.NetworkClient
import ru.practicum.android.diploma.data.network.models.Response
import ru.practicum.android.diploma.data.network.models.VacancyDetailsRequest
import ru.practicum.android.diploma.data.network.models.VacancyDetailsResponse
import ru.practicum.android.diploma.data.network.models.VacancyListRequest
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

    override suspend fun getVacanciesList(dto: Any): Response {
        if (dto is VacancyListRequest) {
            val options: HashMap<String, String> = HashMap()

            if (dto.text.isNotEmpty()) {
                options["text"] = dto.text
            }

            dto.page.let {
                options["page"] = it.toString()
            }

            dto.area.let {
                options["area"] = it.toString()
            }

            dto.salary?.let {
                options["salary"] = it.toString()
            }

            dto.industry?.let {
                options["industry"] = it.toString()
            }

            dto.onlyWithSalary.let {
                options["only_with_salary"] = it.toString()
            }

            return findJobApi.getVacanciesList(
                options = options,
                token = TOKEN
            ).apply { resultCode = ResponseState.SUCCESS }
        }
        return Response().apply { resultCode = ResponseState.UNKNOWN }
    }

//    private fun isConnected(): Boolean {
//
//    }
}
