package ru.practicum.android.diploma.data.prefs

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.android.diploma.FILTER_PREFERENCES

class PrefsStorageClient<T>(
    private val context: Context,
    private val dataKey: String,
    private val type: java.lang.reflect.Type
) : PrefsStorageApi<T> {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(FILTER_PREFERENCES, Context.MODE_PRIVATE)
    private val gson = Gson()

    override fun storageData(data: T) {
        val json = gson.toJson(data, type)
        prefs.edit().putString(dataKey, json).apply()
    }

    override fun getData(): T? {
        val json = prefs.getString(dataKey, null)
        return if (!json.isNullOrEmpty()) {
            gson.fromJson(json, type)
        } else {
            null
        }
    }

    override fun clearData() {
        prefs.edit().remove(dataKey).apply()
    }
}
