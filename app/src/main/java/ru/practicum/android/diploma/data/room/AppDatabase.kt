package ru.practicum.android.diploma.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [FavoriteVacancyEntity::class],
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteVacancyDao(): VacancyDao
}
