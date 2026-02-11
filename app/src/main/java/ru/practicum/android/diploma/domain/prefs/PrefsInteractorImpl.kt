package ru.practicum.android.diploma.domain.prefs

import ru.practicum.android.diploma.data.prefs.PrefsStorageApi

class PrefsInteractorImpl(
    private val prefsStorage: PrefsStorageApi<FilterSettingsModel>
) : PrefsInteractor {
    override fun getFilterSettings(): FilterSettingsModel? {
        return prefsStorage.getData()
    }

    override fun saveFilterSettings(filterModel: FilterSettingsModel) {
        prefsStorage.storageData(filterModel)
    }

    override fun updateData(filterSettings: FilterSettingsModel) {
        prefsStorage.storageData(filterSettings)
    }
}
