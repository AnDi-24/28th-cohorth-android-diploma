package ru.practicum.android.diploma.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_vacancy")
data class VacancyEntity(
    @PrimaryKey(autoGenerate = true)
    val vacancyId: Long
)
