package ru.practicum.android.diploma.data.network.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import ru.practicum.android.diploma.data.network.models.VacancyDto

interface FindJobApi {
    @GET("vacancies/{id}")
    suspend fun getVacancyById(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): VacancyDto?

}
