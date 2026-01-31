package ru.practicum.android.diploma.domain.network.models

data class SearchParams(
    val area: Int? = null,
    val industry: Int? = null,
    val text: String = "",
    val salary: Int? = null,
    val page: Int = 0,
    val onlyWithSalary: Boolean = false
)
