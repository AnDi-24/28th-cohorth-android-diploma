package ru.practicum.android.diploma.util

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    // Добавляем полезные extension-функции
    val isSuccess get() = this is Success
    val isError get() = this is Error


}
