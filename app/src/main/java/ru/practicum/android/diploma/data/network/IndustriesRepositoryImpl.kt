package ru.practicum.android.diploma.data.network

import okio.IOException
import ru.practicum.android.diploma.data.network.api.NetworkClient
import ru.practicum.android.diploma.data.network.models.industries.IndustriesResponse
import ru.practicum.android.diploma.domain.network.api.industries.IndustriesRepository
import ru.practicum.android.diploma.domain.network.models.industries.IndustryModel
import ru.practicum.android.diploma.util.ErrorHandler
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.util.ResponseState

class IndustriesRepositoryImpl(
    private val retrofitClient: NetworkClient
) : IndustriesRepository {
    override suspend fun getIndustries(): Resource<List<IndustryModel>> {
        return try {
            val response = retrofitClient.getIndustries()
            when (response.resultCode) {
                ResponseState.SUCCESS -> {
                    val industriesResponse = response as? IndustriesResponse
                    val industriesDto = industriesResponse?.industries

                    if (!industriesDto.isNullOrEmpty()) {
                        val industriesModel = industriesDto.map { dto ->
                            IndustryModel(
                                id = dto.id,
                                name = dto.name
                            )
                        }
                        Resource.Success(industriesModel)
                    } else {
                        Resource.Error(ResponseState.NULL_DATA.errorMessage)
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
                        message = response.resultCode.errorMessage
                    )
                }
            }
        } catch (e: IOException) {
            Resource.Error(ErrorHandler.handleNetworkError(e))
        } catch (e: IllegalArgumentException) {
            Resource.Error(ErrorHandler.handleGenericError(e))
        }
    }
}
