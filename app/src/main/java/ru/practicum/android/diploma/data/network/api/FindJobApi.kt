package ru.practicum.android.diploma.data.network.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.data.network.models.VacancyDto
import ru.practicum.android.diploma.data.network.models.VacancyListResponse

interface FindJobApi {
    @GET("vacancies/{id}")
    suspend fun getVacancyById(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): VacancyDto?

    @GET("vacancies")
    suspend fun getVacanciesList(
        @QueryMap options: Map<String, String>,
        @Header("Authorization") token: String
    ): VacancyListResponse
}
