package ru.practicum.android.diploma.util.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.room.FavoriteVacancyInteractorImpl
import ru.practicum.android.diploma.domain.room.api.FavoriteVacancyInteractor
import ru.practicum.android.diploma.domain.network.FindVacancyInteractorImpl
import ru.practicum.android.diploma.domain.network.IndustriesInteractorImpl
import ru.practicum.android.diploma.domain.network.api.FindVacancyInteractor
import ru.practicum.android.diploma.domain.network.api.industries.IndustriesInteractor
import ru.practicum.android.diploma.util.VacancyMapper

val domainModule = module {
    single<FavoriteVacancyInteractor> {
        FavoriteVacancyInteractorImpl(
            repository = get(),
            mapper = get(),
        )
    }

    single<FindVacancyInteractor> {
        FindVacancyInteractorImpl(repository = get(), mapper = get())
    }

    single { VacancyMapper }

    single<IndustriesInteractor> {
        IndustriesInteractorImpl(repository = get())
    }
}
