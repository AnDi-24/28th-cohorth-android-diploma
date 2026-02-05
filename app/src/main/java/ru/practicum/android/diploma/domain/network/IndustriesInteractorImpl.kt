package ru.practicum.android.diploma.domain.network

import ru.practicum.android.diploma.domain.network.api.industries.IndustriesInteractor
import ru.practicum.android.diploma.domain.network.api.industries.IndustriesRepository
import ru.practicum.android.diploma.domain.network.models.industries.IndustryModel
import ru.practicum.android.diploma.util.Resource

class IndustriesInteractorImpl(
    private val repository: IndustriesRepository
) : IndustriesInteractor {
    override suspend fun getIndustries(): Resource<List<IndustryModel>> {
        return repository.getIndustries()
    }
}
