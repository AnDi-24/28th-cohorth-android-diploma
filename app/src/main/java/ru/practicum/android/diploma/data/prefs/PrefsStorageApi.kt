package ru.practicum.android.diploma.data.prefs

interface PrefsStorageApi<T> {
    fun storageData(data: T)
    fun getData(): T?
    fun clearData()
}
