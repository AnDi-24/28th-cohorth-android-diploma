package ru.practicum.android.diploma.domain.prefs

data class FilterSettingsModel(
    val industryName: String = "",
    val industry: String = "",
    val salary: Int = 0,
    val showSalary: Boolean = false
)
