package ru.practicum.android.diploma.domain.network.models

data class VacancyDetailsModel(
    val id: String = "id не найден",
    val name: String = "Имя не указано",
    val salary: String = "Зарплата не указана",
    val address: String = "Адрес не указан",
    val experience: String = "Опыт не указан",
    val schedule: String = "График не указан",
    val employment: String = "Тип занятости не указан",
    val description: String = "Описание отсутствует",
    val employer: String = "",
    val skills: List<String> = emptyList(),
    val logo: String = "Лого не указано"
)

//data class SalaryModel(
//    val from: Int?,
//    val to: Int?,
//    val currency: String?
//)
//
//data class AddressModel(
//    val city: String?,
//    val raw: String?
//)


