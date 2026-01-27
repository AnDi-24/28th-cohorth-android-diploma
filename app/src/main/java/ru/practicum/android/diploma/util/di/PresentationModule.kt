package ru.practicum.android.diploma.util.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.VacancyDetailsViewModel

val presentationModule = module {
    viewModel { VacancyDetailsViewModel(
        retrofitInteractor = get()
    ) }

}
