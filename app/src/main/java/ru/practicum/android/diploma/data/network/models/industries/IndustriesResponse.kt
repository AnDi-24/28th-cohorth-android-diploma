package ru.practicum.android.diploma.data.network.models.industries

import ru.practicum.android.diploma.data.network.models.Response

class IndustriesResponse : Response() {
    var industries: List<IndustryDto>? = null
}
