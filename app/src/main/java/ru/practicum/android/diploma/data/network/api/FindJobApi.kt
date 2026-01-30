package ru.practicum.android.diploma.data.network.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.data.network.models.VacancyDto
import ru.practicum.android.diploma.data.network.models.VacanciesResponseDto

interface FindJobApi {
    @GET("vacancies/{id}")
    suspend fun getVacancyById(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): VacancyDto?

    @GET("vacancies")
    suspend fun getVacancies(
        @Query("area") area: Int? = null,
        @Query("industry") industry: Int? = null,
        @Query("text") text: String? = null,
        @Query("salary") salary: Int? = null,
        @Query("page") page: Int? = null,
        @Query("only_with_salary") only_with_salary: Boolean? = null,
        @Header("Authorization") token: String
    ): VacanciesResponseDto?

}
