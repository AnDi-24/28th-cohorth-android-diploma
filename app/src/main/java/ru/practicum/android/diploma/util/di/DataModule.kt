package ru.practicum.android.diploma.util.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.data.local.AppDatabase
import ru.practicum.android.diploma.data.local.FavoriteVacancyRepositoryImpl
import ru.practicum.android.diploma.domain.local.api.FavoriteVacancyRepository

val dataModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "room_db"
        ).build()
    }

    single<FavoriteVacancyRepository> {
        FavoriteVacancyRepositoryImpl(roomDataBase = get())
    }



}
