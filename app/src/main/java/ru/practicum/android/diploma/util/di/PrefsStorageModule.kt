package ru.practicum.android.diploma.util.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel
import ru.practicum.android.diploma.data.prefs.PrefsStorageApi
import ru.practicum.android.diploma.data.prefs.PrefsStorageClient
import ru.practicum.android.diploma.domain.prefs.FilterSettingsModel
import ru.practicum.android.diploma.domain.prefs.PrefsInteractor
import ru.practicum.android.diploma.domain.prefs.PrefsInteractorImpl
import ru.practicum.android.diploma.presentation.FilterViewModel
import com.google.gson.reflect.TypeToken

val prefsStorageModule = module {
    single<PrefsStorageApi<FilterSettingsModel>> {
        PrefsStorageClient<FilterSettingsModel>(
            context = androidContext(),
            dataKey = "filter_settings_key",
            type = object : TypeToken<FilterSettingsModel>() {}.type
        )
    }

    single<PrefsInteractor> {
        PrefsInteractorImpl(
            prefsStorage = get()
        )
    }
    viewModel {
        FilterViewModel(
            prefsInteractor = get()
        )
    }

}
