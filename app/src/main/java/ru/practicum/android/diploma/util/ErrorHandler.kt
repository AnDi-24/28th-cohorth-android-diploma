package ru.practicum.android.diploma.util

import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

object ErrorHandler {

    fun handleNetworkError(e: IOException): String {
        return when (e) {
            is UnknownHostException -> "Нет подключения к интернету"
            is SocketTimeoutException -> "Таймаут соединения"
            is ConnectException -> "Не удалось подключиться к серверу"
            is SSLException -> "Ошибка безопасности соединения"
            else -> "Сетевая ошибка: ${e.message ?: "Проверьте соединение"}"
        }
    }

    fun handleGenericError(e: Exception): String {
        return when (e) {
            is IllegalArgumentException -> "Ошибка параметров: ${e.message}"
            is IllegalStateException -> "Ошибка состояния: ${e.message}"
            else -> "Неизвестная ошибка: ${e.message}"
        }
    }
}
