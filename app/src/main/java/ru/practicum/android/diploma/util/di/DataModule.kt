package ru.practicum.android.diploma.util.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BASE_APP_URL
import ru.practicum.android.diploma.ROOM_DB
import ru.practicum.android.diploma.data.local.AppDatabase
import ru.practicum.android.diploma.data.local.FavoriteVacancyRepositoryImpl
import ru.practicum.android.diploma.data.network.FindVacancyRepositoryImpl
import ru.practicum.android.diploma.data.network.IndustriesRepositoryImpl
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.data.network.api.FindJobApi
import ru.practicum.android.diploma.data.network.api.NetworkClient
import ru.practicum.android.diploma.domain.local.api.FavoriteVacancyRepository
import ru.practicum.android.diploma.domain.network.api.FindVacancyRepository
import ru.practicum.android.diploma.domain.network.api.industries.IndustriesRepository

val dataModule = module {

    // Room

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            ROOM_DB
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
            .baseUrl(BASE_APP_URL)
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

    single<IndustriesRepository> {
        IndustriesRepositoryImpl(retrofitClient = get())
    }
}
