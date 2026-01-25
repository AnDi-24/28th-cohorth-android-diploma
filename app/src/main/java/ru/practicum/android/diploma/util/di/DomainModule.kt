package ru.practicum.android.diploma.util.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.local.FavoriteVacancyInteractorImpl
import ru.practicum.android.diploma.domain.local.api.FavoriteVacancyInteractor

val domainModule = module {

    single<FavoriteVacancyInteractor> {
        FavoriteVacancyInteractorImpl(repository = get())
    }

}
