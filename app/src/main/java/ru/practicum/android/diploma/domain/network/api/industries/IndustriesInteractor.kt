package ru.practicum.android.diploma.domain.network.api.industries

import ru.practicum.android.diploma.domain.network.models.industries.IndustryModel
import ru.practicum.android.diploma.util.Resource

interface IndustriesInteractor {
    suspend fun getIndustries(): Resource<List<IndustryModel>>
}
