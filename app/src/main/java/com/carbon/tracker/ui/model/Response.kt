package com.carbon.tracker.ui.model

sealed class Response<out T> {
    data class Success<out T>(val data: T) : Response<T>()
    data class Fail(val exception: Throwable) : Response<Nothing>()
    object Loading : Response<Nothing>()
}