package com.carbon.tracker.ui.model

import kotlinx.coroutines.CoroutineExceptionHandler

object ExceptionHandler {
    val handler = CoroutineExceptionHandler { _, throwable ->
    }
}