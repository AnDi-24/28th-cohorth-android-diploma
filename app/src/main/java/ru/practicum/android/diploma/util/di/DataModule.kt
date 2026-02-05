package ru.practicum.android.diploma.util.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.room.AppDatabase
import ru.practicum.android.diploma.data.room.FavoriteVacancyRepositoryImpl
import ru.practicum.android.diploma.data.network.FindVacancyRepositoryImpl
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.data.network.api.FindJobApi
import ru.practicum.android.diploma.data.network.api.NetworkClient
import ru.practicum.android.diploma.domain.room.api.FavoriteVacancyRepository
import ru.practicum.android.diploma.domain.network.api.FindVacancyRepository

val dataModule = module {

    // Room

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "room_db"
        ).build()
    }

    single<FavoriteVacancyRepository> {
        FavoriteVacancyRepositoryImpl(
            database = get()
        )
    }

    // Retrofit

    single {
        Retrofit.Builder()
            .baseUrl("https://practicum-diploma-8bc38133faba.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>().create(FindJobApi::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(findJobApi = get())
    }

    single<FindVacancyRepository> {
        FindVacancyRepositoryImpl(retrofitClient = get())
    }

}
