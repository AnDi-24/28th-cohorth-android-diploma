package ru.practicum.android.diploma.data.network

import android.util.Log
import retrofit2.HttpException
import ru.practicum.android.diploma.data.network.api.FindJobApi
import ru.practicum.android.diploma.data.network.api.NetworkClient
import ru.practicum.android.diploma.data.network.models.Response
import ru.practicum.android.diploma.data.network.models.VacancyDetailsRequest
import ru.practicum.android.diploma.data.network.models.VacancyDetailsResponse
import ru.practicum.android.diploma.data.network.models.VacancyDto
import ru.practicum.android.diploma.data.network.models.VacancyListRequest
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.util.ResponseState
import java.io.IOException

private const val TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVC" +
    "J9.eyJpc3MiOiJwcmFjdGljdW0ucnUiLCJhdWQiOiJwcmFjdGljdW0ucnUiLCJ1c" +
    "2VybmFtZSI6InBvcGthIn0.VsTHOxBepYX9fZCbzWNIieL3ypqTULOI_T5WV5Ed8wY"

class RetrofitNetworkClient(
    private val findJobApi: FindJobApi
) : NetworkClient {

    override suspend fun doRequestVacancyDetails(dto: Any): Response {
        if (!isValidDetailsRequest(dto)) {
            return createInvalidDtoResponse()
        }

        return try {
            fetchVacancyDetails(dto as VacancyDetailsRequest)
        } catch (e: HttpException) {
            handleHttpExceptionForDetails(e)
        } catch (e: IOException) {
            handleIoExceptionForDetails(e)
        }
    }

    private fun isValidDetailsRequest(dto: Any): Boolean {
        return dto is VacancyDetailsRequest
    }

    private fun createInvalidDtoResponse(): Response {
        return Response().apply {
            resultCode = ResponseState.INVALID_DTO_TYPE
        }
    }

    private suspend fun fetchVacancyDetails(request: VacancyDetailsRequest): Response {
        val vacancy = findJobApi.getVacancyById(request.expression, TOKEN)
        return if (vacancy == null) {
            createNullDataResponse()
        } else {
            createSuccessResponse(vacancy)
        }
    }

    private fun createNullDataResponse(): Response {
        return Response().apply {
            resultCode = ResponseState.NULL_DATA
        }
    }

    private fun createSuccessResponse(vacancy: VacancyDto): Response {
        return VacancyDetailsResponse(
            vacancyDto = Resource.Success(data = vacancy)
        ).apply {
            resultCode = ResponseState.SUCCESS
        }
    }

    private fun handleHttpExceptionForDetails(e: HttpException): Response {
        logException("HTTP error in doRequestVacancyDetails", e)
        return Response().apply {
            resultCode = ResponseState.HTTP_EXCEPTION
            errorCode = e.code()
        }
    }

    private fun handleIoExceptionForDetails(e: IOException): Response {
        logException("IO error in doRequestVacancyDetails", e)
        return Response().apply {
            resultCode = ResponseState.HTTP_EXCEPTION
        }
    }

    override suspend fun getVacanciesList(dto: Any): Response {
        if (!isValidVacancyRequest(dto)) {
            return createErrorResponse(ResponseState.UNKNOWN)
        }

        return try {
            val options = createVacancyOptions(dto as VacancyListRequest)
            executeVacancyListRequest(options)
        } catch (e: HttpException) {
            handleHttpException(e)
        } catch (e: IOException) {
            handleIOException(e)
        } catch (e: IllegalArgumentException) {
            handleIllegalArgumentException(e)
        }
    }

    private fun isValidVacancyRequest(dto: Any): Boolean {
        return dto is VacancyListRequest
    }

    private suspend fun executeVacancyListRequest(options: Map<String, String>): Response {
        return findJobApi.getVacanciesList(
            options = options,
            token = TOKEN
        ).apply { resultCode = ResponseState.SUCCESS }
    }

    private fun handleHttpException(e: HttpException): Response {
        Log.d("RetrofitNetworkClient", "HttpException: ${e.message()}")
        return createErrorResponse(
            resultCode = ResponseState.HTTP_EXCEPTION,
            errorCode = e.code()
        )
    }

    private fun handleIOException(e: IOException): Response {
        logException("IO error in getVacanciesList", e)
        return createErrorResponse(ResponseState.HTTP_EXCEPTION)
    }

    private fun handleIllegalArgumentException(e: IllegalArgumentException): Response {
        logException("Illegal argument in getVacanciesList", e)
        return createErrorResponse(ResponseState.UNKNOWN)
    }

    private fun createErrorResponse(
        resultCode: ResponseState,
        errorCode: Int? = null
    ): Response {
        return Response().apply {
            this.resultCode = resultCode
            errorCode?.let { this.errorCode = it }
        }
    }

    private fun createVacancyOptions(request: VacancyListRequest): Map<String, String> {
        return buildMap {
            if (request.text.isNotEmpty()) {
                put("text", request.text)
            }

            put("page", request.page.toString())
            put("area", request.area.toString())

            request.salary?.let { put("salary", it.toString()) }
            request.industry?.let { put("industry", it.toString()) }
            put("only_with_salary", request.onlyWithSalary.toString())
        }
    }

    private fun logException(message: String, exception: Exception) {
        Log.e("RetrofitNetworkClient", "$message: ${exception.message}")
    }

}
