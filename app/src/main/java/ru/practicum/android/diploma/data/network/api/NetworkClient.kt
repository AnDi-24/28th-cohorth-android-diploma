package ru.practicum.android.diploma.data.network.api

import ru.practicum.android.diploma.data.network.models.Response

interface NetworkClient {
    suspend fun doRequestVacancyDetails(dto: Any): Response
}
