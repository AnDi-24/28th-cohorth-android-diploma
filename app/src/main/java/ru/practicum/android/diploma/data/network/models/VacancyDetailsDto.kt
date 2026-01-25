package ru.practicum.android.diploma.data.network.models

import com.google.gson.annotations.SerializedName

data class VacancyDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("salary") val salary: Salary?,
    @SerializedName("address") val address: Address?,
    @SerializedName("experience") val experience: Experience?,
    @SerializedName("schedule") val schedule: Schedule?,
    @SerializedName("employment") val employment: Employment?,
    @SerializedName("contacts") val contacts: Contacts?,
    @SerializedName("description") val description: String?,
    @SerializedName("employer") val employer: Employer?,
    @SerializedName("area") val area: Area?,
    @SerializedName("skills") val skills: List<String>?,
    @SerializedName("url") val url: String?,
    @SerializedName("industry") val industry: Industry?,

): Response()


data class Industry(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?
)

data class Salary(
    @SerializedName("id") val id: String?,
    @SerializedName("from") val from: Int?,
    @SerializedName("to") val to: Int?,
    @SerializedName("currency") val currency: String?
)

data class Address(
    @SerializedName("city") val city: String?,
    @SerializedName("street") val street: String?,
    @SerializedName("building") val building: String?,
    @SerializedName("raw") val raw: String?
)

data class Experience(
    @SerializedName("name") val name: String?
)

data class Schedule(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?
)

data class Employer(
    @SerializedName("name") val name: String?,
    @SerializedName("logo") val logo: String?
)

data class Contacts(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("phones") val phones: List<Phone>?
)

data class Area(
    @SerializedName("id") val id: String?,
    @SerializedName("parentId") val parentId: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("areas") val areas: List<String>?
)

data class Phone(
    @SerializedName("formatted") val formatted: String?
)

data class Employment(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?
)
