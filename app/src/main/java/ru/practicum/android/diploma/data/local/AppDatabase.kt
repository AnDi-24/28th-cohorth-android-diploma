package ru.practicum.android.diploma.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [VacancyEntity::class],

)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteVacancyDao(): VacancyDao

}
