package ru.practicum.android.diploma.data.network.models

import ru.practicum.android.diploma.util.Resource

class VacancyDetailsResponse(
    val vacancyDto: Resource<VacancyDto?>? = null
) : Response()
