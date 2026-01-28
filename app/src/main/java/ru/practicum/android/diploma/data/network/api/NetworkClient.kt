package ru.practicum.android.diploma.data.network.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.network.models.Response

interface  NetworkClient {
    suspend fun doRequestVacancyDetails(dto: Any): Response

}
