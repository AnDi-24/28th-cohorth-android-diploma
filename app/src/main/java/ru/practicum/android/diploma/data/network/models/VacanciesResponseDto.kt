package ru.practicum.android.diploma.data.network.models

import com.google.gson.annotations.SerializedName

data class VacanciesResponseDto(
    @SerializedName("found")
    val found: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("vacancies")
    val vacancies: List<VacancyDto>
) : Response()
