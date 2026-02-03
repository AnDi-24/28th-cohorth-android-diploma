package ru.practicum.android.diploma.data.network.models

import ru.practicum.android.diploma.util.ResponseState

open class Response {
    var resultCode: ResponseState = ResponseState.UNKNOWN
    var errorCode: Int? = null
}
