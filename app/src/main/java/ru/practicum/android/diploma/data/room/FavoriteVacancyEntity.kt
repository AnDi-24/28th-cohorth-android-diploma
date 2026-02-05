package ru.practicum.android.diploma.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.practicum.android.diploma.FAVORITE_VACANCIES

@Entity(tableName = FAVORITE_VACANCIES)
data class FavoriteVacancyEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val employerName: String?,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val salaryCurrency: String?,
    val employerLogoUrl: String?,
    val areaName: String?,
    val experience: String?,
    val employment: String?,
    val schedule: String?,
    val description: String?,
    val skills: String?,
    val vacancyUrl: String?,
    val addressCity: String?,
    val addressStreet: String?,
    val addressBuilding: String?,
    val addressRaw: String?,
    val contactName: String?,
    val contactEmail: String?,
    val contactPhones: String?,
    val addedAt: Long = System.currentTimeMillis()
)
