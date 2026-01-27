package ru.practicum.android.diploma.util

enum class ResponseState(val code: Int, val errorMessage: String) {
    SUCCESS(200, "Успешно"),
    NULL_DATA(404, "Данные не найдены"),
    INVALID_DTO_TYPE(1001, "Неверный тип запроса"),
    HTTP_EXCEPTION(-1, "Ошибка сети"),
    UNKNOWN(0, "Неизвестная ошибка")
}
