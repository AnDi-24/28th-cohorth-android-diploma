package ru.practicum.android.diploma.util

enum class ResponseState(val errorMessage: String) {
    SUCCESS( "Успешно"),
    NULL_DATA("Данные не найдены"),
    INVALID_DTO_TYPE("Неверный тип запроса"),
    HTTP_EXCEPTION("Ошибка сети"),
    UNKNOWN("Неизвестная ошибка")
}
