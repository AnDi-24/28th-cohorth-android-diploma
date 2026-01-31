package ru.practicum.android.diploma.data.network.models

import com.google.gson.annotations.SerializedName

class VacancyListResponse(
    @SerializedName("found") val found: Int?,
    @SerializedName("pages") val pages: Int?,
    @SerializedName("page") val page: Int?,
    @SerializedName("per_page") val perPage: Int?,
    @SerializedName("items") val items: List<VacancyDto>?
) : Response()
