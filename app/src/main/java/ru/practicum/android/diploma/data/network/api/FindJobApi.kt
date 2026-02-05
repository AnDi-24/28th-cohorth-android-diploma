package ru.practicum.android.diploma.data.network.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.GET_INDUSTRIES_LIST_ENDPOINT
import ru.practicum.android.diploma.GET_VACANCIES_LIST_ENDPOINT
import ru.practicum.android.diploma.GET_VACANCY_BY_ID_ENDPOINT
import ru.practicum.android.diploma.data.network.models.VacancyDto
import ru.practicum.android.diploma.data.network.models.VacancyListResponse
import ru.practicum.android.diploma.data.network.models.industries.IndustryDto

interface FindJobApi {
    @GET(GET_VACANCY_BY_ID_ENDPOINT)
    suspend fun getVacancyById(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): VacancyDto?

    @GET(GET_VACANCIES_LIST_ENDPOINT)
    suspend fun getVacanciesList(
        @QueryMap options: Map<String, String>,
        @Header("Authorization") token: String
    ): VacancyListResponse

    @GET(GET_INDUSTRIES_LIST_ENDPOINT)
    suspend fun getIndustriesList(
        @Header("Authorization") token: String
    ): List<IndustryDto>
}
