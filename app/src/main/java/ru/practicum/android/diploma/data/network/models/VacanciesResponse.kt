package ru.practicum.android.diploma.data.network.models

import ru.practicum.android.diploma.util.Resource

class VacanciesResponse(
    val vacanciesDto: Resource<VacanciesResponseDto?>? = null
) : Response()
