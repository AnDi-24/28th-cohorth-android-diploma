package ru.practicum.android.diploma.domain.prefs

interface PrefsInteractor {
    fun getFilterSettings(): FilterSettingsModel?
    fun saveFilterSettings(filterModel: FilterSettingsModel)
    fun updateData(filterSettings: FilterSettingsModel)
}
