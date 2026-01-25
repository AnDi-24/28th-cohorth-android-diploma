package ru.practicum.android.diploma.util.di


import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.FavoriteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel

val presentationModule = module {

    viewModel { FavoriteViewModel(roomInteractor = get()) }

}
