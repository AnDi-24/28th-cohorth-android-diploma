package ru.practicum.android.diploma.util.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.FavoriteViewModel
import ru.practicum.android.diploma.presentation.SearchViewModel
import ru.practicum.android.diploma.presentation.VacancyDetailsViewModel

val presentationModule = module {
    viewModel { VacancyDetailsViewModel(
        retrofitInteractor = get(),
        favoriteInteractor = get()
    ) }

    viewModel {
        SearchViewModel(
            searchInteractor = get(),
            industryInteractor = get(),
            prefsInteractor = get()
        )
    }

    viewModel {
        FavoriteViewModel(
            favoriteInteractor = get()
        )
    }
}
