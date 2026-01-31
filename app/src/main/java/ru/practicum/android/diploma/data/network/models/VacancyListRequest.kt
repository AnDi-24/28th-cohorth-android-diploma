package ru.practicum.android.diploma.data.network.models

class VacancyListRequest(
    val area: Int,
    val industry: Int?,
    val text: String,
    val salary: Int?,
    val page: Int,
    val onlyWithSalary: Boolean
) {
}
