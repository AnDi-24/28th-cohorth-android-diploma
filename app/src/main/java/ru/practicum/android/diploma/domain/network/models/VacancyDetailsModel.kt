package ru.practicum.android.diploma.domain.network.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VacancyDetailsModel(
    val id: String,
    val name: String,
    val salary: Salary? = null,
    val address: Address? = null,
    val experience: Experience? = null,
    val schedule: Schedule? = null,
    val employment: Employment? = null,
    val contacts: Contacts? = null,
    val description: String? = null,
    val employer: Employer? = null,
    val area: Area? = null,
    val skills: List<String> = emptyList(),
    val url: String? = null,
    val industry: Industry? = null
) : Parcelable

@Parcelize
data class Salary(
    val id: String,
    val currency: String,
    val from: Int? = null,
    val to: Int? = null
) : Parcelable

@Parcelize
data class Address(
    val id: String,
    val city: String,
    val street: String? = null,
    val building: String? = null,
    val raw: String? = null
) : Parcelable

@Parcelize
data class Experience(
    val id: String,
    val name: String
) : Parcelable

@Parcelize
data class Schedule(
    val id: String,
    val name: String
) : Parcelable

@Parcelize
data class Employment(
    val id: String,
    val name: String
) : Parcelable

@Parcelize
data class Contacts(
    val id: String,
    val name: String,
    val email: String? = null,
    val phones: List<Phone> = emptyList()
) : Parcelable

@Parcelize
data class Phone(
    val comment: String? = null,
    val formatted: String
) : Parcelable

@Parcelize
data class Employer(
    val id: String,
    val name: String,
    val logo: String? = null
) : Parcelable

@Parcelize
data class Area(
    val id: String,
    val parentId: String? = null,
    val name: String,
    val areas: List<Area> = emptyList()
) : Parcelable

@Parcelize
data class Industry(
    val id: String,
    val name: String
) : Parcelable
