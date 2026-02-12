package ru.practicum.android.diploma.util.di

import com.google.gson.reflect.TypeToken
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.FILTER_SETTINGS_KEY
import ru.practicum.android.diploma.data.prefs.PrefsStorageApi
import ru.practicum.android.diploma.data.prefs.PrefsStorageClient
import ru.practicum.android.diploma.domain.prefs.FilterSettingsModel
import ru.practicum.android.diploma.domain.prefs.PrefsInteractor
import ru.practicum.android.diploma.domain.prefs.PrefsInteractorImpl

val prefsStorageModule = module {
    single<PrefsStorageApi<FilterSettingsModel>> {
        PrefsStorageClient<FilterSettingsModel>(
            context = androidContext(),
            dataKey = FILTER_SETTINGS_KEY,
            type = object : TypeToken<FilterSettingsModel>() {}.type
        )
    }

    single<PrefsInteractor> {
        PrefsInteractorImpl(
            prefsStorage = get()
        )
    }
}
